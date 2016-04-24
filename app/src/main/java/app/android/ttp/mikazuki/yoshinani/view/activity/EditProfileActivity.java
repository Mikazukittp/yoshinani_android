package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.ActivityEditProfileBinding;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.viewModel.EditProfileViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.media.ExifInterface.ORIENTATION_FLIP_HORIZONTAL;
import static android.media.ExifInterface.ORIENTATION_FLIP_VERTICAL;
import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.ORIENTATION_TRANSPOSE;
import static android.media.ExifInterface.ORIENTATION_TRANSVERSE;
import static android.media.ExifInterface.ORIENTATION_UNDEFINED;
import static android.media.ExifInterface.TAG_ORIENTATION;

/**
 * @author haijimakazuki
 */
public class EditProfileActivity extends BaseActivity {

    private static final int REQUEST_CHOOSER = 1000;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.image)
    ImageView mImage;

    private Uri mUri;
    private ActivityEditProfileBinding mBinding;
    private EditProfileViewModel mViewModel;
    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        // Toolbarの設定
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setTitle("プロフィール編集");

        UserModel me = Parcels.unwrap(getIntent().getParcelableExtra("me"));
        mViewModel = new EditProfileViewModel(getApplicationContext(), me);
        mUserService = new UserService(getApplicationContext());

        Glide.with(getApplicationContext())
                .load(me.getIconUrl())
                .override(160, 160)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .thumbnail(0.5f)
                .placeholder(me.getIcon())
                .error(me.getIcon())
                .into(mImage);

        mBinding.setViewModel(mViewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @OnClick(R.id.change_password)
    public void changePassword(View v) {
        goTo(ChangePasswordActivity.class);
    }

    @Subscribe
    public void onEvent(FetchDataEvent<UserModel> event) {
        Snackbar.make(findViewById(R.id.container), "変更完了", Snackbar.LENGTH_SHORT).show();
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.image)
    public void showGallery(View v) {
        //カメラの起動Intentの用意
        String photoName = System.currentTimeMillis() + ".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, photoName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mUri);

        // ギャラリー用のIntent作成
        Intent intentGallery;
        intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
        intentGallery.setType("image/*");
        Intent intent = Intent.createChooser(intentCamera, "画像の選択");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentGallery});
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSER && resultCode == RESULT_OK) {
            Uri resultUri = (data != null) ? data.getData() : mUri;
            if (resultUri == null) {
                // 取得失敗
                Log.e("!!!", "取得失敗");
                return;
            }
            // ギャラリーへスキャンを促す
            MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    new String[]{resultUri.getPath()},
                    new String[]{"image/*"},
                    null
            );

            String scheme = resultUri.getScheme();
            if ("content".equals(scheme)) {

                String[] columns = {MediaStore.Images.Media.DATA};
                String id = DocumentsContract.getDocumentId(resultUri);
                String selection = "_id=?";
                String[] selectionArgs = new String[]{id.split(":")[1]};
                try (Cursor cursor_ = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns.DATA}, selection, selectionArgs, null)) {
                    String path = null;
                    if (cursor_ != null && cursor_.moveToFirst()) {
                        path = cursor_.getString(0);
                        if (path != null) {
                            File file = new File(path);
                            Matrix matrix = new Matrix();
                            matrix = getResizedMatrix(file, 512, matrix);
                            matrix = getRotatedMatrix(file, matrix);
                            Bitmap image = getCalculatedSquareBitmap(file, matrix);
                            mImage.setImageBitmap(image);

                            mUserService.uploadProfileIcon(file)
                                    .subscribe(response -> {
                                        if (response.isSuccess()) {
                                            Log.d("!!!", "Success to upload!!!");
//                                    EventBus.getDefault().post(new FetchDataEvent<>(UserModel.from(response.body())));
                                        } else {
                                            Log.e("!!!", "Fail to upload");
                                            Log.e("!!!", ApiUtil.getApiError(response).getMessage());
//                                    EventBus.getDefault().post(new ErrorEvent("パスワード変更失敗", ApiUtil.getApiError(response).getMessage()));
                                        }
                                    });
                            Log.e("!!!", "2");
                            return;
                        }
                    }
                }

//                //
//                Log.d("!!!", "path: " + resultUri.getPath());
//                for (String seg : resultUri.getPathSegments()) {
//                    Log.d("!!!", "seg: " + seg);
//                }
//                try (Cursor cursor = getContentResolver().query(resultUri, null, null, null, null, null)) {
//                    if (cursor != null && cursor.moveToFirst()) {
////                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
////                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//                        for (String columnName : cursor.getColumnNames()) {
//                            Log.d("!!!", columnName + ": " + cursor.getString(cursor.getColumnIndex(columnName)));
//                        }
//                        Bundle extras = cursor.getExtras();
//                        if (extras != null) {
//                            for (String key : extras.keySet()) {
//                                Log.d("!!!", "cursor.getExtra()[" + key + "]: " + extras.get(key));
//                            }
//                        } else {
//                            Log.e("!!!", "cursor.getExtra(): null");
//                        }
//                    }
//                }
//                        File createdImage = bitmapToFile(image, "yoshinani.jpg");
//                        if (createdImage != null) {
//                            Log.d("!!!!!", "stored: " + createdImage.getPath());
//                        } else {
//                            Log.d("!!!!!", "createdImage == null");
//                        }

                try {
                    Bitmap image = getBitmapFromUri(resultUri);
                    mImage.setImageBitmap(image);

                    mUserService.uploadProfileIcon(new File(resultUri.getPath()))
                            .subscribe(response -> {
                                if (response.isSuccess()) {
                                    Log.d("!!!", "Success to upload!!!");
//                                    EventBus.getDefault().post(new FetchDataEvent<>(UserModel.from(response.body())));
                                } else {
                                    Log.e("!!!", "Fail to upload");
                                    Log.e("!!!", ApiUtil.getApiError(response).getMessage());
//                                    EventBus.getDefault().post(new ErrorEvent("パスワード変更失敗", ApiUtil.getApiError(response).getMessage()));
                                }
                            });

                    Log.e("!!!", "3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("!!!", "選択されたファイルの種類が正しくありません");
                return;
            }
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    // google 公式APIより
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
//        return DocumentsContract.getDocumentThumbnail(getContentResolver(), uri, null, null);
//        try (ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r")) {
//        try (AssetFileDescriptor parcelFileDescriptor = getContentResolver().openAssetFileDescriptor(uri, "r")) {
        try (AssetFileDescriptor parcelFileDescriptor = getContentResolver().openTypedAssetFileDescriptor(uri, "image/*", new Bundle())) {
            final Bundle extras = parcelFileDescriptor.getExtras();
            final int orientation = (extras != null) ? extras.getInt(MediaStore.Images.ImageColumns.ORIENTATION, 0) : 0;
            final String EXTRA_ORIENTATION = "android.content.extra.ORIENTATION";
            final int orientation2 = (extras != null) ? extras.getInt(EXTRA_ORIENTATION, 0) : 0;

            if (extras != null) {
                for (String key : extras.keySet()) {
                    Log.d("!!!", "extras[" + key + "]: " + extras.get(key));
                }
            } else {
                Log.d("!!!", "extras: null");
            }
            Log.d("!!!", "orientation: " + orientation);
            Log.d("!!!", "orientation: " + orientation);

            DocumentsContract.getDocumentThumbnail(getContentResolver(), uri, null, null);
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            return image;
        }
    }

    private Matrix getResizedMatrix(File file, int maxPixel, Matrix matrix) {
        // リサイズチェック用にメタデータ読み込み
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        int height = options.outHeight;
        int width = options.outWidth;
        float scale = Math.max((float) maxPixel / width, (float) maxPixel / height);
        // 縮小のみのため、scaleは1.0未満の場合のみマトリクス設定
        if (scale < 1.0) {
            matrix.postScale(scale, scale);
        }
        return matrix;
    }

    private Matrix getRotatedMatrix(File file, Matrix matrix) {
        ExifInterface exifInterface;
        try {
            exifInterface = new ExifInterface(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return matrix;
        }

        // 画像を回転させる処理をマトリクスに追加
        switch (exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_UNDEFINED)) {
            case ORIENTATION_UNDEFINED:
            case ORIENTATION_NORMAL:
                break;
            case ORIENTATION_FLIP_HORIZONTAL:
                matrix.postScale(-1f, 1f);
                break;
            case ORIENTATION_ROTATE_180:
                matrix.postRotate(180f);
                break;
            case ORIENTATION_FLIP_VERTICAL:
                matrix.postScale(1f, -1f);
                break;
            case ORIENTATION_ROTATE_90:
                matrix.postRotate(90f);
                break;
            case ORIENTATION_TRANSVERSE:
                matrix.postRotate(-90f);
                matrix.postScale(1f, -1f);
                break;
            case ORIENTATION_TRANSPOSE:
                matrix.postRotate(90f);
                matrix.postScale(1f, -1f);
                break;
            case ORIENTATION_ROTATE_270:
                matrix.postRotate(-90f);
                break;
        }
        return matrix;
    }

    private Bitmap getCalculatedSquareBitmap(File file, Matrix matrix) {
        // 元画像の取得
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        // 元画像の取得
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int size = Math.min(width, height);

        // マトリクスをつけることで縮小、向きを反映した画像を生成
        Bitmap resizedPicture = Bitmap.createBitmap(bitmap, (width - size) / 2, (height - size) / 2, size, size, matrix, true);
        return resizedPicture;
    }

    private File bitmapToFile(Bitmap bitmap, String filename) {
        File file = new File(filename);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


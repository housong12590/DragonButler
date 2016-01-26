package com.aosijia.dragonbutler.imagegroup.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.adapter.ImageAdapter;
import com.aosijia.dragonbutler.imagegroup.model.Image;
import com.aosijia.dragonbutler.imagegroup.model.ImageFolder;
import com.aosijia.dragonbutler.imagegroup.view.CustomPopupView;
import com.aosijia.dragonbutler.imagegroup.view.DividerItemImagesDecoration;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, CustomPopupView.FolderItemSelectListener, ImageAdapter.OnImageClickListener, View.OnClickListener {

    private static final int LOADER_ID_FOLDER = 10001;

    private RecyclerView mRecyclerView;
    private CustomPopupView mCustomPopupWindowView;
    private ViewAnimator mViewAnimator;
    private ImageAdapter mImageAdapter;
    private List<Image> mSelectedImages;
    private int mMaxSelectedNum;
    private TextView mTextSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_album);
        setTitle("选择照片", null, R.drawable.btn_back, getResources().getString(R.string.action_submit), NO_RES_ID);
        parseIntent();
        mSelectedImages = new ArrayList<>();
        setUpView();
        mTextSubmit = titleRighttextview;
        setBtnRightTextOnClickListener(this);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parseIntent() {
        Intent intent = getIntent();
        mMaxSelectedNum = intent.getIntExtra(NavigatorImage.EXTRA_IMAGE_SELECT_MAX_NUM, 0);
    }


    private void updateSubmitText() {
        mTextSubmit.setEnabled(mSelectedImages.size() > 0);
        mTextSubmit.setText(getSubmitText());
    }

    private String getSubmitText() {
        return mSelectedImages.size() == 0
                ? getResources().getString(R.string.action_submit)
                :
                mMaxSelectedNum == 0
                        ? getResources().getString(R.string.action_submit_string_no_max, mSelectedImages.size())
                        : getResources().getString(R.string.action_submit_string, mSelectedImages.size(), mMaxSelectedNum);
    }


    private void finishWithResult() {
        Intent intent = getIntent();
        intent.putStringArrayListExtra(NavigatorImage.EXTRA_PHOTOS_URL, createUrls(mSelectedImages));
        setResult(RESULT_OK, intent);
        finish();
    }

    private ArrayList<String> createUrls(List<Image> selectedImages) {
        ArrayList<String> results = new ArrayList<>();
        for (Image image : selectedImages) {
            results.add(image.url);
        }
        return results;
    }

    private void setUpView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_album);
        mViewAnimator = (ViewAnimator) findViewById(R.id.view_album_animator);
        mCustomPopupWindowView = (CustomPopupView) findViewById(R.id.view_popup_folder_window);

        setUpTextView();
        mCustomPopupWindowView.setFolderItemSelectListener(this);
        showProgressView();
        setUpRecyclerView();
    }

    private void setUpTextView() {
        mCustomPopupWindowView.setNumText(getString(R.string.album_all));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(LOADER_ID_FOLDER, null, this);
    }

    private void updateContentView(ImageFolder floder) {
        if (floder.images.size() == 0) {
            showEmptyView();
        } else {
            showContentView();
        }
        mImageAdapter.updateFolderImageData(floder);
        mRecyclerView.scrollToPosition(0);
    }

    private void showContentView() {
        mViewAnimator.setDisplayedChild(2);
    }

    private void showEmptyView() {
        mViewAnimator.setDisplayedChild(1);
    }

    private void showProgressView() {
        mViewAnimator.setDisplayedChild(0);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(
                new DividerItemImagesDecoration(
                        getResources().getDimensionPixelSize(R.dimen.inline_padding)));
        mRecyclerView.setPadding(
                getResources().getDimensionPixelSize(R.dimen.inline_padding) / 2,
                0,
                getResources().getDimensionPixelSize(R.dimen.inline_padding) / 2,
                0
        );
        mImageAdapter = new ImageAdapter(this);
        mImageAdapter.setOnImageClickListener(this);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        CursorLoader cursorLoader = new CursorLoader(this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, NavigatorImage.IMAGE_PROJECTION,
                null, null, NavigatorImage.IMAGE_PROJECTION[2] + " DESC");
        return cursorLoader;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        doParseData(data);
    }

    private void doParseData(Cursor cursor) {
        new AsyncTask<Cursor, Void, List>() {

            @Override
            protected List doInBackground(Cursor... params) {
                Cursor data = params[0];
                if (data != null) {
                    List<ImageFolder> folders = new ArrayList();
                    int count = data.getCount();
                    if (count > 0) {
                        data.moveToFirst();
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(NavigatorImage.IMAGE_PROJECTION[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(NavigatorImage.IMAGE_PROJECTION[1]));
                            long dateTime = data.getLong(data.getColumnIndexOrThrow(NavigatorImage.IMAGE_PROJECTION[2]));
                            Image image = new Image(path, name, dateTime);

                            File imageFile = new File(path);
                            if(!imageFile.isFile() || !imageFile.exists()){
                                continue;
                            }
                            File folderFile = imageFile.getParentFile();
                            ImageFolder folder = new ImageFolder();
                            folder.name = folderFile.getName();
                            folder.dir = folderFile.getAbsolutePath();
                            folder.firstImagePath = path;
                            if (!folders.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.count++;
                                folder.images = imageList;
                                folders.add(folder);
                            } else {
                                ImageFolder f = folders.get(folders.indexOf(folder));
                                f.images.add(image);
                                f.count++;
                            }
                        } while (data.moveToNext());

                        return folders;
                    }
                }

                return new ArrayList();
            }

            @Override
            protected void onPostExecute(List list) {
                super.onPostExecute(list);
                mCustomPopupWindowView.updateFolderData(createFoldersWithAllImageFolder(list));
            }
        }.execute(cursor);
    }

    private List createFoldersWithAllImageFolder(List<ImageFolder> folders) {
        if (folders.size() > 0) {
            ImageFolder folder = new ImageFolder();
            folder.name = getResources().getString(R.string.album_all);
            folder.dir = null;
            folder.firstImagePath = folders.get(0).firstImagePath;
            int imageCount = 0;
            for (ImageFolder imageFolder : folders) {
                imageCount += imageFolder.count;
                folder.images.addAll(imageFolder.images);
            }
            folder.count = imageCount;
            folders.add(0, folder);
        }
        return folders;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onFolderItemSelected(ImageFolder imageFolder) {
        updateContentView(imageFolder);
    }

    @Override
    public void onImageSelected(Image image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
        } else if (mSelectedImages.size() == mMaxSelectedNum && mMaxSelectedNum != 0) {
            return;
        } else {
            mSelectedImages.add(image);
        }
        mImageAdapter.updateSelectImages(mSelectedImages);
        updateSubmitText();
    }

    @Override
    public void onCameraSelected() {
        startCamera();
    }

    private void startCamera() {
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            ActivityCompat.startActivityForResult(AlbumActivity.this,
                    new Intent(AlbumActivity.this, UserCameraActivity.class), NavigatorImage.RESULT_TAKE_PHOTO,
                    null);
        } else {
            Toast.makeText(AlbumActivity.this, "内存卡不存在", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) return;
        String photoTakeUrl = data.getStringExtra(NavigatorImage.EXTRA_PHOTO_URL);
        if (requestCode == NavigatorImage.RESULT_TAKE_PHOTO && null != photoTakeUrl) {
            mSelectedImages.add(new Image(photoTakeUrl));
        }
        finishWithResult();
    }

    @Override
    public void onClick(View v) {
        finishWithResult();
    }
}

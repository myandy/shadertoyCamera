package com.anddymao.shadertoy.activity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.anddymao.shadertoy.R;
import com.anddymao.shadertoy.adapter.FilterAdapter;
import com.anddymao.shadertoy.filter.EmptyGPUImageFilter;
import com.anddymao.shadertoy.filter.FilterFactory;
import com.anddymao.shadertoy.filter.FilterItem;
import com.anddymao.shadertoy.filterlibrary.FilterSDK;
import com.anddymao.shadertoy.filterlibrary.base.GPUImageFilter;
import com.anddymao.shadertoy.gpuimage.GPUImage;
import com.anddymao.shadertoy.utils.SaveUtils;

import java.io.File;
import java.io.IOException;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private GLSurfaceView mGLSurfaceView;

    private RecyclerView mRecyclerView;
    private FilterAdapter mAdapter;
    private GPUImage mGPUImage;

    private Bitmap mBitmap;
    private FilterItem mFilterItem;

    private Button mSaveButton;

    private SeekBar mSeekBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mSaveButton = findViewById(R.id.save);
        mSaveButton.setEnabled(false);
        mSaveButton.setOnClickListener(this);
        mGLSurfaceView = findViewById(R.id.surface_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new FilterAdapter(this, FilterFactory.getPortraitFilterItem());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);
        mSeekBar = findViewById(R.id.seekbar);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGLSurfaceView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.x * 4 / 3;
        mGLSurfaceView.setLayoutParams(params);

        mGPUImage = new GPUImage(this);
        mGPUImage.setBackgroundColor(1.0f, 1.0f, 1.0f);

        try {
            mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), getIntent().getData());
            mGPUImage.setImage(mBitmap);
            mGPUImage.setGLSurfaceView(mGLSurfaceView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFilterItem.progress = progress;
                GPUImageFilter filter = mFilterItem.instantiate();
                mSaveButton.setEnabled(!(filter instanceof EmptyGPUImageFilter));
                mGPUImage.setFilter(filter);
                mGPUImage.requestRender();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private FilterAdapter.onFilterChangeListener onFilterChangeListener = new FilterAdapter.onFilterChangeListener() {

        @Override
        public void onFilterChanged(FilterItem filterItem) {
            if (mFilterItem == filterItem) {
                mSeekBar.setProgress(filterItem.progress);
                mSeekBar.setVisibility(mSeekBar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            } else {
                mFilterItem = filterItem;
                mSeekBar.setProgress(filterItem.progress);
                GPUImageFilter filter = filterItem.instantiate();
                mSaveButton.setEnabled(!(filter instanceof EmptyGPUImageFilter));
                mGPUImage.setFilter(filter);
                mGPUImage.requestRender();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                new SaveAsyncTask().execute();
                break;
            default:
                break;
        }
    }

    private class SaveAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            final File file = new File(FilterSDK.sContext.getExternalFilesDir(null), "editor-" + System.currentTimeMillis() / 1000 + ".jpg");
            SaveUtils.saveBitmap(FilterSDK.sContext, mBitmap, mFilterItem.instantiate(), file);
            mSaveButton.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FilterSDK.sContext, "save:" + file, Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }
}

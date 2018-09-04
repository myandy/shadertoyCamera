package com.anddymao.shadertoy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anddymao.shadertoy.R;
import com.anddymao.shadertoy.camera2.CameraFragment;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance())
                    .commit();
        }
    }

}

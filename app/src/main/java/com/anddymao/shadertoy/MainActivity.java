package com.anddymao.shadertoy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anddymao.shadertoy.activity.CameraActivity;
import com.anddymao.shadertoy.activity.EditorActivity;
import com.anddymao.shadertoy.filterlibrary.FilterSDK;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_PHOTO = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FilterSDK.init(getApplicationContext());
        findViewById(R.id.button_camera).setOnClickListener(this);
        findViewById(R.id.button_album).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.button_album:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PHOTO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Uri imageUri = data.getData();
            Log.e("TAG", imageUri.toString());
            Intent intent = new Intent(this, EditorActivity.class);
            intent.setData(imageUri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

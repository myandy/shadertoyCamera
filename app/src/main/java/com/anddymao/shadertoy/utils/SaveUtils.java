package com.anddymao.shadertoy.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.anddymao.shadertoy.gpuimage.GPUImage;
import com.anddymao.shadertoy.filterlibrary.base.GPUImageFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveUtils {

    public static void saveBitmap(Context context, Bitmap bitmap, GPUImageFilter filter, File file) {
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setFilter(filter);
        bitmap = Bitmaps.ensureBitmapSize(bitmap);
        gpuImage.setImage(bitmap);
        bitmap = gpuImage.getBitmapWithFilterApplied(true);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

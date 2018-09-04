package com.anddymao.shadertoy.gpuimage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

import com.anddymao.shadertoy.filterlibrary.base.GPUImageFilter;
import com.anddymao.shadertoy.filterlibrary.utils.Rotation;


/**
 * The main accessor for GPUImage functionality. This class helps to do common
 * tasks through a simple interface.
 */
public class GPUImage {
    private final GPUImageRenderer mRenderer;
    private GLSurfaceView mGlSurfaceView;
    private GPUImageFilter mFilter;
    private Bitmap mCurrentBitmap;
    private ScaleType mScaleType = ScaleType.CENTER_INSIDE;

    /**
     * Instantiates a new GPUImage object.
     *
     * @param context the context
     */
    public GPUImage(final Context context) {
        if (!supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        mFilter = new GPUImageFilter();
        mRenderer = new GPUImageRenderer(mFilter);
        mRenderer.setDrawBoundLines(true);
    }

    /**
     * Checks if OpenGL ES 2.0 is supported on the current device.
     *
     * @param context the context
     * @return true, if successful
     */
    private boolean supportsOpenGLES2(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }

    /**
     * Sets the GLSurfaceView which will display the preview.
     *
     * @param view the GLSurfaceView
     */
    public void setGLSurfaceView(final GLSurfaceView view) {
        mGlSurfaceView = view;
        mGlSurfaceView.setEGLContextClientVersion(2);
        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceView.setZOrderOnTop(true);
        mGlSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        mGlSurfaceView.setRenderer(mRenderer);
        mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mGlSurfaceView.requestRender();
    }

    /**
     * Sets the background color
     *
     * @param red   red color value
     * @param green green color value
     * @param blue  red color value
     */
    public void setBackgroundColor(float red, float green, float blue) {
        mRenderer.setBackgroundColor(red, green, blue);
    }

    /**
     * Request the preview to be rendered again.
     */
    public void requestRender() {
        if (mGlSurfaceView != null) {
            mGlSurfaceView.requestRender();
        }
    }

    /**
     * Sets the mFilter which should be applied to the image which was (or will
     * be) set by setImage(...).
     *
     * @param filter the new mFilter
     */
    public void setFilter(final GPUImageFilter filter) {
        mFilter = filter;
        mRenderer.setFilter(mFilter);
    }

    /**
     * Sets the image on which the mFilter should be applied.
     *
     * @param bitmap the new image
     */
    public void setImage(final Bitmap bitmap) {
        if (mCurrentBitmap != bitmap) {
            mCurrentBitmap = bitmap;
            mRenderer.setImageBitmap(bitmap, false);
        }
    }

    /**
     * This sets the scale type of GPUImage. This has to be run before setting the image.
     * If image is set and scale type changed, image needs to be reset.
     *
     * @param scaleType The new ScaleType
     */
    public void setScaleType(ScaleType scaleType) {
        mScaleType = scaleType;
        mRenderer.setScaleType(scaleType);
        mRenderer.deleteImage();
        mCurrentBitmap = null;
        requestRender();
    }

    /**
     * Sets the rotation of the displayed image.
     *
     * @param rotation new rotation
     */
    public void setRotation(Rotation rotation) {
        mRenderer.setRotation(rotation);
    }

    /**
     * Sets the rotation of the displayed image with flip options.
     *
     * @param rotation new rotation
     */
    public void setRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        mRenderer.setRotation(rotation, flipHorizontal, flipVertical);
    }

    /**
     * Deletes the current image.
     */
    public void deleteImage() {
        mRenderer.deleteImage();
        mCurrentBitmap = null;
        requestRender();
    }

    /**
     * Gets the current displayed image with applied mFilter as a Bitmap.
     *
     * @return the current image with mFilter applied
     */
    public Bitmap getBitmapWithFilterApplied(boolean recycle) {
        return getBitmapWithFilterApplied(mCurrentBitmap, recycle);
    }

    /**
     * Gets the given bitmap with current mFilter applied as a Bitmap.
     *
     * @param bitmap the bitmap on which the current mFilter should be applied
     * @return the bitmap with mFilter applied
     */
    public Bitmap getBitmapWithFilterApplied(final Bitmap bitmap, boolean recycle) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        GPUImageRenderer renderer = new GPUImageRenderer(mFilter);
        //copyPixelsFromBuffer会导致反向,所以竖直方向取反
        renderer.setRotation(Rotation.NORMAL,
                mRenderer.isFlippedHorizontally(), !mRenderer.isFlippedVertically());
        renderer.setScaleType(mScaleType);
        PixelBuffer buffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        renderer.setImageBitmap(bitmap, false);
        buffer.setRenderer(renderer);
        renderer.setDrawBoundLines(false);

        //通过重复利用bitmap,renderer的recycle直接设置为false
        Bitmap result = buffer.getBitmap(recycle ? bitmap : null);
        mFilter.destroy();
        renderer.deleteImage();
        buffer.destroy();
        mRenderer.setFilter(mFilter);
        return result;
    }

    public enum ScaleType {CENTER_INSIDE, CENTER_CROP}
}
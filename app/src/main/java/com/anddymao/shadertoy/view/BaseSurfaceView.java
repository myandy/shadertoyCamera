package com.anddymao.shadertoy.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.anddymao.shadertoy.filter.EmptyGPUImageFilter;
import com.anddymao.shadertoy.filter.FilterItem;
import com.anddymao.shadertoy.filterlibrary.base.GPUImageFilter;
import com.anddymao.shadertoy.filterlibrary.utils.OpenGlUtils;
import com.anddymao.shadertoy.filterlibrary.utils.Rotation;
import com.anddymao.shadertoy.filterlibrary.utils.TextureRotationUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {
    /**
     * 所选择的滤镜，类型为MagicBaseGroupFilter
     * 1.mCameraInputFilter将SurfaceTexture中YUV数据绘制到FrameBuffer
     * 2.filter将FrameBuffer中的纹理绘制到屏幕中
     */
    protected GPUImageFilter mFilter;

    /**
     * SurfaceTexure纹理id
     */
    protected int textureId = OpenGlUtils.NO_TEXTURE;

    /**
     * 顶点坐标
     */
    protected final FloatBuffer gLCubeBuffer;

    /**
     * 纹理坐标
     */
    protected final FloatBuffer gLTextureBuffer;

    /**
     * GLSurfaceView的宽高
     */
    protected int surfaceWidth, surfaceHeight;

    /**
     * 图像宽高
     */
    protected int imageWidth, imageHeight;

    protected ScaleType scaleType = ScaleType.FIT_XY;

    private FilterItem mFilterItem;

    public BaseSurfaceView(Context context) {
        this(context, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gLCubeBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        gLCubeBuffer.put(TextureRotationUtil.CUBE).position(0);

        gLTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        gLTextureBuffer.put(TextureRotationUtil.TEXTURE_NO_ROTATION).position(0);

        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glDisable(GL10.GL_DITHER);
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        surfaceWidth = width;
        surfaceHeight = height;
        onFilterChanged();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    protected void onFilterChanged() {
        if (mFilter != null) {
            mFilter.onDisplaySizeChanged(surfaceWidth, surfaceHeight);
            mFilter.onInputSizeChanged(imageWidth, imageHeight);
        }
    }

    public void setFilter(final FilterItem filterItem) {
        mFilterItem = filterItem;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                if (mFilter != null)
                    mFilter.destroy();
                mFilter = filterItem.instantiate();
                if (mFilter != null)
                    mFilter.init();
                onFilterChanged();
            }
        });
        requestRender();
    }

    public GPUImageFilter getFilter() {
        GPUImageFilter filter = null;
        if (mFilterItem != null) {
            filter = mFilterItem.instantiate();
        }
        if (filter != null && filter instanceof EmptyGPUImageFilter) {
            return null;
        }
        return filter;
    }

    protected void deleteTextures() {
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    GLES20.glDeleteTextures(1, new int[]{
                            textureId
                    }, 0);
                    textureId = OpenGlUtils.NO_TEXTURE;
                }
            });
        }
    }

    protected void adjustSize(int rotation, boolean flipHorizontal, boolean flipVertical) {
        float[] textureCords = TextureRotationUtil.getRotation(Rotation.fromInt(rotation),
                flipHorizontal, flipVertical);
        float[] cube = TextureRotationUtil.CUBE;
        float ratio1 = (float) surfaceWidth / imageWidth;
        float ratio2 = (float) surfaceHeight / imageHeight;
        float ratioMax = Math.max(ratio1, ratio2);
        int imageWidthNew = Math.round(imageWidth * ratioMax);
        int imageHeightNew = Math.round(imageHeight * ratioMax);

        float ratioWidth = imageWidthNew / (float) surfaceWidth;
        float ratioHeight = imageHeightNew / (float) surfaceHeight;

        if (scaleType == ScaleType.CENTER_INSIDE) {
            cube = new float[]{
                    TextureRotationUtil.CUBE[0] / ratioHeight, TextureRotationUtil.CUBE[1] / ratioWidth,
                    TextureRotationUtil.CUBE[2] / ratioHeight, TextureRotationUtil.CUBE[3] / ratioWidth,
                    TextureRotationUtil.CUBE[4] / ratioHeight, TextureRotationUtil.CUBE[5] / ratioWidth,
                    TextureRotationUtil.CUBE[6] / ratioHeight, TextureRotationUtil.CUBE[7] / ratioWidth,
            };
        } else if (scaleType == ScaleType.FIT_XY) {

        } else if (scaleType == ScaleType.CENTER_CROP) {
            float distHorizontal = (1 - 1 / ratioWidth) / 2;
            float distVertical = (1 - 1 / ratioHeight) / 2;
            textureCords = new float[]{
                    addDistance(textureCords[0], distVertical), addDistance(textureCords[1], distHorizontal),
                    addDistance(textureCords[2], distVertical), addDistance(textureCords[3], distHorizontal),
                    addDistance(textureCords[4], distVertical), addDistance(textureCords[5], distHorizontal),
                    addDistance(textureCords[6], distVertical), addDistance(textureCords[7], distHorizontal),
            };
        }
        gLCubeBuffer.clear();
        gLCubeBuffer.put(cube).position(0);
        gLTextureBuffer.clear();
        gLTextureBuffer.put(textureCords).position(0);
    }

    private float addDistance(float coordinate, float distance) {
        return coordinate == 0.0f ? distance : 1 - distance;
    }

    public enum ScaleType {
        CENTER_INSIDE,
        CENTER_CROP,
        FIT_XY
    }
}

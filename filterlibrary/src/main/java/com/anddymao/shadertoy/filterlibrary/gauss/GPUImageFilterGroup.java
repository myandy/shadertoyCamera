package com.anddymao.shadertoy.filterlibrary.gauss;

import android.annotation.SuppressLint;
import android.opengl.GLES20;

import com.anddymao.shadertoy.filterlibrary.base.BaseOriginalFilter;
import com.anddymao.shadertoy.filterlibrary.base.GPUImageFilter;
import com.anddymao.shadertoy.filterlibrary.utils.OpenGlUtils;
import com.anddymao.shadertoy.filterlibrary.utils.Rotation;
import com.anddymao.shadertoy.filterlibrary.utils.TextureRotationUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.anddymao.shadertoy.filterlibrary.utils.TextureRotationUtil.CUBE;
import static com.anddymao.shadertoy.filterlibrary.utils.TextureRotationUtil.TEXTURE_NO_ROTATION;


/**
 * Resembles a filter that consists of multiple filters applied after each
 * other.
 */
public class GPUImageFilterGroup extends BaseOriginalFilter {

    private List<GPUImageFilter> filters;
    private List<GPUImageFilter> mergedFilters;
    private int[] frameBuffers;
    private int[] frameBufferTextures;

    private final FloatBuffer glCubeBuffer;
    private final FloatBuffer glTextureBuffer;
    private final FloatBuffer glTextureFlipBuffer;

    /**
     * Instantiates a new GPUImageFilterGroup with no filters.
     */
    public GPUImageFilterGroup() {
        this(null);
    }

    /**
     * Instantiates a new GPUImageFilterGroup with the given filters.
     *
     * @param filters the filters which represent this filter
     */
    public GPUImageFilterGroup(List<GPUImageFilter> filters) {
        this.filters = filters;
        if (this.filters == null) {
            this.filters = new ArrayList<>();
        } else {
            updateMergedFilters();
        }

        glCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glCubeBuffer.put(CUBE).position(0);

        glTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glTextureBuffer.put(TEXTURE_NO_ROTATION).position(0);

        float[] flipTexture = TextureRotationUtil.getRotation(Rotation.NORMAL, false, true);
        glTextureFlipBuffer = ByteBuffer.allocateDirect(flipTexture.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glTextureFlipBuffer.put(flipTexture).position(0);
    }

    public void addFilter(GPUImageFilter aFilter) {
        if (aFilter == null) {
            return;
        }
        filters.add(aFilter);
        updateMergedFilters();
    }

    /*
     * (non-Javadoc)
     * @see jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter#onInit()
     */
    @Override
    public void onInit() {
        super.onInit();
        for (GPUImageFilter filter : filters) {
            filter.onInit();
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter#onDestroy()
     */
    @Override
    public void onDestroy() {
        destroyFramebuffers();
        for (GPUImageFilter filter : filters) {
            filter.destroy();
        }
        super.onDestroy();
    }

    private void destroyFramebuffers() {
        if (frameBufferTextures != null) {
            GLES20.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);
            frameBufferTextures = null;
        }
        if (frameBuffers != null) {
            GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
            frameBuffers = null;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter#onOutputSizeChanged(int,
     * int)
     */
    @Override
    public void onDisplaySizeChanged(final int width, final int height) {
        super.onDisplaySizeChanged(width, height);
        if (frameBuffers != null) {
            destroyFramebuffers();
        }

        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filters.get(i).onDisplaySizeChanged(width, height);
        }

        if (mergedFilters != null && mergedFilters.size() > 0) {
            size = mergedFilters.size();
            frameBuffers = new int[size - 1];
            frameBufferTextures = new int[size - 1];

            for (int i = 0; i < size - 1; i++) {
                GLES20.glGenFramebuffers(1, frameBuffers, i);
                GLES20.glGenTextures(1, frameBufferTextures, i);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTextures[i]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter#onDraw(int,
     * java.nio.FloatBuffer, java.nio.FloatBuffer)
     */
    @SuppressLint("WrongCall")
    @Override
    public int onDrawFrame(final int textureId, final FloatBuffer cubeBuffer,
                       final FloatBuffer textureBuffer) {
        runPendingOnDrawTasks();
        if (!isInitialized() || frameBuffers == null || frameBufferTextures == null) {
            return OpenGlUtils.NOT_INIT;
        }
        if (mergedFilters != null) {
            int size = mergedFilters.size();
            int previousTexture = textureId;
            for (int i = 0; i < size; i++) {
                GPUImageFilter filter = mergedFilters.get(i);
                boolean isNotLast = i < size - 1;
                if (isNotLast) {
                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                    GLES20.glClearColor(0, 0, 0, 0);
                }

                if (i == 0) {
                    filter.onDrawFrame(previousTexture, cubeBuffer, textureBuffer);
                } else if (i == size - 1) {
                    filter.onDrawFrame(previousTexture, glCubeBuffer, (size % 2 == 0) ? glTextureFlipBuffer : glTextureBuffer);
                } else {
                    filter.onDrawFrame(previousTexture, glCubeBuffer, glTextureBuffer);
                }

                if (isNotLast) {
                    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                    previousTexture = frameBufferTextures[i];
                }
            }
        }
        return OpenGlUtils.ON_DRAWN;
    }

    /**
     * Gets the filters.
     *
     * @return the filters
     */
    public List<GPUImageFilter> getFilters() {
        return filters;
    }

    public List<GPUImageFilter> getMergedFilters() {
        return mergedFilters;
    }

    public void updateMergedFilters() {
        if (filters == null) {
            return;
        }

        if (mergedFilters == null) {
            mergedFilters = new ArrayList<>();
        } else {
            mergedFilters.clear();
        }

        List<GPUImageFilter> filters;
        for (GPUImageFilter filter : this.filters) {
            if (filter instanceof GPUImageFilterGroup) {
                ((GPUImageFilterGroup) filter).updateMergedFilters();
                filters = ((GPUImageFilterGroup) filter).getMergedFilters();
                if (filters == null || filters.isEmpty())
                    continue;
                mergedFilters.addAll(filters);
                continue;
            }
            mergedFilters.add(filter);
        }
    }
}
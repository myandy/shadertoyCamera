package com.anddymao.shadertoy.filter;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.anddymao.shadertoy.R;
import com.anddymao.shadertoy.filterlibrary.FilterSDK;
import com.anddymao.shadertoy.filterlibrary.base.GPUImageFilter;
import com.anddymao.shadertoy.filterlibrary.utils.OpenGlUtils;

import java.nio.FloatBuffer;

public class OesTextureFilter extends GPUImageFilter {
    private float[] mTextureTransformMatrix;
    private int mTextureTransformMatrixLocation;

    public OesTextureFilter() {
        super(OpenGlUtils.readShaderFromRawResource(FilterSDK.sContext, R.raw.default_vertex), OpenGlUtils.readShaderFromRawResource(FilterSDK.sContext, R.raw.oes_fragment_sharder));
    }

    public void onInit() {
        super.onInit();
        mTextureTransformMatrixLocation = GLES20.glGetUniformLocation(mGLProgId, "textureTransform");
    }

    @Override
    public void setTextureTransformMatrix(float[] mtx) {
        mTextureTransformMatrix = mtx;
    }

    @Override
    protected void onDrawArraysPre() {
        if (mTextureTransformMatrix != null) {
            GLES20.glUniformMatrix4fv(mTextureTransformMatrixLocation, 1, false, mTextureTransformMatrix, 0);
        }
    }

    @Override
    public int getInitTextureId() {
        mTextureId = OpenGlUtils.getExternalOESTextureID();
        return mTextureId;
    }

    @Override
    public int onDrawFrame(final int textureId, final FloatBuffer cubeBuffer,
                           final FloatBuffer textureBuffer) {
        GLES20.glUseProgram(mGLProgId);
        runPendingOnDrawTasks();
        if (!mIsInitialized) {
            return OpenGlUtils.NOT_INIT;
        }
        cubeBuffer.position(0);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);

        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        onDrawArraysAfter();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return OpenGlUtils.ON_DRAWN;
    }
}
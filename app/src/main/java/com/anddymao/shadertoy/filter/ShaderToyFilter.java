package com.anddymao.shadertoy.filter;

import android.opengl.GLES20;

import com.anddymao.shadertoy.R;
import com.anddymao.shadertoy.filterlibrary.FilterSDK;
import com.anddymao.shadertoy.filterlibrary.base.BaseOriginalFilter;
import com.anddymao.shadertoy.filterlibrary.utils.OpenGlUtils;

import java.nio.FloatBuffer;

/**
 * Created by AndyMao on 18-6-1.
 */

public class ShaderToyFilter extends BaseOriginalFilter {

    private int[] inputTextureHandles = {-1};
    private int[] inputTextureUniformLocations = {-1};

    final long START_TIME = System.currentTimeMillis();

    public ShaderToyFilter(int shade) {
        super(NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(FilterSDK.sContext, shade));
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();

        inputTextureUniformLocations[0] = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture2");
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                int iResolutionLocation = GLES20.glGetUniformLocation(mGLProgId, "iResolution");
                GLES20.glUniform3fv(iResolutionLocation, 1,
                        FloatBuffer.wrap(new float[]{(float) 1.0, (float) 1, 1.0f}));
//                int iFrameLocation = GLES20.glGetUniformLocation(mGLProgId, "iFrame");
//                GLES20.glUniform1i(iFrameLocation, iFrame);
                inputTextureHandles[0] = OpenGlUtils.loadTexture(FilterSDK.sContext, R.raw.edge_png, new int[2]);
            }
        });
    }

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        float time = ((float) (System.currentTimeMillis() - START_TIME)) / 1000.0f;
        int iTimeLocation = GLES20.glGetUniformLocation(mGLProgId, "iTime");
        GLES20.glUniform1f(iTimeLocation, time);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (3));
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[0]);
        GLES20.glUniform1i(inputTextureUniformLocations[0], 3);
    }
}

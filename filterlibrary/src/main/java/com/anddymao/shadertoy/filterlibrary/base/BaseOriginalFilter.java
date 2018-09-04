package com.anddymao.shadertoy.filterlibrary.base;

public class BaseOriginalFilter extends GPUImageFilter implements FilterDegreeAdjustController {
    private static final String TAG = "BaseOriginalFilter";

    protected static final int DEGREE_VALUE_MIN = 0;
    protected static final int DEGREE_VALUE_MAX = 100;

    protected int mDegree;

    public BaseOriginalFilter() {
        super();
    }

    public BaseOriginalFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
    }

    @Override
    public boolean isDegreeAdjustSupported() {
        return false;
    }

    @Override
    public void setDegree(int degree) {
        if (!isDegreeAdjustSupported()) {
            throw new AssertionError("Degree adjustment of the filter is not supported!");
        }

        if (degree < DEGREE_VALUE_MIN) {
            degree = DEGREE_VALUE_MIN;
        }

        if (degree > DEGREE_VALUE_MAX) {
            degree = DEGREE_VALUE_MAX;
        }

        mDegree = degree;
    }
}

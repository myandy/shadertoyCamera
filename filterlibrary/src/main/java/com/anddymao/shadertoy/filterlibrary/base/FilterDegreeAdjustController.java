package com.anddymao.shadertoy.filterlibrary.base;

public interface FilterDegreeAdjustController {
    /**
     * Returns whether filter is supported for degree adjustment.
     */
    boolean isDegreeAdjustSupported();

    /**
     * Set the filter degree.
     * param degree varies from 0 to 100
     */
    void setDegree(int degree);
}

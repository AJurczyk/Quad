package com.ajurczyk.software.regulators;

/**
 * @author aleksander.jurczyk@gmail.com on 16.09.16.
 */
public interface Iregulator {

    /**
     * Calculates regulation for given values.
     *
     * @param currentValue current value
     * @param desiredValue desired value
     * @return regulation
     */
    float getRegulation(float currentValue, float desiredValue);

    /**
     * Set proportional factor.
     */
    void setP(float factor);

    /**
     * Set integral factor.
     */
    void setI(float factor);

    /**
     * set derivative factor.
     */
    void setD(float factor);
}

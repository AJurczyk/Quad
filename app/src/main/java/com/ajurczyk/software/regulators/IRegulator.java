package com.ajurczyk.software.regulators;

/**
 * @author aleksander.jurczyk@gmail.com on 16.09.16.
 */
public interface IRegulator {

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
    void setProportional(float factor);

    /**
     * Set integral factor.
     */
    void setIntegral(float factor);

    /**
     * set derivative factor.
     */
    void setDerivative(float factor);
}

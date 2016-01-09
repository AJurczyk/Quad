package hardware.impl;

/**
 * @author aleksander.jurczyk@gmail.com on 06.01.16.
 */
public abstract class AbstractPwm {
    protected float duty;

    protected int periodMs;

    public float getFrequency() {
        return (1 / periodMs) * 1000;
    }

    public int getPeriodMs() {
        return periodMs;
    }

    public float getDuty() { return duty; }
}

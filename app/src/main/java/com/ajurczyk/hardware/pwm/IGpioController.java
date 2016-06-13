package com.ajurczyk.hardware.pwm;

/**
 * @author aleksander.jurczyk@gmail.com on 08.01.16.
 */
public interface IGpioController {

    void pwmSetClock(int value);

    void pwmWrite(int pin, int value);

    void pwmSetRange(int value);

    void pinMode(int pin, int mode);

    void pwmSetMode(int mode);
}

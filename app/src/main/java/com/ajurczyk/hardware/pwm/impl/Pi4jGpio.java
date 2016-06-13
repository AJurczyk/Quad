package com.ajurczyk.hardware.pwm.impl;

import com.ajurczyk.hardware.pwm.IGpioController;
import com.pi4j.wiringpi.Gpio;

/**
 * @author aleksander.jurczyk@gmail.com on 08.01.16.
 */
public class Pi4jGpio implements IGpioController {
    Pi4jGpio() {
        Gpio.wiringPiSetup();
    }

    @Override
    public void pwmSetClock(int value) {
        Gpio.pwmSetClock(value);
    }

    @Override
    public void pwmWrite(int pin, int value) {
        Gpio.pwmWrite(pin, value);
    }

    @Override
    public void pwmSetRange(int value) {
        Gpio.pwmSetRange(value);
    }

    @Override
    public void pinMode(int pin, int mode) {
        Gpio.pinMode(pin, mode);
    }

    @Override
    public void pwmSetMode(int mode) {
        Gpio.pwmSetMode(mode);
    }
}

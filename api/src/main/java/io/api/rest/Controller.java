package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroReadOut;
import hardware.gyroacc.impl.Mpu6050;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class Controller {

    @Autowired
    private Mpu6050 gyro;

    @RequestMapping(value = "/gyro")
    public AccGyroReadOut getMeasurements() throws AccGyroIncorrectAxisException, AccGyroReadValueException {
        return gyro.readAll();
    }

    @RequestMapping(value = "/hello")
    public Hello start(@RequestParam String name) {
        return new Hello(666, name.toUpperCase());
    }


}
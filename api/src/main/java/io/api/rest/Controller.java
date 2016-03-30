package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
import hardware.gyroacc.impl.AccGyroReadOut;
import hardware.gyroacc.impl.Mpu6050;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;


@RestController
public class Controller {

    @Autowired
    private Mpu6050 gyro;
//
//    @Autowired
//    private FakeGyro gyro;

    @RequestMapping(value = "/gyro")
    public List<AccGyroReadOut> getMeasurements() throws AccGyroIncorrectAxisException, AccGyroReadValueException,
        InterruptedException {
        List<AccGyroReadOut> readings = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            readings.add(gyro.readAll());
            Thread.sleep(10);
        }
        return readings;
    }

    @RequestMapping(value = "/hello")
    public Hello start(@RequestParam String name) {
        return new Hello(666, name.toUpperCase());
    }


}
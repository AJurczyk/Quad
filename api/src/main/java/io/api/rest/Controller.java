package io.api.rest;

/**
 * @author aleksander.jurczyk@seedlabs.io
 */

import hardware.gyroacc.enums.Axis;
import hardware.gyroacc.exception.AccGyroIncorrectAxisException;
import hardware.gyroacc.exception.AccGyroReadValueException;
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
    public GyroResult getMeasurements() {
        double accX;
        double accY;
        double accZ;
        double gyroX;
        double gyroY;
        double gyroZ;

        try {
            accX = gyro.readAccInG(Axis.X);
            accY = gyro.readAccInG(Axis.Y);
            accZ = gyro.readAccInG(Axis.Z);

            gyroX = gyro.readGyroDeg(Axis.X);
            gyroY = gyro.readGyroDeg(Axis.Y);
            gyroZ = gyro.readGyroDeg(Axis.Z);

            return new GyroResult(accX, accY, accZ, gyroX, gyroY, gyroZ);

        } catch (AccGyroIncorrectAxisException e) {
            e.printStackTrace();
        } catch (AccGyroReadValueException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/hello")
    public Hello start(@RequestParam String name) {
        return new Hello(666, name.toUpperCase());
    }


}
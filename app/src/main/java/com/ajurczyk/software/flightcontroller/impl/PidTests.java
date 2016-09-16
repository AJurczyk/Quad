package com.ajurczyk.software.flightcontroller.impl;

import java.util.concurrent.TimeUnit;

/**
 * @author aleksander.jurczyk@gmail.com on 12.09.16.
 */
public class PidTests {

    public static final void main(String... args) throws InterruptedException {
        float currentAngle = 0f;
        float degV = 0f;
        float dt = 20;
        float desiredAngle = 20f;

        float maxRegulation = 10;
        float minRegulation = -10;

        float maxDegV = 50;
        float minDegV = -50;

        float P = 70f;

        float maxDiff = 0;
        System.out.println("probe\tcurrentAngle \t velocity \t error \t regulation");

        for (int i = 0; i < 200; i++) {
            currentAngle = (degV * dt / 1000) + currentAngle;
            if (currentAngle > maxDiff) {
                maxDiff = currentAngle;
            }
            float regulation = (desiredAngle - currentAngle) * P;
            /*if (regulation < minRegulation) {
                regulation = minRegulation;
            } else if (regulation > maxRegulation) {
                regulation = maxRegulation;
            }*/

            degV = /*degV +*/ regulation;
          /*  if (degV > maxDegV) {
                degV = maxDegV;
            } else if (degV < minDegV) {
                degV = minDegV;
            }*/


            System.out.println(i + "\t" + currentAngle + "\t" + degV + "\t" + (desiredAngle - currentAngle) + "\t" + regulation);
            TimeUnit.MILLISECONDS.sleep(2);
        }

        System.out.println("maxDif: " + maxDiff);

    }
}

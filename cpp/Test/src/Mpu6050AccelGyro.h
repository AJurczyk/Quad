/*
 * Mpu6050AccelGyro.h
 *
 *  Created on: 17 wrz 2015
 *      Author: alek
 */

#ifndef MPU6050ACCELGYRO_H_
#define MPU6050ACCELGYRO_H_

#define ACCEL_XOUT_H 0x3B
#define ACCEL_YOUT_H 0x3D
#define ACCEL_ZOUT_H 0x3F

#include "I2C.h"
using namespace std;

class Mpu6050AccelGyro {
private:
	I2C i2c;
public:
	Mpu6050AccelGyro(int I2CBus, int I2CAddress);
	~Mpu6050AccelGyro();
	void SleepEnabled(bool enable);
	void readFromMemory(char address,int length,char buffer[]);
	double readAccX();
	double readAccY();
	double readAccZ();
	int readGyroX();
	int readGyroY();
	int readGyroZ();
};

#endif /* MPU6050ACCELGYRO_H_ */

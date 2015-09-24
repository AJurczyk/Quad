/*
 * Mpu6050AccelGyro.cpp
 *
 *  Created on: 17 wrz 2015
 *      Author: alek
 */

#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <linux/i2c.h>
#include <linux/i2c-dev.h>
#include <sys/ioctl.h>
#include <stropts.h>
#include <stdio.h>
#include <iostream>
#include <math.h>

#include "Mpu6050AccelGyro.h"

#define MAX_BUS 64
#define BMA180_I2C_BUFFER 0x80

Mpu6050AccelGyro::Mpu6050AccelGyro(int I2CBus, int I2CAddress) :
		i2c(I2CBus, I2CAddress) {
	SleepEnabled(false);
}
Mpu6050AccelGyro::~Mpu6050AccelGyro()
{
	SleepEnabled(true);
}
void Mpu6050AccelGyro::SleepEnabled(bool enable) {
	if (enable) {
		i2c.Write(0x6b, 0x40);
	} else {
		i2c.Write(0x6b, 0x0);
	}
}
void Mpu6050AccelGyro::readFromMemory(char address,int length,char buffer[])
{
	i2c.Write(new char[1]{address},1);
	if(length==1)
	{
		i2c.ReadSingleByte();
	}else
	{
		i2c.ReadBytes(length,buffer);
	}
}
double Mpu6050AccelGyro::readAccX() {
	char buffer[2]={0};
	readFromMemory(ACCEL_XOUT_H,2,buffer);
	int16_t accX=(((int16_t)buffer[0]) << 8) | buffer[1];
	return accX/16.384;
}
double Mpu6050AccelGyro::readAccY() {
	char buffer[2]={0};
		readFromMemory(ACCEL_YOUT_H,2,buffer);
		int16_t accY=(((int16_t)buffer[0]) << 8) | buffer[1];
		return accY/16.384;
}
double Mpu6050AccelGyro::readAccZ() {
	char buffer[2]={0};
		readFromMemory(ACCEL_ZOUT_H,2,buffer);
		int16_t accZ=(((int16_t)buffer[0]) << 8) | buffer[1];
		return accZ/16.384;
}
int Mpu6050AccelGyro::readGyroX() {

}
int Mpu6050AccelGyro::readGyroY() {

}
int Mpu6050AccelGyro::readGyroZ() {

}


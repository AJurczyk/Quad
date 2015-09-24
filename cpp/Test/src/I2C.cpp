/*
 * I2C.cpp
 *
 *  Created on: 19 wrz 2015
 *      Author: alek
 */

#include "I2C.h"
#include <errno.h>

 #include <string.h>
/* #include <stdio.h>
 #include <sys/types.h>
 #include <sys/stat.h>*/

#include <unistd.h>
#include <linux/i2c-dev.h>
#include <sys/ioctl.h>
#include <fcntl.h> //open
#include <stdlib.h> //exit
#include <iostream>
using namespace std;

#define I2CFILENAME "/dev/i2c-1"
I2C::I2C()
{

}
I2C::I2C(int I2CBus, int I2CAddress) {
	this->I2CBus = I2CBus;
	this->I2CAddres = I2CAddress;

	if ((file = open(I2CFILENAME, O_RDWR)) < 0) {
		cout << "Failed to open " << I2CFILENAME;
		exit(1);
	}
	if (ioctl(file, I2C_SLAVE, I2CAddres) < 0) {
		cout << "Failed to acquire bus access and/or talk to slave.\n";
		exit(1);
	}
}
I2C::~I2C()
{
	close(file);
}
int I2C::ReadSingleByte() {

	unsigned char buf[1] = { 0 };
	// Using I2C Read
	int result=read(file, buf, 1);
	return (int)buf[0];
}
void I2C::ReadBytes(int length,char buffer[])
{
	int result=read(file, buffer, length);
}
void I2C::Write(char array[],int arrayLength)
{
	int result = write(file,array,arrayLength);
	if (result!=1)
		cout<<"error while writing to i2c: "<<errno<<" "<<strerror(errno)<<endl;
}
void I2C::Write(char address, char value)
{
	char array[]={address,value};
	Write(array,2);
}


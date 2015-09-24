/*
 * I2C.h
 *
 *  Created on: 19 wrz 2015
 *      Author: alek
 */

#ifndef I2C_H_
#define I2C_H_

#include <stdint.h>

class I2C {
	int I2CBus,I2CAddres,file;
public:
	I2C();
	I2C(int I2CBus, int I2CAddress);
	~I2C();
	void Write(char value[],int arrayLength);
	void Write(char address, char value);
	int ReadSingleByte();
	void ReadBytes(int length,char buffer[]);
};

#endif /* I2C_H_ */

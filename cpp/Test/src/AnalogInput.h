/*
 * AnalogInput.h
 *
 *  Created on: 31 sie 2015
 *      Author: alek
 */

#ifndef ANALOGINPUT_H_
#define ANALOGINPUT_H_
#include <string>
#include <iostream>
#include <map>
using namespace std;

class AnalogInput {
public:
	string ainName;
	AnalogInput(string pin);
	static map<string,string> MapPinToAin;
	int ReadAin();
};

#endif /* ANALOGINPUT_H_ */

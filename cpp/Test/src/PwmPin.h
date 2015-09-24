/*
 * PwmPin.h
 *
 *  Created on: 23 sie 2015
 *      Author: alek
 */


#ifndef PWMPIN_H_
#define PWMPIN_H_

#include <string>
#include <iostream>
using namespace std;

class PwmPin
{
public:
	string path;
	const int PERIOD=5000000;
	string pinName;
	PwmPin();
	PwmPin(string pwm);
	virtual ~PwmPin();
	void SetPwmDuty(int duty);

	bool IsAm33xxPwmExported();
	bool IsPinExportedAsPwm();
	void ExportAm33xxPwm();
	void ExportPwmPin();
	void Run(bool state);
	void SetPwmPeriod(int period);
	string FindOcp2Path();
};

#endif /* PWMPIN_H_ */

//============================================================================
// Name        : Test.cpp
// Author      : Alek
// Version     :
// Copyright   : 
// Description : Hello World in C++, Ansi-style
//============================================================================
#include "PwmPin.h"
#include "pin.h"
#include "AnalogInput.h"
#include "Mpu6050AccelGyro.h"
#include "I2C.h"
#include <iostream>
#include <fstream>
#include <string>
#include <stdlib.h>
#include <unistd.h>
#include <dirent.h> //working on directories
#include <iostream>
#include <fstream>
#include <sstream>
using namespace std;

int read_adc();

int main() {
	PwmPin pwmPin = PwmPin("P9_22");
	pwmPin.SetPwmDuty(4000000);
	AnalogInput Ain0 = AnalogInput("P9_39");
	int adc;
	double currentPower = 0;
	double percent;
	Mpu6050AccelGyro mpu6050 = Mpu6050AccelGyro(1, 0x68);

	for(int i=0;i<100;i++){
		double accX=mpu6050.readAccX();
		cout<<i<< ".\tX = "<<accX<<"\tY = "<<mpu6050.readAccY()<<"\tZ = "<<mpu6050.readAccZ()<<endl;
		usleep(100000);
	}
	/*while(1)
	 {
	 currentPower=percent;
	 adc = Ain0.ReadAin();
	 percent=(adc - 150)/16.5;
	 if(percent<0)
	 percent=0;

	 cout<<"Read: "<<percent<<"% ";
	 if(percent-currentPower>10)
	 percent=currentPower+10;

	 cout<<"Power set: "<<percent<<endl;
	 pwmPin.SetPwmDuty(4000000-percent*10000);
	 usleep(100000);
	 }*/

	/*PwmPin pwmPin= PwmPin("P9_22");
	 pwmPin.SetPwmDuty(4000000);
	 double a =0;

	 while(a!=666.0)
	 {
	 cout << "Select speed (666 = exit):";
	 cin >> a;
	 pwmPin.SetPwmDuty(4000000-a*10000);
	 cout << a << "set"<<endl;
	 }*/
	return 0;
}


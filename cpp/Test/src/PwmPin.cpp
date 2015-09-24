/*
 * PwmPin.cpp
 *
 *  Created on: 23 sie 2015
 *      Author: alek
 */

#include "PwmPin.h"
#include <iostream>
#include <fstream>
#include <string>
#include <stdlib.h>
#include <unistd.h>
#include <dirent.h> //working on directories
#include "SlotsBoneCapeMgr.h"

using namespace std;


PwmPin::PwmPin(string pin) {
	// TODO Auto-generated constructor stub
	pinName=pin;
	if(!IsAm33xxPwmExported())
		ExportAm33xxPwm();
	if(!IsPinExportedAsPwm())
		ExportPwmPin();
	path = FindOcp2Path();
	SetPwmPeriod(PERIOD);
	Run(true);
}

PwmPin::~PwmPin() {
	// TODO Auto-generated destructor stub
	//Run(false);
}
bool FindStringInSlots(string stringToFind)
{
	fstream slots;
	slots.open("/sys/devices/bone_capemgr.8/slots",ios::in);
	string line;
	do
	{
		getline(slots,line);
		if(line.find(stringToFind)!=string::npos)
		{
			slots.close();
			return true;
		}
	}while(!slots.eof());
	slots.close();
	return false;
}
bool PwmPin::IsAm33xxPwmExported()
{
	return FindStringInSlots("am33xx_pwm");
}
bool PwmPin::IsPinExportedAsPwm()
{
	return FindStringInSlots(pinName);
}

void PwmPin::ExportAm33xxPwm()
{
	system("echo am33xx_pwm > /sys/devices/bone_capemgr.8/slots");
	cout<<"[]am33xx_pwm has been exported"<<endl;
}
void PwmPin::ExportPwmPin()
{
	string command="echo bone_pwm_"+pinName+" > /sys/devices/bone_capemgr.8/slots";
	system(command.c_str());
	usleep(1000*500);
	cout<<"[]Pin "<<pinName<<"has been exported as PWM"<<endl;
}

void PwmPin::SetPwmDuty(int duty)
{
	fstream file;
	file.open((path+"/duty").c_str(),ios::out);
	file<<duty;
	file.close();
}
void PwmPin::SetPwmPeriod(int period)
{
	fstream file;
	cout<<"write "<<period<<"period to path: "<<path;
	file.open((path+"/period").c_str(),ios::out);
	file<<period;
	file.close();
}
string PwmPin::FindOcp2Path()
{
	string dir="/sys/devices/ocp.2/";
	DIR *pdir=NULL;
	struct dirent *pent = NULL;
	pdir=opendir(dir.c_str());
	if (pdir==NULL)
	{
		cout<<"error przy otwieraniu lokalizacji";
	}
	while((pent=readdir(pdir))!=NULL)
	{
		string a=pent->d_name;
		if(a.find(pinName)!=string::npos)
		{
			return "/sys/devices/ocp.2/"+a;
		}
	}
	closedir(pdir);
}
void PwmPin::Run(bool state)
{
	fstream file;
	file.open((path+"/run").c_str(),ios::out);
	if(state)
		file<<"1";
	else
		file<<"0";

	file.close();
}

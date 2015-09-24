/*
 * AnalogInput.cpp
 *
 *  Created on: 31 sie 2015
 *      Author: alek
 */

#include "AnalogInput.h"
#include <string>
#include "SlotsBoneCapeMgr.h"
#include <fstream>
#include <sstream>
using namespace std;



AnalogInput::AnalogInput(string pin) {
	if(!SlotsBoneCapeMgr::CheckExported("cape-bone-iio"))
		SlotsBoneCapeMgr::ExportSlot("cape-bone-iio");
	ainName=MapPinToAin[pin];
}
int AnalogInput::ReadAin()
{
	fstream plik;
	stringstream str;
	string line;
	int val;
	string path="/sys/devices/ocp.2/helper.15/"+ainName;
	plik.open(path.c_str(),ios::in);
	getline(plik,line);
	str<<line;
	str>>val;
	plik.close();
	return val;
}
map<string,string> InitPinMap()
					{
	map<string,string>mapa;
	mapa["P9_39"]="AIN0";
	mapa["P9_40"]="AIN1";
	mapa["P9_37"]="AIN2";
	mapa["P9_38"]="AIN3";
	mapa["P9_33"]="AIN4";
	mapa["P9_36"]="AIN5";
	mapa["P9_35"]="AIN6";
	return mapa;
					}
map<string,string> AnalogInput::MapPinToAin = InitPinMap();



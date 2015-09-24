/*
 * Slots.cpp
 *
 *  Created on: 31 sie 2015
 *      Author: alek
 */

#include "SlotsBoneCapeMgr.h"
#include <string>

#include <iostream>
#include <fstream>
#include <stdlib.h>



using namespace std;

bool SlotsBoneCapeMgr::CheckExported(string slot)
{
	fstream slots;
		slots.open("/sys/devices/bone_capemgr.8/slots",ios::in);
		string line;
		do
		{
			getline(slots,line);
			if(line.find(slot)!=string::npos)
			{
				slots.close();
				return true;
			}
		}while(!slots.eof());
		slots.close();
		return false;
}

void SlotsBoneCapeMgr::ExportSlot(string slot)
{
	string command="echo "+slot+" > /sys/devices/bone_capemgr.8/slots";
	system(command.c_str());
		cout<<"[]a"<<slot<<" has been exported"<<endl;
}



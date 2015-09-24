/*
 * pin.cpp
 *
 *  Created on: 23 sie 2015
 *      Author: alek
 */



#include <iostream>
#include <string>
#include "pin.h"
#include <fstream>
#include <sstream>
using namespace std;


pin::pin(string gpio,string dir):name(gpio)
{
	fstream plik;
	plik.open("/sys/class/gpio/export",ios::out);
	plik<<gpio.c_str();
	plik.close();
	cout<<"[]Pin "<<gpio<<" has been exported.\n";
	string path="/sys/class/gpio/gpio"+gpio+"/direction";
	plik.open(path.c_str(),ios::out);
	plik<<dir.c_str();
	plik.close();
	cout<<"[]Direction of pin "<<gpio<<" has been set as "<<dir<<"\n";
}
pin::~pin()
{
	fstream plik;
	plik.open("/sys/class/gpio/unexport",ios::out);
	plik<<name.c_str();
	plik.close();
	cout<<"[]Pin "<<name<<" has been unexported.\n";
}

void pin::set_value(int val)
{
	fstream plik;
	string path="/sys/class/gpio/gpio"+name+"/value";
	plik.open(path.c_str(),ios::out);
	plik<<val;
	plik.close();
}
void pin::set_direction(string dir)
{
	fstream plik;
	string path="/sys/class/gpio/gpio"+name+"/direction";
	plik.open(path.c_str(),ios::out);
	plik<<dir.c_str();
	plik.close();
}

int pin::read_value()
{
	fstream plik;
	stringstream str;
	string line;
	int val;
	string path="/sys/class/gpio/gpio"+name+"/value";
	plik.open(path.c_str(),ios::in);
	getline(plik,line);
	str<<line;
	str>>val;
	plik.close();
	return val;
}



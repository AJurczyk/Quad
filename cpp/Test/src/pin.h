/*
 * pin.h
 *
 *  Created on: 23 sie 2015
 *      Author: alek
 */

#ifndef PIN_H_
#define PIN_H_

#include <iostream>
#include <stdio.h>
#include <string>
using namespace std;

class pin
{	public:
	pin(string,string="in");
	~pin();
	string name;
	void set_value(int);
	void set_direction(string);
	int read_value();
};




#endif /* PIN_H_ */

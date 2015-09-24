/*
 * Slots.h
 *
 *  Created on: 31 sie 2015
 *      Author: alek
 */

#ifndef SLOTSBONECAPEMGR_H_
#define SLOTSBONECAPEMGR_H_
#include <string>
using namespace std;

class SlotsBoneCapeMgr {
public:
	static bool CheckExported(string slot);
	static void ExportSlot(string slot);
};

#endif /* SLOTSBONECAPEMGR_H_ */

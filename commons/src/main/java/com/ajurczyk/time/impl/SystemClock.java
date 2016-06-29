package com.ajurczyk.time.impl;

import com.ajurczyk.time.IClock;

/**
 * @author aleksander.jurczyk@gmail.com on 17.06.16.
 */
public class SystemClock implements IClock {

    @Override
    public long getMiliseconds() {
        return System.currentTimeMillis();
    }
}

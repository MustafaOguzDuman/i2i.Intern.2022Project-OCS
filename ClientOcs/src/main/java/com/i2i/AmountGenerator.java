package com.i2i;

import java.util.Random;

public class AmountGenerator {

    Random rand;
    final int voiceBound = 3600;
    final int smsBound = 1;
    final int dataBound = 10240000;

    //voice: seconds
    //sms: 1
    //data bytes
    AmountGenerator(){
        rand = new Random();
    }
    public int getAmount(String service){
        switch (service) {
            case "VOICE":
                return rand.nextInt(voiceBound);
            case "DATA":
                return rand.nextInt(dataBound);
            case "SMS":
                return smsBound;
            default:
                return -1;
        }
    }
}

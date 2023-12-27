package org.powerSupply.source;

import org.powerSupply.util.MessageBroker;
import org.powerSupply.util.Status;

import java.util.concurrent.TimeUnit;

public class IPS {
    private long charge = 10L;
    private final long MAX_CAPACITY = 10L;
    private final long redLightCharge = 2L;

    public long getCharge() {
        return charge;
    }

    public long getRedLightCharge() {
        return redLightCharge;
    }

    public long getMAX_CAPACITY() {
        return MAX_CAPACITY;
    }

    public void setCharge(long charge) {
        this.charge = charge;
    }

    public synchronized void start() {
        System.out.println("+------------+ Starting IPS +------------+");
        try {
            while (charge > 0 && MessageBroker.IPS_WILLING_TO_RUN) {
                if (charge == redLightCharge) {
                    System.out.println("+------------+ RED LIGHT +------------+");
                }

                if (MessageBroker.ELECTRICITY_STATUS.equals(Status.OFF)) {
                    charge--;
                    System.out.println("IPS Charge down: "+charge);
                    TimeUnit.SECONDS.sleep(1);

                    if(charge == 0){
                        System.out.println("IPS is shut down.");
                        MessageBroker.IPS_WILLING_TO_RUN = false;
                    }
                }
            }
            MessageBroker.IPS_STATUS = Status.OFF;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void startCharging() {
        System.out.println("+------------+ Started Charging IPS +------------+");
        MessageBroker.IPS_STATUS = Status.ON;
        try {
            while (charge < MAX_CAPACITY && MessageBroker.ELECTRICITY_STATUS.equals(Status.ON)) {
                charge++;
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

package org.powerSupply.source;

import org.powerSupply.util.MessageBroker;
import org.powerSupply.util.Status;

import java.util.concurrent.TimeUnit;

public class UPS {
    private int charge = 5;
    private final int MAX_CAPACITY = 5;
    private final int redLightCharge = 1;

    public int getCharge() {
        return charge;
    }

    public int getMAX_CAPACITY() {
        return MAX_CAPACITY;
    }

    public int getRedLightCharge() {
        return redLightCharge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public synchronized void start() {
        System.out.println("+------------+ Starting UPS +------------+");
        try {
            while (charge > 0 && MessageBroker.UPS_WILLING_TO_RUN) {
                if (charge == redLightCharge) {
                    System.out.println("+------------+ RED LIGHT +------------+");
                }

                if (MessageBroker.IPS_STATUS.equals(Status.OFF)) {
                    charge--;
                    System.out.println("UPS Charge down: "+charge);
                    TimeUnit.SECONDS.sleep(1);

                    if(charge == 0){
                        System.out.println("UPS is shut down.");
                        MessageBroker.UPS_WILLING_TO_RUN = false;
                    }
                }
            }
            MessageBroker.UPS_STATUS = Status.OFF;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void startCharging() {
        System.out.println("+------------+ Started Charging UPS +------------+");
        MessageBroker.UPS_STATUS = Status.ON;
        try {
            while (charge < MAX_CAPACITY && MessageBroker.IPS_STATUS.equals(Status.ON)) {
                charge++;
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

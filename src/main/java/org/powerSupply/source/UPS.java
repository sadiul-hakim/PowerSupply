package org.powerSupply.source;

import org.powerSupply.util.MessageBroker;
import org.powerSupply.util.Status;

import java.util.concurrent.TimeUnit;

public class UPS {
    private volatile int charge = 300;
    private final int MAX_CAPACITY = 300;
    private final int redLightCharge = 60;
    public synchronized void start() {
        System.out.println("+------------+ Starting UPS +------------+");
        Thread startThread = new Thread(() -> {
            try {
                while (true) {
                    if (charge > 0) {
                        if (charge == redLightCharge) {
                            System.out.println("+------------+ RED LIGHT +------------+");
                        }

                        if (MessageBroker.IPS_STATUS.equals(Status.OFF)) {
                            synchronized (this) {
                                charge--;
                            }
                            System.out.println("UPS Charge down: " + charge);
                            TimeUnit.SECONDS.sleep(1);

                            if (charge == 0) {
                                System.out.println("UPS is shut down.");
                                MessageBroker.UPS_STATUS = Status.OFF;
                            }
                        }
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        startThread.setName("# UPS Start Thread");
        startThread.start();
    }

    public synchronized void startCharging() {
        System.out.println("+------------+ Started Charging UPS +------------+");

        Thread chargingThread = new Thread(() -> {
            try {
                while (true) {
                    if (charge < MAX_CAPACITY && MessageBroker.IPS_STATUS.equals(Status.ON)) {
                        MessageBroker.UPS_STATUS = Status.ON;
                        synchronized (this) {
                            charge++;
                        }
                        System.out.println("UPS Charge up: " + charge);
                        TimeUnit.SECONDS.sleep(1);
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        chargingThread.setName("# UPS Charging Thread");
        chargingThread.start();
    }
}

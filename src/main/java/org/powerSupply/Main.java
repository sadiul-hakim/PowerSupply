package org.powerSupply;

import org.powerSupply.source.Electricity;
import org.powerSupply.source.IPS;
import org.powerSupply.source.UPS;
import org.powerSupply.util.MessageBroker;
import org.powerSupply.util.Status;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        final Electricity electricity = new Electricity();
        MessageBroker.ELECTRICITY_STATUS = Status.ON;

        Thread ipsThread = new Thread(() -> {

            IPS ips = new IPS();
            MessageBroker.IPS_STATUS = Status.ON;
            MessageBroker.IPS_WILLING_TO_RUN = true;
            ips.start();

        });
        ipsThread.setName("# IPS Thread");
        ipsThread.start();

        Thread upsThread = new Thread(() -> {

            UPS ups = new UPS();
            MessageBroker.UPS_STATUS = Status.ON;
            MessageBroker.UPS_WILLING_TO_RUN = true;
            ups.start();
        });
        upsThread.setName("# UPS Thread");
        upsThread.start();

//        try {
//            ipsThread.join();
//            upsThread.join();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }

        while(true){
            System.out.println("Enter Electricity Status: ");
            System.out.println("1. ON");
            System.out.println("2. OFF");
            int status = input.nextInt();

            if(status == 1){
                MessageBroker.ELECTRICITY_STATUS=Status.ON;
            }else if(status == 2){
                MessageBroker.ELECTRICITY_STATUS=Status.OFF;
            }else {
                System.out.println("You Stupid!");
            }
        }
    }

    public static void flow(Status status) {

    }
}
package org.powerSupply.util;

public class MessageBroker {
    public volatile static Status  ELECTRICITY_STATUS=Status.ON;
    public volatile static Status  IPS_STATUS=Status.ON;
    public volatile static Status  UPS_STATUS=Status.ON;
}

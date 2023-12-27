package org.powerSupply.source;

import org.powerSupply.util.Status;

public class Electricity {
    private Status status = Status.ON;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

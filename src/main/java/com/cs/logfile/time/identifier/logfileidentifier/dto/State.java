package com.cs.logfile.time.identifier.logfileidentifier.dto;


public enum State {
    STARTED("STARTED"),
    FINISHED("FINISHED");


    private String stateValue;

    private State(String stateValue) {
        this.stateValue = stateValue;
    }

    public String getState() {
        return this.stateValue;
    }
}

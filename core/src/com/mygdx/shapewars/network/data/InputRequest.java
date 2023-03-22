package com.mygdx.shapewars.network.data;

import java.util.UUID;

public class InputRequest {

    public String clientId;
    public int valueInput;
    public int directionInput;

    public InputRequest(String clientId, int valueInput, int directionInput) {
        this.clientId = clientId;
        this.valueInput = valueInput;
        this.directionInput = directionInput;
    }

    public InputRequest() {}
}
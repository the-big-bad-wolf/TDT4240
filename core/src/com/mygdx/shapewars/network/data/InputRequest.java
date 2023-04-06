package com.mygdx.shapewars.network.data;

public class InputRequest {

    public String clientId;
    public float valueInput;
    public float directionInput;

    public InputRequest(String clientId, float valueInput, float directionInput) {
        this.clientId = clientId;
        this.valueInput = valueInput;
        this.directionInput = directionInput;
    }

    public InputRequest() {
    }
}
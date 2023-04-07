package com.mygdx.shapewars.network.data;

public class InputRequest {

    public String clientId;
    public float valueInput;
    public float directionInput;
    public boolean firingFlag;

    public InputRequest(String clientId, float valueInput, float directionInput, boolean firingFlag) {
        this.clientId = clientId;
        this.valueInput = valueInput;
        this.directionInput = directionInput;
        this.firingFlag = firingFlag;
    }

    public InputRequest() {
    }
}
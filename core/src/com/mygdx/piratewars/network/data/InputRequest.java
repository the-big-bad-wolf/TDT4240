package com.mygdx.piratewars.network.data;

public class InputRequest {

    public String clientId;
    public float valueInput;
    public float directionShipInput;
    public float directionGunInput;
    public boolean firingFlag;

    public InputRequest(String clientId, float valueInput, float directionShipInput, float directionGunInput, boolean firingFlag) {
        this.clientId = clientId;
        this.valueInput = valueInput;
        this.directionShipInput = directionShipInput;
        this.directionGunInput = directionGunInput;
        this.firingFlag = firingFlag;
    }

    public InputRequest() {
    }
}
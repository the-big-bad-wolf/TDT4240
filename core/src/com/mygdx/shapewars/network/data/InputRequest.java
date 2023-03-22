package com.mygdx.shapewars.network.data;

import java.util.UUID;

public class InputRequest {

    public UUID uuid;
    public int valueInput;
    public int directionInput;

    public InputRequest(UUID uuid, int valueInput, int directionInput) {
        this.uuid = uuid;
        this.valueInput = valueInput;
        this.directionInput = directionInput;
    }
}
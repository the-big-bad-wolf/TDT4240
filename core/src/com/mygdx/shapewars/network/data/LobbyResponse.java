package com.mygdx.shapewars.network.data;

public class LobbyResponse {

    public int numPlayers;
    public int clientShipId;
    public boolean isGameActive;

    public LobbyResponse(int numPlayers, int clientShipId, boolean isGameActive) {
        this.numPlayers = numPlayers;
        this.clientShipId = clientShipId;
        this.isGameActive = isGameActive;
    }

    public LobbyResponse() { }
}

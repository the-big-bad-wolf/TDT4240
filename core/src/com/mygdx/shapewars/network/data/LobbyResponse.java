package com.mygdx.shapewars.network.data;

public class LobbyResponse {

    public int numPlayers;
    public int clientTankId;
    public boolean isGameActive;

    public LobbyResponse(int numPlayers, int clientTankId, boolean isGameActive) {
        this.numPlayers = numPlayers;
        this.clientTankId = clientTankId;
        this.isGameActive = isGameActive;
    }

    public LobbyResponse() { }
}

package com.mygdx.shapewars.network.data;

public class LobbyResponse {

    public int numPlayers;
    public int clientShipId;
    public String selectedMap;
    public boolean isGameActive;

    public LobbyResponse(int numPlayers, int clientShipId, String selectedMap, boolean isGameActive) {
        this.numPlayers = numPlayers;
        this.clientShipId = clientShipId;
        this.selectedMap = selectedMap;
        this.isGameActive = isGameActive;
    }

    public LobbyResponse() { }
}

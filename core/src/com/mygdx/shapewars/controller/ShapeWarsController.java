package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;
import com.badlogic.gdx.Net.Protocol;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private Screen currentScreen;
    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
      this.model = model;
      this.shapeWarsView = view;
      this.mainMenuView = mainMenuView;
      this.currentScreen = mainMenuView;
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
          model.update();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // 0 means no timeout.  Probably not the greatest idea in production!
                serverSocketHint.acceptTimeout = 0;

                // Create the socket server using TCP protocol and listening on 9021
                // Only one app can listen to a port at a time, keep in mind many ports are reserved
                // especially in the lower numbers ( like 21, 80, etc )
                ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 9021, serverSocketHint);

                // Loop forever
                while(true){
                    // Create a socket
                    System.out.println("testbefore");
                    Socket socket = serverSocket.accept(null);

                    // Read data from the socket into a BufferedReader
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    try {
                        // Read to the next newline (\n) and display that text on labelMessage
                        System.out.println(buffer.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //currentScreen = shapeWarsView;
                //currentScreen.show();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.U)) {
                SocketHints socketHints = new SocketHints();
                // Socket will time our in 4 seconds
                socketHints.connectTimeout = 4000;
                //create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
                System.out.println("test");
                Socket socket = Gdx.net.newClientSocket(Protocol.TCP, "10.0.0.17", 9021, socketHints);
                try {
                    // write our entered message to the stream
                    socket.getOutputStream().write(99);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        model.batch.dispose();
    }
}

package com.desenvolvedorindie.platformer.network;

import com.desenvolvedorindie.platformer.network.data.*;
import com.desenvolvedorindie.platformer.world.World;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class GameClient {

    private Client client;

    public GameClient(World world, String name) {
        client = new Client();
        client.start();

        GameServer.register(client);

        client.addListener(new Listener.ThreadedListener(new Listener() {
            public void connected (Connection connection) {

            }

            public void received (Connection connection, Object object) {
                if (object instanceof RegistrationRequired) {
                    Register register = new Register();
                    register.name = name;
                    client.sendTCP(register);
                } else if (object instanceof AddPlayer) {
                    AddPlayer msg = (AddPlayer)object;
                    world.addPlayer(msg.player);
                } else if (object instanceof PlayerUpdate) {
                    world.updatePlayer((PlayerUpdate)object);
                } else if (object instanceof RemovePlayer) {
                    RemovePlayer msg = (RemovePlayer)object;
                    world.removePlayer(msg.name);
                }
            }

            public void disconnected (Connection connection) {

            }
        }));

        try {
            client.connect(5000, "localhost", GameServer.port);
            // Server communication after connection can go here, or in Listener#connected().
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public void movePlayer(MovePlayer movePlayer) {
        client.sendTCP(movePlayer);
    }

    public void login(Login login) {
        client.sendTCP(login);
    }
}

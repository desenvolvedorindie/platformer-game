package com.desenvolvedorindie.platformer.network;


import com.desenvolvedorindie.platformer.network.data.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashSet;

public class GameServer {

    static int port = 25565;
    public Server server;

    private HashSet<Player> logged = new HashSet<>();

    public GameServer() {
        this.server = new Server(1048576, 1048576) {
            @Override
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        register(this.server);

        this.server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                PlayerConnection connection = (PlayerConnection) c;
                Player player = connection.player;

                if (object instanceof Login) {
                    // Ignore if already logged in.
                    if (player != null) return;

                    Login login = (Login) object;

                    // Reject if the name is invalid.
                    String name = login.name;

                    if (!isValid(name)) {
                        c.close();
                        return;
                    }

                    // Reject if already logged in.
                    for (Player other : logged) {
                        if (other.name.equals(name)) {
                            c.close();
                            return;
                        }
                    }

                    player = loadCharacter(name);

                    // Reject if couldn't load player.
                    if (player == null) {
                        c.sendTCP(new RegistrationRequired());
                        return;
                    }

                    loggedIn(connection, player);
                } else if (object instanceof Register) {
                    // Ignore if already logged in.
                    if (player != null) return;

                    Register register = (Register) object;

                    // Reject if the login is invalid.
                    if (!isValid(register.name)) {
                        c.close();
                        return;
                    }

                    player = new Player();
                    player.name = register.name;
                    player.x = 0;
                    player.y = 0;

                    loggedIn(connection, player);
                } else if (object instanceof MovePlayer) {
                    // Ignore if not logged in.
                    if (player == null) return;

                    MovePlayer msg = (MovePlayer) object;

                    player.x = msg.x;
                    player.y = msg.y;

                    PlayerUpdate update = new PlayerUpdate();
                    update.name = player.name;
                    update.x = player.x;
                    update.y = player.y;
                    server.sendToAllTCP(update);
                }
            }

            private boolean isValid(String value) {
                if (value == null) return false;
                value = value.trim();
                if (value.length() == 0) return false;
                return true;
            }

            public void connected(Connection c) {
                System.out.println("Player connected!");
                c.sendTCP("Connected");
            }

            public void disconnected(Connection c) {
                System.out.println("Player disconnected");
                Dead n = new Dead();
                n.id = c.getID();
                server.sendToAllExceptTCP(c.getID(), n);
            }
        });

        try {
            server.bind(port);
        } catch (IOException e) {
            System.out.println("Failed to bind server! (Another server already running?)");
        }

        server.start();

        System.out.println("Server started");
    }

    private Player loadCharacter(String name) {
        for(Player player:logged) {
            if(player.name.equals(name)) {
                return player;
            }
        }
        return null;
    }

    void loggedIn (PlayerConnection c, Player character) {
        c.player = character;

        // Add existing characters to new logged in connection.
        for (Player other : logged) {
            AddPlayer addCharacter = new AddPlayer();
            addCharacter.player = other;
            c.sendTCP(addCharacter);
        }

        logged.add(character);

        // Add logged in player to all connections.
        AddPlayer addCharacter = new AddPlayer();
        addCharacter.player = character;
        server.sendToAllTCP(addCharacter);
    }

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(RegistrationRequired.class);
        kryo.register(Register.class);
        kryo.register(AddPlayer.class);
        kryo.register(PlayerUpdate.class);
        kryo.register(Player.class);
        kryo.register(MovePlayer.class);
    }

    public void stop() {
        this.server.stop();
    }

    class PlayerConnection extends Connection {
        public Player player;
    }

}

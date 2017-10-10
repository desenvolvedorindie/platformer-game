package com.desenvolvedorindie.platformer.network;

import com.badlogic.gdx.math.Vector2;
import com.desenvolvedorindie.platformer.network.data.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer {

    public static int port = 25565;
    static GameServer instance;
    public Server server;
    public boolean problem = false;
    public VirtualMap servermap;

    public GameServer(final VirtualMap servermap) {
        this.servermap = servermap;

        this.server = new Server(1048576, 1048576);

        initKryoClasses(this.server.getKryo());

        this.server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                if ((object instanceof String)) {
                    String str = (String) object;
                    if (str.equals("GetMainLayer")) {
                        servermap.sendCompressedMap(true, c);
                    }
                    if (str.equals("GetBackLayer")) {
                        servermap.sendCompressedMap(false, c);
                    }
                } else if ((object instanceof BlockModification)) {
                    BlockModification nnn = (BlockModification) object;
                    servermap.setBlock(nnn);
                } else if ((object instanceof Player)) {
                    ((Player) object).id = c.getID();
                    server.sendToAllExceptTCP(c.getID(), object);
                } else if ((object instanceof Message)) {
                    ((Message) object).id = c.getID();
                    server.sendToAllTCP(object);
                } else if ((object instanceof PlayerUpdate)) {
                    ((PlayerUpdate) object).id = c.getID();
                    server.sendToAllExceptUDP(c.getID(), object);
                } else if ((object instanceof Click)) {
                    ((Click) object).id = c.getID();
                    server.sendToAllExceptUDP(c.getID(), object);
                }
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
            server.bind(port, port);
        } catch (IOException e) {
            System.out.println("Failed to bind server! (Another server already running?)");
            this.problem = true;
        }

        server.start();

        System.out.println("Server started");
    }

    public static void initKryoClasses(Kryo kryo) {
        kryo.register(BlockModification.class);
        kryo.register(EntityPosModification.class);
        kryo.register(Vector2.class);
        kryo.register(Click.class);
        kryo.register(PlayerUpdate.class);
        kryo.register(Player.class);
        kryo.register(String.class);
        kryo.register(Dead.class);
        kryo.register(Message.class);
    }

    public void sendBlock(BlockModification n) {
        this.server.sendToAllTCP(n);
    }

    public void stop() {
        this.server.stop();
    }

}

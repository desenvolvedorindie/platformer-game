package com.desenvolvedorindie.platformer.network;

import com.badlogic.gdx.files.FileHandle;
import com.desenvolvedorindie.platformer.network.data.BlockModification;
import com.esotericsoftware.kryonet.Connection;

import java.io.IOException;

public class VirtualMap {

    public synchronized void setBlock(BlockModification conf) {
    }

    public void setChanged(int x, int y, int layer) {
    }

    public synchronized void sendCompressedMap(boolean isMain, Connection c) {
    }

    public void sendLayer(boolean isMain, Connection c)
            throws IOException {
    }

    public void loadMap(byte[][] layer, FileHandle[][] mapFile)
            throws IOException {
    }

    public void saveMap() {

    }

}

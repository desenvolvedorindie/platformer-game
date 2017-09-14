package com.desenvolvedorindie.platformer.entity.prefab;

import com.artemis.World;
import com.artemis.annotations.PrefabData;
import com.artemis.prefab.Prefab;
import com.artemis.prefab.PrefabReader;
import com.badlogic.gdx.utils.JsonValue;

@PrefabData("prefab/player.json")
public class PlayerPrefab extends Prefab {

    public PlayerPrefab(World world, PrefabReader<JsonValue> reader) {
        super(world, reader);
    }

}

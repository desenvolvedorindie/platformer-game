package com.desenvolvedorindie.platformer.resource;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;
import net.spookygames.gdx.spriter.data.SpriterData;
import net.spookygames.gdx.spriter.loader.SpriterDataLoader;

public class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    // BLOCKS
    @Asset public static final AssetDescriptor<Texture> dirt = new AssetDescriptor<Texture>("blocks/dirt.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cobblestone = new AssetDescriptor<Texture>("blocks/cobblestone.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> obsidian = new AssetDescriptor<Texture>("blocks/obsidian.png", Texture.class);

    //ENTITIES
    @Asset public static final AssetDescriptor<Texture> player = new AssetDescriptor<Texture>("player/player.png", Texture.class);

    @Asset public static final AssetDescriptor<SpriterData> grayGuy = new AssetDescriptor<SpriterData>("GreyGuy/player.scml", SpriterData.class);

    public static void load() {
        manager.setLoader(SpriterData.class, new SpriterDataLoader(manager.getFileHandleResolver()));

        Texture.setAssetManager(manager);

        manager.load(Assets.class);
    }

}

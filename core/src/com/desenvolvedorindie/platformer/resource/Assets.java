package com.desenvolvedorindie.platformer.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;
import net.spookygames.gdx.spriter.data.SpriterData;
import net.spookygames.gdx.spriter.loader.SpriterDataLoader;

public class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    //Blocks
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_DIRT = new AssetDescriptor<Texture>("tiles/dirt.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_COBBLESTONE = new AssetDescriptor<Texture>("tiles/cobblestone.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_OBSIDIAN = new AssetDescriptor<Texture>("tiles/obsidian.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_SLAB = new AssetDescriptor<Texture>("tiles/slab.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_SLOPE = new AssetDescriptor<Texture>("tiles/slope.png", Texture.class);

    //Items
    @Asset
    public static final AssetDescriptor<Texture> ITEM_SWORD = new AssetDescriptor<Texture>("items/sword.png", Texture.class);

    //Entities
    @Asset
    public static final AssetDescriptor<Texture> player = new AssetDescriptor<Texture>("player/player.png", Texture.class);

    @Asset
    public static final AssetDescriptor<SpriterData> grayGuy = new AssetDescriptor<SpriterData>("GreyGuy/player.scml", SpriterData.class);

    //UI
    @Asset
    public static final AssetDescriptor<TextureAtlas> ui = new AssetDescriptor<TextureAtlas>("ui/ui.atlas", TextureAtlas.class);

    //Shaders
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_PASSTHROUGH = new AssetDescriptor<ShaderProgram>("shaders/passthrough.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_VIGNETTE = new AssetDescriptor<ShaderProgram>("shaders/vignette.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_INVERT = new AssetDescriptor<ShaderProgram>("shaders/invert.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_EMBOSS = new AssetDescriptor<ShaderProgram>("shaders/emboss.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_SKY = new AssetDescriptor<ShaderProgram>("shaders/sky.vert", ShaderProgram.class);

    //Dungeons
    @Asset
    public static final AssetDescriptor<TiledMap> DUNGEON_WORLD = new AssetDescriptor<TiledMap>("dungeons/world.tmx", TiledMap.class);

    public static void load() {
        manager.setLoader(SpriterData.class, new SpriterDataLoader(manager.getFileHandleResolver()));

        manager.setLoader(TiledMap.class, new TmxMapLoader(manager.getFileHandleResolver()));

        Texture.setAssetManager(manager);

        ShaderProgram.pedantic = false;

        manager.load(Assets.class);
    }

    public static void debug() {
        Gdx.app.log("PASSTHROUGH", manager.get(SHADER_PASSTHROUGH).isCompiled() ? "Compiled" : manager.get(SHADER_PASSTHROUGH).getLog());
        Gdx.app.log("VIGNETTE", manager.get(SHADER_VIGNETTE).isCompiled() ? "Compiled" : manager.get(SHADER_VIGNETTE).getLog());
        Gdx.app.log("INVERT", manager.get(SHADER_INVERT).isCompiled() ? "Compiled" : manager.get(SHADER_INVERT).getLog());
        Gdx.app.log("EMBOSS", manager.get(SHADER_EMBOSS).isCompiled() ? "Compiled" : manager.get(SHADER_EMBOSS).getLog());
        Gdx.app.log("SKY", manager.get(SHADER_SKY).isCompiled() ? "Compiled" : manager.get(SHADER_SKY).getLog());
    }

}

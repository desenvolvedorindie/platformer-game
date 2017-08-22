package com.desenvolvedorindie.platformer.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;
import net.spookygames.gdx.spriter.data.SpriterData;
import net.spookygames.gdx.spriter.loader.SpriterDataLoader;

public class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    // BLOCKS
    @Asset
    public static final AssetDescriptor<Texture> dirt = new AssetDescriptor<Texture>("blocks/dirt.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> cobblestone = new AssetDescriptor<Texture>("blocks/cobblestone.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> obsidian = new AssetDescriptor<Texture>("blocks/obsidian.png", Texture.class);

    //ENTITIES
    @Asset
    public static final AssetDescriptor<Texture> player = new AssetDescriptor<Texture>("player/player.png", Texture.class);

    @Asset
    public static final AssetDescriptor<SpriterData> grayGuy = new AssetDescriptor<SpriterData>("GreyGuy/player.scml", SpriterData.class);

    //UI
    @Asset
    public static final AssetDescriptor<TextureAtlas> ui = new AssetDescriptor<TextureAtlas>("ui/ui.atlas", TextureAtlas.class);

    //Shaders
    @Asset
    public static final AssetDescriptor<ShaderProgram> PASSTHROUGH = new AssetDescriptor<ShaderProgram>("shaders/passthrough.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> VIGNETTE = new AssetDescriptor<ShaderProgram>("shaders/vignette.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> INVERT = new AssetDescriptor<ShaderProgram>("shaders/invert.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> EMBOSS = new AssetDescriptor<ShaderProgram>("shaders/emboss.vert", ShaderProgram.class);

    public static void load() {
        manager.setLoader(SpriterData.class, new SpriterDataLoader(manager.getFileHandleResolver()));

        Texture.setAssetManager(manager);

        ShaderProgram.pedantic = false;

        manager.load(Assets.class);
    }

    public static void debug() {
        Gdx.app.log("PASSTHROUGH", manager.get(PASSTHROUGH).isCompiled() ? "Compiled" : manager.get(PASSTHROUGH).getLog());
        Gdx.app.log("VIGNETTE", manager.get(VIGNETTE).isCompiled() ? "Compiled" : manager.get(VIGNETTE).getLog());
        Gdx.app.log("INVERT", manager.get(INVERT).isCompiled() ? "Compiled" : manager.get(INVERT).getLog());
        Gdx.app.log("EMBOSS", manager.get(EMBOSS).isCompiled() ? "Compiled" : manager.get(EMBOSS).getLog());
    }

}

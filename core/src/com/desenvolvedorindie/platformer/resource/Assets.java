package com.desenvolvedorindie.platformer.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
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
    public static final AssetDescriptor<Texture> BLOCK_DIRT = new AssetDescriptor<>("tiles/dirt.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_COBBLESTONE = new AssetDescriptor<>("tiles/cobblestone.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_OBSIDIAN = new AssetDescriptor<>("tiles/obsidian.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_SLAB = new AssetDescriptor<>("tiles/slab.png", Texture.class);
    @Asset
    public static final AssetDescriptor<Texture> BLOCK_SLOPE = new AssetDescriptor<>("tiles/slope.png", Texture.class);

    //Items
    @Asset
    public static final AssetDescriptor<Texture> ITEM_SWORD = new AssetDescriptor<>("items/sword.png", Texture.class);

    //Entities
    @Asset
    public static final AssetDescriptor<Texture> player = new AssetDescriptor<>("player/player.png", Texture.class);

    @Asset
    public static final AssetDescriptor<SpriterData> grayGuy = new AssetDescriptor<>("GreyGuy/player.scml", SpriterData.class);

    //UI
    @Asset
    public static final AssetDescriptor<TextureAtlas> ui = new AssetDescriptor<>("ui/ui.atlas", TextureAtlas.class);

    @Asset
    public static final AssetDescriptor<BitmapFont> FONT_HOBO_16 = new AssetDescriptor<>("fonts/hobo.ttf", BitmapFont.class, new FreetypeFontLoader.FreeTypeFontLoaderParameter() {
        {
            fontFileName = "fonts/hobo.ttf";
            fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
            fontParameters.size = 8;
        }
    });

    //Shaders
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_PASSTHROUGH = new AssetDescriptor<>("shaders/passthrough.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_VIGNETTE = new AssetDescriptor<>("shaders/vignette.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_INVERT = new AssetDescriptor<>("shaders/invert.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_EMBOSS = new AssetDescriptor<>("shaders/emboss.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_SEPIA = new AssetDescriptor<>("shaders/sepia.vert", ShaderProgram.class);
    @Asset
    public static final AssetDescriptor<ShaderProgram> SHADER_SKY = new AssetDescriptor<>("shaders/sky.vert", ShaderProgram.class);

    //Dungeons
    @Asset
    public static final AssetDescriptor<TiledMap> DUNGEON_WORLD = new AssetDescriptor<>("dungeons/world.tmx", TiledMap.class);

    public static void load() {
        manager.setLoader(SpriterData.class, new SpriterDataLoader(manager.getFileHandleResolver()));
        manager.setLoader(TiledMap.class, new TmxMapLoader(manager.getFileHandleResolver()));
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(manager.getFileHandleResolver()));
        manager.setLoader(BitmapFont.class, new FreetypeFontLoader(manager.getFileHandleResolver()));

        Texture.setAssetManager(manager);

        ShaderProgram.pedantic = false;

        manager.load(Assets.class);
    }

    public static void debug() {
        Gdx.app.log("PASSTHROUGH", manager.get(SHADER_PASSTHROUGH).isCompiled() ? "Compiled" : manager.get(SHADER_PASSTHROUGH).getLog());
        Gdx.app.log("VIGNETTE", manager.get(SHADER_VIGNETTE).isCompiled() ? "Compiled" : manager.get(SHADER_VIGNETTE).getLog());
        Gdx.app.log("INVERT", manager.get(SHADER_INVERT).isCompiled() ? "Compiled" : manager.get(SHADER_INVERT).getLog());
        Gdx.app.log("EMBOSS", manager.get(SHADER_EMBOSS).isCompiled() ? "Compiled" : manager.get(SHADER_EMBOSS).getLog());
        Gdx.app.log("SEPIA", manager.get(SHADER_SEPIA).isCompiled() ? "Compiled" : manager.get(SHADER_SEPIA).getLog());
        Gdx.app.log("SKY", manager.get(SHADER_SKY).isCompiled() ? "Compiled" : manager.get(SHADER_SKY).getLog());
    }

}

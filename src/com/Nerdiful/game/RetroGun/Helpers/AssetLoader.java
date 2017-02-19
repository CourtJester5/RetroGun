package com.Nerdiful.game.RetroGun.Helpers;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
 
public class AssetLoader
{
    public static FreeTypeFontGenerator fontGen;
     
    public static Sprite background;
    public static Sprite player, basicEnemy, waveEnemy, blockEnemy;
    public static Sprite bullet;
    public static Sprite pickupHealth;
    public static Sprite HUD_frame, weaponTab, weaponTile, defenseTab, defenseTile;
    public static Sprite shieldFull, shieldLow, healthFull, healthHigh, healthMid, healthFlash, healthLow;
     
    public static Sound shoot, damage, damage2, collision, explosion, explosion2, health;
     
    private static TextureAtlas atlas;
     
    public static void load()
    {
        //TexturePacker.process("C:/Users/dstout/Eclipse/workspaces/ProjectRetroGun/PRG_Textures", "C:/Users/dstout/Eclipse/workspaces/ProjectRetroGun/RetroGun-android/assets/data", "play_textures");
        atlas = new TextureAtlas(Gdx.files.internal("data/play_textures.atlas"));
         
        //Load in textures from atlas as sprites
        background = atlas.createSprite("backgroundTexture");
        player = atlas.createSprite("playerTexture");
        basicEnemy = atlas.createSprite("basicEnemy");
        waveEnemy = atlas.createSprite("waveEnemy");
        blockEnemy = atlas.createSprite("blockEnemy");
         
        HUD_frame = atlas.createSprite("HUD_frame");
        weaponTab = atlas.createSprite("weaponTab");
        weaponTile = atlas.createSprite("weaponTile");
        defenseTab = atlas.createSprite("defenseTab");
        defenseTile = atlas.createSprite("defenseTile");
         
        shieldFull = atlas.createSprite("shieldIcon_full");
        shieldLow = atlas.createSprite("shieldIcon_low");
        healthFull = atlas.createSprite("healthIcon_full");
        healthHigh = atlas.createSprite("healthIcon_high");
        healthMid = atlas.createSprite("healthIcon_mid");
        healthFlash = atlas.createSprite("healthIcon_lowFlash");
        healthLow = atlas.createSprite("healthIcon_low");
         
        bullet = atlas.createSprite("bulletTexture");
        pickupHealth = atlas.createSprite("pickup_health");
         
        //Flip texture to match y-down
        HUD_frame.flip(false, true);
        weaponTab.flip(false, true);
        weaponTile.flip(false, true);
        defenseTab.flip(false, true);
        defenseTile.flip(false, true);
        shieldFull.flip(false, true);
        shieldLow.flip(false, true);
        healthFull.flip(false, true);
        healthHigh.flip(false, true);
        healthMid.flip(false, true);
        healthFlash.flip(false, true);
        healthLow.flip(false, true);
        pickupHealth.flip(false, true);
         
        //Load sfx
        shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
        damage = Gdx.audio.newSound(Gdx.files.internal("sounds/damage.wav"));
        damage2 = Gdx.audio.newSound(Gdx.files.internal("sounds/damage2.wav"));
        collision = Gdx.audio.newSound(Gdx.files.internal("sounds/collision.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosion2 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion2.wav"));
        health = Gdx.audio.newSound(Gdx.files.internal("sounds/health.wav"));
    }
     
    public static void dispose()
    {
        //Dispose of assets
        atlas.dispose();
        shoot.dispose();
        damage.dispose();
        damage2.dispose();
        collision.dispose();
        explosion.dispose();
        explosion2.dispose();
        health.dispose();
    }
}

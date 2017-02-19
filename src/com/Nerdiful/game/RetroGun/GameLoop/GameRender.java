package com.Nerdiful.game.RetroGun.GameLoop;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
 
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.Helpers.EnemyManager;
import com.Nerdiful.game.RetroGun.Helpers.PickupManager;
import com.Nerdiful.game.RetroGun.Helpers.ProjectileManager;
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;
import com.Nerdiful.game.RetroGun.Objects.Player;
 
public class GameRender
{
    private GameUpdate update;
    private OrthographicCamera cam;
    private Batch sBatch;
     
    private Player player;
    private EnemyManager enemies;
    private ProjectileManager playerFire, enemyFire;
    private PickupManager pickups;
     
    private double gameW, gameH;
     
    private final float HEALTH_BAR_MAX_W = 498f;
    private float healthBarW, shieldBarW;
     
    private int numWTiles = 0, numDTiles = 0;
     
    FreeTypeFontGenerator fontGen;
    FreeTypeFontParameter param;
    BitmapFont bFont;
     
    public GameRender(GameUpdate update)
    {
        this.update = update;
        gameW = update.getGameW(); gameH = update.getGameH();
         
        cam = new OrthographicCamera();
        cam.setToOrtho(true, (float) gameW, (float) gameH);
         
        sBatch = new SpriteBatch();
        sBatch.setProjectionMatrix(cam.combined);
        sBatch.enableBlending();
         
        player = update.getPlayer();
        enemies = update.getDifficulty().getEnemies();
        playerFire = update.getPlayerFire();
        pickups = update.getDifficulty().getPickups();
        enemyFire = update.getDifficulty().getEnemyFire();
         
        healthBarW = HEALTH_BAR_MAX_W;
         
        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BEBAS.ttf"));
        param = new FreeTypeFontParameter();
        param.size = 22;
        param.flip = true;
        bFont = fontGen.generateFont(param);
    }
     
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
 
        sBatch.begin();
        renderBG();
        renderProjectiles();
        enemies.draw(sBatch);
        pickups.draw(sBatch);
        renderPlayer();
        renderUI();
        sBatch.end();
    }
     
    private void renderBG()
    {
        sBatch.draw(AssetLoader.background, 0, 0, (float) gameW, (float) gameH);
    }
     
    private void renderProjectiles()
    {
        playerFire.draw(sBatch);
        enemyFire.draw(sBatch);
    }
     
    private void renderPlayer()
    {
        sBatch.draw(AssetLoader.player, (float) player.getPoints()[1].x, (float) player.getPoints()[1].y, (float) player.getWidth(), (float) player.getHeight());
         
        //renderPlayerCollision();
    }
     
    private void renderUI()
    {
        //Set health bar draw length based on player health
        if(player.getHealth() < player.getMaxHealth())
        {
            healthBarW = HEALTH_BAR_MAX_W * ((float) player.getHealth() / (float) player.getMaxHealth());
            shieldBarW = 0;
        }
        else
        {
            healthBarW = HEALTH_BAR_MAX_W;
            shieldBarW = HEALTH_BAR_MAX_W * ((float) (player.getHealth() - player.getMaxHealth()) / player.getMaxShield());
        }
         
        //Draw weapon and defense tabs
        //... HUD_H
        sBatch.draw(AssetLoader.weaponTab, 0, update.getWeaponTabY());
        sBatch.draw(AssetLoader.defenseTab, (float) (gameW - AssetLoader.defenseTab.getWidth()), update.getDefenseTabY());
         
        //Draw weapon and defense tiles
        if(update.getWeaponTabY() < update.TAB_Y)
        {
            for(int i=0; i<player.getNumWeapons()-1; i++)
                sBatch.draw(AssetLoader.weaponTile, 0, update.getWeaponTabY() + AssetLoader.weaponTab.getHeight() + (i * AssetLoader.weaponTile.getHeight()));
        }
        if(update.getDefenseTabY() < update.TAB_Y)
        {
 
            for(int i=0; i<player.getNumDefenses()-1; i++)
                sBatch.draw(AssetLoader.defenseTile, (float) (gameW - AssetLoader.defenseTile.getWidth()),
                        update.getDefenseTabY() + AssetLoader.defenseTab.getHeight() + (i * AssetLoader.defenseTile.getHeight()));
        }
         
        //Draw the HUD frame and begin the ShapeRenderer
        sBatch.draw(AssetLoader.HUD_frame, 0, (int) (gameH) - AssetLoader.HUD_frame.getHeight());
        sBatch.end();
         
        ShapeRenderer shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(cam.combined);
        shapes.begin(ShapeType.Filled);
         
        //Change health bar color based on player health
        if(healthBarW / HEALTH_BAR_MAX_W > 0.5)
            shapes.setColor(Color.GREEN);
        else if(healthBarW / HEALTH_BAR_MAX_W < 0.125)
            shapes.setColor(Color.RED);
        else
            shapes.setColor(Color.ORANGE);
         
        //Draw health and round progression bars
        shapes.rect(151f + ((HEALTH_BAR_MAX_W - healthBarW) / 2), (float) gameH - AssetLoader.HUD_frame.getHeight() + (AssetLoader.healthFull.getHeight() / 2), healthBarW, 13f);
        shapes.setColor(Color.LIGHT_GRAY);
        shapes.rect(152f, (float) (gameH) - 32f, update.getDifficulty().getProgressPercent() * 496f, 30f);
         
        //Draw shield bars
        if(shieldBarW > 0)
        {
            shapes.setColor(Color.CYAN);
            shapes.rect(151, (float) gameH - AssetLoader.HUD_frame.getHeight() + (AssetLoader.healthFull.getHeight() / 2), shieldBarW / 2, 13f);
            shapes.rect((float) gameW - 151f - (shieldBarW / 2), (float) gameH - AssetLoader.HUD_frame.getHeight() + (AssetLoader.healthFull.getHeight() / 2), shieldBarW / 2, 13f);
        }
         
        //Restart SpriteBatch
        shapes.end();
        shapes.dispose();
        sBatch.setProjectionMatrix(cam.combined);
        sBatch.begin();
 
        //Draw Score
        Sprite healthIcons;
        bFont.draw(sBatch, update.getScore() + "", (int) (gameW / 2), (int) (gameH - bFont.getCapHeight() - 7));
         
        //Set and draw shield and health icons
        if(player.getHealth() == player.getMaxHealth() + player.getMaxShield())
            healthIcons = AssetLoader.shieldFull;
        else if(player.getHealth() > player.getMaxHealth())
            healthIcons = AssetLoader.shieldLow;
        else if(player.getHealth() == player.getMaxHealth())
            healthIcons = AssetLoader.healthFull;
        else if(player.getHealth() / (double) player.getMaxHealth() > 0.5)
            healthIcons = AssetLoader.healthHigh;
        else if(player.getHealth() / (double) player.getMaxHealth() < 0.125)
            healthIcons = AssetLoader.healthLow;
        else
            healthIcons = AssetLoader.healthMid;
         
        sBatch.draw(healthIcons, 138, (int) (gameH) - AssetLoader.HUD_frame.getHeight() + 3, 25, 25);
        sBatch.draw(healthIcons, 637, (int) (gameH) - AssetLoader.HUD_frame.getHeight() + 3, 25, 25);
    }
     
    private void renderPlayerCollision()
    {
        Vector2[] playerCollision = player.getPoints();
         
        sBatch.end();
        ShapeRenderer shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(cam.combined);
        shapes.begin(ShapeType.Line);
        shapes.setAutoShapeType(true);
        shapes.setColor(Color.BLUE);
        shapes.circle((float) player.getCenter().x, (float) player.getCenter().y, (float)player.getRadius());
        shapes.setColor(Color.RED);
        shapes.triangle((float) playerCollision[0].x, (float) playerCollision[0].y, (float) playerCollision[1].x, (float) playerCollision[1].y,
                (float) playerCollision[2].x, (float) playerCollision[2].y);
        shapes.set(ShapeType.Filled);
        shapes.circle((float) player.getCenter().x, (float) player.getCenter().y, (float) 1);
        shapes.end();
        shapes.dispose();
        sBatch.setProjectionMatrix(cam.combined);
        sBatch.begin();
    }
}

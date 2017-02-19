package com.Nerdiful.game.RetroGun;
 
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
 
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;
import com.Nerdiful.game.RetroGun.Screens.SplashScreen;
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.GameLoop.GameRender;
 
public class MainGame extends Game
{
    private GameUpdate update;
    private GameRender render;
     
    private double gameW = 800;
    private double gameH;
     
    private int[] progress;
     
    @Override
    public void create()
    {
        gameDimensions();
        setScreen(new SplashScreen(this));
    }
     
    private void gameDimensions()
    {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        gameH = (gameW * screenH) / screenW;
         
        System.out.println("Screen Res: " + screenW + ", " + screenH);
        System.out.println("Game Res: " + gameW + ", " + gameH);
    }
     
    @Override
    public void dispose()
    {
        super.dispose();
        AssetLoader.dispose();
    }
     
    public void resetScore()
    {
        progress = new int[] {0, 1};
    }
     
    public void setProgress(int[] progress)
    {
        this.progress = progress;
    }
     
    public int[] getProgress()
    {
        return progress;
    }
     
    public double getGameW()
    {
        return gameW;
    }
     
    public double getGameH()
    {
        return gameH;
    }
}

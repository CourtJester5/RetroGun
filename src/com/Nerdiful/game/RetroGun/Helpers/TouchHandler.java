package com.Nerdiful.game.RetroGun.Helpers;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
 
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
 
public class TouchHandler implements InputProcessor
{
    GameUpdate update;
     
    double scale;
     
    public TouchHandler(GameUpdate update)
    {
        this.update = update;
        scale = update.getGameW() / (double) Gdx.graphics.getWidth();
    }
 
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        update.setTouches(pointer, new Vector2((float) (screenX * scale), (float) (screenY * scale)));
        return true;
    }
 
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        update.updateTouches(pointer, new Vector2((float) (screenX * scale), (float) (screenY * scale)));
        return true;
    }
 
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        update.removeTouches(pointer, new Vector2((float) (screenX * scale), (float) (screenY * scale)));
        return true;
    }
     
    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }
 
    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }
 
    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }
 
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }
 
    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
     
}

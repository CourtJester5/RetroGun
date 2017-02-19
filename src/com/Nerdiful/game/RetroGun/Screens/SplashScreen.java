package com.Nerdiful.game.RetroGun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.Nerdiful.game.RetroGun.MainGame;
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;

public class SplashScreen implements Screen
{
	private final int DISPLAY_TIME = 5;

	private OrthographicCamera cam;
	private SpriteBatch spriteBatch;
	
	private MainGame game;
	private double time = 0;
	
	public SplashScreen(MainGame game)
	{
		this.game = game;

		cam = new OrthographicCamera();
		cam.setToOrtho(true, (float) game.getGameW(), (float) game.getGameH());
		
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(cam.combined);
	}

	@Override
	public void render(float delta)
	{
		Texture splashTexture = new Texture(Gdx.files.internal("data/splash.png"));
		TextureRegion splash = new TextureRegion(splashTexture, 0, 0, 1020, 574);
		splash.flip(false, true);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.draw(splash, 0, 0, (float) game.getGameW(), (float) game.getGameH());
		spriteBatch.end();
		time += delta;
		
		if(Gdx.input.justTouched() || time >= DISPLAY_TIME)
		{
			AssetLoader.load();
			splashTexture.dispose();
			game.setScreen(new MainMenuScreen(game, false));
		}
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}

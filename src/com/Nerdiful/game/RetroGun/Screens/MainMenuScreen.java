package com.Nerdiful.game.RetroGun.Screens;

import com.Nerdiful.game.RetroGun.MainGame;
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen
{
	final MainGame game;
	Stage stage;
	Skin skin;
	SpriteBatch sBatch;
	boolean saveProgress;
	
	FreeTypeFontGenerator fontGen;
	FreeTypeFontParameter param;
	BitmapFont bFont;
	
	public MainMenuScreen(MainGame game, boolean saveProgress)
	{
		this.game = game;
		this.saveProgress = saveProgress;
		
		sBatch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		skin = new Skin();
		Gdx.input.setInputProcessor(stage);
		
		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BEBAS.ttf"));
		param = new FreeTypeFontParameter();
		param.size = 50;
		bFont = fontGen.generateFont(param);

		skin.add("bFont", bFont);
		skin.add("bg", AssetLoader.background);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.newDrawable("bg", Color.LIGHT_GRAY);
		buttonStyle.down = skin.newDrawable("bg", Color.DARK_GRAY);
		buttonStyle.font = skin.getFont("bFont");
		skin.add("bStyle", buttonStyle);
		
		TextButton playButton = new TextButton("Play", buttonStyle);
		TextButton quitButton = new TextButton("Quit", buttonStyle);
		playButton.setPosition((float) game.getGameW() / 4, (float) (game.getGameH() - (game.getGameH() / 4)));
		quitButton.setPosition((float) game.getGameW() / 4, (float) game.getGameH() / 4);
		stage.addActor(playButton);
		stage.addActor(quitButton);
		
		playButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				playButton();
			}
		});
		
		quitButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				quitButton();
			}
		});
	}
	
	private void playButton()
	{
		game.setScreen(new PlayScreen(game, saveProgress));
	}
	
	private void quitButton()
	{
		Gdx.app.exit();
	}
	
	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
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
	public void dispose()
	{
		stage.dispose();
		skin.dispose();
	}
	
}

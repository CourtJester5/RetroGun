package com.Nerdiful.game.RetroGun.Screens;

import com.badlogic.gdx.Screen;

import com.Nerdiful.game.RetroGun.MainGame;
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.GameLoop.GameRender;
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate.GameState;

public class PlayScreen implements Screen
{
	private MainGame game;
	private GameUpdate update;
	private GameRender render;
	
	private final int NO_DELAYS_PER_YIELD = 15;
	private final int MAX_FRAME_SKIPS = 5;
	
	private double gameW, gameH;
	
	private float fps = 45;
	private int skips = 0;
	private int noDelays = 0;
	private long afterTime;
	private float period;
	private float sleepTime;
	private float overSleepTime = 0L;
	private long excess = 0L;
	
	private float[] fpsLog = new float[(int) fps];
	private int fpsLogCount = 0;
	
	public PlayScreen(MainGame mainGame, boolean saveScore)
	{
		game = mainGame;
		gameW = game.getGameW(); gameH = game.getGameH();
		period = 1 / fps;
		System.out.println("Period: " + period);
		System.out.println("FPS: " + fps);
		
		if(saveScore)
			update = new GameUpdate(gameW, gameH, mainGame.getProgress());
		else
			update = new GameUpdate(gameW, gameH);
		render = new GameRender(update);
	}
	
	//Game Loop
	@Override
	public void render(float delta)
	{
		if(delta < period)
			delta = period;
		//System.out.println(delta);
		
		update.update(delta);
		render.render();
		
		if(update.getState() == GameState.GAMEOVER)
		{
			game.resetScore();
			game.setScreen(new MainMenuScreen(game, false));
		}
		else if(update.getState() == GameState.ROUND_END)
		{
			game.setProgress(new int[] {update.getScore(), update.getRound() + 1});
			game.setScreen(new MainMenuScreen(game, true));
		}
		
		afterTime = System.nanoTime();
		sleepTime = ((period - delta) - overSleepTime);
		
		if(sleepTime > 0)
		{
			try
			{
				//System.out.println("Sleep: " + sleepTime * 1000 + " ms");
				Thread.sleep((long) (sleepTime * 1000));
			}
			catch(InterruptedException e) {}
			overSleepTime = ((System.nanoTime() - afterTime) - sleepTime) / 1000000000;
		}
		else
		{
			//System.out.println("Did not Sleep: " + sleepTime * 1000 + " ms");
			excess -= sleepTime;
			overSleepTime = 0L;
			
			if(noDelays ++ >= NO_DELAYS_PER_YIELD)
			{
				//System.out.println("Yielding");
				Thread.yield();
				noDelays = 0;
			}
		}
		
		skips = 0;
		while(excess > period && skips < MAX_FRAME_SKIPS)
		{
			//System.out.println("Skipping Frame");
			excess -= period;
			update.update(period);
			skips++;
		}
		
		//displayFPS(delta);
	}
	
	private void displayFPS(float delta)
	{
		fpsLog[fpsLogCount%(int)fps] = delta;
		if(fpsLogCount > fps)
		{
			float fpsDisp = 0;
			for(int i=0; i<(int)fps; i++)
			{
				fpsDisp += fpsLog[i];
			}
			System.out.println("FPS: " + 1 / (fpsDisp / fps));
		}
		else
			System.out.println(fpsLogCount);
		fpsLogCount++;
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

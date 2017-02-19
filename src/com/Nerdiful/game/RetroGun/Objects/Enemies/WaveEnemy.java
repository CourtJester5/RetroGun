package com.Nerdiful.game.RetroGun.Objects.Enemies;

import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile.Weapon;
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;

public class WaveEnemy extends Enemy
{
	private double waveMult = .5;
	
	private static int waveHealth = 30;
	private static double waveSpeed = 1.0;
	private static double waveCooldown = 1.0;
	private static int waveW = 25;
	private static int waveH = 25;
	private static int waveValue = 150;
	private static Weapon waveEquipped = Weapon.BULLET;

	private double originY;
	private int sinAmp = 80;
	private int sinSpeed = 80;
	private int sinOffset = random.nextInt(360);
	
	public WaveEnemy()
	{
		health = waveHealth;
		speed = waveSpeed;
		cooldown = waveCooldown;
		equipped = waveEquipped;
		w = waveW; h = waveH;
		value = waveValue;
		type = Enemies.WAVE;
		multiplier = waveMult;

		x = (int) GameUpdate.gameW + (w / 2);
		y = random.nextInt((int) (GameUpdate.gameH - GameUpdate.HUD_H) - (h + (2 * sinAmp))) + ((h / 2) + sinAmp);
		originY = y;
		
		init();
	}
	
	@Override
	public void update(float delta)
	{
		x -= speed;
		y = (sinAmp * Math.sin((x + sinOffset) / sinSpeed)) + originY;

		super.update(delta);
	}
}

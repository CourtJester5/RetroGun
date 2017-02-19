package com.Nerdiful.game.RetroGun.Objects.Enemies;

import com.Nerdiful.game.RetroGun.Objects.Projectiles.Projectile.Weapon;
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;

public class BasicEnemy extends Enemy
{
	private double basicMult = 0.5;
	
	private int basicHealth = 30;
	private double basicSpeed = 1.75;
	private double basicCooldown = 1.25;
	private int basicW = 25;
	private int basicH = 25;
	private int basicValue = 100;
	private Weapon basicEquipped = Weapon.BULLET;
	
	public BasicEnemy()
	{
		health = basicHealth;
		speed = basicSpeed;
		cooldown = basicCooldown;
		equipped = basicEquipped;
		w = basicW; h = basicH;
		value = basicValue;
		type = Enemies.BASIC;
		multiplier = basicMult;

		x = (int) GameUpdate.gameW + (w / 2);
		y = random.nextInt((int) (GameUpdate.gameH - GameUpdate.HUD_H) - h) + (h / 2);
		
		init();
	}
	
	@Override
	public void update(float delta)
	{
		x -= speed;

		super.update(delta);
	}
}

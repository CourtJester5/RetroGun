package com.Nerdiful.game.RetroGun.Helpers;
 
import java.util.Random;
 
import com.Nerdiful.game.RetroGun.GameLoop.GameUpdate;
import com.Nerdiful.game.RetroGun.Objects.Player;
import com.Nerdiful.game.RetroGun.Objects.Enemies.Enemy;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Bullet;
 
public class DifficultyManager
{
    private float difficulty = 0;
    private int roundNum = 1;
    private float levelLength = 300;
    private float levelElapsed = 0;
    private float damageModifier;
     
    private GameUpdate update;
    private ProjectileManager enemyFire;
    private EnemyManager enemies;
    private PickupManager pickups;
     
    Random random = new Random();
     
    public DifficultyManager(GameUpdate update)
    {
        damageModifier = difficulty;
         
        this.update = update;
        enemyFire = new ProjectileManager(update);
        enemies = new EnemyManager(this);
        pickups = new PickupManager(this);
    }
     
    public DifficultyManager(GameUpdate update, int[] progress)
    {
        damageModifier = difficulty;
         
        this.update = update;
        roundNum = progress[1];
        enemyFire = new ProjectileManager(update);
        enemies = new EnemyManager(this);
        pickups = new PickupManager(this);
    }
     
    public void update(float delta)
    {
        levelElapsed += delta;
        if(levelElapsed > levelLength)
            levelElapsed = levelLength;
        difficulty += levelElapsed;
         
        updateEnemies(delta);
        pickups.update(delta, update.getScore());
        if(levelElapsed >= levelLength && enemies.numEnemies() == 0)
            update.end();
    }
     
    public void updateEnemies(float delta)
    {
        enemies.update(delta);
        enemyFire.update();
    }
     
    public void checkEnemyCollisions(Player player, ProjectileManager playerFire)
    {
        enemies.checkCollisions(player, playerFire);
    }
     
    public void addEnemyFire(Player player, Enemy enemy)
    {
        switch(enemy.getWeapon())
        {
            case BULLET:
                //enemyFire.addProj(new Bullet(enemy.getMultiplier(), enemy.getX(), enemy.getY(), player.getCenter().x, player.getCenter().y));
                enemyFire.addProj(new Bullet(enemy.getMultiplier(), enemy.getX(), enemy.getY(), player.getCenter().x, random.nextDouble() * update.getGameH()));
                break;
            case LASAR:
                break;
            case MISSILE:
                break;
            default:
                break;
        }
    }
     
    public GameUpdate getGameUpdate()
    {
        return update;
    }
     
    public EnemyManager getEnemies()
    {
        return enemies;
    }
     
    public PickupManager getPickups()
    {
        return pickups;
    }
     
    public ProjectileManager getEnemyFire()
    {
        return enemyFire;
    }
     
    public int getRound()
    {
        return roundNum;
    }
     
    public float getProgressPercent()
    {
        return levelElapsed / levelLength;
    }
}

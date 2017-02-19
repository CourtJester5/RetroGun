package com.Nerdiful.game.RetroGun.GameLoop;
 
import java.util.Map;
import java.util.HashMap;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
 
import com.Nerdiful.game.RetroGun.Objects.Player;
import com.Nerdiful.game.RetroGun.Objects.Projectiles.Bullet;
import com.Nerdiful.game.RetroGun.Helpers.AssetLoader;
import com.Nerdiful.game.RetroGun.Helpers.DifficultyManager;
import com.Nerdiful.game.RetroGun.Helpers.ProjectileManager;
import com.Nerdiful.game.RetroGun.Helpers.TouchHandler;
 
public class GameUpdate
{
    public final float TAB_Y;
    public static enum GameState {RUNNING, PAUSED, GAMEOVER, ROUND_END};
    private GameState gameState;
     
    private Player player;
    private ProjectileManager playerFire;
    private DifficultyManager difficulty;
     
    private Map<Integer, Vector2> touchLeft, touchRight, touchHUD;
     
    public static double HUD_H, gameW, gameH;
    private int score = 0;
     
    private int weaponPointer = -1, defensePointer = -1;
    private float weaponTabY, defenseTabY;
     
    public GameUpdate(double gameW, double gameH)
    {
        gameState = GameState.RUNNING;
         
        HUD_H = AssetLoader.HUD_frame.getHeight();
        this.gameW = gameW; this.gameH = gameH;
        TAB_Y = (float) gameH - AssetLoader.weaponTile.getHeight() - AssetLoader.weaponTab.getHeight();
        weaponTabY = TAB_Y; defenseTabY = TAB_Y;
        player = new Player(gameW, gameH);
         
        touchLeft = new HashMap<Integer, Vector2>();
        touchRight = new HashMap<Integer, Vector2>();
        touchHUD = new HashMap<Integer, Vector2>();
         
        playerFire = new ProjectileManager(this);
        difficulty = new DifficultyManager(this);
         
        Gdx.input.setInputProcessor(new TouchHandler(this));
    }
    public GameUpdate(double gameW, double gameH, int[] progress)
    {
        gameState = GameState.RUNNING;
         
        HUD_H = AssetLoader.HUD_frame.getHeight();
        this.gameW = gameW; this.gameH = gameH;
        TAB_Y = (float) gameH - AssetLoader.weaponTile.getHeight() - AssetLoader.weaponTab.getHeight();
        weaponTabY = TAB_Y; defenseTabY = TAB_Y;
        score = progress[0];
        player = new Player(gameW, gameH);
         
        touchLeft = new HashMap<Integer, Vector2>();
        touchRight = new HashMap<Integer, Vector2>();
        touchHUD = new HashMap<Integer, Vector2>();
         
        playerFire = new ProjectileManager(this);
        difficulty = new DifficultyManager(this, progress);
         
        Gdx.input.setInputProcessor(new TouchHandler(this));
    }
     
    /*
     * Called approximately once every frame to update all of the game elements
     * Update the game progression, the player, enemies, all fire, and check for collisions
     */
    public void update(float delta)
    {
        /*
         * If the game is currently running
         * update the game difficulty
         * update the player
         * update the players fire
         * check if the player is colliding with any enemy fire
         * check if the enemy is colliding with the player or the players fire
         */
        if(gameState == GameState.RUNNING)
        {
            difficulty.update(delta);
            updatePlayer();
            updatePlayerFire(delta);
             
            player.checkCollisions(difficulty.getEnemyFire());
            difficulty.checkEnemyCollisions(player, playerFire);
        }
         
        /*
         * If the player has lost all their health
         * play the death sound clip
         * begin game over processes
         */
        if(player.getHealth() <= 0)
        {
            AssetLoader.explosion.play();
            gameState = GameState.GAMEOVER;
        }
    }
     
    /*
     * Player is updated each time the game updates
     * Update the player location based on the where the player is attempting to move
     */
    private void updatePlayer()
    {
        /*
         * If the player is attempting to move
         * move the player to the touch Y location
         */
        if(!touchLeft.isEmpty())
        {
            Object[] touches = touchLeft.values().toArray();
            Vector2 touch = (Vector2) touches[0];
             
            if(touchLeft.size() > 0)
                player.moveToY(touch.y);
        }
    }
     
    /*
     * Players fire is updated each time the game updates
     * Add to the player weapon cool down timer or reset is when necessary.
     * Shoot the currently equipped weapon if the timer has been reset and the player is attempting to shoot.
     */
    private void updatePlayerFire(float delta)
    {
        //Update each player projectile
        playerFire.update();
         
        /*
         * If the cool down for the players next shot has been reach
         * reset the cool down timer
         */
        if(player.nextShot() >= player.getCooldown())
            player.setNextShot(0.0);
         
        /*
         * If the player is attempting to shoot and the cool down timer has been reset
         * add a new projectile based on what weapon the player has equipped.
         * The target is where the touch is located
         */
        if(!touchRight.isEmpty() && player.nextShot() == 0)
        {
            Object[] touches = touchRight.values().toArray();
            Vector2 touch = (Vector2) touches[0];
             
            switch(player.getWeapon())
            {
                case BULLET:
                    playerFire.addProj(new Bullet(player.getPoints()[0].x, player.getPoints()[0].y, touch.x, touch.y));
                    break;
                case LASAR:
                    break;
                case MISSILE:
                    break;
                default:
                    break;
                     
            }
            player.setNextShot(player.nextShot() + delta);
        }
         
        //Increase the cool down timer if it has not been reset
        else if(player.nextShot() > 0)
            player.setNextShot(player.nextShot() + delta);
         
    }
     
    /*
     * Call back function based on player touches
     * Store touch locations based on their screen locations and the game state.
     */
    public void setTouches(int pointer, Vector2 touch)
    {
        /*
         * If the game is currently running
         * store player movement and firing touches if touched above the HUD.
         * Pause the game and store weapon and defense touches and pointers
         */
        if(gameState == GameState.RUNNING)
        {
            /*
             * If touched above the HUD
             * store player movement touches if touched on the left quarter of the screen
             * and player firing touches if touched on the right three quarters of the screen
             */
            if(touch.y < TAB_Y)
            {
                if(touch.x < gameW / 4)
                {
                    touchLeft.clear();
                    touchLeft.put(pointer, touch);
                }
                else if(touch.x > gameW / 4)
                {
                    touchRight.clear();
                    touchRight.put(pointer, touch);
                }
            }
             
            /*
             * Pause the game if the touch is below the HUD
             * Store the HUD touch and track the weapon and defense touch pointers
             */
            else
            {
                gameState = GameState.PAUSED;
                if(touch.x < 150)
                {
                    touchHUD.put(pointer, touch);
                    weaponPointer = pointer;
                }
                else if(touch.x > gameW - 150)
                {
                    touchHUD.put(pointer, touch);
                    defensePointer = pointer;
                }
            }
        }
         
        /*
         * If the game is currently paused
         * based on weapon and defense tab locations and touch location
         * resume game if touch is outside HUD or weapon or defense tiles
         * move weapon and defense tabs if selected
         * assign weapons or defenses if tiles selected
         */
        else if(gameState == GameState.PAUSED)
        {
            /*
             * If the weapon or defense tabs are extended
             * resume the game and reset tabs if touched outside tiles or HUD
             * assign player movement and firing touches accordingly
             */
            if(weaponTabY < TAB_Y || defenseTabY < TAB_Y)
            {
                /*
                 * If the HUD or weapon or defense tabs or tiles are not touched
                 * resume game play and assign player movement and firing touches
                 * reset weapon and defense tabs
                 */
                if(touch.y < TAB_Y && (touch.x > 150 && touch.x < gameW - 150) || (touch.x < 150 && touch.y < weaponTabY)
                        || (touch.x > gameW - 150 && touch.y < defenseTabY))
                {
                    gameState = GameState.RUNNING;
                     
                    /*
                     * If the touch is in the left quarter of the screen
                     * clear any other left touches and add the current
                     */
                    if(touch.x < gameW / 4)
                    {
                        touchLeft.clear();
                        touchLeft.put(pointer, touch);
                    }
                     
                    /*
                     * If the touch is in the right three quarters of the screen
                     * clear any other right touches and add the current
                     */
                    else
                    {
                        touchRight.clear();
                        touchRight.put(pointer, touch);
                    }
                     
                    weaponTabY = TAB_Y; defenseTabY = TAB_Y;
                }
                 
                /*
                 * If the HUD or weapon or defense tabs or tiles are selected
                 * track HUD touches and weapon and defense pointers if tabs are selected
                 * assign weapons and defenses if selected
                 */
                else
                {
                    /*
                     * If weapon tabs or tiles are touched
                     * track touch Y if tab is selected
                     * assign weapon if tile is selected
                     */
                    if(touch.x < 150)
                    {
                        if(touch.y > weaponTabY && touch.y < weaponTabY + AssetLoader.weaponTab.getHeight())
                        {
                            touchHUD.put(pointer, touch);
                            weaponPointer = pointer;
                        }
                        else
                        {
                            //...
                            //Get weapon tile
                        }
                    }
                     
                    /*
                     * If defense tabs or tiles are touched
                     * track touch Y if tab is selected
                     * assign defense if tile is selected
                     */
                    else if(touch.x > gameW - 150)
                    {
                        if(touch.y > defenseTabY && touch.y < defenseTabY + AssetLoader.defenseTab.getHeight())
                        {
                            touchHUD.put(pointer, touch);
                            defensePointer = pointer;
                        }
                        else
                        {
                            //...
                            //Get defense tile
                        }
                    }
                }
            }
            /*
             * If the game is paused and neither weapon or defense tabs are extended
             * resume game if touch is above HUD and assign player movement and firing touches accordingly
             * Track weapon and defense touches
             */
            else
            {
                /*
                 * If touch is above the HUD
                 * resume the game and assign player movement and firing touches
                 */
                if(touch.y < TAB_Y)
                {
                    gameState = GameState.RUNNING;
                     
                    /*
                     * If the touch is in the left quarter of the screen
                     * clear any other left touches and add the current
                     */
                    if(touch.x < gameW / 4)
                    {
                        touchLeft.clear();
                        touchLeft.put(pointer, touch);
                    }
                     
                    /*
                     * If the touch is in the right three quarters of the screen
                     * clear any other right touches and add the current
                     */
                    else
                    {
                        touchRight.clear();
                        touchRight.put(pointer, touch);
                    }
                     
                    weaponTabY = TAB_Y; defenseTabY = TAB_Y;
                }
                 
                /*
                 * If the touch is within the HUD
                 * Store weapon and defense touches and pointers
                 */
                else
                {
                    if(touch.x < 150)
                    {
                        touchHUD.put(pointer, touch);
                        weaponPointer = pointer;
                        //System.out.println("Weapon Touch");
                    }
                    if(touch.x > gameW - 150)
                    {
                        touchHUD.put(pointer, touch);
                        defensePointer = pointer;
                        //System.out.println("Defense Touch");
                    }
                }
            }
        }
    }
     
    /*
     * Callback function if touches are updated
     * Update touch data based on touch location on screen 
     */
    public void updateTouches(int pointer, Vector2 touch)
    {
        /*
         * If game is running
         * update player movement and firing touches if updated touches are above the HUD
         */
        if(gameState == GameState.RUNNING)
        {
            if(touch.y < TAB_Y)
            {
                if(touch.x < gameW / 4)
                {
                    if(touchLeft.containsKey(pointer))
                        touchLeft.put(pointer, touch);
                }
                else
                {
                    if(touchRight.containsKey(pointer))
                        touchRight.put(pointer, touch);
                }
            }
        }
         
        /*
         * If the game is paused
         * Update weapon and defense touches if the stored pointers match the updated touch
         */
        else if(gameState == GameState.PAUSED)
        {
            if(pointer == weaponPointer)
            {
                touchHUD.put(pointer, touch);
                updateHUD(weaponPointer);
            }
            else if(pointer == defensePointer)
            {
                touchHUD.put(pointer, touch);
                updateHUD(defensePointer);
            }
        }
    }
     
    /*
     * Callback function if touches are removed
     * Update the game or the HUD depending on the state of the game and the location of the touch released
     */
    public void removeTouches(int pointer, Vector2 touch)
    {
        /*
         * If the game is currently running
         * If touch is removed on left quarter of screen, clear left touch array
         * If touch is removed on right three quarters of screen, clear right touch array
         */
        if(gameState == GameState.RUNNING)
        {
            if(touch.x < gameW / 4)
                touchLeft.clear();
            else if(touch.x > gameW / 4)
                touchRight.clear();
        }
         
        /*
         * If the game is currently paused and there is either a weapon touch or defense touch
         */
        else if(gameState == GameState.PAUSED)
        {           
            if(weaponPointer != -1 || defensePointer != -1)
            {
                /*
                 * If a tab is dragged at least one tile up and released
                 * and if the tab is the weapon tab and there is more than one weapon
                 * set the weapon tab position as high as possible.
                 * Do the same for the defense tab if it is selected.
                 */
                if(touch.y < TAB_Y - AssetLoader.weaponTile.getHeight())
                {
                    if(pointer == weaponPointer && player.getNumWeapons() > 1)
                        weaponTabY = TAB_Y - (AssetLoader.weaponTile.getHeight() * (player.getNumWeapons() - 1));
                    else if(pointer == defensePointer && player.getNumDefenses() > 1)
                        defenseTabY = TAB_Y - (AssetLoader.defenseTile.getHeight() * (player.getNumDefenses() - 1));
                }
                /*
                 * If the a tab is dragged less than one tile up and released
                 * set the appropriate tab released to its default position
                 */
                else
                {
                    if(pointer == weaponPointer)
                        weaponTabY = TAB_Y;
                    else if(pointer == defensePointer)
                        defenseTabY = TAB_Y;
                }
                 
                /*
                 * Reset the weapon and defense Map pointers for the released tab
                 */
                if(pointer == weaponPointer)
                    weaponPointer = -1;
                else if(pointer == defensePointer)
                    defensePointer = -1;
            }
        }
    }
     
    /*
     * Called whenever touches affect the HUD
     * Move weapon and defense tabs and tiles
     */
    private void updateHUD(int pointer)
    {
        //Store the Y value of the touch from the pointer
        float touchY = touchHUD.get(pointer).y;
         
        /*
         * If the touch is above the HUD and the player has more than one weapon or defense
         * match the weapon or defense tab Y to the touch Y if the touch is not above the number of weapons of defenses tiles
         */
        if(touchY < TAB_Y)
        {
            /*
             * If the weapon tab is selected and the player has more than one weapon available
             * set the tab location at its max if the touch is above the max number of tiles
             * or set the tab location at the touch itself
             */
            if(pointer == weaponPointer && player.getNumWeapons() > 1)
            {
                if(touchY < TAB_Y - AssetLoader.weaponTile.getHeight() * (player.getNumWeapons() - 1))
                    weaponTabY = TAB_Y - (AssetLoader.weaponTile.getHeight() * (player.getNumWeapons() - 1));
                else
                    weaponTabY = touchY;
            }
             
            /*
             * If the defense tab is selected and the player has more than one defense available
             * set the tab location at its max if the touch is above the max number of tiles
             * or set the tab location at the touch itself
             */
            else if(pointer == defensePointer && player.getNumDefenses() > 1)
            {
                if(touchY < TAB_Y - AssetLoader.defenseTile.getHeight() * (player.getNumDefenses() - 1))
                    defenseTabY = TAB_Y - (AssetLoader.defenseTile.getHeight() * (player.getNumDefenses() - 1));
                else
                    defenseTabY = touchY;
            }
             
        /*
         * If the touch is within the HUD
         * set the weapon or defense tab at its default
         */
        }
        else
        {
            if(pointer == weaponPointer)
                weaponTabY = TAB_Y;
            else if(pointer == defensePointer)
                defenseTabY = TAB_Y;
        }
    }
     
    /*
     * Return the player object
     */
    public Player getPlayer()
    {
        return player;
    }
     
    /*
     * Return the DifficultyManager
     */
    public DifficultyManager getDifficulty()
    {
        return difficulty;
    }
     
    /*
     * Return the manager of the players fire
     */
    public ProjectileManager getPlayerFire()
    {
        return playerFire;
    }
     
    /*
     * Return the width of the game
     */
    public double getGameW()
    {
        return gameW;
    }
     
    /*
     * Return the height of the game
     */
    public double getGameH()
    {
        return gameH;
    }
     
    /*
     * return the Y location of the weapon tab
     */
    public float getWeaponTabY()
    {
        return weaponTabY;
    }
     
    /*
     * Return the Y location of the defense tab
     */
    public float getDefenseTabY()
    {
        return defenseTabY;
    }
     
    /*
     * Add the supplied value to the players score
     */
    public void addScore(int value)
    {
        score += value;
    }
     
    /*
     * Return the current players score
     */
    public int getScore()
    {
        return score;
    }
     
    /*
     * Return the current round number
     */
    public int getRound()
    {
        return difficulty.getRound();
    }
     
    /*
     * Return what state the game is currently in
     */
    public GameState getState()
    {
        return gameState;
    }
     
    /*
     * Set the state of the game to the end of the round
     */
    public void end()
    {
        gameState = GameState.ROUND_END;
    }
}

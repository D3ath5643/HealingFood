package d3ath5643.healingFood;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class for HealingFood.
 * 
 * @author d3ath5643
 * @version: 1.0
 */
public class HealingFoodMain extends JavaPlugin{
    
    @Override
    public void onEnable()
    {
        new HealingFoodListener(this);
        HealingFoodUtil.createConfig(this);
        HealingFoodUtil.loadConfig(this);
        HealingFoodUtil.addPermissions(this);
    }
    
    @Override
    public void onDisable()
    {
    }
    
    
}

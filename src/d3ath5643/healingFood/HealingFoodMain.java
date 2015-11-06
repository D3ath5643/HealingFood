package d3ath5643.healingFood;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class for HealingFood.
 * 
 * @author d3ath5643
 * @version: 1.0
 */
public class HealingFoodMain extends JavaPlugin{
    public Permission pluginPermissions = new Permission("HealingFood.*");
    public HashMap<Material, Integer> saturationMap = new HashMap<Material, Integer>();
    public HashMap<Material, Integer> hungerMap = new HashMap<Material, Integer>();
    public boolean absorptionOverflow = true, ambient = true, particles = false;
    public int regenLevel = 3, saturationToHealthRatio = 2, absorptionLength = 480;
    public int requiredHunger = 0;
    
    
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

package d3ath5643.healingFood;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.PluginManager;

/**
 * Class which contains all the utility functions for HealingFood.
 * 
 * @author d3ath5643
 * @version 1.0
 */
public class HealingFoodUtil {
    public static HashMap<Material, Integer> saturationMap = new HashMap<Material, Integer>();
    public static boolean absorptionOverflow = true, ambient = true, particles = false;
    public static int regenLevel = 3, saturationToHealthRatio = 2, absorptionLength = 480;
    
    private static final int regenBaseTicks = 50, baseAbsorption = 4;
    
    public static void createConfig(HealingFoodMain plugin)
    {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public static void loadConfig(HealingFoodMain plugin){
        populateSaturationMap(plugin);
        
        ambient = plugin.getConfig().getBoolean("ambient");
        particles = plugin.getConfig().getBoolean("particles");
        
        regenLevel = plugin.getConfig().getInt("regenLevel");
        saturationToHealthRatio = plugin.getConfig().getInt("saturationToHealthRatio");
        absorptionLength = plugin.getConfig().getInt("absorptionLength");
        
        if(regenLevel <= 0)
            regenLevel = 1;
        if(saturationToHealthRatio <= 0)
            saturationToHealthRatio = 1;
        if(absorptionLength <= 0)
            absorptionOverflow = false;
    }
    
    public static void addPermissions(HealingFoodMain plugin)
    {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.addPermission(plugin.absorbPermission);
        pm.addPermission(plugin.regenPermission);
    }
    
    public static int getLength(Material mat)
    {
        int restoreHealth = getRestoreHealth(mat);
        return (int)Math.ceil((restoreHealth * regenBaseTicks) / 
                Math.pow(2, HealingFoodUtil.regenLevel - 1));
    }
    
    public static int getRestoreHealth(Material mat)
    {
        return saturationMap.get(mat) / saturationToHealthRatio;
    }
    
    public static int getAbsorptionLevel(int extraHealth)
    {
        return (int)Math.ceil((double) extraHealth / baseAbsorption);
    }
    
    private static void populateSaturationMap(HealingFoodMain plugin)
    {
        if(plugin.getConfig().get("foodList") instanceof MemorySection)
        {
            MemorySection foodListMemSection = (MemorySection) plugin.getConfig().get("foodList");
            Map<String, Object> foodList = foodListMemSection.getValues(false);
            
            for(String mat: foodList.keySet())
            {
                Material matKey = Material.getMaterial(mat);
                Integer matVal = foodListMemSection.getInt(mat);
                
                if(matKey == null)
                {
                    plugin.getLogger().warning("No matching material found for " + mat + 
                                               ". Use F3 + h to find the proper name.");
                    continue;
                }
                if(!matKey.isEdible())
                {
                    plugin.getLogger().warning("Material " + mat + " is not an edible material.");
                    continue;
                }
                
                if(matVal == null)
                {
                    plugin.getLogger().warning("Formatting error in Material " + mat + 
                                               ". Needs to be of integer type.");
                    continue;
                }
                if(matVal < 0)
                    matVal = 0;
                
                saturationMap.put(matKey, matVal);
            }
        } 
        else
        {
            plugin.getLogger().warning("Due to a formatting error in foodList, " +
                                       "the saturation map could not be populated!" +
                                       " The HealingFood plugin will not work properly" +
                                       " untill this issue is fixed!");
        }
    }
}

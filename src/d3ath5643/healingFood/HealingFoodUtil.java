package d3ath5643.healingFood;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

/**
 * Class which contains all the utility functions for HealingFood.
 * 
 * @author d3ath5643
 * @version 1.0
 */
public class HealingFoodUtil {    
    private static final int regenBaseTicks = 50, baseAbsorption = 4;
    
    public static void createConfig(HealingFoodMain plugin)
    {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    public static void loadConfig(HealingFoodMain plugin){
        populateSaturationMap(plugin);
        
        plugin.ambient = plugin.getConfig().getBoolean("ambient");
        plugin.particles = plugin.getConfig().getBoolean("particles");
        
        plugin.regenLevel = plugin.getConfig().getInt("regenLevel");
        plugin.saturationToHealthRatio = plugin.getConfig().getInt("saturationToHealthRatio");
        plugin.absorptionLength = plugin.getConfig().getInt("absorptionLength");
        
        if(plugin.regenLevel <= 0)
            plugin.regenLevel = 1;
        if(plugin.saturationToHealthRatio <= 0)
            plugin.saturationToHealthRatio = 1;
        if(plugin.absorptionLength <= 0)
            plugin.absorptionOverflow = false;
    }
    
    public static void addPermissions(HealingFoodMain plugin)
    {
        Map<String, Boolean> children = plugin.pluginPermissions.getChildren();
        children.put("HealingFood.regen", true);
        children.put("HealingFood.absorb", true);
        plugin.pluginPermissions.recalculatePermissibles();
        
        plugin.getServer().getPluginManager().addPermission(plugin.pluginPermissions);
    }
    
    public static boolean hasPermission(Player p, String perName)
    {
        return p.hasPermission(perName) || p.hasPermission("HealingFood.*");
    }
    
    public static int getLength(HealingFoodMain plugin, Material mat)
    {
        int restoreHealth = getRestoreHealth(plugin, mat);
        return (int)Math.ceil((restoreHealth * regenBaseTicks) / 
                Math.pow(2, plugin.regenLevel - 1));
    }
    
    public static int getRestoreHealth(HealingFoodMain plugin, Material mat)
    {
        return plugin.saturationMap.get(mat) / plugin.saturationToHealthRatio;
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
                
                plugin.saturationMap.put(matKey, matVal);
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

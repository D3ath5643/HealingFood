package d3ath5643.healingFood;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Used to listen for the consumption of a food by the player.
 * 
 * @author d3ath5643
 * @version 1.0
 */
public class HealingFoodListener implements Listener{

    HealingFoodMain plugin;
    
    public HealingFoodListener(HealingFoodMain plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e){
        if(plugin.saturationMap.containsKey(e.getItem().getType()) &&
           e.getPlayer().getFoodLevel() + plugin.hungerMap.get(e.getItem().getType()) >= plugin.requiredHunger)
        {
            int regenLength = HealingFoodUtil.getLength(plugin, e.getItem().getType());
            int regenHealth = HealingFoodUtil.getRestoreHealth(plugin, e.getItem().getType());
            int currPlayerHealth = (int) e.getPlayer().getHealth();
            
            if(regenLength < 0)
                regenLength = 1;
            
            if(HealingFoodUtil.hasPermission(e.getPlayer(), "HealingFood.regen") &&
                    currPlayerHealth != (int)e.getPlayer().getMaxHealth())
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                                                   regenLength,
                                                   plugin.regenLevel - 1,
                                                   plugin.ambient,
                                                   plugin.particles));
            
            if(HealingFoodUtil.hasPermission(e.getPlayer(), "HealingFood.absorb") &&
                    plugin.absorptionOverflow && 
                    currPlayerHealth + regenHealth > (int)e.getPlayer().getMaxHealth())
            {
                int extraHealth = currPlayerHealth + regenHealth - (int) e.getPlayer().getMaxHealth();
                int absorptionLevel = HealingFoodUtil.getAbsorptionLevel(extraHealth);
                
                if(absorptionLevel <= 0)
                    absorptionLevel = 1;
                
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,
                                                   plugin.absorptionLength,
                                                   absorptionLevel - 1,
                                                   plugin.ambient,
                                                   plugin.particles), true);
            }
        }
    }
}

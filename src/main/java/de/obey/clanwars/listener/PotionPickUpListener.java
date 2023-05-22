package de.obey.clanwars.listener;
/*

    Author - Obey -> ClanWars
       16.05.2023 / 21:50

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class PotionPickUpListener implements Listener {

    @EventHandler
    public void on(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();

        if(event.getItem().getItemStack().getType() == Material.POTION){
            new BukkitRunnable() {
                @Override
                public void run() {
                    stackPotions(player);
                }
            }.runTaskLater(Init.getInstance(), 1);
        }
    }


    public static void stackPotions(final Player player ) {
        final ItemStack[] contents = player.getInventory().getContents();
        int changed = 0;
        int i = 0;
        while (i < contents.length) {
            ItemStack current = contents[i];
            if (current != null && current.getType() != Material.AIR && current.getAmount() > 0 && current.getAmount() < 64 && current.getType() == Material.POTION) {
                int needed = 64 - current.getAmount();
                int i2 = i + 1;
                while (i2 < contents.length) {
                    ItemStack nextCurrent = contents[i2];
                    if (nextCurrent != null && nextCurrent.getType() != Material.AIR && nextCurrent.getAmount() > 0 && current.getType() == nextCurrent.getType() && current.getDurability() == nextCurrent.getDurability() && (current.getItemMeta() == null && nextCurrent.getItemMeta() == null || current.getItemMeta() != null && current.getItemMeta().equals((Object)nextCurrent.getItemMeta()))) {
                        if (nextCurrent.getAmount() > needed) {
                            current.setAmount(64);
                            nextCurrent.setAmount(nextCurrent.getAmount() - needed);
                            break;
                        }
                        contents[i2] = null;
                        current.setAmount(current.getAmount() + nextCurrent.getAmount());
                        needed = 64 - current.getAmount();
                        ++changed;
                    }
                    ++i2;
                }
            }
            ++i;
        }

        if(changed > 0) {
            player.getInventory().setContents(contents);
            player.updateInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 5, 5);
        }
    }

}

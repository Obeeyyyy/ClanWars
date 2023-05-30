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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public final class PotionPickUpListener implements Listener {

    private final ArrayList<Player> stacking = new ArrayList<>();

    @EventHandler
    public void on(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();

        if(event.getItem().getItemStack().getType() == Material.POTION){

            if(stacking.contains(player)) {
                event.setCancelled(true);
                return;
            }

            stacking.add(player);
            event.getItem().remove();
            event.setCancelled(true);

            new BukkitRunnable() {
                @Override
                public void run() {
                    stackPotions(player, event.getItem().getItemStack());
                }
            }.runTaskLater(Init.getInstance(), 3);
        }
    }

    private void stackPotions(final Player player, final ItemStack newItem) {
        final ItemStack[] contents = player.getInventory().getContents();
        int changed = 0;
        int slot = 0;

        if(newItem.getAmount() == 64) {
            player.getInventory().addItem(newItem);
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 5, 5);
            stacking.remove(player);
            return;
        }

        while (slot < contents.length) {
            final ItemStack slotItem = contents[slot];

            if (slotItem != null && slotItem.getType() != Material.AIR &&
                    slotItem.getAmount() > 0 && slotItem.getAmount() < 64 &&
                    slotItem.getType() == Material.POTION) {

                int needed = 64 - slotItem.getAmount();

                if (newItem.getType() != Material.AIR &&
                        newItem.getAmount() > 0 && newItem.getAmount() < 64 &&
                        slotItem.getType() == newItem.getType() &&
                        slotItem.getDurability() == newItem.getDurability() &&
                        (slotItem.getItemMeta() == null && newItem.getItemMeta() == null ||
                                slotItem.getItemMeta() != null && slotItem.getItemMeta().equals(newItem.getItemMeta()))) {

                    if (newItem.getAmount() > needed) {
                        slotItem.setAmount(64);
                        newItem.setAmount(newItem.getAmount() - needed);
                        changed++;
                        break;
                    }

                    slotItem.setAmount(slotItem.getAmount() + newItem.getAmount());
                    newItem.setAmount(0);
                    changed++;
                    break;
                }
            }
            ++slot;
        }

        if (changed > 0) {
            player.getInventory().setContents(contents);

            if(newItem.getAmount() > 0)
                player.getInventory().addItem(newItem);

            player.updateInventory();
        } else {
            player.getInventory().addItem(newItem);
        }

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 5, 5);
        stacking.remove(player);
    }

}

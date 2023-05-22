package de.obey.clanwars.listener;
/*

    Author - Obey -> ClanWars
       21.05.2023 / 15:01

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.objects.ClanWar;
import de.obey.clanwars.utils.InventoryUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class WarLootListener implements Listener {

    private ClanWar clanWar;

    @EventHandler
    public void on(final InventoryClickEvent event) {

        if(!InventoryUtil.startsWithInventoryTitle(event.getInventory(), "§6§lWarLoot"))
            return;

        if(!InventoryUtil.startsWithInventoryTitle(event.getClickedInventory(), "§6§lWarLoot"))
            return;

        if(event.getSlot() < 44)
            return;

        if(clanWar == null)
            clanWar = Init.getInstance().getClanWar();

        event.setCancelled(true);

        final int currentSite = Integer.parseInt(event.getInventory().getTitle().split(" ")[1]);

        if(event.getSlot() == 49) {
            if (clanWar.getWarLoot().getInventory(currentSite + 1) != null) {
                event.getWhoClicked().openInventory(clanWar.warLoot.getInventory(currentSite + 1));
            }
            return;
        }

        if(event.getSlot() == 47) {
            if (clanWar.getWarLoot().getInventory(currentSite - 1) != null) {
                event.getWhoClicked().openInventory(clanWar.warLoot.getInventory(currentSite - 1));
            }
        }
    }

}

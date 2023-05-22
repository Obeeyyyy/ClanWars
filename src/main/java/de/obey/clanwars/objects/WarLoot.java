package de.obey.clanwars.objects;
/*

    Author - Obey -> ClanWars
       21.05.2023 / 14:10

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.utils.InventoryUtil;
import de.obey.clanwars.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class WarLoot {

    private final HashMap<Integer, Inventory> inventories = new HashMap<>();

    public WarLoot(final ArrayList<ItemStack> items) {
        int invCount = (items.size() > 315 ? 8 : items.size() > 270 ? 7 : items.size() > 225 ? 6 : items.size() > 180 ? 5 : items.size() > 135 ? 4 : items.size() > 90 ? 3 : items.size() > 45 ? 2 : 1);
        int itemCount = 0;

        Bukkit.getConsoleSender().sendMessage("§c§lNEW WAR LOOT > " + items.size());

        for (int i = 1; i <= invCount; i++) {
            Bukkit.getConsoleSender().sendMessage("§a§lINV " + i);

            final Inventory inv = createInventory(i, invCount);

            while (inv.firstEmpty() != -1 && itemCount < items.size()) {
                inv.addItem(items.get(itemCount));
                itemCount++;
                Bukkit.getConsoleSender().sendMessage("§a§lITEM - INV=" + i + "ITEM: " + itemCount);
            }

            inventories.put(i, inv);
        }
    }

    public Inventory getInventory(final int site) {
        return inventories.get(site);
    }

    private Inventory createInventory(final int site, final int maxSites) {
        final Inventory inv = Bukkit.createInventory(null, 9*6, "§6§lWarLoot " + site);
        final ItemStack pane = new ItemBuilder(Material.IRON_FENCE).setDisplayname("§7-§8/§7-").build();

        InventoryUtil.fillFromTo(inv, pane, 45, 53);

        if(site < maxSites)
            inv.setItem(51, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                            .setTextur("MjkxYWM0MzJhYTQwZDdlN2E2ODdhYTg1MDQxZGU2MzY3MTJkNGYwMjI2MzJkZDUzNTZjODgwNTIxYWYyNzIzYSJ9fX0=", UUID.randomUUID())
                    .setDisplayname("§8-§a>").build());

        if(site > 1)
            inv.setItem(47, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3)
                    .setTextur("YWZhNGM4MjcxMDgzNzQ4MGRmNTc1Y2EwZDY0Y2VmMmZjZGFkYWVjZTcwOTFiNzA3NmI5MjNjNjdlNWY0ZTg0OSJ9fX0=", UUID.randomUUID())
                    .setDisplayname("§a<§8-").build());

        return inv;
    }

}

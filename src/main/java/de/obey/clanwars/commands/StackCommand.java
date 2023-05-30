package de.obey.clanwars.commands;
/*

    Author - Obey -> ClanWars
       10.05.2023 / 21:56

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.objects.StackConfig;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class StackCommand implements CommandExecutor {

    private StackConfig stackConfig;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(stackConfig == null)
            stackConfig = Init.getInstance().getStackConfig();

        if(!(sender instanceof Player))
            return false;

        final Player player = (Player) sender;

        if(!player.hasPermission("system.stack")) {
            player.sendMessage("§c§oDu hast keine Rechte dafür§8.");
            return false;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("rl")) {

            if(!player.hasPermission("system.stack.reload")) {
                player.sendMessage("§c§oDu hast keine Rechte dafür§8.");
                return false;
            }

            stackConfig.reloadCfg();
            player.sendMessage("§aConfig neu geladen§8.");
            return false;
        }

        final ItemStack[] contents = player.getInventory().getContents();

        int changed = 0;
        int i = 0;

        while(i < contents.length) {
            final ItemStack item = contents[i];

            if (item != null && (stackConfig.isStackable(item.getType()) || stackConfig.isStackArmor(item.getType()))) {
                int maxStack = item.getType() == Material.POTION ? 64 : stackConfig.isStackArmor(item.getType()) ? stackConfig.getArmorMaxStack() : item.getMaxStackSize();

                int needed = maxStack - item.getAmount();
                int i2 = i + 1;

                while (i2 < contents.length) {
                    final ItemStack itemSearch = contents[i2];

                    if (itemSearch != null && itemSearch.getType() != Material.AIR && itemSearch.getAmount() < maxStack
                            && itemSearch.getType() == item.getType()
                            && (itemSearch.getItemMeta() == null && item.getItemMeta() == null || itemSearch.getItemMeta() != null && itemSearch.getItemMeta().equals(item.getItemMeta())
                    )) {

                        if (itemSearch.getType() == Material.POTION) {
                            if (itemSearch.getDurability() != item.getDurability()) {
                                i2++;
                                continue;
                            }
                        }

                        if (itemSearch.getAmount() > needed) {
                            item.setAmount(maxStack);
                            itemSearch.setAmount(itemSearch.getAmount() - needed);
                            changed++;
                            i2++;
                            continue;
                        }

                        contents[i2] = null;
                        item.setAmount(item.getAmount() + itemSearch.getAmount());
                        needed -= itemSearch.getAmount();
                        changed++;
                    }
                    i2++;
                }
            }
            i++;
        }

        if(changed > 0 ){
            player.getInventory().setContents(contents);
            player.sendMessage("§aDeine Items wurden gestackt§8.");
        } else {
            player.sendMessage("§cEs konnten keine Items gestackt werden§8.");
        }


        return false;
    }
}

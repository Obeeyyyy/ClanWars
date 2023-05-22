package de.obey.clanwars.commands;
/*

    Author - Obey -> ClanWars
       03.05.2023 / 13:52

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.utils.Util;
import de.obey.clanwars.handler.LocationHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ClanWarsCommand implements CommandExecutor {

    private LocationHandler locationHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(locationHandler == null)
            locationHandler = Init.getInstance().getLocationHandler();

        if(!(sender instanceof Player))
            return false;

        final Player player =(Player) sender;

        if(!player.hasPermission("clanwar.admin")) {
            Util.sendMessage(player, "§c§oDu hast keine Rechte dafür§8. (§7clanwar.admin§8)");
            return false;
        }

        if(args.length == 2) {
            if (args[0].equalsIgnoreCase("set")) {
                locationHandler.setLocation(args[1], player.getLocation());
                Util.sendMessage(player, "Du hast die Location §8'§7" + args[1] + "§8'§7 gesetzt§8.");
                return false;
            }
        }

        Util.sendSyntax(player, "/clanwar set <name> | Setze eine Location.",
                "Benötigte Locations: spawn, arena1, arena2, spectating, lobby");

        return false;
    }
}

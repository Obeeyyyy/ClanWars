package de.obey.clanwars.commands;
/*

    Author - Obey -> ClanWars
       24.04.2023 / 20:27

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.multi.multiclan.api.MultiClanAPI;
import de.multi.multiclan.commands.ClanCommand;
import de.obey.clanwars.Init;
import de.obey.clanwars.utils.Util;
import de.obey.clanwars.objects.ClanWar;
import org.bukkit.entity.Player;

public final class WarSubCommand implements ClanCommand {

    private ClanWar clanWar;

    @Override
    public boolean onCommand(final Player player, final String[] args) {
        final MultiClanAPI api = Init.getInstance().getApi();

        if(!api.isPlayerInClan(player.getUniqueId())) {
            Util.sendMessage(player, "§c§oDu bist in keinem Clan§8.");
            return false;
        }

        if(clanWar == null)
            clanWar = Init.getInstance().getClanWar();

        if(args.length == 3) {
            String clanName = args[2].toLowerCase();

            if(!exist(player, api, clanName))
                return false;

            clanName = api.getClan(clanName).getClan();

            if(!isLeader(player, api))
                return false;

            if (args[1].equalsIgnoreCase("start")) {
                clanWar.sendRequest(player, api.getClan(player.getUniqueId()).getClan(), clanName);
                return false;
            }

            if(args[1].equalsIgnoreCase("accept")) {
                clanWar.acceptRequest(player, clanName, api.getClan(player.getUniqueId()).getClan());
                return false;
            }

            if(args[1].equalsIgnoreCase("deny")) {
                clanWar.denyRequest(player, clanName, api.getClan(player.getUniqueId()).getClan());
                return false;
            }

            return false;
        }

        if(args.length == 2) {
            if(args[1].equalsIgnoreCase("join")) {
                clanWar.join(player, api.getClan(player.getUniqueId()).getClan());
                return false;
            }

            if(args[1].equalsIgnoreCase("leave")) {
                clanWar.leave(player);
                return false;
            }

            if(args[1].equalsIgnoreCase("loot")) {
                clanWar.openWarLoot(player);
                return false;
            }
        }

        Util.sendSyntax(player, "/clan war start <clanname>",
                "/clan war accept <clanname>",
                "/clan war deny <clanname>",
                "/clan war join",
                "/clan war leave",
                "/clan war loot");

        return false;
    }

    private boolean exist(final Player player, final MultiClanAPI api, final String clanName) {
        final boolean state = api.getClan(clanName) != null;

        if(!state) {
            Util.sendMessage(player, "§c§oDer Clan §8'§f" + clanName + "§8'§c§o existiert nicht§8.");
            return false;
        }

        return true;
    }

    private boolean isLeader(final Player player , final MultiClanAPI api){
        final boolean state = api.getRank(api.getClan(player.getUniqueId()).getClan().toLowerCase(), player.getName()) == MultiClanAPI.RankType.OWNER;

        if(!state) {
            Util.sendMessage(player, "§c§oDu bist nicht der Clanleader§8.");
            return false;
        }

        return true;
    }
}

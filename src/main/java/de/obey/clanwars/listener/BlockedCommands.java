package de.obey.clanwars.listener;
/*

    Author - Obey -> ClanWars
       19.05.2023 / 16:22

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.utils.Util;
import de.obey.clanwars.objects.ClanWar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class BlockedCommands implements Listener {

    private ClanWar clanWar;

    // commands im clanwar blockieren

    @EventHandler
    public void on(final AsyncPlayerChatEvent event) {
        if (clanWar == null)
            clanWar = Init.getInstance().getClanWar();

        if (!clanWar.isInWar(event.getPlayer()))
            return;

        final String command = event.getMessage().split(" ")[0];

        if (command.equalsIgnoreCase("/spawn") ||
            command.equalsIgnoreCase("/tpa") ||
            command.equalsIgnoreCase("/warp") ||
            command.equalsIgnoreCase("/enderchest") ||
            command.equalsIgnoreCase("/ec")) {

            event.setCancelled(true);
            Util.sendMessage(event.getPlayer(), "Dieser Command ist im War §c§odeaktiviert§8.");
            return;
        }
    }

}

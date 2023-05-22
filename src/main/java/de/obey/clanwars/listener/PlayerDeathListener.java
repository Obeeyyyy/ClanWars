package de.obey.clanwars.listener;
/*

    Author - Obey -> ClanWars
       19.05.2023 / 20:57

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.objects.ClanWar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class PlayerDeathListener implements Listener {

    private ClanWar clanWar;

    @EventHandler
    public void on(final PlayerDeathEvent event) {

        if(clanWar == null)
            clanWar = Init.getInstance().getClanWar();

        final Player player = event.getEntity();

        if(!clanWar.isInWar(player))
            return;

        if(!clanWar.isFighting())
            return;

        clanWar.die(player, player.getKiller(), event.getDrops());
        event.getDrops().clear();
        event.setDeathMessage("");

    }

}

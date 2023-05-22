package de.obey.clanwars.listener;
/*

    Author - Obey -> ClanWars
       19.05.2023 / 16:18

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.objects.ClanWar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class PlayerHitListener implements Listener {

    private ClanWar clanWar;

    @EventHandler
    public void on(final EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        if(clanWar == null)
            clanWar = Init.getInstance().getClanWar();

        final Player player = (Player) event.getEntity();

        if(!clanWar.isInWar(player))
            return;

        if(!clanWar.isFighting())
            event.setCancelled(true);
    }
}

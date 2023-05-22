package de.obey.clanwars.utils;
/*

    Author - Obey -> ClanWars
       24.04.2023 / 20:35

    You are NOT allowed to use this code in any form
 without permission from me, obey, the creator of this code.
*/

import de.multi.multiclan.MultiClan;
import de.multi.multiclan.clan.ClanPlayer;
import de.obey.clanwars.Init;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@UtilityClass
public final class Util {

    public void sendMessage(final Player player, final String message) {
        player.sendMessage(MultiClan.getInstance().getConfigLanguage().getCachedLanguages().get("prefix").get(0) + ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendSyntax(final Player player, final String... lines) {
        final String prefix = MultiClan.getInstance().getConfigLanguage().getCachedLanguages().get("prefix").get(0);
        player.sendMessage(prefix + "Bitte nutze§8:");

        for (final String line : lines)
            player.sendMessage("§8 -§7" + ChatColor.translateAlternateColorCodes('&', line));

    }

    public void sendMessageToClanMembers(final String clan, final String message) {
        for (final ClanPlayer clanPlayer : Init.getInstance().getApi().getClan(clan).getPlayer()) {
            final Player player = Bukkit.getPlayer(clanPlayer.getPlayer());
            if(player != null && player.isOnline()) {
                sendMessage(player, message);
            }
        }
    }

    public String getTimeFromMillis(long millis) {
        int minutes = 0;
        int seconds = 0;

        while (millis >= 1000) {
            seconds++;
            millis -= 1000;
        }

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }

        return (minutes > 0 ? minutes + "min " : "") +(seconds > 0 ? seconds + "sek " : "");
    }

}

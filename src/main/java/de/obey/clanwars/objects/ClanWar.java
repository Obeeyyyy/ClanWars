package de.obey.clanwars.objects;
/*

    Author - Obey -> ClanWars
       06.05.2023 / 15:33

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import de.obey.clanwars.utils.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
public final class ClanWar {

    private final int teamAmount = 6; // spieler pro team.

    private String clan1, clan2, lastWon;
    private int state = 0; // 0 = clear, 1 = starting, 2 = running, 3 = endphase

    private HashMap<String, ArrayList<Player>> players = new HashMap<>(); // team1, team2
    private HashMap<Player, String> teams = new HashMap<>();

    private long startMillis;

    private HashMap<String, String> warRequests = new HashMap<>();

    private BukkitTask runnable;

    public WarLoot warLoot;
    public ArrayList<ItemStack> tempWarLootList = new ArrayList<>();

    public ClanWar() {}

    public boolean isInWar(final Player player) {
        return teams.containsKey(player);
    }

    public boolean isFighting() {
        return state == 2;
    }

    public void die(final Player player, final Player killer, final List<ItemStack> drops) {
        final String playerClan = teams.get(player);
        final String killerClan = teams.get(killer);

        if(!drops.isEmpty())
            tempWarLootList.addAll(drops);

        new BukkitRunnable() {
            @Override
            public void run() {
                Init.getInstance().getLocationHandler().teleportToLocation(player, "spectating");
                player.setGameMode(GameMode.SPECTATOR);
                players.get(playerClan).remove(player);
                players.get("spectating").add(player);

                if(killer != null) {
                    sendMessageToAllParticipants("§c" + player.getName() + "§7 wurde von §a" + killer.getName() + "§7 getötet§8. (§f" + players.get(playerClan).size() + " vs " + players.get(killerClan).size() + "§8)");
                } else {
                    sendMessageToAllParticipants("§c" + player.getName() + "§7 ist gestorben§8. (§f" + players.get(playerClan).size() + " vs " + players.get(killerClan).size() + "§8)");
                }

                if(players.get(playerClan).size() == 0) {
                    // end war
                    state = 3;
                    final long runningMillis = System.currentTimeMillis() - startMillis;

                    sendMessageToAllParticipants("Der War ist vorbei§8.§7 Gewonnen hat §a" + killerClan + "§8.");
                    sendMessageToAllParticipants("Der War hat " + Util.getTimeFromMillis(runningMillis) + "gedauert§8.");

                    sendMessageToAllParticipants("Die Kriegsbeute kann mit §8'§f/clan war loot§8' §7abgeholt werden§8.");

                    lastWon = killerClan;

                    endWar();
                }
            }
        }.runTaskLater(Init.getInstance(), 10);
    }

    public void sendRequest(final Player requestingPlayer, final String challenger, final String challenged) {
        if(challenger.equalsIgnoreCase(challenged)) {
            Util.sendMessage(requestingPlayer, "§c§oDu kannst keine Clanwaranfrage an deinen Clan senden§8.");
            return;
        }

        if(state != 0) {
            Util.sendMessage(requestingPlayer, "§c§oEs läuft bereits ein Clanwar§8.");
            return;
        }

        if (warRequests.containsKey(challenger)) {
            Util.sendMessage(requestingPlayer, "Du hast bereits eine Clanwaranfrage an " + warRequests.get(challenger) + " gesendet§8.");
            return;
        }

        warRequests.put(challenger, challenged);

        Util.sendMessageToClanMembers(challenger, "Der Clanleader hat " + challenged + " eine Clanwaranfrage gesendet§8.");
        Util.sendMessageToClanMembers(challenged, "Ihr habt eine Clanwaranfrage von " + challenger + " erhalten§8.");
        Util.sendMessageToClanMembers(challenged, "Nutze /clan war accept <" + challenger + "> um anzunehmen§8.");
        Util.sendMessageToClanMembers(challenged, "Nutze /clan war deny <" + challenger + "> um abzulehnen§8.");
    }

    public void acceptRequest(final Player accepter, final String challengerClan, final String challengedClan) {
        if (!warRequests.containsKey(challengerClan)) {
            Util.sendMessage(accepter, "Du hast keine Clanwaranfrage von " + challengerClan + "§8.");
            return;
        }

        warRequests.remove(challengerClan);
        warRequests.remove(challengedClan);

        if(state != 0) {
            Util.sendMessage(accepter, "§c§oEs läuft bereits ein Clanwar§8.");
            return;
        }

        teams.clear();
        players.clear();
        warLoot = null;
        tempWarLootList.clear();

        clan1 = challengerClan;
        clan2 = challengedClan;

        players.put(clan1, new ArrayList<>());
        players.put(clan2, new ArrayList<>());
        players.put("spectating", new ArrayList<>());

        startWarRunnable();

        Util.sendMessageToClanMembers(challengerClan, challengedClan + " hat die Clanwaranfrage §a§oangenommen§8.");
        Util.sendMessageToClanMembers(challengerClan, "Nutze §8'§f/clan war join§8' §7um dem War beizutreten§8.");
        Util.sendMessageToClanMembers(challengerClan, "Der War startet in 60 Sekunden§8.");

        Util.sendMessageToClanMembers(challengedClan, "Ihr habt die Clanwaranfrage von " + challengerClan + " §a§oangenommen§8.");
        Util.sendMessageToClanMembers(challengedClan, "Nutze §8'§f/clan war join§8' §7um dem War beizutreten§8.");
        Util.sendMessageToClanMembers(challengedClan, "Der War startet in 60 Sekunden§8.");
    }

    public void denyRequest(final Player accepter, final String challengerClan, final String challengedClan) {
        if (!warRequests.containsKey(challengerClan)) {
            Util.sendMessage(accepter, "Du hast keine Clanwaranfrage von " + challengerClan + "§8.");
            return;
        }

        warRequests.remove(challengerClan);
        warRequests.remove(challengedClan);

        Util.sendMessageToClanMembers(challengerClan, challengedClan + " hat die Clanwaranfrage §c§oabgelehnt§8.");
        Util.sendMessageToClanMembers(challengedClan, "Ihr habt die Clanwaranfrage von " + challengerClan + " §c§oabgelehnt§8.");
    }

    private void startWarRunnable() {
        state = 1; // setting state to starting
        startMillis = System.currentTimeMillis();

        runnable = new BukkitRunnable() {

            int remaining = 60; // sekunden bis kireg startet

            @Override
            public void run() {

                if(remaining > 2 && (remaining == 60 || remaining == 30 || remaining == 20 || remaining <= 10)) {
                    sendMessageToAllParticipants("Der War startet in §e" + remaining + "§7 sekunden§8.");
                }

                if(remaining == 2) {
                    sendMessageToAllParticipants("Alle Spieler werden in die Arena teleportiert §8... §c§omacht euch bereit !");
                    players.get(clan1).forEach(player -> Init.getInstance().getLocationHandler().teleportToLocation(player, "arena1"));
                    players.get(clan2).forEach(player -> Init.getInstance().getLocationHandler().teleportToLocation(player, "arena2"));
                }

                if(remaining == 0) {
                    if(teams.size() == 0) {
                        endWar();
                        return;
                    }

                    state = 2;
                    startMillis = System.currentTimeMillis();
                    sendMessageToAllParticipants("PvP ist aktiviert§8, §7viel Glück§8!");
                    cancel();
                }

                remaining--;

            }
        }.runTaskTimer(Init.getInstance(), 20, 20);
    }

    public void endWar() {
        runnable.cancel();

        new BukkitRunnable() {

            @Override
            public void run() {
                teams.keySet().forEach(player -> {
                    Init.getInstance().getLocationHandler().teleportToLocation(player, "spawn");
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 1);
                });

                teams.clear();
                players.clear();

                if(!tempWarLootList.isEmpty()) {
                    warLoot = new WarLoot(tempWarLootList);
                    tempWarLootList.clear();
                }

                state = 0;
            }
        }.runTaskLater(Init.getInstance(), 60);

        for (Entity entity : Init.getInstance().getLocationHandler().getLocation("arena1").getWorld().getEntities()) {
            if(entity instanceof Item)
                entity.remove();
        }
    }

    public void join(final Player player, final String clan) {
        if(state != 1) {
            Util.sendMessage(player, "Es laufen keine War vorbereitungen§8.");
            return;
        }

        if(!clan1.equalsIgnoreCase(clan) && !clan2.equalsIgnoreCase(clan)) {
            Util.sendMessage(player, "Dein Clan nimmt nicht an diesem War teil§8.");
            return;
        }

        if(teams.containsKey(player)) {
            Util.sendMessage(player, "Du nimmst bereits am War teil§8,§7 nutze /clan war leave um zu verlassen§8.");
            return;
        }

        if(players.get(clan).size() >= teamAmount) {
            Util.sendMessage(player, "Dein Team ist bereits voll§8, §7pro Clan kämpfen " + teamAmount + " Spieler§8.");
            return;
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);

        teams.put(player, clan);
        players.get(clan).add(player);

        Init.getInstance().getLocationHandler().teleportToLocation(player, "lobby");

        sendMessageToAllParticipants(player.getName() + " §8(§f" + clan + "§8)§7 hat den War §a§obetreten§8. [§e" + teams.size() + "§7/§e" + (teamAmount * 2) + "§8]");
    }

    public void leave(final Player player) {
        final String clan = teams.get(player);

        players.get(clan).remove(player);
        teams.remove(player);

        Init.getInstance().getLocationHandler().teleportToLocation(player, "spawn");

        sendMessageToAllParticipants(player.getName() + " §8(§f" + clan + "§8)§7 hat den War §c§overlassen§8. [§e" + teams.size() + "§7/§e" + (teamAmount * 2) + "§8]");
    }

    public void sendMessageToAllParticipants(final String message) {
        for (final ArrayList<Player> value : players.values()) {
            for (final Player player : value) {
                Util.sendMessage(player, message);
            }
        }
    }

    public void chat(final Player player, final String message) {
        final String team = teams.get(player);

        for (Player player1 : players.get(team)) {
            player1.sendMessage("§8[§9§lCW§8] §7" + player1.getName() + "§8: §f" + message);
        }
    }

    public void openWarLoot(final Player player) {
        if(lastWon == null || warLoot == null || warLoot.getInventory(1) == null) {
            Util.sendMessage(player, "Die Beutelager sind Leer§8.");
            return;
        }

        if(!Init.getInstance().getApi().getClan(player.getUniqueId()).getClan().equalsIgnoreCase(lastWon)) {
            Util.sendMessage(player, "Die Beute gehört " + lastWon + "§8.");
            return;
        }

        player.openInventory(warLoot.getInventory(1));
        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
    }
}

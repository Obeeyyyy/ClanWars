package de.obey.clanwars;
/*

    Author - Obey -> ClanWars
       24.04.2023 / 20:19

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.multi.multiclan.MultiClan;
import de.multi.multiclan.api.MultiClanAPI;
import de.obey.clanwars.commands.ClanWarsCommand;
import de.obey.clanwars.commands.StackCommand;
import de.obey.clanwars.commands.WarSubCommand;
import de.obey.clanwars.handler.LocationHandler;
import de.obey.clanwars.listener.*;
import de.obey.clanwars.objects.ClanWar;
import de.obey.clanwars.objects.StackConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
public final class Init extends JavaPlugin {

    private MultiClanAPI api;
    private LocationHandler locationHandler;
    private StackConfig stackConfig;

    private ClanWar clanWar = new ClanWar();

    @Override
    public void onEnable() {

        final PluginManager pluginManager = Bukkit.getPluginManager();

        if(pluginManager.getPlugin("MultiClan") == null) {
            pluginManager.disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage("§cDieses Plugin benötigt MultiClan§8.");
            return;
        }

        createFolderAndFiles();

        locationHandler = new LocationHandler();
        stackConfig = new StackConfig();

        getCommand("stack").setExecutor(new StackCommand());
        getCommand("clanwar").setExecutor(new ClanWarsCommand());

        api = MultiClan.getMultiClanAPI();
        api.registerClanCommand(new WarSubCommand(), new String[] {"war"});

        pluginManager.registerEvents(new PotionPickUpListener(), this);
        pluginManager.registerEvents(new PlayerHitListener(), this);
        pluginManager.registerEvents(new BlockedCommands(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new WarLootListener(), this);
    }

    private void createFolderAndFiles() {
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        final File stackConfig = new File(getDataFolder().getPath() + "/stackConfig.yml");

        if(!stackConfig.exists()) {
            try {
                stackConfig.createNewFile();
            } catch (IOException ignore) {}
        }
    }

    public static Init getInstance() {
        return getPlugin(Init.class);
    }
}

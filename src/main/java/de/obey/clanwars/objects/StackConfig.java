package de.obey.clanwars.objects;
/*

    Author - Obey -> ClanWars
       10.05.2023 / 22:02

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public final class StackConfig {

    private final File file;
    private YamlConfiguration cfg;

    private ArrayList<String> materials = new ArrayList<>();

    public StackConfig() {
        file = new File(Init.getInstance().getDataFolder().getPath() + "/stackConfig.yml");
        reloadCfg();
    }

    public boolean isStackable(final Material material) {
        return materials.contains(material.name().toLowerCase());
    }

    public void reloadCfg() {
        cfg = YamlConfiguration.loadConfiguration(file);

        if(!cfg.contains("items")) {
            cfg.set("items", Arrays.asList("potion", "stick"));
            saveCfg();
        } else {
            materials = (ArrayList<String>) cfg.get("items");
        }
    }

    public void saveCfg() {
        try {
            cfg.save(file);
        } catch (IOException ignored) {}
    }
}

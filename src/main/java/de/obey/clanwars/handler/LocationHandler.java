package de.obey.clanwars.handler;
/*

    Author - Obey -> ClanWars
       29.04.2023 / 19:39

    You are NOT allowed to use this code in any form 
 without permission from me, obey, the creator of this code.
*/

import de.obey.clanwars.Init;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class LocationHandler {

    private final File file;
    private final YamlConfiguration cfg;

    private final HashMap<String, Location> locations = new HashMap<>();

    public LocationHandler() {
        file = new File(Init.getInstance().getDataFolder().getPath() + "/locations.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void teleportToLocation(final Player player, final String location) {
        if(getLocation(location) == null) {
            Bukkit.getConsoleSender().sendMessage("§4§l CLANWAR <:> Location existert nicht ! " + location);
            return;
        }

        player.teleport(getLocation(location));
    }

    public void setLocation(final String locationName, final Location location) {
        cfg.set("location." + locationName, encode(location));
        saveToFile();
    }

    public Location getLocation(final String locationName) {
        if(locations.containsKey(locationName))
            return locations.get(locationName);

        if(cfg.contains("location." + locationName)) {
            final Location location = decode(cfg.getString("location." + locationName));
            locations.put(locationName, location);
            return location;
        }

        return null;
    }

    private Location decode(final String crypt) {

        final String[] cryptedData = crypt.split("#");
        final Location location = new Location(Bukkit.getWorld(cryptedData[0]),
                Double.parseDouble(cryptedData[1]),
                Double.parseDouble(cryptedData[2]),
                Double.parseDouble(cryptedData[3]),
                Float.parseFloat(cryptedData[4]),
                Float.parseFloat(cryptedData[5]));

        if(location.getWorld() == null)
            return null;

        return location;
    }

    private String encode(final Location location) {
        return location.getWorld().getName() +  "#" + location.getX() + "#" + location.getY() + "#" + location.getZ() + "#" + location.getYaw() + "#" + location.getPitch();
    }

    public void saveToFile() {
        try {
            cfg.save(file);
        } catch (final IOException ignored) {}
    }

}

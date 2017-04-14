package aderm.pw.commissary.events;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import aderm.pw.commissary.Main;

public class SignClick implements Listener {

    private Main main;

    public SignClick(Main pl) {
        main = pl;
    }

    private String CName;

    @EventHandler
    public void signClick(PlayerInteractEvent e) {

        String prefix = main.getConfig().getString("Commissary-Prefix");

        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                if (sign.getLine(0).equalsIgnoreCase(col(main.getConfig().getString("First-Line")))) {
                    String name = ChatColor.stripColor(sign.getLine(1));

                    if(!(main.commissaries.contains(name))) {
                        p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
                        return;
                    }

                    CName = name;

                    int pointsToEnter = main.commissaries.getInt(name + ".Points");
                    int currentPoints = main.players.getInt(p.getUniqueId().toString() + ".Points");
                    if (pointsToEnter <= currentPoints) {
                        main.players.set(p.getUniqueId().toString() + ".inCommissary", true);
                        saveLoc(p);
                        timer(p);
                        int finalPoints = currentPoints - pointsToEnter;
                        main.players.set(p.getUniqueId().toString() + ".Points", finalPoints);
                        if (!(pointsToEnter <= 0)) {
                            p.sendMessage(col(main.getConfig().getString("Deducted-Points").replace("{points}", String.valueOf(pointsToEnter)).replace("{prefix}", prefix)));
                        }
                        savePlayers();

                        double x = main.commissaries.getDouble(name + ".X");
                        double y = main.commissaries.getDouble(name + ".Y");
                        double z = main.commissaries.getDouble(name + ".Z");
                        float yaw = (float) main.commissaries.getDouble(name + ".Yaw");
                        float pitch = (float) main.commissaries.getDouble(name + ".Pitch");

                        p.teleport(new Location(p.getWorld(), x, y, z, yaw, pitch));
                    } else {
                        p.sendMessage(col(main.getConfig().getString("Not-Enough-To-Enter").replace("{prefix}", prefix)));
                    }
                }
            }
        }
    }

    private void timer(Player p) {
        new BukkitRunnable() {
            int time = main.commissaries.getInt(CName + ".Time");

            @Override
            public void run() {
                time--;
                if (time <= 0) {

                    if (!main.players.getBoolean(p.getUniqueId().toString() + ".inCommissary")) {
                        cancel();
                        removeLoc(p);
                        return;
                    }

                    cancel();
                    teleportPlayer(p);
                    removeLoc(p);
                }
            }

        }.runTaskTimer(main, 0, 20);
    }

    private void saveLoc(Player p) {
        String UUID = p.getUniqueId().toString();
        float x = p.getLocation().getBlockX();
        float y = p.getLocation().getBlockY();
        float z = p.getLocation().getBlockZ();
        float yaw = p.getLocation().getYaw();
        float pitch = p.getLocation().getPitch();

        main.locations.set(UUID + ".X", x);
        main.locations.set(UUID + ".Y", y);
        main.locations.set(UUID + ".Z", z);
        main.locations.set(UUID + ".Yaw", yaw);
        main.locations.set(UUID + ".Pitch", pitch);
        saveLocations();
    }

    private void removeLoc(Player p) {
        String UUID = p.getUniqueId().toString();
        main.locations.set(UUID + ".X", null);
        main.locations.set(UUID + ".Y", null);
        main.locations.set(UUID + ".Z", null);
        main.locations.set(UUID + ".Yaw", null);
        main.locations.set(UUID + ".Pitch", null);
        main.players.set(UUID + ".inCommissary", null);
        main.locations.set(UUID, null);
        saveLocations();
    }

    private void teleportPlayer(Player p) {
        String prefix = main.getConfig().getString("Commissary-Prefix");
        String UUID = p.getUniqueId().toString();
        double x = main.locations.getDouble(UUID + ".X");
        double y = main.locations.getDouble(UUID + ".Y");
        double z = main.locations.getDouble(UUID + ".Z");
        float yaw = (float) main.locations.getDouble(UUID + ".Yaw");
        float pitch = (float) main.locations.getDouble(UUID + ".Pitch");
        p.teleport(new Location(p.getWorld(), x, y, z, yaw, pitch));
        p.sendMessage(col(main.getConfig().getString("Teleported-To-Original-Location").replace("{prefix}", prefix)));
    }

    private void saveLocations() {
        try {
            main.locations.save(main.locationsyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayers() {
        try {
            main.players.save(main.playersyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String col(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
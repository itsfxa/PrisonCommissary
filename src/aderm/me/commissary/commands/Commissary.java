package aderm.me.commissary.commands;

import aderm.me.commissary.Main;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Commissary implements CommandExecutor {

    private Main main;

    public Commissary(Main pl) {
        main = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        String prefix = main.getConfig().getString("Commissary-Prefix");

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getConfig().getString(col("Must-Be-Player")));
            return true;
        }

        Player p = (Player) sender;

        if (!(p.hasPermission("commissary.admin"))) {
            p.sendMessage(col(main.getConfig().getString("No-Permission")));
            return true;
        }

        if (p.hasPermission("commissary.admin")) {

            // Help msg for commissary commands.
            if (args.length == 0) {
                p.sendMessage(col("&cCommissary help page:"));
                p.sendMessage(col("&7/commissary create <name> <points> <time in seconds>"));
                p.sendMessage(col("&7/commissary tp <name>"));
                p.sendMessage(col("&7/commissary settp <name>"));
                p.sendMessage(col("&7/commissary setprice <name> <new points>"));
                p.sendMessage(col("&7/commissary settime <name> <new time>"));
                p.sendMessage(col("&7/commissary remove <name>"));
                return true;
            }

        // args 1 error messages
        if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    main.reloadConfig();
                    p.sendMessage(col(main.getConfig().getString("Config-Reloaded")));
                    return true;
                }

                if (args[0].equalsIgnoreCase("create")) {
                    p.sendMessage(col("&cPlease specify a commissary name, points, and time!"));
                    p.sendMessage(col("&cUsage: /commissary create test 10 120"));
                    return true;
                }


                if (args[0].equalsIgnoreCase("tp")) {
                    p.sendMessage(col("&cPlease specify a commissary to TP to!"));
                    p.sendMessage(col("&cUsage: /commissary tp test"));
                    return true;
                }


                if (args[0].equalsIgnoreCase("settp")) {
                    p.sendMessage(col("&cPlease specify a commissary name!"));
                    p.sendMessage(col("&cUsage: /commissary settp test"));
                    return true;
                }


                if (args[0].equalsIgnoreCase("setprice")) {
                    p.sendMessage(col("&cPlease specify the commisary and the new price!"));
                    p.sendMessage(col("&cUsage: /commissary setprice test 100"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("settime")) {
                    p.sendMessage(col("&cPlease specify the commisary and the new time!"));
                    p.sendMessage(col("&cUsage: /commissary settime test 100"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    p.sendMessage(col("&cPlease specify a commissary to remove!"));
                    p.sendMessage(col("&cUsage: /commissary remove test"));
                    return true;
                }

            }


            // args 2
            if (args.length == 2) {
                // tp cmd
                if (args[0].equalsIgnoreCase("tp")) {
                    String toTp = args[1];

                    if (!main.commissaries.contains(toTp)) {
                        p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
                        return true;
                    }

                    float x = main.commissaries.getInt(toTp + ".X");
                    float y = main.commissaries.getInt(toTp + ".Y");
                    float z = main.commissaries.getInt(toTp + ".Z");
                    float yaw = main.commissaries.getInt(toTp + ".Yaw");
                    float pitch = main.commissaries.getInt(toTp + ".Pitch");
                    Location teleport = new Location(p.getWorld(), x, y, z, yaw, pitch);
                    p.teleport(teleport);
                    p.sendMessage(col(main.getConfig().getString("Commissary-TP").replace("{prefix}", prefix).replace("{commissary}", toTp)));

                    return true;
                }

                // settp cmd
                if (args[0].equalsIgnoreCase("settp")) {
                    String setTp = args[1];

                    if (!main.commissaries.contains(setTp)) {
                        p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
                        return true;
                    }

                    main.commissaries.set(setTp + ".X", p.getLocation().getX());
                    main.commissaries.set(setTp + ".Y", p.getLocation().getY());
                    main.commissaries.set(setTp + ".Z", p.getLocation().getZ());
                    main.commissaries.set(setTp + ".Yaw", p.getLocation().getYaw());
                    main.commissaries.set(setTp + ".Pitch", p.getLocation().getPitch());
                    saveCommissaries();
                    p.sendMessage(col(main.getConfig().getString("Commissary-SetTp").replace("{commissary}", setTp).replace("{prefix}", prefix)));

                    return true;
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    String name = args[1];

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
                        return true;
                    }

                    main.commissaries.set(name + ".Points", null);
                    main.commissaries.set(name + ".X", null);
                    main.commissaries.set(name + ".Y", null);
                    main.commissaries.set(name + ".Z", null);
                    main.commissaries.set(name + ".Yaw", null);
                    main.commissaries.set(name + ".Pitch", null);
                    main.commissaries.set(name, null);
                    saveCommissaries();
                    p.sendMessage(col(main.getConfig().getString("Commissary-Removed").replace("{commissary}", name).replace("{prefix}", prefix)));
                    return true;
                }

                // args[2] error messages
                if (args[0].equalsIgnoreCase("create")) {
                    String name = args[1];
                    p.sendMessage(col("&cPlease specify the amount of points and time!"));
                    p.sendMessage(col("&cUsage: /commissary create " + name + " 10 120"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("setprice")) {
                    String name = args[1];
                    p.sendMessage(col("&cPlease specify the new price!"));
                    p.sendMessage(col("&cUsage: /commissary setprice " + name + " 100"));
                    return true;
                }

                if (args[0].equalsIgnoreCase("settime")) {
                    p.sendMessage(col("&cPlease specify the new time!"));
                    p.sendMessage(col("&cUsage: /commissary settime " + args[1] + " 100"));
                    return true;
                }
            }


            // args 3
            if (args.length == 3) {

                // set cmd
                if (args[0].equalsIgnoreCase("setprice")) {
                    String name = args[1];
                    String price = args[2];

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
                        return true;
                    }

                    if (isNumber(price)) {
                        main.commissaries.set(name + ".Points", Integer.valueOf(price));
                        saveCommissaries();
                        p.sendMessage(col(main.getConfig().getString("Commissary-Setprice").replace("{commissary}", name).replace("{price}", price).replace("{prefix}", prefix)));
                    } else {
                        p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Price").replace("{price}", price).replace("{prefix}", prefix)));
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("settime")) {
                    String name = args[1];
                    String time = args[2];

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
                        return true;
                    }

                    if (isNumber(time)) {
                        main.commissaries.set(name + ".Time", Integer.valueOf(time));
                        saveCommissaries();
                        p.sendMessage(col(main.getConfig().getString("Commissary-Time-Set").replace("{prefix}", prefix).replace("{time}", time).replace("{commissary}", name)));
                    } else {
                        p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Time").replace("{time}", time).replace("{prefix}", prefix)));
                    }
                    return true;
                }

                // args[3] error msg
                if (args[0].equalsIgnoreCase("create")) {
                    String name = args[1];
                    String points = args[2];
                    p.sendMessage(col("&cPlease specify the amount of time!"));
                    p.sendMessage(col("&cUsage: /commissary create " + name + " " + points + " 120"));
                    return true;
                }
            }

            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("create")) {
                    String name = args[1];
                    String price = args[2];
                    String time = args[3];
                    if (main.commissaries.contains(name)) {

                        p.sendMessage(col(main.getConfig().getString("Commissary-Already-Exists").replace("{commissary}", name).replace("{prefix}", prefix)));
                        return true;
                    }

                    if (isNumber(price) && isNumber(time)) {
                        main.commissaries.set(name, null);
                        main.commissaries.set(name + ".Points", Integer.valueOf(price));
                        main.commissaries.set(name + ".X", p.getLocation().getX());
                        main.commissaries.set(name + ".Y", p.getLocation().getY());
                        main.commissaries.set(name + ".Z", p.getLocation().getZ());
                        main.commissaries.set(name + ".Yaw", p.getLocation().getYaw());
                        main.commissaries.set(name + ".Pitch", p.getLocation().getPitch());
                        main.commissaries.set(name + ".Time", Integer.valueOf(time));
                        saveCommissaries();
                        p.sendMessage(col(main.getConfig().getString("Commissary-Created").replace("{commissary}", name).replace("{price}", price).replace("{time}", time).replace("{prefix}", prefix)));

                    } /* The price is NOT a number, provide error message. */ else {
                        if (!isNumber(price)) {
                            p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Price").replace("{price}", price).replace("{prefix}", prefix)));
                        } else if (!isNumber(time)) {
                            p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Time").replace("{time}", time).replace("{prefix}", prefix)));
                        }
                    }
                    return true;
                    // end of create command.
                }
                return false;
            }
        }

        if (args.length >= 5) {
            p.sendMessage(col("&cUsage: /commissary"));
            return true;
        }

        return false;
    }


    private String col(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public boolean isNumber(String price) {
        if (NumberUtils.isNumber(price)) {
            return true;
        } else {
            return false;
        }
    }

    private void saveCommissaries() {
        try {
            main.commissaries.save(main.commissaryyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
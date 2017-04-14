package aderm.pw.commissary.commands;

import java.io.IOException;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import aderm.pw.commissary.Main;

public class Points implements CommandExecutor {

    // TODO make all the messages in the config. (23 Messages)

    private Main main;

    public Points(Main pl) {
        main = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        String prefix = main.getConfig().getString("Points-Prefix");

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getConfig().getString("Must-Be-Player"));
            return true;
        }

        Player p = (Player) sender;

        // help msg for points
        if (args.length == 0) {
            int points = main.players.getInt(p.getUniqueId().toString() + ".Points");
            p.sendMessage(col(main.getConfig().getString("Current-Points").replace("{prefix}", prefix).replace("{points}", String.valueOf(points))));
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                } else {
                    p.sendMessage(col("&cPoints help page:"));
                    p.sendMessage(col("&7/points"));
                    p.sendMessage(col("&7/points set <player> <points>"));
                    p.sendMessage(col("&7/points <give/add> <player> <points>"));
                    p.sendMessage(col("&7/points take <player> <points>"));
                    p.sendMessage(col("&7/points pay <player> <points>"));
                    p.sendMessage(col("&7/points reset <player>"));
                    return true;
                }
            }

            // error messages for extra args.
            if (args[0].equalsIgnoreCase("set")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify a player and the amount of points!"));
                p.sendMessage(col("&cUsage: /points set <player> 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify a player and the amount of points!"));
                p.sendMessage(col("&cUsage: /points give <player> 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("take")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify a player and the amount of points!"));
                p.sendMessage(col("&cUsage: /points take <player> 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("pay")) {
                p.sendMessage(col("&cSpecify a player and the amount of points!"));
                p.sendMessage(col("&cUsage: /points pay <player> 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("reset")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify a player to reset!"));
                p.sendMessage(col("&cUsage: /points reset <player>"));
                return true;
            }

            // end of error messages for extra agrs
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reset")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                Player playerToReset = Bukkit.getPlayer(args[1]);

                if (playerToReset == null) {
                    p.sendMessage(col(main.getConfig().getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])));
                    return true;
                }

                // If player is online
                String UUID = playerToReset.getUniqueId().toString();
                main.players.set(UUID + ".Points", 0);
                playerToReset.sendMessage(col(main.getConfig().getString("Player-Points-Reset").replace("{prefix}", prefix)));
                p.sendMessage(col(main.getConfig().getString("Points-Reset").replace("{prefix}", prefix).replace("{player}", playerToReset.getName())));
                savePlayers();
                return true;
            }

            // more error messages
            if (args[0].equalsIgnoreCase("set")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify the amount of points!"));
                p.sendMessage(col("&cUsage: /points set " + args[1] + " 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify the amount of points!"));
                p.sendMessage(col("&cUsage: /points give " + args[1] + " 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("take")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                p.sendMessage(col("&cSpecify the amount of points!"));
                p.sendMessage(col("&cUsage: /points take " + args[1] + " 10"));
                return true;
            }

            if (args[0].equalsIgnoreCase("pay") && p.hasPermission("points.admin")) {
                p.sendMessage(col("&cSpecify the amount of points!"));
                p.sendMessage(col("&cUsage: /points pay " + args[1] + " 10"));
                return true;
            }

            return true;
        }

        if (args.length == 3) {

            // start of set
            if (args[0].equalsIgnoreCase("set")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                String UUID;
                String points = args[2];
                Player playerToSet = Bukkit.getPlayer(args[1]);

                if (playerToSet == null) {
                    p.sendMessage(col(main.getConfig().getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])));
                    return true;
                }

                UUID = playerToSet.getUniqueId().toString();
                if (isNumber(points)) {

                    if (Integer.valueOf(points) < 0) {
                        p.sendMessage(col(main.getConfig().getString("Points-Set-Equals-<0").replace("{prefix}", prefix)));
                        return true;
                    }

                    main.players.set(UUID + ".Points", Integer.valueOf(points));
                    savePlayers();
                    p.sendMessage(col(main.getConfig().getString("Points-Set").replace("{prefix}", prefix).replace("{points}", points).replace("{player}", playerToSet.getName())));
                    playerToSet.sendMessage(col(main.getConfig().getString("Player-Points-Set").replace("{prefix}", prefix).replace("{points}", points)));
                } else {
                    p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Number")));
                }

                return true;
            }

            // start of give
            if ((args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add"))) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                String UUID;
                String points = args[2];
                Player playerToGive = Bukkit.getPlayer(args[1]);

                if (playerToGive == null) {
                    p.sendMessage(col(main.getConfig().getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])));
                    return true;
                }

                UUID = playerToGive.getUniqueId().toString();
                if (isNumber(points)) {

                    if (Integer.valueOf(points) <= 0) {
                        p.sendMessage(col(main.getConfig().getString("Points-Add-Equals-0").replace("{prefix}", prefix)));
                        return true;
                    }

                    int currentPoints = main.players.getInt(UUID + ".Points");
                    int finalPoints = Integer.valueOf(points) + currentPoints;
                    main.players.set(UUID + ".Points", finalPoints);
                    savePlayers();
                    p.sendMessage(col(main.getConfig().getString("Points-Added").replace("{prefix}", prefix).replace("{points}", points).replace("{player}", playerToGive.getName())));
                    playerToGive.sendMessage(col(main.getConfig().getString("Player-Points-Added").replace("{prefix}", prefix).replace("{points}", points).replace("{player}", playerToGive.getName())));
                } else {
                    p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Number")));
                }

                return true;
            }

            // start of take
            if (args[0].equalsIgnoreCase("take")) {

                if (!(p.hasPermission("points.admin"))) {
                    p.sendMessage(col(main.getConfig().getString("No-Permission")));
                    return true;
                }

                String UUID;
                String points = args[2];
                Player playerToTake = Bukkit.getPlayer(args[1]);

                if (playerToTake == null) {
                    p.sendMessage(col(main.getConfig().getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])));
                    return true;
                }

                UUID = playerToTake.getUniqueId().toString();

                if (isNumber(points)) {
                    int currentPoints = main.players.getInt(UUID + ".Points");

                    if (currentPoints <= 0) {
                        p.sendMessage(col(main.getConfig().getString("Points-Take-Equals-<0").replace("{prefix}", prefix)));
                        return true;
                    }

                    if (currentPoints > Integer.valueOf(points)) {
                        int finalPoints = currentPoints - Integer.valueOf(points);
                        main.players.set(UUID + ".Points", finalPoints);
                        playerToTake.sendMessage(col(main.getConfig().getString("Player-Points-Taken").replace("{prefix}", prefix).replace("{points}", points)));
                        p.sendMessage(col(main.getConfig().getString("Points-Taken").replace("{prefix}", prefix).replace("{player}", playerToTake.getName()).replace("{points}", points)));
                        savePlayers();
                        return true;
                    }

                    int finalPoints = Integer.valueOf(points) - currentPoints;
                    main.players.set(UUID + ".Points", finalPoints);
                    playerToTake.sendMessage(col(main.getConfig().getString("Player-Points-Taken").replace("{prefix}", prefix).replace("{points}", points)));
                    p.sendMessage(col(main.getConfig().getString("Points-Taken").replace("{prefix}", prefix).replace("{player}", playerToTake.getName()).replace("{points}", points)));
                    savePlayers();
                    return true;

                } else if (!(isNumber(points))) {
                    p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Number")));
                }
                return true;
            }

            // start of pay
            if (args[0].equalsIgnoreCase("pay") && p.hasPermission("points.pay")) {
                String UUID;
                String points = args[2];

                Player playerToBePaid = Bukkit.getPlayer(args[1]);

                if (playerToBePaid == null) {
                    p.sendMessage(col(main.getConfig().getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])));
                    return true;
                }

                UUID = p.getUniqueId().toString();

                int currentPoints = main.players.getInt(UUID + ".Points");
                int pointsToBePaid = Integer.valueOf(points);
                if (isNumber(points)) {

                    if (p == playerToBePaid) {
                        p.sendMessage(col(main.getConfig().getString("Points-Pay-Yourself").replace("{prefix}", prefix)));
                        return true;
                    }

                    if (currentPoints < pointsToBePaid) {
                        p.sendMessage(col(main.getConfig().getString("Not-Enough-To-Pay-Player").replace("{prefix}", prefix)));
                        return true;
                    }

                    if (pointsToBePaid <= 0) {
                        p.sendMessage(col(main.getConfig().getString("Pay-Less-Than-0-Points").replace("{prefix}", prefix)));
                        return true;
                    }

                    if (currentPoints >= pointsToBePaid) {
                        main.players.set(playerToBePaid.getUniqueId().toString() + ".Points", main.players.getInt(playerToBePaid.getUniqueId().toString()) + pointsToBePaid);
                        playerToBePaid.sendMessage(col(main.getConfig().getString("Was-Paid-Points").replace("{prefix}", prefix).replace("{player}", p.getName()).replace("{points}", String.valueOf(pointsToBePaid))));

                        main.players.set(p.getUniqueId().toString() + ".Points", main.players.getInt(p.getUniqueId().toString() + ".Points") - pointsToBePaid);
                        p.sendMessage(col(main.getConfig().getString("Paid-Points").replace("{prefix}", prefix).replace("{player}", playerToBePaid.getName()).replace("{points}", String.valueOf(pointsToBePaid))));
                        savePlayers();
                    }
                } else {
                    p.sendMessage(col(main.getConfig().getString("Not-A-Valid-Number")));
                    return true;
                }
                return true;
            }

            if (args.length >= 4) {
                p.sendMessage(col("&cUsage: /points help"));
                return true;
            }
            return true;
        }

        // end of all cmds
        return true;
    }

    private String col(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private boolean isNumber(String price) {
        if (NumberUtils.isNumber(price)) {
            return true;
        } else {
            return false;
        }
    }

    private void savePlayers() {
        try {
            main.players.save(main.playersyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
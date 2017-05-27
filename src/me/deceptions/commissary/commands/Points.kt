package me.deceptions.commissary.commands

import me.deceptions.commissary.Main
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException

class Points(private val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {

        val prefix = main.config.getString("Points-Prefix")

        if (sender !is Player) {
            sender.sendMessage(main.config.getString("Must-Be-Player"))
            return true
        }

        val p = sender

        // help msg for points
        if (args.isEmpty()) {
            val points = main.players.getInt(p.uniqueId.toString() + ".Points")
            p.sendMessage(col(main.config.getString("Current-Points").replace("{prefix}", prefix).replace("{points}", points.toString())))
            return true
        }

        if (args.size == 1) {
            if (args[0].equals("help", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                } else {
                    p.sendMessage(col("&cPoints help page:"))
                    p.sendMessage(col("&7/points"))
                    p.sendMessage(col("&7/points set <player> <points>"))
                    p.sendMessage(col("&7/points <give/add> <player> <points>"))
                    p.sendMessage(col("&7/points take <player> <points>"))
                    p.sendMessage(col("&7/points pay <player> <points>"))
                    p.sendMessage(col("&7/points see <player>"))
                    p.sendMessage(col("&7/points reset <player>"))
                    return true
                }
            }

            // error messages for extra args.
            if (args[0].equals("set", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify a player and the amount of points!"))
                p.sendMessage(col("&cUsage: /points set <player> 10"))
                return true
            }

            if (args[0].equals("give", ignoreCase = true) || args[0].equals("add", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify a player and the amount of points!"))
                p.sendMessage(col("&cUsage: /points give <player> 10"))
                return true
            }

            if (args[0].equals("take", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify a player and the amount of points!"))
                p.sendMessage(col("&cUsage: /points take <player> 10"))
                return true
            }

            if (args[0].equals("pay", ignoreCase = true)) {
                p.sendMessage(col("&cSpecify a player and the amount of points!"))
                p.sendMessage(col("&cUsage: /points pay <player> 10"))
                return true
            }

            if (args[0].equals("reset", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify a player to reset!"))
                p.sendMessage(col("&cUsage: /points reset <player>"))
                return true
            }

            if (args[0].equals("see", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify a players points to see!"))
                p.sendMessage(col("&cUsage: /points see <player>"))
                return true
            }

            // end of error messages for extra agrs
        }

        if (args.size == 2) {
            if (args[0].equals("reset", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                val playerToReset = Bukkit.getPlayer(args[1])

                if (playerToReset == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
                    return true
                }

                // If player is online
                val UUID = playerToReset.uniqueId.toString()
                main.players.set(UUID + ".Points", 0)
                playerToReset.sendMessage(col(main.config.getString("Player-Points-Reset").replace("{prefix}", prefix)))
                p.sendMessage(col(main.config.getString("Points-Reset").replace("{prefix}", prefix).replace("{player}", playerToReset.name)))
                savePlayers()
                return true
            }

            if (args[0].equals("see", ignoreCase = true)) {
                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                val playerToSee = Bukkit.getPlayer(args[1])

                if (playerToSee == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
                    return true
                }

                val points = main.config.getInt(playerToSee.uniqueId.toString() + ".Points")
                p.sendMessage(col(main.config.getString("Points-See").replace("{prefix}", prefix).replace("{player}", playerToSee.name).replace("{points}", points.toString())))
                return true
            }

            // more error messages
            if (args[0].equals("set", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify the amount of points!"))
                p.sendMessage(col("&cUsage: /points set " + args[1] + " 10"))
                return true
            }

            if (args[0].equals("give", ignoreCase = true) || args[0].equals("add", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify the amount of points!"))
                p.sendMessage(col("&cUsage: /points give " + args[1] + " 10"))
                return true
            }

            if (args[0].equals("take", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                p.sendMessage(col("&cSpecify the amount of points!"))
                p.sendMessage(col("&cUsage: /points take " + args[1] + " 10"))
                return true
            }

            if (args[0].equals("pay", ignoreCase = true) && p.hasPermission("points.admin")) {
                p.sendMessage(col("&cSpecify the amount of points!"))
                p.sendMessage(col("&cUsage: /points pay " + args[1] + " 10"))
                return true
            }

            return true
        }

        if (args.size == 3) {

            // start of set
            if (args[0].equals("set", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                val UUID: String
                val points = args[2]
                val playerToSet = Bukkit.getPlayer(args[1])

                if (playerToSet == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
                    return true
                }

                UUID = playerToSet.uniqueId.toString()
                if (isNumber(points)) {

                    if (Integer.valueOf(points) < 0) {
                        p.sendMessage(col(main.config.getString("Points-Set-Equals-<0").replace("{prefix}", prefix)))
                        return true
                    }

                    main.players.set(UUID + ".Points", Integer.valueOf(points))
                    savePlayers()
                    p.sendMessage(col(main.config.getString("Points-Set").replace("{prefix}", prefix).replace("{points}", points).replace("{player}", playerToSet.name)))
                    playerToSet.sendMessage(col(main.config.getString("Player-Points-Set").replace("{prefix}", prefix).replace("{points}", points)))
                } else {
                    p.sendMessage(col(main.config.getString("Not-A-Valid-Number")))
                }

                return true
            }

            // start of give
            if (args[0].equals("give", ignoreCase = true) || args[0].equals("add", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                val UUID: String
                val points = args[2]
                val playerToGive = Bukkit.getPlayer(args[1])

                if (playerToGive == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
                    return true
                }

                UUID = playerToGive.uniqueId.toString()
                if (isNumber(points)) {

                    if (Integer.valueOf(points) <= 0) {
                        p.sendMessage(col(main.config.getString("Points-Add-Equals-0").replace("{prefix}", prefix)))
                        return true
                    }

                    val currentPoints = main.players.getInt(UUID + ".Points")
                    val finalPoints = Integer.valueOf(points)!! + currentPoints
                    main.players.set(UUID + ".Points", finalPoints)
                    savePlayers()
                    p.sendMessage(col(main.config.getString("Points-Added").replace("{prefix}", prefix).replace("{points}", points).replace("{player}", playerToGive.name)))
                    playerToGive.sendMessage(col(main.config.getString("Player-Points-Added").replace("{prefix}", prefix).replace("{points}", points).replace("{player}", playerToGive.name)))
                } else {
                    p.sendMessage(col(main.config.getString("Not-A-Valid-Number")))
                }

                return true
            }

            // start of take
            if (args[0].equals("take", ignoreCase = true)) {

                if (!p.hasPermission("points.admin")) {
                    p.sendMessage(col(main.config.getString("No-Permission")))
                    return true
                }

                val UUID: String
                val points = args[2]
                val playerToTake = Bukkit.getPlayer(args[1])

                if (playerToTake == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
                    return true
                }

                UUID = playerToTake.uniqueId.toString()

                if (isNumber(points)) {
                    val currentPoints = main.players.getInt(UUID + ".Points")

                    if (currentPoints <= 0) {
                        p.sendMessage(col(main.config.getString("Points-Take-Equals-<0").replace("{prefix}", prefix)))
                        return true
                    }

                    if (currentPoints > Integer.valueOf(points)) {
                        val finalPoints = currentPoints - Integer.valueOf(points)!!
                        main.players.set(UUID + ".Points", finalPoints)
                        playerToTake.sendMessage(col(main.config.getString("Player-Points-Taken").replace("{prefix}", prefix).replace("{points}", points)))
                        p.sendMessage(col(main.config.getString("Points-Taken").replace("{prefix}", prefix).replace("{player}", playerToTake.name).replace("{points}", points)))
                        savePlayers()
                        return true
                    }

                    val finalPoints = Integer.valueOf(points)!! - currentPoints
                    main.players.set(UUID + ".Points", finalPoints)
                    playerToTake.sendMessage(col(main.config.getString("Player-Points-Taken").replace("{prefix}", prefix).replace("{points}", points)))
                    p.sendMessage(col(main.config.getString("Points-Taken").replace("{prefix}", prefix).replace("{player}", playerToTake.name).replace("{points}", points)))
                    savePlayers()
                    return true

                } else if (!isNumber(points)) {
                    p.sendMessage(col(main.config.getString("Not-A-Valid-Number")))
                }
                return true
            }

            // start of pay
            if (args[0].equals("pay", ignoreCase = true) && p.hasPermission("points.pay")) {
                val UUID: String
                val points = args[2]

                val playerToBePaid = Bukkit.getPlayer(args[1])

                if (playerToBePaid == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
                    return true
                }

                UUID = p.uniqueId.toString()

                val currentPoints = main.players.getInt(UUID + ".Points")
                val pointsToBePaid = Integer.valueOf(points)!!
                if (isNumber(points)) {

                    if (p === playerToBePaid) {
                        p.sendMessage(col(main.config.getString("Points-Pay-Yourself").replace("{prefix}", prefix)))
                        return true
                    }

                    if (currentPoints < pointsToBePaid) {
                        p.sendMessage(col(main.config.getString("Not-Enough-To-Pay-Player").replace("{prefix}", prefix)))
                        return true
                    }

                    if (pointsToBePaid <= 0) {
                        p.sendMessage(col(main.config.getString("Pay-Less-Than-0-Points").replace("{prefix}", prefix)))
                        return true
                    }

                    if (currentPoints >= pointsToBePaid) {
                        main.players.set(playerToBePaid.uniqueId.toString() + ".Points", main.players.getInt(playerToBePaid.uniqueId.toString()) + pointsToBePaid)
                        playerToBePaid.sendMessage(col(main.config.getString("Was-Paid-Points").replace("{prefix}", prefix).replace("{player}", p.name).replace("{points}", pointsToBePaid.toString())))

                        main.players.set(p.uniqueId.toString() + ".Points", main.players.getInt(p.uniqueId.toString() + ".Points") - pointsToBePaid)
                        p.sendMessage(col(main.config.getString("Paid-Points").replace("{prefix}", prefix).replace("{player}", playerToBePaid.name).replace("{points}", pointsToBePaid.toString())))
                        savePlayers()
                    }
                } else {
                    p.sendMessage(col(main.config.getString("Not-A-Valid-Number")))
                    return true
                }
                return true
            }

            if (args.size >= 4) {
                p.sendMessage(col("&cUsage: /points help"))
                return true
            }
            return true
        }
        return true
    }

    private fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

    private fun isNumber(price: String): Boolean {
        if (NumberUtils.isNumber(price)) {
            return true
        } else {
            return false
        }
    }

    fun savePlayers() {
        try {
            main.players.save(main.playersyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getPoints(p: Player): Int {
        return main.players.getInt(p.uniqueId.toString() + ".Points")
    }

}
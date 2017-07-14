package me.deceptions.commissary.commands

import me.deceptions.commissary.Main
import org.apache.commons.lang3.math.NumberUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import java.io.IOException

class AddPoints(private val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {

        val prefix = main.config.getString("Points-Prefix")

        if (sender is Player) {
            sender.sendMessage(col(main.config.getString("AddPoints-Player").replace("{prefix}", prefix)))
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(col(main.config.getString("AddPoints-Help").replace("{prefix}", prefix)))
            return true
        }

        if (args.size == 1) {
            val p = Bukkit.getPlayer(args[0])

            if (p == null) {
                sender.sendMessage(col(main.config.getString("AddPoints-Offline").replace("{prefix}", prefix).replace("{player}", args[0])))
                return true
            }

            sender.sendMessage(col(main.config.getString("AddPoints-Points").replace("{prefix}", prefix).replace("{player}", p.name)))

        }

        if (args.size == 2) {
            val p = Bukkit.getPlayer(args[0])
            val points = args[1]

            if (p == null) {
                sender.sendMessage(col(main.config.getString("AddPoints-Offline").replace("{prefix}", prefix).replace("{player}", args[0])))
                return true
            }

            if (Integer.valueOf(points) <= 0) {
                sender.sendMessage(col(main.config.getString("AddPoints-Less-Than-0").replace("{prefix}", prefix)))
                return true
            }

            if (isNumber(points)) {
                sender.sendMessage(col(main.config.getString("AddPoints-Added").replace("{prefix}", prefix).replace("{player}", p.name).replace("{points}", points)))
                val currentPoints = main.players.getInt(p.uniqueId.toString() + ".Points")
                val finalPoints = currentPoints + Integer.valueOf(points)
                main.players.set(p.uniqueId.toString() + ".Points", finalPoints)
                p.sendMessage(col(main.config.getString("AddPoints-Given").replace("{prefix}", prefix).replace("{points}", points)))
                savePlayers()
            } else {
                sender.sendMessage(col(main.config.getString("Not-A-Valid-Number")))
            }

        }

        if (args.size >= 3) {
            sender.sendMessage(col("&cUsage: /addpoints <player> <points>"))
            return true
        }

        return false
    }

    private fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

    private fun isNumber(number: String): Boolean {
        if (NumberUtils.isNumber(number)) {
            return true
        } else {
            return false
        }
    }

    private fun savePlayers() {
        try {
            main.players.save(main.playersyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
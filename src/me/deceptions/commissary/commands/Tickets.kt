package me.deceptions.commissary.commands

import me.deceptions.commissary.Main
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Tickets(val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {

        val prefix = main.config.getString("Tickets-Prefix")

        if (sender !is Player) {
            sender.sendMessage(main.config.getString("Must-Be-Player"))
            return true
        }

        val p = sender

        // Help message
        if(args.isEmpty()) {
            p.sendMessage(col("&7Tickets help page:"))
            p.sendMessage(col("&7/ticket give <commissary>"))
            if(main.config.getBoolean("Buy-Tickets-Enabled")) {
                p.sendMessage(col("&7/ticket buy <commissary>"))
            }
            return true
        }



        return false
    }

    fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

}
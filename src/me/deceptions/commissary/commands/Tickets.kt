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
        if (args.isEmpty()) {
            p.sendMessage(col("&7Tickets help page:"))
            p.sendMessage(col("&7/ticket give <commissary>"))
            if (main.config.getBoolean("Buy-Tickets-Enabled")) {
                p.sendMessage(col("&7/ticket buy <commissary>"))
            }
            return true
        }

        // Currently all error messages.
        if (args.size == 1) {

            return true
        }

        // Currently all error messages, again.
        if (args.size == 2) {
            return true
        }

        // Actual commands.
        if (args.size == 3) {
            if (args[0].equals("give", ignoreCase = true)) {
                TODO("Add the code here")
                return true
            }


            return true
        }

        // Args too large message
        if (args.size >= 4) {

            return true
        }

        return false
    }

    fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

}
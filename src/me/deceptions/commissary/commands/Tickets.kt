package me.deceptions.commissary.commands

import me.deceptions.commissary.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

class Tickets(val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {

        val prefix = main.config.getString("Ticket-Prefix")

        if (sender !is Player) {
            sender.sendMessage(main.config.getString("Must-Be-Player"))
            return true
        }

        val p = sender

        // Help message
        if (args.isEmpty()) {
            p.sendMessage(col("&7Tickets help page:"))
            p.sendMessage(col("&7/ticket give <player> <commissary name>"))
            if (main.config.getBoolean("Buy-Tickets-Enabled")) {
                p.sendMessage(col("&7/ticket buy <commissary>"))
            }
            return true
        }

        // Currently all error messages.
        if (args.size == 1) {
            // add the error messages, stop being lazy.
            return true
        }

        // Currently all error messages, again.
        if (args.size == 2) {
            return true
        }

        // Actual commands.
        if (args.size == 3) {
            if (args[0].equals("give", ignoreCase = true)) {
                val target = Bukkit.getPlayer(args[1])
                val ticketName = args[2]

                if (!main.commissaries.contains(ticketName)) {
                    p.sendMessage(col(main.config.getString("Commissary-Name-Does-Not-Exist").replace("{prefix}", prefix)))
                    return true
                }

                if (target == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[2])))
                    return true
                }

                if (target.inventory.firstEmpty() < 0) {
                    p.sendMessage(col(main.config.getString("Give-Ticket-Inventory-Full").replace("{prefix}", prefix)))
                    return true
                }

                val lore = ArrayList<String>()
                val ticket: ItemStack = ItemStack(Material.valueOf(main.config.getString("Ticket-Item")))
                val ticketNameConfig = main.config.getString("Ticket-Name")
                val ticketMeta: ItemMeta = ticket.itemMeta
                ticketMeta.displayName = col(ticketNameConfig).replace("{commissary}", ticketName)

                if(main.config.getBoolean("Ticket-Lore1-Enabled")) {
                    if(main.config.getString("Ticket-Lore1") != "") {
                        lore.add(col(main.config.getString("Ticket-Lore1")))
                    }
                }

                if(main.config.getBoolean("Ticket-Lore2-Enabled")) {
                    if(main.config.getString("Ticket-Lore2") != "") {
                        lore.add(col(main.config.getString("Ticket-Lore2")))
                    }
                }
                if(!lore.isEmpty()) {
                    ticketMeta.lore = lore
                }
                ticket.itemMeta = ticketMeta
                target.inventory.addItem(ticket)
                return true
            }


            // new command, buy.
            if (args[0].equals("buy", ignoreCase = true)) {
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
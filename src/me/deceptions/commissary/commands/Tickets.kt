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
import java.io.IOException
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
            p.sendMessage(col("&7/tickets give <player> <commissary name>"))
            if (main.config.getBoolean("Buy-Tickets-Enabled")) {
                p.sendMessage(col("&7/tickets buy <commissary>"))
            }
            return true
        }

        // Currently all error messages.
        if (args.size == 1) {
            if (args[0].equals("give", ignoreCase = true)) {
                val player = args[1]
                p.sendMessage(col("&cUsage: /tickets give $player <commissary name>"))
            }
            if (main.config.getBoolean("Buy-Tickets-Enabled")) {
                if (args[0].equals("buy", ignoreCase = true)) {
                    p.sendMessage(col("&cUsage: /tickets buy <commissary name>"))
                }
            }
            return true
        }

        // Error messages and buy command
        if (args.size == 2) {
            if (args[0].equals("give", ignoreCase = true)) {
                val target = args[1]
                p.sendMessage(col("&cUsage: /tickets give $target <commissary name>"))
                return true
            }

            if (main.config.getBoolean("Buy-Tickets-Enabled")) {
                if (args[0].equals("buy", ignoreCase = true)) {
                    val commissary = args[1]
                    val currentPoints = main.players.getInt(p.uniqueId.toString() + ".Points")
                    val pointsToBuy = main.commissaries.getInt(commissary + ".Points")

                    if (pointsToBuy > currentPoints) {
                        p.sendMessage(col(main.config.getString("Ticket-Not-Enough-Points").replace("{prefix}", prefix)))
                        return true
                    }

                    val lore = ArrayList<String>()
                    val ticket: ItemStack = ItemStack(Material.valueOf(main.config.getString("Ticket-Item")))
                    val ticketNameConfig = main.config.getString("Ticket-Name")
                    val ticketMeta: ItemMeta = ticket.itemMeta
                    ticketMeta.displayName = col(ticketNameConfig).replace("{commissary}", commissary)

                    if (main.config.getBoolean("Ticket-Lore1-Enabled")) {
                        if (main.config.getString("Ticket-Lore1") != "") {
                            lore.add(col(main.config.getString("Ticket-Lore1")))
                        }
                    }

                    if (main.config.getBoolean("Ticket-Lore2-Enabled")) {
                        if (main.config.getString("Ticket-Lore2") != "") {
                            lore.add(col(main.config.getString("Ticket-Lore2")))
                        }
                    }
                    if (!lore.isEmpty()) {
                        ticketMeta.lore = lore
                    }
                    ticket.itemMeta = ticketMeta
                    p.inventory.addItem(ticket)
                    val newPoints: Int = currentPoints - pointsToBuy
                    main.players.set(p.uniqueId.toString() + ".Points", newPoints)
                    p.sendMessage(col(main.config.getString("Ticket-Bought").replace("{prefix}", prefix).replace("{points}", pointsToBuy.toString())))
                    savePlayers()
                }
            }
        }


        // Give command
        if (args.size == 3) {
            if (args[0].equals("buy", ignoreCase = true)) {
                p.sendMessage(col("&cUsage: /ticket buy"))
                return true
            }

            if (args[0].equals("give", ignoreCase = true)) {
                val target = Bukkit.getPlayer(args[1])
                val ticketName = args[2]

                if (!main.commissaries.contains(ticketName)) {
                    p.sendMessage(col(main.config.getString("Commissary-Name-Does-Not-Exist").replace("{prefix}", prefix)))
                    return true
                }

                if (target == null) {
                    p.sendMessage(col(main.config.getString("Player-Offline").replace("{prefix}", prefix).replace("{player}", args[1])))
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

                if (main.config.getBoolean("Ticket-Lore1-Enabled")) {
                    if (main.config.getString("Ticket-Lore1") != "") {
                        lore.add(col(main.config.getString("Ticket-Lore1")))
                    }
                }

                if (main.config.getBoolean("Ticket-Lore2-Enabled")) {
                    if (main.config.getString("Ticket-Lore2") != "") {
                        lore.add(col(main.config.getString("Ticket-Lore2")))
                    }
                }
                if (!lore.isEmpty()) {
                    ticketMeta.lore = lore
                }
                ticket.itemMeta = ticketMeta
                target.inventory.addItem(ticket)
                p.sendMessage(col(main.config.getString("Ticket-Given").replace("{prefix}", prefix).replace("{player}", target.name)))
                target.sendMessage(col(main.config.getString("Ticket-Received").replace("{prefix}", prefix).replace("{player}", p.name)))
                return true
            }
            return true
        }

        // Args too large message
        if (args.size >= 4) {
            p.sendMessage(col("&cUsage: /ticket"))
            return true
        }

        return false
    }

    fun savePlayers() {
        try {
            main.players.save(main.playersyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

}
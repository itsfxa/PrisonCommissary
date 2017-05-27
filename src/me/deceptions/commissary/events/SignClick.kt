package me.deceptions.commissary.events

import me.deceptions.commissary.Main
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.io.IOException

class SignClick(private val main: Main) : Listener {

    lateinit var CName: String

    @EventHandler
    fun signClick(e: PlayerInteractEvent) {

        val prefix = main.config.getString("Commissary-Prefix")

        val p = e.player

        if (e.action === Action.RIGHT_CLICK_BLOCK) {
            if (e.clickedBlock.type === Material.WALL_SIGN) {
                val sign = e.clickedBlock.state as Sign

                if (sign.getLine(0) == col(main.config.getString("First-Line"))) {
                    val name = ChatColor.stripColor(sign.getLine(1))

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
                        return
                    }

                    CName = name

                    if (main.config.getString("Tickets-Or-Points").equals("Tickets")) {
                        if (p.itemInHand.equals(Material.valueOf(main.config.getString("Ticket-Item")))) {
                            p.inventory.itemInHand = null
                            main.players.set(p.uniqueId.toString() + ".inCommissary", true)
                            saveLoc(p)
                            timer(p)
                            savePlayers()
                            val x = main.commissaries.getDouble(name + ".X")
                            val y = main.commissaries.getDouble(name + ".Y")
                            val z = main.commissaries.getDouble(name + ".Z")
                            val yaw = main.commissaries.getDouble(name + ".Yaw").toFloat()
                            val pitch = main.commissaries.getDouble(name + ".Pitch").toFloat()
                            p.teleport(Location(p.world, x, y, z, yaw, pitch))
                        }
                    } else if (main.config.getString("Tickets-Or-Points").equals("Points")){

                        val pointsToEnter = main.commissaries.getInt(name + ".Points")
                        val currentPoints = main.players.getInt(p.uniqueId.toString() + ".Points")
                        if (pointsToEnter <= currentPoints) {
                            main.players.set(p.uniqueId.toString() + ".inCommissary", true)
                            saveLoc(p)
                            timer(p)
                            val finalPoints = currentPoints - pointsToEnter
                            main.players.set(p.uniqueId.toString() + ".Points", finalPoints)
                            if (pointsToEnter > 0) {
                                p.sendMessage(col(main.config.getString("Deducted-Points").replace("{points}", pointsToEnter.toString()).replace("{prefix}", prefix)))
                            }
                            savePlayers()

                            val x = main.commissaries.getDouble(name + ".X")
                            val y = main.commissaries.getDouble(name + ".Y")
                            val z = main.commissaries.getDouble(name + ".Z")
                            val yaw = main.commissaries.getDouble(name + ".Yaw").toFloat()
                            val pitch = main.commissaries.getDouble(name + ".Pitch").toFloat()

                            p.teleport(Location(p.world, x, y, z, yaw, pitch))
                        } else {
                            p.sendMessage(col(main.config.getString("Not-Enough-To-Enter").replace("{prefix}", prefix)))
                        }
                    }
                }
            }
        }
    }

    fun timer(p: Player) {
        object : BukkitRunnable() {
            internal var time = main.commissaries.getInt(CName + ".Time")

            override fun run() {
                time--
                if (time <= 0) {

                    if (!main.players.getBoolean(p.uniqueId.toString() + ".inCommissary")) {
                        cancel()
                        removeLoc(p)
                        return
                    }

                    cancel()
                    teleportPlayer(p)
                    removeLoc(p)
                }
            }

        }.runTaskTimer(main, 0, 20)
    }

    private fun saveLoc(p: Player) {
        val UUID = p.uniqueId.toString()
        val x = p.location.x
        val y = p.location.y
        val z = p.location.z
        val yaw = p.location.yaw
        val pitch = p.location.pitch
        main.locations.set(UUID + ".X", x)
        main.locations.set(UUID + ".Y", y)
        main.locations.set(UUID + ".Z", z)
        main.locations.set(UUID + ".Yaw", yaw)
        main.locations.set(UUID + ".Pitch", pitch)
        saveLocations()
    }

    private fun removeLoc(p: Player) {
        val UUID = p.uniqueId.toString()
        main.locations.set(UUID + ".X", null)
        main.locations.set(UUID + ".Y", null)
        main.locations.set(UUID + ".Z", null)
        main.locations.set(UUID + ".Yaw", null)
        main.locations.set(UUID + ".Pitch", null)
        main.players.set(UUID + ".inCommissary", null)
        main.locations.set(UUID, null)
        saveLocations()
    }

    private fun teleportPlayer(p: Player) {
        val prefix = main.config.getString("Commissary-Prefix")
        val UUID = p.uniqueId.toString()
        val x = main.locations.getDouble(UUID + ".X")
        val y = main.locations.getDouble(UUID + ".Y")
        val z = main.locations.getDouble(UUID + ".Z")
        val yaw = main.locations.getDouble(UUID + ".Yaw").toFloat()
        val pitch = main.locations.getDouble(UUID + ".Pitch").toFloat()
        p.teleport(Location(p.world, x, y, z, yaw, pitch))
        p.sendMessage(col(main.config.getString("Teleported-To-Original-Location").replace("{prefix}", prefix)))
    }

    private fun saveLocations() {
        try {
            main.locations.save(main.locationsyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun savePlayers() {
        try {
            main.players.save(main.playersyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

}
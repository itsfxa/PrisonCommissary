package me.deceptions.commissary

import me.deceptions.commissary.commands.AddPoints
import me.deceptions.commissary.commands.Commissary
import me.deceptions.commissary.commands.Points
import me.deceptions.commissary.commands.Tickets
import me.deceptions.commissary.events.Joins
import me.deceptions.commissary.events.SignClick
import me.deceptions.commissary.events.SignCreate
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

import java.io.File
import java.io.IOException

class Main : JavaPlugin() {

    // Locations file - Used for storing last locations of players.
    var locationsyml = File(dataFolder, "locations.yml")
    var locations: FileConfiguration = YamlConfiguration.loadConfiguration(locationsyml)

    // Players file - Used for storing player data.
    var playersyml = File(dataFolder, "players.yml")
    var players: FileConfiguration = YamlConfiguration.loadConfiguration(playersyml)

    // Commissaries file - Used for storing commissary locations and data.
    var commissaryyml = File(dataFolder, "commissaries.yml")
    var commissaries: FileConfiguration = YamlConfiguration.loadConfiguration(commissaryyml)

    override fun onEnable() {
        super.onEnable()
        registerListeners()
        registerCommands()
        config.options().copyDefaults(true)
        saveDefaultConfig()
        saveFiles()

        // register PlaceholderAPI placeholders.
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPI(this).hook()
        } else {
            println("[PrisonCommissary] PlaceholderAPI not found. - Commissary point placeholder will not work!")
        }
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(Joins(this), this)
        pm.registerEvents(SignClick(this), this)
        pm.registerEvents(SignCreate(this), this)
    }

    private fun registerCommands() {
        getCommand("commissary").executor = Commissary(this)
        getCommand("points").executor = Points(this)
        getCommand("addpoints").executor = AddPoints(this)
        getCommand("tickets").executor = Tickets(this)
    }

    private fun saveFiles() {
        try {
            locations.save(locationsyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            players.save(playersyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            commissaries.save(commissaryyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
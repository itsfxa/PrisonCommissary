package aderm.me.commissary

import aderm.me.commissary.commands.AddPoints
import aderm.me.commissary.commands.Commissary
import aderm.me.commissary.commands.Points
import aderm.me.commissary.events.Joins
import aderm.me.commissary.events.SignClick
import aderm.me.commissary.events.SignCreate
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
        registerListeners()
        registerCommands()
        config.options().copyDefaults(true)
        saveDefaultConfig()
        saveFiles()

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Placeholder(this).hook()
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
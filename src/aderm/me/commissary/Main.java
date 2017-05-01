package aderm.me.commissary;

import aderm.me.commissary.commands.AddPoints;
import aderm.me.commissary.commands.Commissary;
import aderm.me.commissary.commands.Points;
import aderm.me.commissary.events.Joins;
import aderm.me.commissary.events.SignClick;
import aderm.me.commissary.events.SignCreate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    // Locations file - Used for storing last locations of players.
    public File locationsyml = new File(getDataFolder(), "locations.yml");
    public FileConfiguration locations = YamlConfiguration.loadConfiguration(locationsyml);

    // Players file - Used for storing player data.
    public File playersyml = new File(getDataFolder(), "players.yml");
    public FileConfiguration players = YamlConfiguration.loadConfiguration(playersyml);

    // Commissaries file - Used for storing commissary locations and data.
    public File commissaryyml = new File(getDataFolder(), "commissaries.yml");
    public FileConfiguration commissaries = YamlConfiguration.loadConfiguration(commissaryyml);

    public void onEnable() {
        registerListeners();
        registerCommands();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveFiles();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholder(this).hook();
        } else {
            System.out.println("[PrisonCommissary] PlaceholderAPI not found. - Commissary point placeholder will not work!");
        }
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Joins(this), this);
        pm.registerEvents(new SignClick(this), this);
        pm.registerEvents(new SignCreate(this), this);
    }

    private void registerCommands() {
        getCommand("commissary").setExecutor(new Commissary(this));
        getCommand("points").setExecutor(new Points(this));
        getCommand("addpoints").setExecutor(new AddPoints(this));
    }

    private void saveFiles() {
        try {
            locations.save(locationsyml);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            players.save(playersyml);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            commissaries.save(commissaryyml);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
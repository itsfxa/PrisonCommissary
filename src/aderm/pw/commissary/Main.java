package aderm.pw.commissary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import aderm.pw.commissary.commands.AddPoints;
import aderm.pw.commissary.events.SignCreate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import aderm.pw.commissary.commands.Commissary;
import aderm.pw.commissary.commands.Points;
import aderm.pw.commissary.events.Joins;
import aderm.pw.commissary.events.SignClick;

public class Main extends JavaPlugin {

    public static String uid = "%%__USER__%%";

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
        authenticate();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveFiles();

        System.out.println("[PrisonCommissary] Licensed to: https://www.spigotmc.org/members/" + uid);

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

    private void authenticate() {
        try {
            URLConnection localURLConnection = new URL("https://backend.aderm.pw/Commissary.txt").openConnection();
            localURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            localURLConnection.connect();
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localURLConnection.getInputStream(), Charset.forName("UTF-8")));
            String str1;

            while ((str1 = localBufferedReader.readLine()) != null) {
                if (str1.equals(uid)) {
                    getServer().getPluginManager().disablePlugin(this);
                    System.out.println("[PrisonCommissary] Plugin disabled due to it being leaked.");
                    System.out.println("[PrisonCommissary] Contact @Aderm on spigot for more information.");
                    return;
                }
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
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
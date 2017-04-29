
/* Proof of code being mine. AdermDev @ MCM. 22/04/2017 */

package aderm.me.commissary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

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

    public static boolean updateAvailable = false;

    public void onEnable() {
        registerListeners();
        registerCommands();
        authenticate();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveFiles();
        updateAvailable();

        System.out.println("[PrisonCommissary] Licensed to: http://www.mc-market.org/members/" + uid);

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
        pm.registerEvents(new UpdateChecker(), this);
    }

    private void registerCommands() {
        getCommand("commissary").setExecutor(new Commissary(this));
        getCommand("points").setExecutor(new Points(this));
        getCommand("addpoints").setExecutor(new AddPoints(this));
    }

    private void authenticate() {
        try {
            URLConnection localURLConnection = new URL("https://www.aderm.me/Commissary.php").openConnection();
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
                    System.out.println("[PrisonCommissary] Contact @AdermDev on mcm for more information.");
                    return;
                }
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    private void updateAvailable() {
        try {
            URLConnection localURLConnection = new URL("https://www.aderm.me/CommissaryUpdate.php").openConnection();
            localURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            localURLConnection.connect();
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localURLConnection.getInputStream(), Charset.forName("UTF-8")));
            String str1;

            while ((str1 = localBufferedReader.readLine()) != null) {
                if (!str1.equals(getDescription().getVersion())) {
                    updateAvailable = true; // update is available
                    return;
                } else {
                    updateAvailable = false; // no update
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

    public static String uid = "USER";

}
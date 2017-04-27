package aderm.me.commissary;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateChecker implements Listener {

    private Main main;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp() && main.updateAvailable) {
            e.getPlayer().sendMessage(col("&cAn update is available for PrisonCommissary!"));
            e.getPlayer().sendMessage(col("&cContact Aderm on MCM / Skype to get the updated version."));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p.getKiller() instanceof Player) {

            Player killer = p.getKiller();

            int currentKills = plugin.players.getInt(killer + ".Kills");
            plugin.players.set(killer + ".Kills", currentKills + 1);
            savePlayers();
            int newKills = plugin.players.getInt(killer + ".Kills");
            killer.sendMessage("You're on a " + newKills + " killstreak!");

            if (killer.isDead()) {
                killer.sendMessage("Your killstreak ended! You had " + newKills + " killstreak");
                plugin.players.set(killer + ".Kills", 0);
                savePlayers();
            }
        }
    }

    private String col(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
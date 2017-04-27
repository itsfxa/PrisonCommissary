package aderm.me.commissary.events;

import aderm.me.commissary.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignCreate implements Listener {

    private Main main;

    public SignCreate(Main pl) {
        main = pl;
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent e) {
        Player p = e.getPlayer();

        String prefix = main.getConfig().getString("Commissary-Prefix");

        if (e.getLine(0).equalsIgnoreCase("[Commissary]")) {
            if (!(p.hasPermission("commissary.createsign"))) {
                p.sendMessage(col(main.getConfig().getString("Cannot-Create-Signs").replace("{prefix}", prefix)));
                return;
            }
        }

        if (!(e.getLine(0).equalsIgnoreCase("[Commissary]"))) {
            return;
        }

        if (!(main.commissaries.contains(String.valueOf(e.getLine(1))))) {
            p.sendMessage(col(main.getConfig().getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)));
            return;
        }

        if (e.getLine(0).equals("[Commissary]")) {

            int points = main.commissaries.getInt(e.getLine(1) + ".Points");
            int time = main.commissaries.getInt(e.getLine(1) + ".Time");

            if (main.commissaries.contains(String.valueOf(e.getLine(1)))) {
                e.setLine(0, col(main.getConfig().getString("First-Line")));
                e.setLine(1, col(main.getConfig().getString("Second-Line").replace("{name}", e.getLine(1))));
                e.setLine(2, col(main.getConfig().getString("Third-Line").replace("{points}", String.valueOf(points))));
                e.setLine(3, col(main.getConfig().getString("Fourth-Line").replace("{seconds}", String.valueOf(time))));
            }
        }
    }

    private String col(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
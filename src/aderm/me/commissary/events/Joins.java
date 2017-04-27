package aderm.me.commissary.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import aderm.me.commissary.Main;

import java.io.IOException;

public class Joins implements Listener {

    private Main main;

    public Joins(Main pl) {
        main = pl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!(main.players.contains(p.getUniqueId().toString()))) {
            main.players.set(p.getUniqueId().toString() + ".Points", Integer.valueOf(0));

            try {
                main.players.save(main.playersyml);
            } catch (IOException i) {
                i.printStackTrace();
            }

        }
    }

}
package aderm.pw.commissary;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Placeholder extends EZPlaceholderHook {

    private Main main;

    public Placeholder(Main main) {
        super(main, "prisoncommissary");
        this.main = main;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (identifier.equals("points")) {
            return String.valueOf(main.players.getInt(p.getUniqueId().toString() + ".Points"));
        }
        if (p == null) {
            return "";
        }
        return null;
    }
}
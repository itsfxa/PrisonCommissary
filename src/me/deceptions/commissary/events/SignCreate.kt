package aderm.me.commissary.events

import aderm.me.commissary.Main
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent

class SignCreate(private val main: Main) : Listener {

    @EventHandler
    fun onSignPlace(e: SignChangeEvent) {
        val p = e.player

        val prefix = main.config.getString("Commissary-Prefix")

        if (e.getLine(0).equals("[Commissary]")) {
            if (!p.hasPermission("commissary.createsign")) {
                p.sendMessage(col(main.config.getString("Cannot-Create-Signs").replace("{prefix}", prefix)))
                return
            }
        }

        if (!e.getLine(0).equals("[Commissary]")) {
            return
        }

        if (!main.commissaries.contains(e.getLine(1).toString())) {
            p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
            return
        }

        if (e.getLine(0) == "[Commissary]") {

            val points = main.commissaries.getInt(e.getLine(1) + ".Points")
            val time = main.commissaries.getInt(e.getLine(1) + ".Time")

            if (main.commissaries.contains(e.getLine(1).toString())) {
                e.setLine(0, col(main.config.getString("First-Line")))
                e.setLine(1, col(main.config.getString("Second-Line").replace("{name}", e.getLine(1))))
                e.setLine(2, col(main.config.getString("Third-Line").replace("{points}", points.toString())))
                e.setLine(3, col(main.config.getString("Fourth-Line").replace("{seconds}", time.toString())))
            }
        }
    }

    private fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

}
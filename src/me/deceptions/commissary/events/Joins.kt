package aderm.me.commissary.events

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

import aderm.me.commissary.Main

import java.io.IOException

class Joins(private val main: Main) : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        if (!main.players.contains(p.uniqueId.toString())) {
            main.players.set(p.uniqueId.toString() + ".Points", Integer.valueOf(0))

            try {
                main.players.save(main.playersyml)
            } catch (i: IOException) {
                i.printStackTrace()
            }

        }
    }

}
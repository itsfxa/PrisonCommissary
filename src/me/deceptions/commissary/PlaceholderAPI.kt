package me.deceptions.commissary

import me.clip.placeholderapi.external.EZPlaceholderHook
import org.bukkit.entity.Player

class PlaceholderAPI(private val main: Main) : EZPlaceholderHook(main, "prisoncommissary") {

    override fun onPlaceholderRequest(p: Player?, identifier: String): String? {
        if (identifier == "points") {
            return main.players.getInt(p!!.uniqueId.toString() + ".Points").toString()
        }
        if (p == null) {
            return ""
        }
        return null
    }
}
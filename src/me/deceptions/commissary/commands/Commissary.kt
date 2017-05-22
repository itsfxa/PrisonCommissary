package me.deceptions.commissary.commands

import me.deceptions.commissary.Main
import org.apache.commons.lang.math.NumberUtils
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

import java.io.IOException

class Commissary(private val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, cmd: Command, commandLabel: String, args: Array<String>): Boolean {

        val prefix = main.config.getString("Commissary-Prefix")

        if (sender !is Player) {
            sender.sendMessage(main.config.getString(col("Must-Be-Player")))
            return true
        }

        val p = sender

        if (!p.hasPermission("commissary.admin")) {
            p.sendMessage(col(main.config.getString("No-Permission")))
            return true
        }

        if (p.hasPermission("commissary.admin")) {

            // Help msg for commissary commands.
            if (args.isEmpty()) {
                p.sendMessage(col("&cCommissary help page:"))
                p.sendMessage(col("&7/commissary create <name> <points> <time in seconds>"))
                p.sendMessage(col("&7/commissary tp <name>"))
                p.sendMessage(col("&7/commissary settp <name>"))
                p.sendMessage(col("&7/commissary setprice <name> <new points>"))
                p.sendMessage(col("&7/commissary settime <name> <new time>"))
                p.sendMessage(col("&7/commissary remove <name>"))
                return true
            }

            // args 1 error messages
            if (args.size == 1) {
                if (args[0].equals("reload", ignoreCase = true)) {
                    main.reloadConfig()
                    p.sendMessage(col(main.config.getString("Config-Reloaded")))
                    return true
                }

                if (args[0].equals("create", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify a commissary name, points, and time!"))
                    p.sendMessage(col("&cUsage: /commissary create test 10 120"))
                    return true
                }


                if (args[0].equals("tp", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify a commissary to TP to!"))
                    p.sendMessage(col("&cUsage: /commissary tp test"))
                    return true
                }


                if (args[0].equals("settp", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify a commissary name!"))
                    p.sendMessage(col("&cUsage: /commissary settp test"))
                    return true
                }


                if (args[0].equals("setprice", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify the commisary and the new price!"))
                    p.sendMessage(col("&cUsage: /commissary setprice test 100"))
                    return true
                }

                if (args[0].equals("settime", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify the commisary and the new time!"))
                    p.sendMessage(col("&cUsage: /commissary settime test 100"))
                    return true
                }

                if (args[0].equals("remove", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify a commissary to remove!"))
                    p.sendMessage(col("&cUsage: /commissary remove test"))
                    return true
                }

            }


            // args 2
            if (args.size == 2) {
                // tp cmd
                if (args[0].equals("tp", ignoreCase = true)) {
                    val toTp = args[1]

                    if (!main.commissaries.contains(toTp)) {
                        p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
                        return true
                    }

                    val x = main.commissaries.getInt(toTp + ".X").toFloat()
                    val y = main.commissaries.getInt(toTp + ".Y").toFloat()
                    val z = main.commissaries.getInt(toTp + ".Z").toFloat()
                    val yaw = main.commissaries.getInt(toTp + ".Yaw").toFloat()
                    val pitch = main.commissaries.getInt(toTp + ".Pitch").toFloat()
                    val teleport = Location(p.world, x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)
                    p.teleport(teleport)
                    p.sendMessage(col(main.config.getString("Commissary-TP").replace("{prefix}", prefix.toString()).replace("{commissary}", toTp)))

                    return true
                }

                // settp cmd
                if (args[0].equals("settp", ignoreCase = true)) {
                    val setTp = args[1]

                    if (!main.commissaries.contains(setTp)) {
                        p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
                        return true
                    }

                    main.commissaries.set(setTp + ".X", p.location.x)
                    main.commissaries.set(setTp + ".Y", p.location.y)
                    main.commissaries.set(setTp + ".Z", p.location.z)
                    main.commissaries.set(setTp + ".Yaw", p.location.yaw)
                    main.commissaries.set(setTp + ".Pitch", p.location.pitch)
                    saveCommissaries()
                    p.sendMessage(col(main.config.getString("Commissary-SetTp").replace("{commissary}", setTp).replace("{prefix}", prefix)))

                    return true
                }

                if (args[0].equals("remove", ignoreCase = true)) {
                    val name = args[1]

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
                        return true
                    }

                    main.commissaries.set(name + ".Points", null)
                    main.commissaries.set(name + ".X", null)
                    main.commissaries.set(name + ".Y", null)
                    main.commissaries.set(name + ".Z", null)
                    main.commissaries.set(name + ".Yaw", null)
                    main.commissaries.set(name + ".Pitch", null)
                    main.commissaries.set(name, null)
                    saveCommissaries()
                    p.sendMessage(col(main.config.getString("Commissary-Removed").replace("{commissary}", name).replace("{prefix}", prefix)))
                    return true
                }

                // args[2] error messages
                if (args[0].equals("create", ignoreCase = true)) {
                    val name = args[1]
                    p.sendMessage(col("&cPlease specify the amount of points and time!"))
                    p.sendMessage(col("&cUsage: /commissary create $name 10 120"))
                    return true
                }

                if (args[0].equals("setprice", ignoreCase = true)) {
                    val name = args[1]
                    p.sendMessage(col("&cPlease specify the new price!"))
                    p.sendMessage(col("&cUsage: /commissary setprice $name 100"))
                    return true
                }

                if (args[0].equals("settime", ignoreCase = true)) {
                    p.sendMessage(col("&cPlease specify the new time!"))
                    p.sendMessage(col("&cUsage: /commissary settime " + args[1] + " 100"))
                    return true
                }
            }


            // args 3
            if (args.size == 3) {

                // set cmd
                if (args[0].equals("setprice", ignoreCase = true)) {
                    val name = args[1]
                    val price = args[2]

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
                        return true
                    }

                    if (isNumber(price)) {
                        main.commissaries.set(name + ".Points", Integer.valueOf(price))
                        saveCommissaries()
                        p.sendMessage(col(main.config.getString("Commissary-Setprice").replace("{commissary}", name).replace("{price}", price).replace("{prefix}", prefix)))
                    } else {
                        p.sendMessage(col(main.config.getString("Not-A-Valid-Price").replace("{price}", price).replace("{prefix}", prefix)))
                    }
                    return true
                }

                if (args[0].equals("settime", ignoreCase = true)) {
                    val name = args[1]
                    val time = args[2]

                    if (!main.commissaries.contains(name)) {
                        p.sendMessage(col(main.config.getString("Commissary-Does-Not-Exist").replace("{prefix}", prefix)))
                        return true
                    }

                    if (isNumber(time)) {
                        main.commissaries.set(name + ".Time", Integer.valueOf(time))
                        saveCommissaries()
                        p.sendMessage(col(main.config.getString("Commissary-Time-Set").replace("{prefix}", prefix).replace("{time}", time).replace("{commissary}", name)))
                    } else {
                        p.sendMessage(col(main.config.getString("Not-A-Valid-Time").replace("{time}", time).replace("{prefix}", prefix)))
                    }
                    return true
                }

                // args[3] error msg
                if (args[0].equals("create", ignoreCase = true)) {
                    val name = args[1]
                    val points = args[2]
                    p.sendMessage(col("&cPlease specify the amount of time!"))
                    p.sendMessage(col("&cUsage: /commissary create $name $points 120"))
                    return true
                }
            }

            if (args.size == 4) {
                if (args[0].equals("create", ignoreCase = true)) {
                    val name = args[1]
                    val price = args[2]
                    val time = args[3]
                    if (main.commissaries.contains(name)) {

                        p.sendMessage(col(main.config.getString("Commissary-Already-Exists").replace("{commissary}", name).replace("{prefix}", prefix)))
                        return true
                    }

                    if (isNumber(price) && isNumber(time)) {
                        main.commissaries.set(name, null)
                        main.commissaries.set(name + ".Points", Integer.valueOf(price))
                        main.commissaries.set(name + ".X", p.location.x)
                        main.commissaries.set(name + ".Y", p.location.y)
                        main.commissaries.set(name + ".Z", p.location.z)
                        main.commissaries.set(name + ".Yaw", p.location.yaw)
                        main.commissaries.set(name + ".Pitch", p.location.pitch)
                        main.commissaries.set(name + ".Time", Integer.valueOf(time))
                        saveCommissaries()
                        p.sendMessage(col(main.config.getString("Commissary-Created").replace("{commissary}", name).replace("{price}", price).replace("{time}", time).replace("{prefix}", prefix)))

                    } /* The price is NOT a number, provide error message. */
                    else {
                        if (!isNumber(price)) {
                            p.sendMessage(col(main.config.getString("Not-A-Valid-Price").replace("{price}", price).replace("{prefix}", prefix)))
                        } else if (!isNumber(time)) {
                            p.sendMessage(col(main.config.getString("Not-A-Valid-Time").replace("{time}", time).replace("{prefix}", prefix)))
                        }
                    }
                    return true
                    // end of create command.
                }
                return false
            }
        }

        if (args.size >= 5) {
            p.sendMessage(col("&cUsage: /commissary"))
            return true
        }

        return false
    }


    private fun col(text: String): String {
        return ChatColor.translateAlternateColorCodes('&', text)
    }

    fun isNumber(price: String): Boolean {
        if (NumberUtils.isNumber(price)) {
            return true
        } else {
            return false
        }
    }

    private fun saveCommissaries() {
        try {
            main.commissaries.save(main.commissaryyml)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}
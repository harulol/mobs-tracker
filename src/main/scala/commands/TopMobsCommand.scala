package dev.hawu.plugins.trial
package commands

import dev.hawu.plugins.api.commands.{AbstractCommandClass, CommandArgument, CommandSource}
import dev.hawu.plugins.trial.gui.TopMobsGui
import org.bukkit.plugin.java.JavaPlugin

class TopMobsCommand(pl: JavaPlugin) extends AbstractCommandClass("topmobs"):

    allowPlayers()
    setPermission("trial.topmobs")
    register(pl)
    
    override def run(sender: CommandSource, args: CommandArgument): Unit =
        TopMobsGui.open(sender.getPlayer)

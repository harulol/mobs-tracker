package dev.hawu.plugins.trial

import dev.hawu.plugins.api.events.Events
import dev.hawu.plugins.trial.gui.TopMobsGui
import dev.hawu.plugins.trial.managers.{GuiManager, UserMap}
import dev.hawu.plugins.trial.commands.TopMobsCommand
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class TrialPlugin extends JavaPlugin:

    private def dontBoot: Boolean =
        var value = false

        if Bukkit.getPluginManager.getPlugin("HikariLibrary") == null then
            getLogger.severe("This plugin was developed against a framework which can be retrieved here:")
            getLogger.severe("- https://github.com/harulol/hikari-library")
            value = true

        value

    override def onEnable(): Unit =
        if dontBoot then
            getLogger.severe("One or more dependencies are missing, could not boot.")
            Bukkit.getPluginManager.disablePlugin(this)
            return ()

        ConfigurationSerialization.registerClass(classOf[User])

        UserMap.onEnable(this)
        TopMobsGui.onEnable(this)

        TopMobsCommand(this)

        Events.registerEvents(this, RandomListener)

    override def onDisable(): Unit =
        if dontBoot then return ()

        UserMap.onDisable()

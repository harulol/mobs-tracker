package dev.hawu.plugins.trial
package managers

import org.bukkit.{Bukkit, OfflinePlayer}
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

import java.io.File
import java.util.UUID
import scala.collection.{mutable, *}
import scala.jdk.CollectionConverters.*
import scala.util.*

object UserMap:

    private var plugin: Option[JavaPlugin] = None
    private val users: mutable.Map[UUID, User] = mutable.HashMap()

    def onEnable(pl: JavaPlugin): Unit =
        plugin = Some(pl)
        val usersFolder = File(pl.getDataFolder, "users")

        if !usersFolder.exists() then
            usersFolder.mkdirs()

        for file <- usersFolder.listFiles() do
            val result = Try {
                val config = YamlConfiguration.loadConfiguration(file)
                config.get("data").asInstanceOf[User]
            }
            result match {
                case Failure(exception) =>
                    exception.printStackTrace()
                    pl.getLogger.severe(s"Unable to deserialize file ${file.getName}.")
                case Success(value) =>
                    users.put(value.uuid, value)
            }
        end for

        for
            player <- Bukkit.getOfflinePlayers
            if player.getUniqueId.toUser.isEmpty
        do
            users.put(player.getUniqueId, User(player.getUniqueId))
    end onEnable

    def onDisable(): Unit =
        val usersFolder = File(plugin.get.getDataFolder, "users")

        if !usersFolder.exists() then
            usersFolder.mkdirs()

        for (uuid, user) <- users do
            val file = File(usersFolder, s"${uuid.toString}.yml")
            val config = YamlConfiguration.loadConfiguration(file)
            config.set("data", user)
            config.save(file)

        users.clear()

    def manage(player: Player): Unit =
        if player.toUser.isEmpty then
            users.put(player.getUniqueId, new User(player.getUniqueId))

    extension (uuid: UUID)
        def toUser: Option[User] = users.get(uuid)
        def asUser: User = toUser.get

    extension (player: OfflinePlayer)
        def toUser: Option[User] = users.get(player.getUniqueId)
        def asUser: User = toUser.get

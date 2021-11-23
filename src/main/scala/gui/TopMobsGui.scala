package dev.hawu.plugins.trial
package gui

import dev.hawu.plugins.api.Strings
import dev.hawu.plugins.api.gui.{GuiElement, GuiModel}
import dev.hawu.plugins.api.gui.pagination.GuiPaginator
import dev.hawu.plugins.api.items.ItemStackBuilder
import dev.hawu.plugins.api.reflect.MinecraftVersion
import dev.hawu.plugins.trial.managers.GuiManager
import dev.hawu.plugins.trial.managers.UserMap.asUser
import org.bukkit.Material
import org.bukkit.entity.{EntityType, Player}
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

import scala.jdk.CollectionConverters.*

object TopMobsGui:

    private var plugin: Option[JavaPlugin] = None

    def onEnable(pl: JavaPlugin): Unit =
        plugin = Some(pl)

    def getVersionDependentEgg: Material =
        if Material.getMaterial("MONSTER_EGG") != null then
            Material.MONSTER_EGG
        else
            Material.getMaterial("LEGACY_MONSTER_EGG")

    //noinspection ScalaDeprecation
    def getDisplay(entityType: EntityType): ItemStack = ItemStack(getVersionDependentEgg, 1, entityType.getTypeId)

    def normalize(name: String, delimiter: String = "_"): String =
        name.split(delimiter).map(s =>
            if s.length <= 1 then s.toUpperCase
            else s"${s.head.toUpper}${s.tail.toLowerCase}"
        ).mkString(" ")

    //noinspection ConvertExpressionToSAM
    def open(player: Player): Unit =
        GuiPaginator.newBuilder[(EntityType, Int)]
            .setCollection(player.asUser.kills.toSeq.asJavaCollection)
            .setPredicate((tuple, filter) => tuple._1.name.toLowerCase.contains(filter.toLowerCase))
            .setModelSupplier(() => {
                val model = GuiModel(54, "Top Mobs")
                GuiManager.mountCloseButton(model)
                model
            })
            .setItemGenerator((tuple, _) => {
                new GuiElement[Unit] {
                    override def render(): ItemStack = ItemStackBuilder.from(getDisplay(tuple._1))
                        .name(s"&e&l${normalize(tuple._1.name)}")
                        .lore(s"&7Kills: &e${Strings.format(tuple._2)}")
                        .build()
                }
            })
            .build(player)

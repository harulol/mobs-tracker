package dev.hawu.plugins.trial

import dev.hawu.plugins.api.title.{TitleComponent, TitlePacketAdapter}
import dev.hawu.plugins.trial.gui.TopMobsGui
import dev.hawu.plugins.trial.managers.UserMap
import org.bukkit.event.entity.{EntityDamageByEntityEvent, EntityDeathEvent}
import org.bukkit.event.{EventHandler, Listener}
import dev.hawu.plugins.trial.managers.UserMap.*
import org.bukkit.entity.{LivingEntity, Player}
import org.bukkit.event.player.PlayerJoinEvent

object RandomListener extends Listener:

    @EventHandler
    def onKill(event: EntityDeathEvent): Unit =
        val killer = event.getEntity.getKiller
        if killer == null then return ()

        val kills = killer.asUser.kills.getOrElse(event.getEntityType, 0) + 1
        killer.asUser.kills.update(event.getEntityType, kills)

    @EventHandler
    def onHit(event: EntityDamageByEntityEvent): Unit =
        val damager = event.getDamager
        if !damager.isInstanceOf[Player] || !event.getEntity.isInstanceOf[LivingEntity] then return ()

        val player = damager.asInstanceOf[Player]
        val entity = event.getEntity.asInstanceOf[LivingEntity]
        val currentHealth = if entity.getHealth - event.getFinalDamage < 0 then 0.0 else entity.getHealth - event.getFinalDamage
        val title = TitleComponent(s"&e${TopMobsGui.normalize(event.getEntityType.name())} &a$currentHealth &c/ ${entity.getMaxHealth}", "",
            5, 20, 5)
        TitlePacketAdapter.getAdapter.sendActionBar(player, title)

    @EventHandler
    def onJoin(event: PlayerJoinEvent): Unit =
        UserMap.manage(event.getPlayer)

package dev.hawu.plugins.trial

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.EntityType

import java.util
import java.util.UUID
import scala.collection.*
import scala.jdk.CollectionConverters.*

case class User(
    uuid: UUID,
    kills: mutable.Map[EntityType, Int] = mutable.HashMap().withDefaultValue(0),
) extends ConfigurationSerializable:

    private def mapToSerializable(tuple: (EntityType, Int)): (String, Int) = tuple match {
        case (entityType, count) => entityType.name() -> count
    }

    override def serialize(): util.Map[String, Any] = Map(
        "uuid" -> uuid.toString,
        "kills" -> kills.map(mapToSerializable).asJava,
    ).asJava

object User:

    def deserialize(map: util.Map[String, Any]): User =
        val uuid = UUID.fromString(map.get("uuid").asInstanceOf[String])
        val kills = map.get("kills").asInstanceOf[util.Map[String, Int]].asScala.map(
            tuple => EntityType.valueOf(tuple._1) -> tuple._2
        )

        User(uuid, kills.withDefaultValue(0))


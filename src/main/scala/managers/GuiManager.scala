package dev.hawu.plugins.trial
package managers

import dev.hawu.plugins.api.gui.{GuiElement, GuiModel}
import dev.hawu.plugins.api.items.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object GuiManager:
    
    def mountCloseButton(model: GuiModel, slot: Int = 49): Unit =
        model.mount(slot, new GuiElement[Unit] {
            override def handleClick(event: InventoryClickEvent): Unit =
                event.setCancelled(true)
                event.getWhoClicked.closeInventory()
            
            override def render(): ItemStack = ItemStackBuilder.of(Material.BARRIER)
                .name("&c&lClose")
                .lore("&7Closes this menu.")
                .build()
        })
    
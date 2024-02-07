package dev.danae.gregorail;

import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.webhooks.WebhookType;
import dev.danae.gregorail.util.webhooks.WebhookUtils;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;


public final class RailListener implements Listener
{
  // Event listener for when an entity enters a vehicle
  @EventHandler
  public void onVehicleEnterEvent(VehicleEnterEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if the entered entity is a player
    if (!(e.getEntered() instanceof Player player))
      return;
    
    // Check if the minecart has a code
    if (MinecartUtils.getCode(cart) == null)
      return;
    
    // Execute the appropriate webhooks
    RailPlugin.getInstance().executeWebhook(WebhookType.PLAYER_ENTERED_CART, WebhookUtils.createPlayerEnteredCartPayload(cart, player));
  }
  
  // Event handler for when an entity exits a vehicle
  @EventHandler
  public void onVehicleExitEvent(VehicleExitEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if the entered entity is a player
    if (!(e.getExited()instanceof Player player))
      return;
    
    // Check if the minecart has a code
    if (MinecartUtils.getCode(cart) == null)
      return;
    
    // Execute the appropriate webhooks
    RailPlugin.getInstance().executeWebhook(WebhookType.PLAYER_EXITED_CART, WebhookUtils.createPlayerExitedCartPayload(cart, player));
  }
  
  // Event listener for when a vehicle moves
  @EventHandler
  public void onVehicleMoveEvent(VehicleMoveEvent e)
  {    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;

    // Check if the minecart has a code
    if (MinecartUtils.getCode(cart) == null)
      return;
    
    // Check if the minecart moved to a new block (to avoid spamming the event)
    if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())
      return;
    
    // Execute the appropriate webhooks
    RailPlugin.getInstance().executeWebhook(WebhookType.CART_MOVED, WebhookUtils.createCartMovedPayload(cart));
  }
  
  // Event listener for when a vehicle is destroyed
  @EventHandler
  public void onVehicleDestroy(VehicleDestroyEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if the minecart has a code
    if (MinecartUtils.getCode(cart) == null)
      return;
    
    // Execute the appropriate webhooks
    RailPlugin.getInstance().executeWebhook(WebhookType.CART_DESTROYED, WebhookUtils.createCartDestroyedPayload(cart));
    
    // Clear the code and custom name of the minecart to prevent it from dropping
    MinecartUtils.setCode(cart, null);
  }
}

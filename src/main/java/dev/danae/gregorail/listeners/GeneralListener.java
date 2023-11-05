package dev.danae.gregorail.listeners;

import dev.danae.gregorail.util.minecart.MinecartUtils;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;


public final class GeneralListener implements Listener
{
  // Event listener for when a vehicle is destroyed
  @EventHandler
  public void onVehicleDestroy(VehicleDestroyEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Clear the code and custom name of the minecart to prevent it from dropping
    if (MinecartUtils.getCode(cart) != null)
      MinecartUtils.setCode(cart, null);
  }
}

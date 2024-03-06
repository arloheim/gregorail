/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.events.MinecartDestroyedEvent;
import dev.danae.gregorail.model.events.MinecartEnteredEvent;
import dev.danae.gregorail.model.events.MinecartExitedEvent;
import dev.danae.gregorail.model.events.MinecartMovedEvent;
import dev.danae.gregorail.model.minecart.MinecartCode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;


public class GregoRailListener extends GregoRailPluginComponent implements Listener
{
  // Constructor
  public GregoRailListener(GregoRailPlugin plugin)
  {
    super(plugin);
  }
  
  
  // Event listener for when an entity enters a vehicle
  @EventHandler
  public void onVehicleEnterEvent(VehicleEnterEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart minecart))
      return;
    
    // Check if the entered entity is a player
    if (!(e.getEntered() instanceof Player player))
      return;
    
    // Check if the minecart has a code
    var cart = this.getManager().createCart(minecart);
    if (cart.getCode().isEmpty())
      return;
    
    // Call an event
    Bukkit.getPluginManager().callEvent(new MinecartEnteredEvent(cart, player));
  }
  
  // Event handler for when an entity exits a vehicle
  @EventHandler
  public void onVehicleExitEvent(VehicleExitEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart minecart))
      return;
    
    // Check if the entered entity is a player
    if (!(e.getExited()instanceof Player player))
      return;
    
    // Check if the minecart has a code
    var cart = this.getManager().createCart(minecart);
    if (cart.getCode().isEmpty())
      return;
    
    // Call an event
    Bukkit.getPluginManager().callEvent(new MinecartExitedEvent(cart, player));
  }
  
  // Event listener for when a vehicle moves
  @EventHandler
  public void onVehicleMoveEvent(VehicleMoveEvent e)
  {    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart minecart))
      return;

    // Check if the minecart has a code
    var cart = this.getManager().createCart(minecart);
    if (cart.getCode().isEmpty())
      return;
    
    // Check if the minecart moved to a new block (to avoid spamming the event)
    if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())
      return;
    
    // Call an event
    Bukkit.getPluginManager().callEvent(new MinecartMovedEvent(cart, e.getFrom().getBlock().getLocation(), e.getTo().getBlock().getLocation()));
  }
  
  // Event listener for when a vehicle is destroyed
  @EventHandler
  public void onVehicleDestroy(VehicleDestroyEvent e)
  {
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart minecart))
      return;
    
    // Check if the minecart has a code
    var cart = this.getManager().createCart(minecart);
    if (cart.getCode().isEmpty())
      return;
    
    // Call an event
    Bukkit.getPluginManager().callEvent(new MinecartDestroyedEvent(cart, e.getAttacker()));
    
    // Clear the code and custom name of the minecart to prevent it from dropping
    cart.setCode(MinecartCode.empty());
  }
}

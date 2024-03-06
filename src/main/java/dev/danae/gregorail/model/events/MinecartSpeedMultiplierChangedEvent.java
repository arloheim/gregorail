package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.minecart.Minecart;
import org.bukkit.event.HandlerList;


public class MinecartSpeedMultiplierChangedEvent extends MinecartEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The original speed multiplier of the minecart
  private final double originalSpeedMultiplier;
  
  // The new speed multiplier of the minecart
  private final double speedMultiplier;
  
  
  // Constructor
  public MinecartSpeedMultiplierChangedEvent(Minecart cart, double originalSpeedMultiplier, double speedMultiplier)
  {
    super(cart);
    
    this.originalSpeedMultiplier = originalSpeedMultiplier;
    this.speedMultiplier = speedMultiplier;
  }
  
  
  // Return the original speed multiplier of the minecart
  public double getOriginalSpeedMultiplier()
  {
    return this.originalSpeedMultiplier;
  }
  
  // Return the new speed multiplier of the minecart
  public double getSpeedMultiplier()
  {
    return this.speedMultiplier;
  }
  
  
  // Return the list of handlers of the event
  @Override
  public HandlerList getHandlers()
  {
    return HANDLERS;
  }
  
  // Statically return the list of handlers of the event
  public static HandlerList getHandlerList() 
  {
    return HANDLERS;
  }
}

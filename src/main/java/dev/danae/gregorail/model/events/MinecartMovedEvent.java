package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.Minecart;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;


public class MinecartMovedEvent extends MinecartEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The original location of the minecart
  private final Location from;
  
  // The new location of the minecart
  private final Location to;
  
  
  // Constructor
  public MinecartMovedEvent(Minecart cart, Location from, Location to)
  {
    super(cart);
    
    this.from = from;
    this.to = to;
  }
  
  
  // Return the original location of the minecart
  public Location getFrom()
  {
    return this.from;
  }
  
  // Return the new location of the minecart
  public Location getTo()
  {
    return this.to;
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

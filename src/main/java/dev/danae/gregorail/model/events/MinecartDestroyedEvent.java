package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.Minecart;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;


public class MinecartDestroyedEvent extends MinecartEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The entity that destroyed the minecart
  private final Entity attacker;
  
  
  // Constructor
  public MinecartDestroyedEvent(Minecart cart, Entity attacker)
  {
    super(cart);
    
    this.attacker = attacker;
  }
  
  
  // Return the entity that destroyed the minecart
  public Entity getAttacker()
  {
    return this.attacker;
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

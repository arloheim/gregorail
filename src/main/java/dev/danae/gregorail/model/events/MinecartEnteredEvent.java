package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.minecart.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;


public class MinecartEnteredEvent extends MinecartEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The player that interacted with the minecart
  private final Player player;
  
  
  // Constructor
  public MinecartEnteredEvent(Minecart cart, Player player)
  {
    super(cart);
    
    this.player = player;
  }
  
  
  // Return the player that interacted with the minecart
  public Player getPlayer()
  {
    return this.player;
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

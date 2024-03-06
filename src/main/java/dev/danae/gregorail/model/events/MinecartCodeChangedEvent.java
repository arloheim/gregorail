package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.minecart.Minecart;
import dev.danae.gregorail.model.minecart.MinecartCode;
import org.bukkit.event.HandlerList;


public class MinecartCodeChangedEvent extends MinecartEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The original code of the minecart
  private final MinecartCode originalCode;
  
  // The new code of the minecart
  private final MinecartCode code;
  
  
  // Constructor
  public MinecartCodeChangedEvent(Minecart cart, MinecartCode originalCode, MinecartCode code)
  {
    super(cart);
    
    this.originalCode = originalCode;
    this.code = code;
  }
  
  
  // Return the original code of the minecart
  public MinecartCode getOriginalCode()
  {
    return this.originalCode;
  }
  
  // Return the new code of the minecart
  public MinecartCode getCode()
  {
    return this.code;
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

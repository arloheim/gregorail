package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Minecart;
import org.bukkit.event.HandlerList;


public class MinecartCodeChangedEvent extends MinecartEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The original code of the minecart
  private final Code originalCode;
  
  // The new code of the minecart
  private final Code code;
  
  
  // Constructor
  public MinecartCodeChangedEvent(Minecart cart, Code originalCode, Code code)
  {
    super(cart);
    
    this.originalCode = originalCode;
    this.code = code;
  }
  
  
  // Return the original code of the minecart
  public Code getOriginalCode()
  {
    return this.originalCode;
  }
  
  // Return the new code of the minecart
  public Code getCode()
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

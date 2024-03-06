package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.minecart.Minecart;
import org.bukkit.event.Event;


public abstract class MinecartEvent extends Event
{
  // The minecart of the event
  private final Minecart cart;
  
  
  // Constructor
  public MinecartEvent(Minecart cart)
  {
    this.cart = cart;
  }
  
  
  // Return the cart of the event
  public Minecart getCart()
  {
    return this.cart;
  }
}

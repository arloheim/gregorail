package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.Minecart;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;


public class BlockShapeChangedEvent extends BlockEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The rail associated with the block
  private final Rail rail;
  
  // The original shape of the block
  private final Rail.Shape originalShape;
  
  // The new shape of the block
  private final Rail.Shape shape;
  
  // The minecart that caused the change
  private final Minecart cause;
  
  
  // Constructor
  public BlockShapeChangedEvent(Block block, Rail rail, Rail.Shape originalShape, Rail.Shape shape, Minecart cause)
  {
    super(block);
    
    this.rail = rail;
    this.originalShape = originalShape;
    this.shape = shape;
    this.cause = cause;
  }
  
  
  // Return the rail associated with the block
  public Rail getRail()
  {
    return this.rail;
  }
  
  // Return the original shape of the minecart
  public Rail.Shape getOriginalShape()
  {
    return this.originalShape;
  }
  
  // Return the new shape of the minecart
  public Rail.Shape getShape()
  {
    return this.shape;
  }
  
  // Return the minecart that caused the change
  public Minecart getCause()
  {
    return this.cause;
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

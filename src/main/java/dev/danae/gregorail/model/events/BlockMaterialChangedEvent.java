package dev.danae.gregorail.model.events;

import dev.danae.gregorail.model.minecart.Minecart;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;


public class BlockMaterialChangedEvent extends BlockEvent
{
  // List of handlers of the event
  private static final HandlerList HANDLERS = new HandlerList();
  
  
  // The original material of the block
  private final Material originalMaterial;
  
  // The new material of the block
  private final Material material;
  
  // The minecart that caused the change
  private final Minecart cause;
  
  
  // Constructor
  public BlockMaterialChangedEvent(Block block, Material originalMaterial, Material material, Minecart cause)
  {
    super(block);
    
    this.originalMaterial = originalMaterial;
    this.material = material;
    this.cause = cause;
  }
  
  
  // Return the original material of the minecart
  public Material getOriginalMaterial()
  {
    return this.originalMaterial;
  }
  
  // Return the new material of the minecart
  public Material getMaterial()
  {
    return this.material;
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

package dev.danae.gregorail.model;

import java.util.Map;
import java.util.function.UnaryOperator;
import dev.danae.common.messages.MessageManager;
import dev.danae.gregorail.model.arguments.ArgumentTypeManager;
import dev.danae.gregorail.model.persistence.DataTypeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.minecart.RideableMinecart;


public interface Manager extends MessageManager, ArgumentTypeManager, DataTypeManager
{
  // Create a minecart
  public Minecart createCart(RideableMinecart minecart);
  
  // Return all defined tags of minecart codes
  public Map<Code, CodeTag> getDefinedCodeTags();
  
  // Return the tag of a code
  public CodeTag getCodeTag(Code code);
  
  // Set the tag of a code
  public void setCodeTag(Code code, CodeTag tag);

  // Set the tag of a code using the specified update function
  public void setCodeTag(Code code, UnaryOperator<CodeTag> updater);
  
  // Remove the tag of a code
  public void removeCodeTag(Code code);
  
  // Return the radius in blocks to search for blocks while parsing a location
  public int getBlockSearchRadius();
  
  // Return the distance in blocks to search for carts while setting or querying codes
  public int getCartSearchDistance();
  
  // Return the nearest minecart at the specified location
  public Minecart findNearestCart(Location center, int distance);
  
  // Return the nearest minecart at the specified location with the default search distance
  public Minecart findNearestCart(Location center);
  
  // Return the nearest minecart at the location, or the minecart that the player is riding
  public Minecart findNearestOrRidingCart(Location center, int distance, CommandSender sender);
  
  // Return the nearest minecart at the location with the default search distance, or the minecart that the player is riding
  public Minecart findNearestOrRidingCart(Location center, CommandSender sender);

  // Update the code of a cart
  public boolean updateCartCode(Minecart cart, Code code);
  
  // Update the speed multiplier of a cart
  public boolean updateCartSpeedMultiplier(Minecart cart, Double speedMultiplier);
  
  // Update the shape of a block
  public boolean updateBlockShape(Block block, Rail.Shape shape, Minecart cause);
  
  // Update the material of a block
  public boolean updateBlockMaterial(Block block, Material material, Minecart cause);
  
  // Play a sound
  public boolean playSound(Location location, NamespacedKey sound, Minecart cause, float volume, float pitch);

  // Run a task
  public void runTask(Runnable task);


  // Format a location to a component
  public default Component formatLocation(Location location)
  {
    if (location == null)
      return this.deserializeMessage("no-location-found", Map.of());
    
    return this.deserializeMessage("location-format", Map.of(
      "location", String.format("%d %d %d", location.getX(), location.getY(), location.getZ())));
  }

  // Format a block to a component
  public default Component formatBlock(Block block)
  {
    if (block == null)
      return this.deserializeMessage("no-block-found", Map.of());
    
    return this.deserializeMessage("block-format", Map.of(
      "material", block.getType().getKey().getKey(),
      "location", this.formatLocation(block.getLocation())));
  }

  // Format a minecart to a component
  public default Component formatMinecart(Minecart minecart)
  {
    if (minecart == null)
      return this.deserializeMessage("no-minecart-found", Map.of());
    
    return this.deserializeMessage("minecart-format", Map.of(
      "uuid", minecart.getId(),
      "code", minecart.getCode(),
      "location", this.formatLocation(minecart.getLocation())));
  }
}

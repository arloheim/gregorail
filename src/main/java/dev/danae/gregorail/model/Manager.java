package dev.danae.gregorail.model;

import dev.danae.gregorail.model.minecart.Minecart;
import dev.danae.gregorail.model.minecart.MinecartCode;
import dev.danae.gregorail.model.minecart.MinecartCodeDataType;
import dev.danae.gregorail.model.minecart.MinecartCodeTag;
import dev.danae.gregorail.model.minecart.MinecartDataType;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.minecart.RideableMinecart;


public interface Manager
{  
  // Get the persistent minecart data type
  public MinecartDataType getMinecartDataType();
  
  // Get the persistent minecart code data type
  public MinecartCodeDataType getMinecartCodeDataType();
  
  // Create a minecart
  public Minecart createCart(RideableMinecart minecart);
  
  // Return all defined tags of minecart codes
  public Map<MinecartCode, MinecartCodeTag> getDefinedCodeTags();
  
  // Return the tag of a minecart code
  public MinecartCodeTag getCodeTag(MinecartCode code);
  
  // Set the tag of a minecart code
  public void setCodeTag(MinecartCode code, MinecartCodeTag tag);

  // Set the tag of a minecart code using the specified update function
  public void setCodeTag(MinecartCode code, UnaryOperator<MinecartCodeTag> updater);
  
  // Remove the tag of a minecart code
  public void removeCodeTag(MinecartCode code);
  
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
  public boolean updateCartCode(Minecart cart, MinecartCode code);
  
  // Update the speed multiplier of a cart
  public boolean updateCartSpeedMultiplier(Minecart cart, Double speedMultiplier);
  
  // Update the shape of a block
  public boolean updateBlockShape(Block block, Rail.Shape shape, Minecart cause);
  
  // Update the material of a block
  public boolean updateBlockMaterial(Block block, Material material, Minecart cause);
  
  // Play a sound
  public boolean playSound(Location location, NamespacedKey sound, Minecart cause, float volume, float pitch);
}

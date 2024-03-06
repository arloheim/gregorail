package dev.danae.gregorail.model;

import dev.danae.gregorail.model.minecart.Minecart;
import dev.danae.gregorail.model.minecart.MinecartCode;
import dev.danae.gregorail.model.minecart.persistence.MinecartCodeDataType;
import dev.danae.gregorail.model.minecart.persistence.MinecartDataType;
import java.util.Map;
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
  
  // Return all defined display names
  public Map<MinecartCode, String> getDefinedDisplayNames();
  
  // Return the display name of a code
  public String getDisplayName(MinecartCode code);
  
  // Set the display name of a code
  public void setDisplayName(MinecartCode code, String displayName);
  
  // Remove the display name of a code
  public void removeDisplayName(MinecartCode code);
  
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

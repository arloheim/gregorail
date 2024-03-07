package dev.danae.gregorail.model;

import dev.danae.gregorail.model.persistence.CodeDataType;
import dev.danae.gregorail.model.persistence.MinecartDataType;
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
  
  // Get the persistent code data type
  public CodeDataType getCodeDataType();
  
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

  // Return all defined dynamic signs
  public Map<NamespacedKey, DynamicSign> getDefinedDynamicSigns();

  // Return the dymaic sign with the specified key
  public DynamicSign getDynamicSign(NamespacedKey key);

  // Set the dynamic sign with the specified key
  public void setDynamicSign(NamespacedKey key, DynamicSign dynamicSign);

  // Remove the dynamic sign with the specified key
  public void removeDynamicSign(NamespacedKey key);
  
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
}

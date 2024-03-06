package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.events.BlockMaterialChangedEvent;
import dev.danae.gregorail.model.events.BlockShapeChangedEvent;
import dev.danae.gregorail.model.events.MinecartCodeChangedEvent;
import dev.danae.gregorail.model.events.MinecartSpeedMultiplierChangedEvent;
import dev.danae.gregorail.model.events.SoundPlayedEvent;
import dev.danae.gregorail.model.minecart.MinecartCode;
import dev.danae.gregorail.model.minecart.Minecart;
import dev.danae.gregorail.model.minecart.persistence.MinecartCodeDataType;
import dev.danae.gregorail.model.minecart.persistence.MinecartDataType;
import dev.danae.gregorail.util.Cuboid;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;


public class GregoRailManager extends GregoRailPluginComponent implements Manager
{
  // The options for the manager
  private final GregoRailPluginOptions options;
  
  // The file to store defined display names in
  private final File displayNamesFile;
  
  // The map of all defined display names
  private final Map<MinecartCode, String> definedDisplayNames = new HashMap<>();
  
  // Persistent data types
  public final MinecartDataType minecartDataType = new MinecartDataType(this);
  public final MinecartCodeDataType minecartCodeDataType = new MinecartCodeDataType(this);
  
  
  // Constructor
  public GregoRailManager(GregoRailPlugin plugin, GregoRailPluginOptions options, File displayNamesFile)
  {
    super(plugin);
    
    this.options = options;
    this.displayNamesFile = displayNamesFile;
    
    this.loadDisplayNames();
  }
  
  
  // Load the display names from the configuration file
  private void loadDisplayNames()
  {
    try
    {
      if (!this.displayNamesFile.exists())
        this.displayNamesFile.createNewFile();
      
      var config = new YamlConfiguration();
      config.load(this.displayNamesFile);
      
      this.definedDisplayNames.clear();
      for (String key : config.getKeys(false))
        this.definedDisplayNames.put(MinecartCode.of(key), config.getString(key));
    }
    catch (IOException | InvalidConfigurationException ex)
    {
      this.getPlugin().getLogger().log(Level.WARNING, "Could not load the defined display names", ex);
    }
  }
  
  // Save the display names to the configuration file
  private void saveDisplayNames()
  {
    try
    {
      var config = new YamlConfiguration();
      for (var e : this.definedDisplayNames.entrySet())
        config.set(e.getKey().getId(), e.getValue());
      
      config.save(this.displayNamesFile);
    }
    catch (IOException ex)
    {
      this.getPlugin().getLogger().log(Level.WARNING, "Could not save the defined display names", ex);
    }
  }
  
  
  // Get the persistent minecart data type
  @Override
  public MinecartDataType getMinecartDataType()
  {
    return this.minecartDataType;
  }

  // Get the persistent minecart codedata type
  @Override
  public MinecartCodeDataType getMinecartCodeDataType()
  {
    return this.minecartCodeDataType;
  }
  
  // Create a minecart
  @Override
  public Minecart createCart(RideableMinecart minecart)
  {
    if (minecart == null)
      return null;
    return new GregoRailMinecart(this.getPlugin(), minecart);
  }
  
  // Return all defined display names
  @Override
  public Map<MinecartCode, String> getDefinedDisplayNames()
  {
    return this.definedDisplayNames;
  }
  
  // Return the display name of a code
  @Override
  public String getDisplayName(MinecartCode code)
  {
    var displayName = this.definedDisplayNames.get(code);
    return displayName != null ? displayName : code.getId();
  }
  
  // Set the display name of a code
  @Override
  public void setDisplayName(MinecartCode code, String displayName)
  {
    this.definedDisplayNames.put(code, displayName);
    this.saveDisplayNames();
  }
  
  // Remove the display name of a code
  @Override
  public void removeDisplayName(MinecartCode code)
  {
    this.definedDisplayNames.remove(code);
    this.saveDisplayNames();
  }
  
  // Return the radius in blocks to search for blocks while parsing a location
  @Override
  public int getBlockSearchRadius()
  {
    return this.options.getBlockSearchRadius();
  }
  
  // Return the distance in blocks to search for carts while setting or querying codes
  @Override
  public int getCartSearchDistance()
  {
    return this.options.getCartSearchDistance();
  }
  
  // Return the nearest minecart at the specified location
  @Override
  public Minecart findNearestCart(Location center, int distance)
  {
    return this.createCart(Cuboid.findNearestEntity(center, distance, RideableMinecart.class));
  }
  
  // Return the nearest minecart at the specified location with the default search distance
  @Override
  public Minecart findNearestCart(Location center)
  {
    return this.findNearestCart(center, this.getCartSearchDistance());
  }
  
  // Return the nearest minecart at the location, or the minecart that the player is riding
  @Override
  public Minecart findNearestOrRidingCart(Location center, int distance, CommandSender sender)
  {
    if (center != null)
      return this.findNearestCart(center, distance);
    else if (sender instanceof Player player && player.getVehicle() instanceof RideableMinecart minecart)
      return this.createCart(minecart);
    if (sender instanceof BlockCommandSender blockCommandSender)
      return this.findNearestCart(blockCommandSender.getBlock().getLocation(), distance);
    if (sender instanceof Entity entity)
      return this.findNearestCart(entity.getLocation(), distance);
    else
      return null;
  }
  
  // Return the nearest minecart at the location with the default search distance, or the minecart that the player is riding
  @Override
  public Minecart findNearestOrRidingCart(Location center, CommandSender sender)
  {
    return this.findNearestOrRidingCart(center, this.getCartSearchDistance(), sender);
  }
  
  // Update the code of a cart
  @Override
  public boolean updateCartCode(Minecart cart, MinecartCode code)
  {
    // Validate the cart
    if (cart == null)
      throw new IllegalArgumentException("No cart found");
    
    // Check if the code has been changed
    if (code != null)
    {
      // Save the original code
      var originalCode = cart.getCode();
      
      // Assign the code to the cart
      cart.setCode(code);
      
      // Call an event
      Bukkit.getPluginManager().callEvent(new MinecartCodeChangedEvent(cart, originalCode, code));
    }
    
    // Return if the code has been changed
    return code != null;
  }

  // Update the speed multiplier of a cart
  @Override
  public boolean updateCartSpeedMultiplier(Minecart cart, Double speedMultiplier)
  {
    // Validate the cart
    if (cart == null)
      throw new IllegalArgumentException("No cart found");
    
    // Validate the speed multiplier
    if (speedMultiplier != null && (speedMultiplier < 0.0 || speedMultiplier > 4.0))
      throw new IllegalArgumentException(String.format(Locale.ROOT, "%f is an invalid speed multiplier, speed multipliers must be between 0.0 and 4.0", speedMultiplier));
    
    // Check if the speed multiplier has been changed
    if (speedMultiplier != null)
    {
      // Save the original speed multiplier
      var originalSpeedMultiplier = cart.getSpeedMultiplier();
      
      // Set the speed multiplier
      cart.setSpeedMultiplier(speedMultiplier);
      
      // Call an event
      Bukkit.getPluginManager().callEvent(new MinecartSpeedMultiplierChangedEvent(cart, originalSpeedMultiplier, speedMultiplier));
    }
    
    // Return if the speed multiplier has been changed
    return speedMultiplier != null;
  }

  // Update the shape of a block
  @Override
  public boolean updateBlockShape(Block block, Rail.Shape shape, Minecart cause)
  {
    // Validate the block
    if (block == null)
      throw new IllegalArgumentException("No block found");
    if (!EnumSet.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL).contains(block.getType()))
      throw new IllegalArgumentException(String.format("%s is not a rail block", Formatter.formatBlockToString(block)));
    
    // Validate the shape
    var rail = (Rail)block.getBlockData();
    if (shape != null && !rail.getShapes().contains(shape))
      throw new IllegalArgumentException(String.format("%s cannot be set to shape %s", Formatter.formatBlockToString(block), shape.toString().toLowerCase()));
    
    // Check if the shape has been changed
    if (shape != null)
    {    
      // Save the original shape
      var originalShape = ((Rail)block.getBlockData()).getShape();
      
      // Set the shape of the block
      rail.setShape(shape);
      block.setBlockData(rail);
      
      // Call an event
      Bukkit.getPluginManager().callEvent(new BlockShapeChangedEvent(block, rail, originalShape, shape, cause));
    }
    
    // Return if the shape has been changed
    return shape != null;
  }

  // Update the material of a block
  @Override
  public boolean updateBlockMaterial(Block block, Material material, Minecart cause)
  {
    // Validate the block
    if (block == null)
      throw new IllegalArgumentException("No block found");
    
    // Check if the material has been changed
    if (material != null)
    {
      // Save the original material
      var originalMaterial = block.getType();
      
      // Set the material of the block
      block.setType(material);
      
      // Call an event
      Bukkit.getPluginManager().callEvent(new BlockMaterialChangedEvent(block, originalMaterial, material, cause));
    }
    
    // Return if the material has been changed
    return material != null;
  }
  
  // Play a sound
  @Override
  public boolean playSound(Location location, NamespacedKey sound, Minecart cause, float volume, float pitch)
  {
    // Check if the sound has been set
    if (sound != null)
    {
      // Play the sound
      location.getWorld().playSound(location, sound.toString(), volume, pitch);
      
      // Call an event
      Bukkit.getPluginManager().callEvent(new SoundPlayedEvent(location, sound, cause));
    }
    
    // Return if the sound has been set
    return sound != null;
  }
}

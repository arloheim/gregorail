package dev.danae.gregorail.location;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;


public class LocationUtils
{  
  // Pattern for parsing locations
  private static final Pattern pattern = Pattern.compile(
    // Current location: "~"
    "(?<cur>~)|" +
    // Numeric location: "x y z" or "~x ~y ~z
    "(?<xyz>(?:(?<x>0|-?[1-9][0-9]*)|(?<rx>~(?<dx>-?[1-9][0-9]*)?))\\s+(?:(?<y>0|-?[1-9][0-9]*)|(?<ry>~(?<dy>-?[1-9][0-9]*)?))\\s+(?:(?<z>0|-?[1-9][0-9]*)|(?<rz>~(?<dz>-?[1-9][0-9]*)?)))|" + 
    // Block location: "@block" or "^block"
    "(?<block>(?<mode>[@^])(?<name>[a-z_][a-z0-9_]*))");
  
  // Default radius for searching for blocks and entities
  private static final int searchRadius = 10;
  
  
  // Parse a location from a string
  public static Location parseLocation(Location loc, String string) throws LocationException
  {
    // Match the string against the pattern
    var m = pattern.matcher(string);
    if (!m.matches())
      throw new LocationException(string, String.format("The location string \"%s\" contains an invalid format", string));
    
    // Check for a current location
    if (m.group("cur") != null)
      return loc;
    
    // Check for a numeric location
    if (m.group("xyz") != null)
    {
      var x = m.group("rx") != null ? loc.getBlockX() + (m.group("dx") != null ? Integer.parseInt(m.group("dx")) : 0) : Integer.parseInt(m.group("x"));
      var y = m.group("ry") != null ? loc.getBlockY() + (m.group("dy") != null ? Integer.parseInt(m.group("dy")) : 0) : Integer.parseInt(m.group("y"));
      var z = m.group("rz") != null ? loc.getBlockZ() + (m.group("dz") != null ? Integer.parseInt(m.group("dz")) : 0) : Integer.parseInt(m.group("z"));
      
      return new Location(loc.getWorld(), x, y, z);
    }
    
    // Check for a block location
    if (m.group("block") != null)
    {      
      // Parse the block name
      var materialName = m.group("name");
      var material = Material.matchMaterial(materialName);
      if (material == null)
        throw new LocationException(string, String.format("The material \"%s\" could not be found", materialName));
      if (!material.isBlock())
        throw new LocationException(string, String.format("The material \"%s\" is not a block material", materialName));
      
      // Find the block
      var block = Cuboid.of(loc, searchRadius).findNearestBlockToCenter(material);
      if (block == null)
        return null;
      
      // Parse the block mode and return the appropriate position
      var mode = m.group("mode");
      if (mode.equals("^"))
        return block.getLocation().add(0, 1, 0);
      else
        return block.getLocation();
    }
    
    // No suitable location found
    return null;
  }
  
  
  // Parse the nearest entity matching the predicate from a string
  public static Entity parseEntity(Location loc, String string, Predicate<Entity> predicate) throws LocationException
  {
    var location = parseLocation(loc, string);
    if (location == null)
      return null;
    
    return getEntity(location, predicate);
  }
  
  // Parse the nearest entity of the specified class from a string
  public static <T extends Entity> T parseEntity(Location loc, String string, Class<T> cls) throws LocationException
  {
    var location = parseLocation(loc, string);
    if (location == null)
      return null;
    
    return getEntity(location, cls);
  }
  
  // Return the block from a string
  public static Block parseBlockAtLocation(Location loc, String string) throws LocationException
  {
    var location = parseLocation(loc, string);
    if (location == null)
      return null;
    
    return location.getBlock();
  }
  
  
  // Get the nearest entity matching the predicate at a location
  public static Entity getEntity(Location loc, Predicate<Entity> predicate)
  {
    return Cuboid.of(loc, searchRadius).findNearestEntityToCenter(cls);
  }
  
  // Get the nearest entity of the specified class at a location
  public static <T extends Entity> T getEntity(Location loc, Class<T> cls)
  {
    return Cuboid.of(loc, searchRadius).findNearestEntityToCenter(cls, predicate);
  }
  
  
  // Format a location to a string
  public static String formatLocation(Location location)
  {
    return String.format("[%d %d %d]", location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }
  
  // Format a block to a string
  public static String formatBlock(Block block)
  {
    return String.format("Block %s at %s", block.getType(), formatLocation(block.getLocation()));
  }
  
  // Format an entity to a string
  public static String formatEntity(Entity entity)
  {
    return String.format("Entity %s \"%s\" at %s", entity.getType(), entity.getCustomName() != null ? entity.getCustomName() : entity.getName(), formatLocation(entity.getLocation()));
  }
}

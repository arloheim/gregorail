package dev.danae.gregorail.location;

import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.Material;


public class LocationParser
{  
  // Pattern for parsing locations
  private static final Pattern PATTERN = Pattern.compile(
    // Current location: "~"
    "(?<cur>~)|" +
    // Absolute location: "[x,y,z]"
    "(?<abs>\\[(?<x>0|-?[1-9][0-9]*)\\s+(?<y>0|-?[1-9][0-9]*)\\s+(?<z>0|-?[1-9][0-9]*)\\])|" + 
    // Relative location: "[~x,~y,~z]"
    "(?<rel>\\[~(?<dx>-?[1-9][0-9]*)?\\s+~(?<dy>-?[1-9][0-9]*)?\\s+~(?<dz>-?[1-9][0-9]*)?\\])|" + 
    // Block location: "@block" or "^block"
    "(?<block>(?<mode>[@^])(?<name>[a-z_][a-z0-9_]*))");
  
  // Default radius for searching for blocks
  // TODO: Make this configurable
  private static final int BLOCK_RADIUS = 10;
  
  
  // Return a location from a string
  public static Location parse(Location loc, String string) throws LocationException
  {
    // Match the string against the pattern
    var m = PATTERN.matcher(string);
    if (!m.matches())
      throw new LocationException(string, String.format("The location string \"%s\" contains an invalid format", string));
    
    // Check for a current location
    if (m.group("cur") != null)
      return loc;
    
    // Check for an absolute location
    if (m.group("abs") != null)
    {
      var x = Integer.parseInt(m.group("x"));
      var y = Integer.parseInt(m.group("y"));
      var z = Integer.parseInt(m.group("z"));
      
      return new Location(loc.getWorld(), x, y, z);
    }
    
    // Check for a relative location
    if (m.group("rel") != null)
    {
      var dx = m.group("dx") != null ? Integer.parseInt(m.group("dx")) : 0;
      var dy = m.group("dy") != null ? Integer.parseInt(m.group("dy")) : 0;
      var dz = m.group("dz") != null ? Integer.parseInt(m.group("dz")) : 0;
      
      return new Location(loc.getWorld(), loc.getBlockX() + dx, loc.getBlockY() + dy, loc.getBlockZ() + dz);
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
      var block = Cuboid.of(loc, BLOCK_RADIUS).findNearestBlockToCenter(material);
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
}

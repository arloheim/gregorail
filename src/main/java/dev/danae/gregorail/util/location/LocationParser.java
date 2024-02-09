package dev.danae.gregorail.util.location;

import dev.danae.gregorail.util.parser.Parser;
import dev.danae.gregorail.util.parser.ParserException;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.block.Block;


public class LocationParser
{
  // Pattern for parsing locations
  private static final Pattern pattern = Pattern.compile(
    // Current location: "~"
    "(?<cur>~)|" +
    // Numeric location: "x y z" or "~x ~y ~z
    "(?<xyz>(?:(?<x>0|-?[1-9][0-9]*)|(?<rx>~(?<dx>-?[1-9][0-9]*)?))\\s+(?:(?<y>0|-?[1-9][0-9]*)|(?<ry>~(?<dy>-?[1-9][0-9]*)?))\\s+(?:(?<z>0|-?[1-9][0-9]*)|(?<rz>~(?<dz>-?[1-9][0-9]*)?)))|" + 
    // Block location: "@block" or "^block"
    "(?<block>(?<mode>[@^])(?<name>[a-z_][a-z0-9_]*))");
  
  
  // Parse a location from a string
  public static Location parseLocation(String string, Location origin, int distance) throws ParserException
  {
    // Match the string against the pattern
    var m = pattern.matcher(string);
    if (!m.matches())
      throw new ParserException(String.format("The location string \"%s\" contains an invalid format", string));
    
    // Check for a current location
    if (m.group("cur") != null)
      return origin;
    
    // Check for a numeric location
    else if (m.group("xyz") != null)
    {
      var x = m.group("rx") != null ? origin.getBlockX() + (m.group("dx") != null ? Parser.parseInt(m.group("dx")) : 0) : Parser.parseInt(m.group("x"));
      var y = m.group("ry") != null ? origin.getBlockY() + (m.group("dy") != null ? Parser.parseInt(m.group("dy")) : 0) : Parser.parseInt(m.group("y"));
      var z = m.group("rz") != null ? origin.getBlockZ() + (m.group("dz") != null ? Parser.parseInt(m.group("dz")) : 0) : Parser.parseInt(m.group("z"));
      
      return new Location(origin.getWorld(), x, y, z);
    }
    
    // Check for a block location
    else if (m.group("block") != null)
    {      
      var mode = m.group("mode");
      var material = Parser.parseMaterial(m.group("name"), true);
      
      // Find the block
      var block = Cuboid.of(origin, distance).findNearestBlockToCenter(material);
      if (block == null)
        return null;
      
      // Return the appropriate position based on the mode
      return switch (mode)
      {
        case "@" -> block.getLocation();
        case "^" -> block.getLocation().add(0, 1, 0);
        default -> null;
      };
    }
    
    // No suitable location found
    else
      return null;
  }
  
  // Parse a location from a string and return its block
  public static Block parseBlockAtLocation(String string, Location origin, int distance) throws ParserException
  {
    var location = parseLocation(string, origin, distance);
    if (location == null)
      return null;
    
    return location.getBlock();
  }
}

package dev.danae.gregorail.util.location;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
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
  
  
  // Parse a location from a string
  public static Location parseLocation(Location loc, String string, int distance) throws InvalidLocationException
  {
    try
    {
      // Match the string against the pattern
      var m = pattern.matcher(string);
      if (!m.matches())
        throw new InvalidLocationException(String.format("The location string \"%s\" contains an invalid format", string));
    
      // Check for a current location
      if (m.group("cur") != null)
        return loc;
    
      // Check for a numeric location
      else if (m.group("xyz") != null)
      {
        var x = m.group("rx") != null ? loc.getBlockX() + (m.group("dx") != null ? Integer.parseInt(m.group("dx")) : 0) : Integer.parseInt(m.group("x"));
        var y = m.group("ry") != null ? loc.getBlockY() + (m.group("dy") != null ? Integer.parseInt(m.group("dy")) : 0) : Integer.parseInt(m.group("y"));
        var z = m.group("rz") != null ? loc.getBlockZ() + (m.group("dz") != null ? Integer.parseInt(m.group("dz")) : 0) : Integer.parseInt(m.group("z"));
      
        return new Location(loc.getWorld(), x, y, z);
      }
    
      // Check for a block location
      else if (m.group("block") != null)
      {      
        // Parse the block name
        var materialName = m.group("name");
        var material = Material.matchMaterial(materialName);
        if (material == null)
          throw new InvalidLocationException(String.format("Material \"%s\" is an invalid material", materialName.toLowerCase()));
        if (!material.isBlock())
          throw new InvalidLocationException(String.format("Material \"%s\" is not a block material", materialName.toLowerCase()));
      
        // Find the block
        var block = Cuboid.of(loc, distance).findNearestBlockToCenter(material);
        if (block == null)
          return null;
      
        // Parse the block mode and return the appropriate position
        var mode = m.group("mode");
        if (mode.equals("@"))
          return block.getLocation();
        else if (mode.equals("^"))
          return block.getLocation().add(0, 1, 0);
        else
          return null;
      }
    
      // No suitable location found
      else
        return null;
    }
    catch (NumberFormatException ex)
    {
      throw new InvalidLocationException(String.format("The location string \"%s\" contains an invalid number format: %s", string, ex.getMessage()));
    }
  }
  
  // Parse a location from a string and return its block
  public static Block parseBlockAtLocation(Location loc, String string, int distance) throws InvalidLocationException
  {
    var location = parseLocation(loc, string, distance);
    if (location == null)
      return null;
    
    return location.getBlock();
  }
  
  
  // Get the nearest entity that matches the predicate at a location
  public static Entity findNearestEntity(Location loc, Predicate<Entity> predicate, int distance)
  {
    return Cuboid.of(loc, distance).findNearestEntityToCenter(predicate);
  }
  
  // Get the nearest entity of the specified class at a location
  public static <T extends Entity> T findNearestEntity(Location loc, Class<T> cls, int distance)
  {
    return Cuboid.of(loc, distance).findNearestEntityToCenter(cls);
  }
  
  // Get the nearest entity of the specified class and that matches the predicate at a location
  public static <T extends Entity> T findNearestEntity(Location loc, Class<T> cls, Predicate<T> predicate, int distance)
  {
    return Cuboid.of(loc, distance).findNearestEntityToCenter(cls, predicate);
  }
  
  
  // Format a location to a text component
  public static BaseComponent[] formatLocation(Location location)
  {
    if (location == null)
      return new ComponentBuilder("null").create();
    
    var string = String.format("%d %d %d", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    
    return new ComponentBuilder()
      .append(String.format("[%s]", string), ComponentBuilder.FormatRetention.NONE).color(ChatColor.BLUE)
        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("Click to copy \"%s\" to clipboard", string))))
        .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, string))
      .create();
  }
  
  // Format a block state to a text component
  public static BaseComponent[] formatBlockState(BlockState blockState)
  {
    if (blockState == null)
      return new ComponentBuilder("null").create();
    
    return new ComponentBuilder()
      .append("Block ", ComponentBuilder.FormatRetention.NONE)
      .append(blockState.getType().getKey().getKey(), ComponentBuilder.FormatRetention.NONE)
      .append(" at ", ComponentBuilder.FormatRetention.NONE)
      .append(formatLocation(blockState.getLocation()), ComponentBuilder.FormatRetention.NONE)
      .create();
  }
  
  // Format a block to a text component
  public static BaseComponent[] formatBlock(Block block)
  {
    return formatBlockState(block.getState());
  }
  
  // Format an entity to a text component
  public static BaseComponent[] formatEntity(Entity entity)
  {
    if (entity == null)
      return new ComponentBuilder("null").create();
    
    return new ComponentBuilder()
      .append("Entity ", ComponentBuilder.FormatRetention.NONE)
      .append(entity.getType().getKey().getKey(), ComponentBuilder.FormatRetention.NONE)
      .append(" at ", ComponentBuilder.FormatRetention.NONE)
      .append(formatLocation(entity.getLocation()), ComponentBuilder.FormatRetention.NONE)
      .create();
  }
}

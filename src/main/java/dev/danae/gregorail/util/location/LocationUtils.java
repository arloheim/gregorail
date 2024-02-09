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
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;


public class LocationUtils
{  
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

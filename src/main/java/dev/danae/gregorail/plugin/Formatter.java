package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.model.Minecart;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;


public class Formatter
{  
  // Return a stream of all block material names
  public static Stream<String> getAllMaterials(boolean requireBlock)
  {
    return getAllMaterials(requireBlock, UnaryOperator.identity());
  }
  
  // Return a stream of all block material names prefixed with the specified string
  public static Stream<String> getAllMaterials(boolean requireBlock, UnaryOperator<String> mapper)
  {
    return Arrays.stream(Material.values())
      .filter(material -> !requireBlock || material.isBlock())
      .map(material -> mapper.apply(material.name().toLowerCase()))
      .sorted();
  }
  
  // Return a stream of all shape names
  public static Stream<String> getAllShapes()
  {
    return getAllShapes(UnaryOperator.identity());
  }
  
  // Return a stream of all shape names
  public static Stream<String> getAllShapes(UnaryOperator<String> mapper)
  {
    return Arrays.stream(Rail.Shape.values())
      .map(shape -> mapper.apply(shape.name().toLowerCase()))
      .sorted();
  }
  
  // Return a stream of all sound names
  public static Stream<String> getAllSounds()
  {
    return getAllSounds(UnaryOperator.identity());
  }
  
  // Return a stream of all sound names
  public static Stream<String> getAllSounds(UnaryOperator<String> mapper)
  {
    return Arrays.stream(Sound.values())
      .map(sound -> mapper.apply(sound.getKey().toString()))
      .sorted();
  }
  
  
  // Format a location to a text component
  public static BaseComponent[] formatLocation(Location location)
  {
    if (location == null)
      return new ComponentBuilder("No Location found").create();
    
    var string = String.format("%d %d %d", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    
    return new ComponentBuilder()
      .append(String.format("[%s]", string), ComponentBuilder.FormatRetention.NONE).color(ChatColor.BLUE)
        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("Click to copy \"%s\" to clipboard", string))))
        .event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, string))
      .create();
  }
  
  // Format a location to a string
  public static String formatLocationToString(Location location)
  {
    return BaseComponent.toPlainText(formatLocation(location));
  }
  
  // Format a block to a text component
  public static BaseComponent[] formatBlock(Block block)
  {
    if (block == null)
      return new ComponentBuilder("No block found").create();
    
    return new ComponentBuilder()
      .append("Block ", ComponentBuilder.FormatRetention.NONE)
      .append(block.getType().getKey().getKey(), ComponentBuilder.FormatRetention.NONE)
      .append(" at ", ComponentBuilder.FormatRetention.NONE)
      .append(formatLocation(block.getLocation()))
      .create();
  }
  
  // Format a block to a string
  public static String formatBlockToString(Block block)
  {
    return BaseComponent.toPlainText(formatBlock(block));
  }
  
  // Format a minecart to a text component
  public static BaseComponent[] formatCart(Minecart cart)
  {
    if (cart == null)
      return new ComponentBuilder("No cart found").create();
    
    return new ComponentBuilder()
      .append("Minecart ", ComponentBuilder.FormatRetention.NONE)
      .append(!cart.getCode().isEmpty() ? String.format("with code %s", cart.getCode().getId()) : "without code", ComponentBuilder.FormatRetention.NONE)
      .append(" at ", ComponentBuilder.FormatRetention.NONE)
      .append(formatLocation(cart.getLocation()))
      .create();
  }
  
  // Format an minecart to a string
  public static String formatCartToString(Minecart cart)
  {
    return BaseComponent.toPlainText(formatCart(cart));
  }
  
  // Format a speed multiplier to a text component
  public static BaseComponent[] formatSpeedMultiplier(double speedMultiplier)
  {
    return new ComponentBuilder()
      .append(String.format(Locale.ROOT, "%.2f", speedMultiplier))
        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format(Locale.ENGLISH, "%.2f m/s, %.2f km/h", speedMultiplier * GregoRailMinecart.DEFAULT_SPEED * 20, speedMultiplier * GregoRailMinecart.DEFAULT_SPEED * 20 * 3.6))))
      .create();
  }
  
  // Format a speed multiplier to a string
  public static String formatSpeedMultiplierToString(double speedMultiplier)
  {
    return BaseComponent.toPlainText(formatSpeedMultiplier(speedMultiplier));
  }
  
  // Format a sound to a text component
  public static BaseComponent[] formatSound(NamespacedKey sound)
  {
    return new ComponentBuilder()
      .append("Sound ", ComponentBuilder.FormatRetention.NONE)
      .append(sound.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create();
  }
  
  // Format a sound to a string
  public static String formatSoundToString(NamespacedKey sound)
  {
    return BaseComponent.toPlainText(formatSound(sound));
  }
  
  
  
  // Format an tag list message
  public static BaseComponent[] formatTagListMessage(Map<Code, CodeTag> codeTags)
  {
    var builder = new ComponentBuilder(String.format("%d code tags are defined", codeTags.size()));
    for (var e : codeTags.entrySet())
    {
      if (e.getValue() == null)
        continue;
      
      var name = e.getValue().getName();
      var url = e.getValue().getUrl();

      builder.append(String.format("\n- %s: ", e.getKey()), ComponentBuilder.FormatRetention.NONE);

      if (name != null)
        builder.append(name, ComponentBuilder.FormatRetention.NONE);
      else
        builder.append("<empty>", ComponentBuilder.FormatRetention.NONE);

      if (url != null)
      {
        builder
          .append(" (", ComponentBuilder.FormatRetention.NONE)
          .append("link", ComponentBuilder.FormatRetention.NONE).color(ChatColor.BLUE).underlined(true)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format(url))))
            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
          .append(")", ComponentBuilder.FormatRetention.NONE);
      }
    }
    return builder.create();
  }
  
  // Format a tag name changed message
  public static BaseComponent[] formatTagNameChangedMessage(Code code, String name)
  {
    return new ComponentBuilder()
      .append("The name of code tag ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(" has been changed to \"", ComponentBuilder.FormatRetention.NONE)
      .append(TextComponent.fromLegacyText(name), ComponentBuilder.FormatRetention.NONE)
      .append("\"", ComponentBuilder.FormatRetention.NONE)
      .create();
  }

  // Format a tag URL changed message
  public static BaseComponent[] formatTagUrlChangedMessage(Code code, String url)
  {
    return new ComponentBuilder()
      .append("The URL of code tag ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(" has been changed to \"", ComponentBuilder.FormatRetention.NONE)
      .append(url, ComponentBuilder.FormatRetention.NONE)
      .append("\"", ComponentBuilder.FormatRetention.NONE)
      .create();
  }

  // Format a tag name cleared message
  public static BaseComponent[] formatTagNameClearedMessage(Code code)
  {
    return new ComponentBuilder()
      .append("The name of code tag ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(" has been cleared", ComponentBuilder.FormatRetention.NONE)
      .create();
  }

  // Format a tag URL cleared message
  public static BaseComponent[] formatTagUrlClearedMessage(Code code)
  {
    return new ComponentBuilder()
      .append("The URL of code tag ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(" has been cleared", ComponentBuilder.FormatRetention.NONE)
      .create();
  }
  
  // Format a tag removed message
  public static BaseComponent[] formatTagRemovedMessage(Code code)
  {
    return new ComponentBuilder()
      .append("Code tag ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(" has been removed", ComponentBuilder.FormatRetention.NONE)
      .create();
  }
  
  // Format a cart code changed message
  public static BaseComponent[] formatCartCodeChangedMessage(Minecart cart, Code originalCode, Code code)
  {
    return new ComponentBuilder()
      .append(formatCart(cart), ComponentBuilder.FormatRetention.NONE)
      .append(": code changed to ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(", was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalCode.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create();
  }
  
  // Format a cart code changed message
  public static BaseComponent[] formatCartCodeClearedMessage(Minecart cart, Code originalCode)
  {
    return new ComponentBuilder()
      .append(formatCart(cart), ComponentBuilder.FormatRetention.NONE)
      .append(": code cleared, was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalCode.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create();
  }
  
  // Format a cart code retained message
  public static BaseComponent[] formatCartCodeRetainedMessage(Minecart cart, Code originalCode)
  {
    return new ComponentBuilder()
      .append("The code of ", ComponentBuilder.FormatRetention.NONE)
      .append(formatCart(cart), ComponentBuilder.FormatRetention.NONE)
      .append(": code unchanged, was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalCode.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create();
  }
  
  // Format a cart speed changed message
  public static BaseComponent[] formatCartSpeedChangedMessage(Minecart cart, double originalSpeedMultiplier, double speedMultiplier)
  {
    // Send information about the updated cart
    return new ComponentBuilder()
      .append(formatCart(cart), ComponentBuilder.FormatRetention.NONE)
      .append(": speed multiplier to ", ComponentBuilder.FormatRetention.NONE)
      .append(formatSpeedMultiplier(speedMultiplier), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(", was ", ComponentBuilder.FormatRetention.NONE)
      .append(formatSpeedMultiplier(originalSpeedMultiplier), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create();
  }
  
  // Format a cart speed retained message
  public static BaseComponent[] formatCartSpeedRetainedMessage(Minecart cart, double originalSpeedMultiplier)
  {
    return new ComponentBuilder()
      .append(formatCart(cart), ComponentBuilder.FormatRetention.NONE)
      .append(": speed multiplier unchanged, was ", ComponentBuilder.FormatRetention.NONE)
      .append(formatSpeedMultiplier(originalSpeedMultiplier), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create();
  }
  
  // Format a block shape changed message
  public static BaseComponent[] formatBlockShapeChangedMessage(Block block, Rail.Shape originalShape, Rail.Shape shape, Minecart cause)
  {
    var builder = new ComponentBuilder()
      .append(formatBlock(block), ComponentBuilder.FormatRetention.NONE)
      .append(": shape changed to ", ComponentBuilder.FormatRetention.NONE)
      .append(shape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(", was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalShape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (cause != null)
      appendDetectedCart(builder, cause);
    return builder.create();
  }
  
  // Format a block shape retained message
  public static BaseComponent[] formatBlockShapeRetainedMessage(Block block, Rail.Shape originalShape, Minecart cause)
  {
    var builder = new ComponentBuilder()
      .append(formatBlock(block), ComponentBuilder.FormatRetention.NONE)
      .append(": shape unchanged, was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalShape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (cause != null)
      appendDetectedCart(builder, cause);
    return builder.create();
  }
  
  // Format a block material changed message
  public static BaseComponent[] formatBlockMaterialChangedMessage(Block block, Material originalMaterial, Material material, Minecart cause)
  {
    var builder = new ComponentBuilder()
      .append(formatBlock(block), ComponentBuilder.FormatRetention.NONE)
      .append(": material changed to ", ComponentBuilder.FormatRetention.NONE)
      .append(material.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .append(", was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalMaterial.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (cause != null)
      appendDetectedCart(builder, cause);
    
    return builder.create();
  }
  
  // Format a block material retained message
  public static BaseComponent[] formatBlockMaterialRetainedMessage(Block block, Material originalMaterial, Minecart cause)
  {
    var builder = new ComponentBuilder()
      .append(formatBlock(block), ComponentBuilder.FormatRetention.NONE)
      .append(": material unchanged, was ", ComponentBuilder.FormatRetention.NONE)
      .append(originalMaterial.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (cause != null)
      appendDetectedCart(builder, cause);
    return builder.create();
  }
  
  // Format a play sound message
  public static BaseComponent[] formatPlaySoundMessage(Location location, NamespacedKey sound, Minecart cause)
  {
    var builder = new ComponentBuilder()
      .append(formatSound(sound), ComponentBuilder.FormatRetention.NONE)
      .append(" played at ", ComponentBuilder.FormatRetention.NONE)
      .append(formatLocation(location), ComponentBuilder.FormatRetention.NONE);
    
    if (cause != null)
      appendDetectedCart(builder, cause);
    return builder.create();
  }
  
  
  // Append a detected cart to a component builder
  private static void appendDetectedCart(ComponentBuilder builder, Minecart cart)
  {
    builder
      .append(", detected ", ComponentBuilder.FormatRetention.NONE)
      .append(formatCart(cart));
  }
}

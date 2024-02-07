package dev.danae.gregorail.commands;

import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.minecart.Code;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.minecart.RideableMinecart;

public class CommandMessages
{
  // Append a detected cart to a message
  private static void appendDetectedCart(ComponentBuilder builder, RideableMinecart cart)
  {
    builder
      .append(", detected ", ComponentBuilder.FormatRetention.NONE)
      .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE);
  }
  
  
  // Send a cart code changed message
  public static void sendCartCodeChangedMessage(CommandContext context, RideableMinecart cart, Code code)
  {
    context.sendMessage(new ComponentBuilder()
      .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
      .append(" now has code ", ComponentBuilder.FormatRetention.NONE)
      .append(code.toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create());
  }
  
  // Send a cart code cleared message
  public static void sendCartCodeClearedMessage(CommandContext context, RideableMinecart cart)
  {
    context.sendMessage(new ComponentBuilder()
      .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
      .append(" now has no code", ComponentBuilder.FormatRetention.NONE)
      .create());
  }
  
  // Send a cart code unchanged message
  public static void sendCartCodeUnchangedMessage(CommandContext context, RideableMinecart cart)
  {
    context.sendMessage(new ComponentBuilder()
      .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
      .append(" still has original code ", ComponentBuilder.FormatRetention.NONE)
      .append(MinecartUtils.getCode(cart).toString(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create());
  }
  
  // Send a cart speed changed message
  public static void sendCartSpeedChangedMessage(CommandContext context, RideableMinecart cart, double speedMultiplier)
  {
    // Send information about the updated cart
    context.sendMessage(new ComponentBuilder()
      .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
      .append(" now has speed multiplier ", ComponentBuilder.FormatRetention.NONE)
      .append(CommandUtils.formatSpeedMultiplier(speedMultiplier), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create());
  }
  
  // Send a cart speed unchanged message
  public static void sendCartSpeedUnchangedMessage(CommandContext context, RideableMinecart cart)
  {
    context.sendMessage(new ComponentBuilder()
      .append(LocationUtils.formatEntity(cart), ComponentBuilder.FormatRetention.NONE)
      .append(" still has original speed multiplier ", ComponentBuilder.FormatRetention.NONE)
      .append(CommandUtils.formatSpeedMultiplier(MinecartUtils.getSpeedMultiplier(cart)), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN)
      .create());
  }
  
  // Send a block changed message
  public static void sendBlockChangedMessage(CommandContext context, BlockState blockState, Material material, RideableMinecart detectedCart)
  {
    var builder = new ComponentBuilder()
      .append(LocationUtils.formatBlockState(blockState), ComponentBuilder.FormatRetention.NONE)
      .append(" now has material ", ComponentBuilder.FormatRetention.NONE)
      .append(material.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (detectedCart != null)
      appendDetectedCart(builder, detectedCart);            
    context.sendMessage(builder.create());
  }
  
  // Send a block unchanged message
  public static void sendBlockUnchangedMessage(CommandContext context, BlockState blockState, Material material, RideableMinecart detectedCart)
  {
    var builder = new ComponentBuilder()
      .append(LocationUtils.formatBlockState(blockState), ComponentBuilder.FormatRetention.NONE)
      .append(" still has original material ", ComponentBuilder.FormatRetention.NONE)
      .append(material.getKey().getKey(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (detectedCart != null)
      appendDetectedCart(builder, detectedCart);            
    context.sendMessage(builder.create());
  }
  
  // Send a switch changed message
  public static void sendSwitchChangedMessage(CommandContext context, Block block, Rail.Shape shape, RideableMinecart detectedCart)
  {
    var builder = new ComponentBuilder()
      .append(LocationUtils.formatBlock(block), ComponentBuilder.FormatRetention.NONE)
      .append(" now has shape ", ComponentBuilder.FormatRetention.NONE)
      .append(shape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (detectedCart != null)
      appendDetectedCart(builder, detectedCart);            
    context.sendMessage(builder.create());
  }
  
  // Send a switch unchanged message
  public static void sendSwitchUnchangedMessage(CommandContext context, Block block, Rail.Shape shape, RideableMinecart detectedCart)
  {
    var builder = new ComponentBuilder()
      .append(LocationUtils.formatBlock(block), ComponentBuilder.FormatRetention.NONE)
      .append(" still has original shape ", ComponentBuilder.FormatRetention.NONE)
      .append(shape.toString().toLowerCase(), ComponentBuilder.FormatRetention.NONE).color(ChatColor.GREEN);
    
    if (detectedCart != null)
      appendDetectedCart(builder, detectedCart);            
    context.sendMessage(builder.create());
  }
}

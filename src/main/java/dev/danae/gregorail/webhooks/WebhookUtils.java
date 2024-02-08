package dev.danae.gregorail.webhooks;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;


public class WebhookUtils
{
  // Create a webhook payload for when the code of a cart was set due to a /gcart command
  public static JsonObject createCartCodeSetPayload(RideableMinecart minecart)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.CART_CODE_SET.toString()));
    obj.add("minecart", createMinecartPayload(minecart));
    return obj;
  }
  
  // Create a webhook payload for when the code of a cart was cleared due to a /gcart command
  public static JsonObject createCartCodeClearedPayload(RideableMinecart minecart)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.CART_CODE_CLEARED.toString()));
    obj.add("minecart", createMinecartPayload(minecart)); 
    return obj;
  }
   
  // Create a webhook payload for when the speed of a cart was changed due to a /gcart command
  public static JsonObject createCartSpeedChangedPayload(RideableMinecart minecart)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.CART_SPEED_CHANGED.toString()));
    obj.add("minecart", createMinecartPayload(minecart));
    return obj;
  }
  
  // Create a webhook payload for when a block was changed due to a /grail command
  public static JsonObject createBlockChangedPayload(BlockState blockState, Material material, RideableMinecart cause)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.BLOCK_CHANGED.toString()));
    obj.add("block", createBlockStatePayload(blockState));
    obj.add("material", new JsonPrimitive(material.getKey().getKey()));
    obj.add("cause", createMinecartPayload(cause));
    return obj;
  }
  
  // Create a webhook payload for when a switch was changed due to a /grail command
  public static JsonObject createSwitchChangedPayload(BlockState blockState, Rail.Shape shape, RideableMinecart cause)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.SWITCH_CHANGED.toString()));
    obj.add("block", createBlockStatePayload(blockState));
    obj.add("shape", new JsonPrimitive(shape.toString()));
    obj.add("cause", createMinecartPayload(cause));
    return obj;
  }
  
  // Create a webhook payload for when a player entered a cart with a code
  public static JsonObject createPlayerEnteredCartPayload(RideableMinecart minecart, Player player)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.PLAYER_ENTERED_CART.toString()));
    obj.add("minecart", createMinecartPayload(minecart));
    obj.add("player", createPlayerPayload(player));
    return obj;
  }
  
  // Create a webhook payload for when a player exited a cart with a code
  public static JsonObject createPlayerExitedCartPayload(RideableMinecart minecart, Player player)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.PLAYER_EXITED_CART.toString()));
    obj.add("minecart", createMinecartPayload(minecart));
    obj.add("player", createPlayerPayload(player));
    return obj;
  }
  
  // Create a webhook payload for when a cart with a code moved
  public static JsonObject createCartMovedPayload(RideableMinecart minecart)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.CART_MOVED.toString()));
    obj.add("minecart", createMinecartPayload(minecart));
    return obj;
  }
  
  // Create a webhook payload for when a cart with a code is destroyed
  public static JsonObject createCartDestroyedPayload(RideableMinecart minecart)
  {
    var obj = new JsonObject();
    obj.add("type", new JsonPrimitive(WebhookType.CART_DESTROYED.toString()));
    obj.add("minecart", createMinecartPayload(minecart));
    return obj;
  }
  
          
  // Create a webhook payload for a minecart
  private static JsonElement createMinecartPayload(RideableMinecart minecart)
  {
    if (minecart == null)
      return JsonNull.INSTANCE;
    
    var code = MinecartUtils.getCode(minecart);
    
    var obj = new JsonObject();
    obj.add("id", new JsonPrimitive(minecart.getEntityId()));
    obj.add("code", code != null ? new JsonPrimitive(code.getId()) : JsonNull.INSTANCE);
    obj.add("displayName", code != null ? new JsonPrimitive(ChatColor.stripColor(CodeUtils.getDisplayName(code))) : JsonNull.INSTANCE);
    obj.add("speed", new JsonPrimitive(MinecartUtils.getSpeedMultiplier(minecart)));   
    obj.add("passenger", createPlayerPayload(MinecartUtils.getRidingPlayer(minecart))); 
    obj.add("location", createLocationPayload(minecart.getLocation())); 
    return obj;
  }
  
  // Create a webhook payload for a player
  private static JsonElement createPlayerPayload(Player player)
  {
    if (player == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("uuid", new JsonPrimitive(player.getUniqueId().toString()));
    obj.add("name", new JsonPrimitive(player.getName()));
    return obj;
  }
  
  // Create a webhook payload for a block state
  private static JsonElement createBlockStatePayload(BlockState blockState)
  {
    if (blockState == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("location", createLocationPayload(blockState.getLocation()));
    obj.add("material", new JsonPrimitive(blockState.getType().getKey().getKey()));
    return obj;
  }
  
  // Create a webhook payload for a location
  private static JsonElement createLocationPayload(Location location)
  {
    if (location == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("world", new JsonPrimitive(location.getWorld().getName()));
    obj.add("x", new JsonPrimitive(location.getBlockX()));
    obj.add("y", new JsonPrimitive(location.getBlockY()));
    obj.add("z", new JsonPrimitive(location.getBlockZ()));
    return obj;
  }
}

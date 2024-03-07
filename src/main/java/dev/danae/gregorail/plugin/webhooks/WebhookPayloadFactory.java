package dev.danae.gregorail.plugin.webhooks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.Minecart;
import dev.danae.gregorail.plugin.webhooks.serializers.LocationSerializer;
import dev.danae.gregorail.plugin.webhooks.serializers.CodeSerializer;
import dev.danae.gregorail.plugin.webhooks.serializers.CodeTagSerializer;
import dev.danae.gregorail.plugin.webhooks.serializers.MinecartSerializer;
import dev.danae.gregorail.plugin.webhooks.serializers.PlayerSerializer;
import dev.danae.gregorail.plugin.webhooks.serializers.WorldSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.Player;


public class WebhookPayloadFactory extends ManagerComponent
{
  // The serializer for webhook payloads
  private final Gson gson;
  
  
  // Constructor
  public WebhookPayloadFactory(Manager manager)
  {
    super(manager);
    
    this.gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationSerializer(manager))
      .registerTypeAdapter(Player.class, new PlayerSerializer(manager))
      .registerTypeAdapter(World.class, new WorldSerializer(manager))
      .registerTypeAdapter(Minecart.class, new MinecartSerializer(manager))
      .registerTypeAdapter(Code.class, new CodeSerializer(manager))
      .registerTypeAdapter(CodeTag.class, new CodeTagSerializer(manager))
      .serializeNulls()
      .setPrettyPrinting()
      .create();
  }
  
  
  // Create a webhook payload for a cart
  public JsonObject createCartPayload(Minecart minecart)
  {
    var obj = new JsonObject();
    obj.add("minecart", this.gson.toJsonTree(minecart, Minecart.class));
    return obj;
  }
    
  // Create a webhook payload for a player that interacts with a cart
  public JsonObject createCartPayload(Minecart minecart, Player player)
  {
    var obj = new JsonObject();
    obj.add("minecart", this.gson.toJsonTree(minecart, Minecart.class));
    obj.add("player", this.gson.toJsonTree(player, Player.class));
    return obj;
  }
  
  // Create a webhook payload for a modified cart location
  public JsonObject createCartLocationPayload(Minecart minecart, Location from, Location to)
  {
    var obj = new JsonObject();
    obj.add("minecart", this.gson.toJsonTree(minecart, Minecart.class));
    obj.add("from", this.gson.toJsonTree(from, Location.class));
    obj.add("to", this.gson.toJsonTree(to, Location.class));
    return obj;
  }
  
  // Create a webhook payload for a modified cart code
  public JsonObject createCartCodePayload(Minecart minecart, Code originalCode, Code code)
  {
    var obj = new JsonObject();
    obj.add("minecart", this.gson.toJsonTree(minecart, Minecart.class));
    obj.add("originalCode", !originalCode.isEmpty() ? new JsonPrimitive(originalCode.getId()) : JsonNull.INSTANCE);
    obj.add("code", !code.isEmpty() ? new JsonPrimitive(code.getId()) : JsonNull.INSTANCE);
    return obj;
  }
  
  // Create a webhook payload for a modified cart speed multiplier
  public JsonObject createCartSpeedMultiplierPayload(Minecart minecart, double originalSpeedMultiplier, double speedMultiplier)
  {
    var obj = new JsonObject();
    obj.add("minecart", this.gson.toJsonTree(minecart, Minecart.class));
    obj.add("originalSpeedMultiplier", new JsonPrimitive(originalSpeedMultiplier));
    obj.add("speedMultiplier", new JsonPrimitive(speedMultiplier));
    return obj;
  }
  
  // Create a webhook payload for when a switch was changed due to a /grail command
  public JsonObject createBlockShapePayload(Block block, Rail.Shape originalShape, Rail.Shape shape, Minecart cause)
  {
    var obj = new JsonObject();
    obj.add("location", this.gson.toJsonTree(block.getLocation(), Location.class));
    obj.add("originalShape", new JsonPrimitive(originalShape.toString()));
    obj.add("shape", new JsonPrimitive(shape.toString()));
    obj.add("cause", this.gson.toJsonTree(cause, Minecart.class));
    return obj;
  }
  
  // Create a webhook payload for a modified block material
  public JsonObject createBlockMaterialPayload(Block block, Material originalMaterial, Material material, Minecart cause)
  {
    var obj = new JsonObject();
    obj.add("location", this.gson.toJsonTree(block.getLocation(), Location.class));
    obj.add("originalMaterial", new JsonPrimitive(originalMaterial.getKey().getKey()));
    obj.add("material", new JsonPrimitive(material.getKey().getKey()));
    obj.add("cause", this.gson.toJsonTree(cause, Minecart.class));
    return obj;
  }
  
  // Create a webhook payload fpr a played sound
  public JsonObject createSoundPayload(Location location, NamespacedKey sound, Minecart cause)
  {
    var obj = new JsonObject();
    obj.add("location", this.gson.toJsonTree(location, Location.class));
    obj.add("sound", new JsonPrimitive(sound.toString()));
    obj.add("cause", this.gson.toJsonTree(cause, Minecart.class));
    return obj;
  }
}

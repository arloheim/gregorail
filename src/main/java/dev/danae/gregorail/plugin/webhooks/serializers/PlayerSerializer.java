package dev.danae.gregorail.plugin.webhooks.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import java.lang.reflect.Type;
import org.bukkit.entity.Player;


public class PlayerSerializer extends ManagerComponent implements JsonSerializer<Player>
{
  // Constructor
  public PlayerSerializer(Manager manager)
  {
    super(manager);
  }
  
  
  // Serialize a player to a JSON element
  @Override
  public JsonElement serialize(Player player, Type type, JsonSerializationContext context)
  {
    if (player == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("uuid", new JsonPrimitive(player.getUniqueId().toString()));
    obj.add("name", new JsonPrimitive(player.getName()));
    return obj;
  }
}

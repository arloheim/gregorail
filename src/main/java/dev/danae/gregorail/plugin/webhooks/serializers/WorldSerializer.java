package dev.danae.gregorail.plugin.webhooks.serializers;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import org.bukkit.World;


public class WorldSerializer extends ManagerComponent implements JsonSerializer<World>
{
  // Constructor
  public WorldSerializer(Manager manager)
  {
    super(manager);
  }
  
  
  // Serialize a world to a JSON element
  @Override
  public JsonElement serialize(World world, Type type, JsonSerializationContext context)
  {
    if (world == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("uuid", new JsonPrimitive(world.getUID().toString()));
    obj.add("name", new JsonPrimitive(world.getName()));
    return obj;
  }
}

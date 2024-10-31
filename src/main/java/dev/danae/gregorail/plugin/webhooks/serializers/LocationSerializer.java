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
import org.bukkit.Location;
import org.bukkit.World;


public class LocationSerializer extends ManagerComponent implements JsonSerializer<Location>
{
  // Constructor
  public LocationSerializer(Manager manager)
  {
    super(manager);
  }
  
  
  // Serialize a location to a JSON element
  @Override
  public JsonElement serialize(Location location, Type type, JsonSerializationContext context)
  {
    if (location == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("world", context.serialize(location.getWorld(), World.class));
    obj.add("x", new JsonPrimitive(location.getBlockX()));
    obj.add("y", new JsonPrimitive(location.getBlockY()));
    obj.add("z", new JsonPrimitive(location.getBlockZ()));
    return obj;
  }
}

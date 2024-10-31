package dev.danae.gregorail.plugin.webhooks.serializers;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.danae.gregorail.model.CodeTag;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import net.md_5.bungee.api.ChatColor;


public class CodeTagSerializer extends ManagerComponent implements JsonSerializer<CodeTag>
{
  // Constructor
  public CodeTagSerializer(Manager manager)
  {
    super(manager);
  }
  
  
  // Serialize a minecart code tag to a JSON element
  @Override
  public JsonElement serialize(CodeTag codeTag, Type type, JsonSerializationContext context)
  {
    if (codeTag == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("name", codeTag.getName() != null ? new JsonPrimitive(ChatColor.stripColor(codeTag.getName())) : JsonNull.INSTANCE);
    obj.add("url", codeTag.getUrl() != null ? new JsonPrimitive(codeTag.getUrl()) : JsonNull.INSTANCE);
    return obj;
  }
}

package dev.danae.gregorail.plugin.webhooks.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import java.lang.reflect.Type;


public class CodeSerializer extends ManagerComponent implements JsonSerializer<Code>
{
  // Constructor
  public CodeSerializer(Manager manager)
  {
    super(manager);
  }
  
  
  // Serialize a minecart to a JSON element
  @Override
  public JsonElement serialize(Code code, Type type, JsonSerializationContext context)
  {
     return !code.isEmpty() ? new JsonPrimitive(code.getId()) : JsonNull.INSTANCE;
  }
}

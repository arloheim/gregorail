package dev.danae.gregorail.model.minecart;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;


public class MinecartCodeDataType implements PersistentDataType<String, MinecartCode> 
{  
  // Static instance for the persistent data type
  public static final MinecartCodeDataType MINECART_CODE = new MinecartCodeDataType();
  
  
  // Return the primitive type of the data type
  @Override
  public Class<String> getPrimitiveType() 
  {
    return String.class;
  }

  // Return the complex type of the data type
  @Override
  public Class<MinecartCode> getComplexType() 
  {
    return MinecartCode.class;
  }

  // Convert a complex type to a primitive type
  @Override
  public String toPrimitive(MinecartCode complex, PersistentDataAdapterContext context) 
  {
    return complex.getId();
  }

  // Convert a primitive type to a complex type
  @Override
  public MinecartCode fromPrimitive(String primitive, PersistentDataAdapterContext context) 
  {
    return primitive != null ? MinecartCode.of(primitive) : MinecartCode.empty();
  }
}

package dev.danae.gregorail.model.minecart;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;


public class MinecartCodeDataType extends ManagerComponent implements PersistentDataType<String, MinecartCode> 
{  
  // Constructor
  public MinecartCodeDataType(Manager manager)
  {
    super(manager);
  }
  
  
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

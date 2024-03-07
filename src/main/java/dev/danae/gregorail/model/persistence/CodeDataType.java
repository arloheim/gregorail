package dev.danae.gregorail.model.persistence;

import dev.danae.gregorail.model.Code;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;


public class CodeDataType extends ManagerComponent implements PersistentDataType<String, Code> 
{  
  // Constructor
  public CodeDataType(Manager manager)
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
  public Class<Code> getComplexType() 
  {
    return Code.class;
  }

  // Convert a complex type to a primitive type
  @Override
  public String toPrimitive(Code complex, PersistentDataAdapterContext context) 
  {
    return complex.getId();
  }

  // Convert a primitive type to a complex type
  @Override
  public Code fromPrimitive(String primitive, PersistentDataAdapterContext context) 
  {
    return primitive != null ? Code.of(primitive) : Code.empty();
  }
}

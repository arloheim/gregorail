package dev.danae.gregorail.model.minecart.persistence;

import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.minecart.Minecart;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;


public class MinecartDataType extends ManagerComponent implements PersistentDataType<byte[], Minecart> 
{
  // Constructor
  public MinecartDataType(Manager manager)
  {
    super(manager);
  }
  
  
  // Return the primitive type of the data type
  @Override
  public Class<byte[]> getPrimitiveType() 
  {
    return byte[].class;
  }

  // Return the complex type of the data type
  @Override
  public Class<Minecart> getComplexType() 
  {
    return Minecart.class;
  }

  // Convert a complex type to a primitive type
  @Override
  public byte[] toPrimitive(Minecart complex, PersistentDataAdapterContext context) 
  {
    if (complex == null)
      return new byte[0];
    
    var uuid = complex.getUUID();
    
    var bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  // Convert a primitive type to a complex type
  @Override
  public Minecart fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) 
  {
    if (primitive.length == 0)
      return null;
    
    var bb = ByteBuffer.wrap(primitive);
    long firstLong = bb.getLong();
    long secondLong = bb.getLong();
    var uuid = new UUID(firstLong, secondLong);
    
    return this.getManager().createCart((RideableMinecart)Bukkit.getEntity(uuid));
  }
}

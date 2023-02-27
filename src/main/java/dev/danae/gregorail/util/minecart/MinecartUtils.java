package dev.danae.gregorail.util.minecart;

import dev.danae.gregorail.RailPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.persistence.PersistentDataType;


public class MinecartUtils
{
  // The default speed of a minecart in meters per tick
  public static final double defaultSpeed = 0.4;
  
  
  // Namespaced key for storing the code of a minecart
  private static final NamespacedKey codeKey = new NamespacedKey(RailPlugin.getInstance(), "minecart_code");
  
  
  // Get the code of a minecart
  public static Code getCode(RideableMinecart minecart)
  {
    if (minecart == null)
      throw new NullPointerException("minecart must not be null");
    
    var id = minecart.getPersistentDataContainer().get(codeKey, PersistentDataType.STRING);
    return id != null ? new Code(id) : null;
  }
  
  // Set the code of a minecart
  public static void setCode(RideableMinecart minecart, Code code)
  {
    if (minecart == null)
      throw new NullPointerException("minecart must not be null");
    
    if (code == null)
    {
      minecart.getPersistentDataContainer().remove(codeKey);
      
      minecart.setCustomNameVisible(false);
      minecart.setCustomName(null);
    }
    else
    {      
      minecart.getPersistentDataContainer().set(codeKey, PersistentDataType.STRING, code.getId());

      var displayName = CodeUtils.getDisplayName(code);
      minecart.setCustomNameVisible(true);
      minecart.setCustomName(displayName != null ? displayName : code.getId());
    }
  }
  
  // Return if the code of a minecart matches the query
  public static boolean matchCode(RideableMinecart minecart, Query query)
  {
    if (minecart == null)
      throw new NullPointerException("minecart must not be null");
    if (query == null)
      throw new NullPointerException("query must not be null");
    
    var code = getCode(minecart);
    if (code == null)
      return false;
    
    return query.matches(code);
  }
  
  
  // Get the speed multiplier of a minecart
  public static double getSpeedMultiplier(RideableMinecart minecart)
  {
    return minecart.getMaxSpeed() / defaultSpeed;
  }
  
  // Set the speed multiplier of a minecart
  public static void setSpeedMultiplier(RideableMinecart minecart, double speedMultiplier)
  {
    minecart.setMaxSpeed(speedMultiplier * defaultSpeed);
  }
  
  
  // Return the player that is riding the minecart, if any
  public static Player getRidingPlayer(RideableMinecart minecart)
  {
    return minecart.getPassengers().stream()
      .filter(e -> e instanceof Player)
      .map(e -> (Player)e)
      .findFirst()
      .orElse(null);
  }
}

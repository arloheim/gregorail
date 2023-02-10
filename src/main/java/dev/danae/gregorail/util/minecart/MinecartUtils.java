package dev.danae.gregorail.util.minecart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.location.LocationUtils;
import dev.danae.gregorail.util.query.Query;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.persistence.PersistentDataType;


public class MinecartUtils
{
  // Namespaced key for storing the code of a minecart
  public static final NamespacedKey codeKey = new NamespacedKey(RailPlugin.getInstance(), "minecart_code");
  
  // Pattern to validate the code of a minecart
  public static final Pattern codePattern = Pattern.compile("[a-z0-9_]+", Pattern.CASE_INSENSITIVE);
  
  
  // Get the code of a minecart
  public static String getCode(RideableMinecart minecart)
  {
    if (minecart == null)
      throw new NullPointerException("minecart must not be null");
    
    return minecart.getPersistentDataContainer().get(codeKey, PersistentDataType.STRING);
  }
  
  // Set the code of a minecart
  public static void setCode(RideableMinecart minecart, String code) throws InvalidMinecartCodeException
  {
    if (minecart == null)
      throw new NullPointerException("minecart must not be null");
    
    if (code == null)
      minecart.getPersistentDataContainer().remove(codeKey);
    else if (codePattern.matcher(code).matches())
      minecart.getPersistentDataContainer().set(codeKey, PersistentDataType.STRING, code);
    else
      throw new InvalidMinecartCodeException(String.format("Code \"%s\" is an invalid minecart code; codes may only contain alphanumeric characters and underscores", code));
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
  
  
  // Return the nearest minecart at the specified location
  public static RideableMinecart findMinecart(Location loc)
  {
    return LocationUtils.findNearestEntity(loc, RideableMinecart.class);
  }
  
  // Return the nearest minecart that matches the predicate at the specified location
  public static RideableMinecart findMinecart(Location loc, Predicate<RideableMinecart> predicate)
  {
    return LocationUtils.findNearestEntity(loc, RideableMinecart.class, predicate);
  }
  
  // Return the nearest minecart with the code at the specified location
  public static RideableMinecart findMinecartWithCode(Location loc, String code)
  {
    return findMinecart(loc, minecart -> getCode(minecart).equals(code));
  }
  
  // Return the nearest minecart whose code matches the query at the specified location
  public static RideableMinecart findMinecartWithCodeMatch(Location loc, Query query)
  {
    return findMinecart(loc, minecart -> matchCode(minecart, query));
  }
}

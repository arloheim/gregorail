package dev.danae.gregorail.util.minecart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.query.Query;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.persistence.PersistentDataType;


public class MinecartUtils
{
  // Namespaced key for storing the code of a minecart
  private static final NamespacedKey codeKey = new NamespacedKey(RailPlugin.getInstance(), "minecart_code");
  
  // Pattern to validate the code of a minecart
  private static final Pattern codePattern = Pattern.compile("[a-z0-9_]+", Pattern.CASE_INSENSITIVE);
  
  // Pattern to split multiple codes of a minecart
  private static final Pattern codeDelimiterPattern = Pattern.compile("\\|");
  
  
  // Check if a code is valid
  public static boolean isValidCode(String code)
  {
    return codePattern.matcher(code).matches();
  }
  
  // Assert that a code is valid
  public static void assertIsValidCode(String code) throws InvalidMinecartCodeException
  {
    if (!isValidCode(code))
      throw new InvalidMinecartCodeException(String.format("Code \"%s\" is an invalid minecart code; codes may only contain alphanumeric characters and underscores", code));
  }
  
  
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
    {
      minecart.getPersistentDataContainer().remove(codeKey);
      
      minecart.setCustomNameVisible(false);
      minecart.setCustomName(null);
    }
    else
    {
      assertIsValidCode(code);
      
      minecart.getPersistentDataContainer().set(codeKey, PersistentDataType.STRING, code);

      minecart.setCustomNameVisible(true);
      minecart.setCustomName(code);
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
  
  
  // Split a string containing possible multiple codes into a list
  public static List<String> splitCodes(String code)
  {
    return Arrays.asList(codeDelimiterPattern.split(code));
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

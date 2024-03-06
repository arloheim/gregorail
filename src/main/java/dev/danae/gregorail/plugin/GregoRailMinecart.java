package dev.danae.gregorail.plugin;

import dev.danae.gregorail.model.minecart.MinecartCode;
import dev.danae.gregorail.model.minecart.Minecart;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;


public class GregoRailMinecart extends GregoRailPluginComponent implements Minecart
{  
  // The default speed of a minecart in meters per tick
  public static final double DEFAULT_SPEED = 0.4;
  
  
  // The Bukkit minecart that is been backed by this cart
  private final RideableMinecart minecart;
  
  // Keys for storing properties of a minecart
  private final NamespacedKey codeKey;
  
  
  // Constructor
  public GregoRailMinecart(GregoRailPlugin plugin, RideableMinecart minecart)
  {
    super(plugin);
    
    this.minecart = minecart;
    
    this.codeKey = new NamespacedKey(plugin, "minecart_code");
  }
  
  
  // Return the identifier of the cart
  @Override
  public UUID getUUID()
  {
    return this.minecart.getUniqueId();
  }
  
  // Return the location of the cart
  @Override
  public Location getLocation()
  {
    return this.minecart.getLocation();
  }
  
  // Return the player that is riding the cart
  @Override
  public Player getPassenger()
  {
    return this.minecart.getPassengers().stream()
      .filter(e -> e instanceof Player)
      .map(e -> (Player)e)
      .findFirst()
      .orElse(null);
  }
  
  
  // Get the code of the cart
  @Override
  public MinecartCode getCode()
  {
    return this.minecart.getPersistentDataContainer().getOrDefault(this.codeKey, this.getManager().getMinecartCodeDataType(), MinecartCode.empty());
  }
  
  // Set the code of the cart
  @Override
  public void setCode(MinecartCode code)
  {    
    if (!code.isEmpty())
    {      
      this.minecart.getPersistentDataContainer().set(this.codeKey, this.getManager().getMinecartCodeDataType(), code);
      this.minecart.setCustomNameVisible(true);
      this.minecart.setCustomName(this.getManager().getDisplayName(code));
    }
    else
    {
      this.minecart.getPersistentDataContainer().remove(this.codeKey);
      this.minecart.setCustomNameVisible(false);
      this.minecart.setCustomName(null);
    }    
  }
  
  // Get the speed multiplier of the cart
  @Override
  public double getSpeedMultiplier()
  {
    return this.minecart.getMaxSpeed() / DEFAULT_SPEED;
  }
  
  // Set the speed multiplier of the cart
  @Override
  public void setSpeedMultiplier(double speedMultiplier)
  {
    this.minecart.setMaxSpeed(speedMultiplier * DEFAULT_SPEED);
  }
}

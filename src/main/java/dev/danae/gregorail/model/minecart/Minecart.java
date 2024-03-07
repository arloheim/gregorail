package dev.danae.gregorail.model.minecart;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface Minecart
{  
  // Return the identifier of the cart
  public UUID getId();
  
  // Return the location of the cart
  public Location getLocation();
  
  // Return the player that is riding the cart
  public Player getPassenger();
  
  // Get the code of the cart
  public MinecartCode getCode();
  
  // Set the code of the cart
  public void setCode(MinecartCode code);
  
  // Get the speed multiplier of the cart
  public double getSpeedMultiplier();
  
  // Set the speed multiplier of the cart
  public void setSpeedMultiplier(double speedMultiplier);
}

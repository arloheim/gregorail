package dev.danae.gregorail.model;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface Minecart
{
  // Return the identifier of the cart
  public UUID getId();

  // Return the custom name of the cart
  public Component getName();
  
  // Return the location of the cart
  public Location getLocation();
  
  // Return the player that is riding the cart
  public Player getPassenger();
  
  // Get the code of the cart
  public Code getCode();
  
  // Set the code of the cart
  public void setCode(Code code);
  
  // Get the speed multiplier of the cart
  public double getSpeedMultiplier();
  
  // Set the speed multiplier of the cart
  public void setSpeedMultiplier(double speedMultiplier);
}

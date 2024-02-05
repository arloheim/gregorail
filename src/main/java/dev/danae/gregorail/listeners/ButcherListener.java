package dev.danae.gregorail.listeners;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.location.Cuboid;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.persistence.PersistentDataType;


public final class ButcherListener implements Listener
{
  // Namespaced key for storing if an entity has been butchered
  public static final NamespacedKey butcheredKey = new NamespacedKey(RailPlugin.getInstance(), "entity_butchered");
  
  
  // The options for the butcher listener
  private final ButcherOptions options;
  
  
  // Constructor
  public ButcherListener(ButcherOptions options)
  {
    this.options = options;
  }
  
  
  // Event listener for when a vehicle moves
  @EventHandler
  public void onVehicleMoveEvent(VehicleMoveEvent e)
  {
    // Check if the listener is enabled
    if (!this.options.isEnabled())
      return;
    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if a player is riding the minecart
    var player = MinecartUtils.getRidingPlayer(cart);
    if (player == null)
      return;
    
    // Check if the minecart moved to a new block (to avoid spamming the event)
    if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())
      return;
    
    // Get all mobs around the minecart and kill them if they are eligible
    for (var entity : Cuboid.of(cart.getLocation(), this.options.getRadius()).findEntities(Mob.class, mob -> this.isEligibleToBeKilled(mob, player)))
      this.kill(entity, player);
  }
  
  // Event listener for when a vehicle collides with an entity
  @EventHandler
  public void onVehicleEntityCollision(VehicleEntityCollisionEvent e)
  {
    // Check if the listener is enabled
    if (!this.options.isEnabled())
      return;
    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if a player is riding the minecart
    var player = MinecartUtils.getRidingPlayer(cart);
    if (player == null)
      return;
    
    // Check if the collided entity is a mob and not a player
    if (!(e.getEntity() instanceof Mob mob))
      return;
    
    // Kill the mob if it is eligible
    if (this.isEligibleToBeKilled(mob, player))
      this.kill(mob, player);
    
    // Cancel the collision
    e.setCollisionCancelled(true);
  }
  
  // Event listener for when an entity dies
  @EventHandler
  public void onEntityDeath(EntityDeathEvent e)
  {
    if (e.getEntity().getPersistentDataContainer().getOrDefault(butcheredKey, PersistentDataType.BYTE, (byte)0) == 0)
      return;
    
    e.getDrops().clear();
    e.setDroppedExp(0);
  }
  
  
  // Check if the specified mob is eligible to be killed
  private boolean isEligibleToBeKilled(Mob mob, Player source)
  {
    if (mob instanceof Player)
      return false;
    if (this.options.getIgnoreEntitiesOfType().contains(mob.getType()))
      return false;
    if (this.options.isIgnoreNamedEntities() && mob.getCustomName() != null)
      return false;
    return true;
  }
  
  // Kill the specified mob
  private void kill(Mob entity, Player source)
  {
    if (entity.isDead())
      return;
    
    if (this.options.isLightningBoltEffect())
      entity.getWorld().strikeLightningEffect(entity.getLocation());
    
    if (this.options.isDisableItemDrops())
      entity.getPersistentDataContainer().set(butcheredKey, PersistentDataType.BYTE, (byte)1);
    
    entity.damage(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), source);
  }
}

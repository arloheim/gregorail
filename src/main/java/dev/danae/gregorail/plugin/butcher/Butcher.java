package dev.danae.gregorail.plugin.butcher;

import dev.danae.common.util.Cuboid;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.GregoRailPluginComponent;
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


public final class Butcher extends GregoRailPluginComponent implements Listener
{  
  // The options for the butcher manager
  private final ButcherOptions options;
  
  // Keys for storing properties on an entity
  private final NamespacedKey entityButcheredKey;
  
  
  // Constructor
  public Butcher(GregoRailPlugin plugin, ButcherOptions options)
  {    
    super(plugin);
    
    this.options = options;
    
    this.entityButcheredKey = new NamespacedKey(plugin, "entity_butchered");
  }
  
  
  // Event listener for when a vehicle moves
  @EventHandler
  public void onVehicleMoveEvent(VehicleMoveEvent e)
  {
    // Check if the listener is enabled
    if (!this.options.isEnabled())
      return;
    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart minecart))
      return;
    
    // Check if a player is riding the minecart
    var cart = this.getManager().createCart(minecart);
    var player = cart.getPassenger();
    if (player == null)
      return;
    
    // Check if the minecart moved to a new block (to avoid spamming the event)
    if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())
      return;
    
    // Get all mobs around the minecart and kill them if they are eligible
    for (var entity : Cuboid.around(cart.getLocation(), this.options.getRadius()).findEntities(Mob.class, mob -> this.isEligibleToBeKilled(mob)))
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
    if (!(e.getVehicle() instanceof RideableMinecart minecart))
      return;
    
    // Check if a player is riding the minecart
    var cart = this.getManager().createCart(minecart);
    var player = cart.getPassenger();
    if (player == null)
      return;
    
    // Check if the collided entity is a mob and not a player
    if (!(e.getEntity() instanceof Mob mob))
      return;
    
    // Kill the mob if it is eligible
    if (this.isEligibleToBeKilled(mob))
      this.kill(mob, player);
    
    // Cancel the collision
    e.setCancelled(true);
  }
  
  // Event listener for when an entity dies
  @EventHandler
  public void onEntityDeath(EntityDeathEvent e)
  {
    if (e.getEntity().getPersistentDataContainer().getOrDefault(this.entityButcheredKey, PersistentDataType.BYTE, (byte)0) == 0)
      return;
    
    e.getDrops().clear();
    e.setDroppedExp(0);
  }
  
  
  // Check if the specified mob is eligible to be killed
  private boolean isEligibleToBeKilled(Mob mob)
  {
    if (mob instanceof Player)
      return false;
    if (this.options.getIgnoreEntitiesOfType().contains(mob.getType()))
      return false;
    if (this.options.isIgnoreNamedEntities() && mob.customName() != null)
      return false;
    return true;
  }
  
  // Kill the specified mob
  private void kill(Mob mob, Player source)
  {
    if (mob.isDead())
      return;
    
    if (this.options.isLightningBoltEffect())
      mob.getWorld().strikeLightningEffect(mob.getLocation());
    
    if (this.options.isDisableItemDrops())
      mob.getPersistentDataContainer().set(this.entityButcheredKey, PersistentDataType.BYTE, (byte)1);
    
    mob.damage(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), source);
  }
}

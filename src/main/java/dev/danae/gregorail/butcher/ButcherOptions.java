package dev.danae.gregorail.butcher;

import java.util.EnumSet;
import org.bukkit.entity.EntityType;


public final class ButcherOptions
{
  // Should mobs within the specified radius of a player riding a minecart be killed?
  private boolean enabled = true;
  
  // The radius in which mobs will be killed
  private int radius = 5;
  
  // Should a lightning bolt effect be used when a mob is killed?
  private boolean lightningBoltEffect = true;
  
  // The entity types to ignore besides players
  private EnumSet<EntityType> ignoreEntitiesOfType = EnumSet.of(EntityType.ALLAY);
  
  // Should entities bearing a custom name be ignored?
  private boolean ignoreNamedEntities = true;
  
  // Should item drops of killed entities be disabled?
  private boolean disableItemDrops = true;
  
  
  // Return if mobs within the specified radius of a player riding a minecart should be killed
  public boolean isEnabled()
  {
    return this.enabled;
  }
  
  // Set if mobs within the specified radius of a player riding a minecart should be killed  
  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }
  
  // Return the radius in which mobs will be killed
  public int getRadius()
  {
    return this.radius;
  }
  
  // Set the radius in which mobs will be killed
  public void setRadius(int radius)
  {
    this.radius = radius;
  }
  
  // Return if a lightning bolt effect should be used when a mob is killed?
  public boolean isLightningBoltEffect()
  {
    return this.lightningBoltEffect;
  }
  
  // Set if a lightning bolt effect should be used when a mob is killed?
  public void setLightningBoltEffect(boolean lightningBoltEffect)
  {
    this.lightningBoltEffect = lightningBoltEffect;
  }
  
  // Return the entity types to ignore besides players
  public EnumSet<EntityType> getIgnoreEntitiesOfType()
  {
    return this.ignoreEntitiesOfType;
  }
  
  // Set the entity types to ignore besides players
  public void setIgnoreEntitiesOfType(EnumSet<EntityType> ignoreEntitiesOfType)
  {
    this.ignoreEntitiesOfType = ignoreEntitiesOfType;
  }
  
  // Return if entities bearing a custom name should be ignored
  public boolean isIgnoreNamedEntities()
  {
    return this.ignoreNamedEntities;
  }
  
  // Set if entities bearing a custom name should be ignored
  public void setIgnoreNamedEntities(boolean ignoreNamedEntities)
  {
    this.ignoreNamedEntities = ignoreNamedEntities;
  }
  
  // Return if item drops of killed entities should be disabled
  public boolean isDisableItemDrops()
  {
    return this.disableItemDrops;
  }
  
  // Set if item drops of killed entities should be disabled
  public void setDisableItemDrops(boolean disableItemDrops)
  {
    this.disableItemDrops = disableItemDrops;
  }
}

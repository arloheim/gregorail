package dev.danae.gregorail.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;


public final class Cuboid
{
  // The world of the cuboid
  private final World world;
  
  // The boundaries of the cuboid
  private final BoundingBox bounds;
  
  
  // Constructor
  private Cuboid(World world, BoundingBox bounds)
  {
    this.world = world;
    this.bounds = bounds;
  }
  
  
  // Return the world of the cuboid
  public World getWorld()
  {
    return this.world;
  }
  
  // Return the boundaries of the cuboid
  public BoundingBox getBounds()
  {
    return this.bounds;
  }
  
  // Return the corners of the cuboid
  public Location getMin()
  {
    return new Location(this.world, Math.floor(this.bounds.getMinX()), Math.floor(this.bounds.getMinY()), Math.floor(this.bounds.getMinZ()));
  }
  public Location getMax()
  {
    return new Location(this.world, Math.ceil(this.bounds.getMaxX()), Math.ceil(this.bounds.getMaxY()), Math.ceil(this.bounds.getMaxZ()));
  }
  
  // Return the center of the cuboid
  public Location getCenter()
  {
    return new Location(this.world, Math.round(this.bounds.getCenterX()), Math.round(this.bounds.getCenterY()), Math.round(this.bounds.getCenterZ()));
  }
  
  
  // Return an iterator over the locations in the cuboid
  public Collection<Location> locations()
  {    
    return this.locationsStream()
      .collect(Collectors.toCollection(ArrayList::new));
  }
  
  // Return an iterator over the blocks in the cuboid
  public Collection<Block> blocks()
  {    
    return this.locationsStream()
      .map(l -> l.getBlock())
      .collect(Collectors.toCollection(ArrayList::new));
  }
  
  
  // Find all entities that match the specified predicate in the cuboid
  public Collection<Entity> findEntities(Predicate<Entity> predicate)
  {
    return this.world.getNearbyEntities(this.bounds, predicate);
  }
  
  // Find all entities with the specified class in the cuboid
  public <T extends Entity> Collection<T> findEntities(Class<T> cls)
  {    
    return this.findEntities(e -> cls.isAssignableFrom(e.getClass())).stream()
      .map(e -> (T)e)
      .collect(Collectors.toCollection(ArrayList::new));
  }
  
  // Find an entity that matches the specified predicate in the cuboid that is nearest to the specified location
  public Entity findNearestEntityTo(Predicate<Entity> predicate, Location loc)
  {
    return this.findEntities(predicate).stream()
      .sorted(compareEntityDistanceTo(loc))
      .findFirst()
      .orElse(null);
  }
  
  // Find an entity with the specified class in the cuboid that is nearest to the specified location
  public <T extends Entity> T findNearestEntityTo(Class<T> cls, Location loc)
  {
    return this.findEntities(cls).stream()
      .sorted(compareEntityDistanceTo(loc))
      .findFirst()
      .orElse(null);
  }
  
  // Find an entity that matches the specified predicate in the cuboid that is nearest to the center
  public Entity findNearestEntityToCenter(Predicate<Entity> predicate)
  {
    return this.findNearestEntityTo(predicate, this.getCenter());
  }
  
  // Find an entity with the specified class in the cuboid that is nearest to the center
  public <T extends Entity> T findNearestEntityToCenter(Class<T> cls)
  {
    return this.findNearestEntityTo(cls, this.getCenter());
  }
  
  
  // Find all blocks with the specified material in the cuboid
  public Collection<Block> findBlocks(Material material)
  {
    if (!material.isBlock())
      throw new IllegalArgumentException("material must be a block material");
    
    var blockList = new ArrayList<Block>();
    for (var block : this.blocks())
    {
      if (block.getType() == material)
        blockList.add(block);
    }
    return blockList;
  }
  
  // Find a block with the specified material in the cuboid that is nearest to the specified location
  public Block findNearestBlockTo(Material material, Location loc)
  {
    return this.findBlocks(material).stream()
      .sorted(compareBlockDistanceTo(loc))
      .findFirst()
      .orElse(null);
  }
  
  // Find a block with the specified material in the cuboid that is nearest to the center of the cuboid
  public Block findNearestBlockToCenter(Material material)
  {
    return this.findNearestBlockTo(material, this.getCenter());
  }
  
  
  // Return a stream over the locations in the cuboid
  private Stream<Location> locationsStream()
  {    
    var min = this.getMin();
    var max = this.getMax();
    
    Stream.Builder<Location> builder = Stream.builder();
    for (var y = min.getBlockY(); y <= max.getBlockY(); y ++)
      for (var x = min.getBlockX(); x <= max.getBlockX(); x ++)
        for (var z = min.getBlockZ(); z <= max.getBlockZ(); z ++)
          builder.accept(new Location(this.world, x, y, z));
    return builder.build();
  }
  
  
  // Create a cuboid with the specified corners
  public static Cuboid of(Location min, Location max)
  {
    if (min == null)
      throw new IllegalArgumentException("min must not be null");
    if (max == null)
      throw new IllegalArgumentException("min must not be null");
    if (!Objects.equals(min.getWorld(), max.getWorld()))
      throw new IllegalArgumentException("min and max must be located in the same world");
    
    var world = min.getWorld();
    return new Cuboid(world, BoundingBox.of(world.getBlockAt(min), world.getBlockAt(max)));
  }
  
  // Create a cuboid with the specified center and radius
  public static Cuboid of(Location center, int radius)
  {
    if (center == null)
      throw new IllegalArgumentException("loc must not be null");
    if (radius < 0)
      throw new IllegalArgumentException("radius must not be smaller than zero");
    
    var world = center.getWorld();
    return new Cuboid(world, BoundingBox.of(world.getBlockAt(center)).expand(radius));
  }
  
  
  // Return a comparator that compares the distances of two locations to the specified location
  public static Comparator<Location> compareDistanceTo(Location loc)
  {
    return Comparator.comparingDouble(l -> l.distance(loc));
  }
  
  // Return a comparator that compares the distances of two entities to the specified location
  public static Comparator<Entity> compareEntityDistanceTo(Location loc)
  {
    return Comparator.comparing(e -> e.getLocation(), compareDistanceTo(loc));
  }
  
  // Return a comparator that compares the distances of two blocks to the specified location
  public static Comparator<Block> compareBlockDistanceTo(Location loc)
  {
    return Comparator.comparing(b -> b.getLocation(), compareDistanceTo(loc));
  }
}

package dev.danae.gregorail.util;

import java.util.List;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class InventoryUtils
{
  // Return the minimal inventory size for the specified size
  public static int minimalInventorySize(int size)
  {
    if (size < 0 || size > 54)
      throw new IllegalArgumentException("size must be between 0 and 54");
    
    while (size % 9 > 0) 
      size ++;
    return Math.max(9, size);
  }
  
  
  // Create an inventory that automatically calculates the correct size
  public static Inventory createInventory(InventoryHolder owner, int size, String title)
  {
    return Bukkit.createInventory(owner, minimalInventorySize(size), title);
  }
  public static Inventory createInventory(InventoryHolder owner, int size)
  {
    return Bukkit.createInventory(owner, minimalInventorySize(size));
  }
  
  // Create an inventory of the specified list of items
  public static Inventory createInventory(InventoryHolder owner, List<ItemStack> items, String title)
  {
    if (items.size() > 54)
      throw new IllegalArgumentException("items must contain at most 54 elements");
    
    var inventory = createInventory(owner, items.size(), title);
    for (var item : items)
      inventory.addItem(item);
    return inventory;
  }
  public static Inventory createInventory(InventoryHolder owner, List<ItemStack> items)
  {
    if (items.size() > 54)
      throw new IllegalArgumentException("items must contain at most 54 elements");
    
    var inventory = createInventory(owner, items.size());
    for (var item : items)
      inventory.addItem(item);
    return inventory;
  }
  
  // Create an inventory of the specified list of objects, converting them to item stacks
  public static <T> Inventory createInventory(InventoryHolder owner, List<T> items, Function<T, ItemStack> mapper, String title)
  {
    return createInventory(owner, items.stream().map(mapper).toList(), title);
  }
  public static <T> Inventory createInventory(InventoryHolder owner, List<T> items, Function<T, ItemStack> mapper)
  {
    return createInventory(owner, items.stream().map(mapper).toList());
  }
}

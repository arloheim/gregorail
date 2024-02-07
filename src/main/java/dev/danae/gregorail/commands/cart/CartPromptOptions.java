package dev.danae.gregorail.commands.cart;

import org.bukkit.Material;

public class CartPromptOptions
{
  // The title to use in the cart prompt command
  public String title = "Select a code";
  
  // The material to use for items in the cart prompt command
  public Material itemMaterial = Material.MINECART;
  
  
  // Return the title to use in the cart prompt command
  public String getTitle()
  {
    return this.title;
  }
  
  // Set the title to use in the cart prompt command
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  // Return the material to use for items in the cart prompt command
  public Material getItemMaterial()
  {
    return this.itemMaterial;
  }
  
  // Set the material to use for items in the cart prompt command
  public void setItemMaterial(Material itemMaterial)
  {
    this.itemMaterial = itemMaterial;
  }
}

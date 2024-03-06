package dev.danae.gregorail.plugin;

import dev.danae.gregorail.plugin.webhooks.Webhook;
import java.util.LinkedList;
import java.util.List;


public class GregoRailPluginOptions
{
  // The radius in blocks to search for blocks while parsing a location
  private int blockSearchRadius = 10;
  
  // The distance in blocks to search for carts while setting or querying codes
  private int cartSearchDistance = 10;
  
  // Are webhooks enabled in the plugin
  public boolean webhooksEnabled = true;
  
  // The list of registered webhooks
  private final List<Webhook> webhooks = new LinkedList<>();
  
  
  // Return the radius in blocks to search for blocks while parsing a location
  public int getBlockSearchRadius()
  {
    return this.blockSearchRadius;
  }
  
  // Set the radius in blocks to search for blocks while parsing a location
  public void setBlockSearchRadius(int blockSearchRadius)
  {
    this.blockSearchRadius = blockSearchRadius;
  }
  
  // Return the distance in blocks to search for carts while setting or querying codes
  public int getCartSearchDistance()
  {
    return this.cartSearchDistance;
  }
  
  // Set the distance in blocks to search for carts while setting or querying codes
  public void setCartSearchDistance(int cartSearchDistance)
  {
    this.cartSearchDistance = cartSearchDistance;
  }
  
  // Return if webhooks are enabled in the plugin
  public boolean isWebhooksEnabled()
  {
    return this.webhooksEnabled;
  }
  
  // Set if webhooks are enabled in the plugin
  public void setWebhooksEnabled(boolean webhooksEnabled)
  {
    this.webhooksEnabled = webhooksEnabled;
  }
  
  // Return the registered webhooks
  public List<Webhook> getWebhooks()
  {
    return this.webhooks;
  }
}

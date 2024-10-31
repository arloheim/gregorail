package dev.danae.gregorail.plugin.webhooks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.danae.gregorail.plugin.GregoRailPlugin;
import dev.danae.gregorail.plugin.GregoRailPluginComponent;
import org.bukkit.Bukkit;


public final class Webhook extends GregoRailPluginComponent
{    
  // The name of the webhook
  private final String name;
  
  // The type of the webhook
  private final EnumSet<WebhookType> type;
  
  // The URL of the webhook
  private final URL url;
  
  
  // Constructor
  public Webhook(GregoRailPlugin plugin, String name, EnumSet<WebhookType> type, URL url)
  {    
    super(plugin);
    
    this.name = name;
    this.type = type;
    this.url = url;
  }
  
  
  // Return the name of the webhook
  public String getName()
  {
    return this.name;
  }
  
  // Return the type of the webhook
  public EnumSet<WebhookType> getType()
  {
    return this.type;
  }
  
  // Return the URL of the webhook
  public URL getUrl()
  {
    return this.url;
  }
  
  
  // Execute the webhook with the specified payload
  public final void execute(WebhookType type, JsonObject payload) throws WebhookException
  {
    try 
    {
      // Check if the webhook contains the type
      if (!this.type.contains(type))
        return;
      
      // Add additional data to the payload
      payload.add("type", new JsonPrimitive(type.toString().toLowerCase()));
      
      // Create the connection
      var connection = (HttpURLConnection)this.url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("User-Agent", String.format("%s/%s (%s)", this.getPlugin().getPluginMeta().getName(), this.getPlugin().getPluginMeta().getVersion(), Bukkit.getVersion()));
      connection.setDoOutput(true);
      
      // Write the payload
      try (var stream = connection.getOutputStream())
      {
        stream.write(payload.toString().getBytes("utf-8"));
      }
      
      // Handle the response
      var responseCode = connection.getResponseCode();
      if (responseCode < 200 || responseCode >= 300)
        throw new WebhookException(String.format("The server responded with status %d %s", responseCode, connection.getResponseMessage())); 
    }
    catch (IOException ex)
    {
      throw new WebhookException(ex.getMessage(), ex);
    }
  }
}

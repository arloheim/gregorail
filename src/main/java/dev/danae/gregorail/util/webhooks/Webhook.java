package dev.danae.gregorail.util.webhooks;

import com.google.gson.JsonElement;
import dev.danae.gregorail.RailPlugin;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumSet;
import java.util.logging.Level;
import org.bukkit.Bukkit;


public final class Webhook
{    
  // The name of the webhook
  private final String name;
  
  // The type of the webhook
  private final EnumSet<WebhookType> type;
  
  // The URL of the webhook
  private final URL url;
  
  
  // Constructor
  public Webhook(String name, EnumSet<WebhookType> type, URL url)
  {    
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
  public final void execute(JsonElement payload)
  {
    try 
    {
      // Create the connection
      var connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("User-Agent", String.format("%s/%s (%s)", RailPlugin.getInstance().getDescription().getName(), RailPlugin.getInstance().getDescription().getVersion(), Bukkit.getVersion()));
      connection.setDoOutput(true);
      
      // Handle the payload
      try (var stream = connection.getOutputStream())
      {
        stream.write(payload.toString().getBytes("utf-8"));
      }
      
      // Handle the response
      var responseCode = connection.getResponseCode();
      if (responseCode < 200 || responseCode >= 300)
        RailPlugin.getInstance().getLogger().log(Level.WARNING, "Could not execute webhook {0}: {1} {2}", new Object[] {this.name, responseCode, connection.getResponseMessage()}); 
    }
    catch (IOException ex)
    {
      RailPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not execute webhook {0}: {1}", new Object[] {this.name, ex.getMessage()}); 
    }
  }
}

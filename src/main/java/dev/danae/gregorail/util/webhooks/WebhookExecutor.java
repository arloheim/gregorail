package dev.danae.gregorail.util.webhooks;

import com.google.gson.JsonObject;


public interface WebhookExecutor
{
  // Execute a webhook witht the specified type and payload
  public void executeWebhook(WebhookType type, JsonObject payload);
}

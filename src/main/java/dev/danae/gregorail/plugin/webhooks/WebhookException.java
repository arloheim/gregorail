package dev.danae.gregorail.plugin.webhooks;


public class WebhookException extends Exception
{
  // Constructor
  public WebhookException(String message)
  {
    super(message);
  }
  public WebhookException(String message, Throwable cause)
  {
    super(message, cause);
  }
}

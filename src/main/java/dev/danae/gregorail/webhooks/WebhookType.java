package dev.danae.gregorail.webhooks;


public enum WebhookType
{
  // The code of a cart was set due to a /gcart command
  CART_CODE_SET,
  
  // The code of a cart was cleared due to a /gcart command
  CART_CODE_CLEARED,
  
  // The speed of a cart was changed due to a /gcart command
  CART_SPEED_CHANGED,
  
  // A block was changed due to a /grail command
  BLOCK_CHANGED,
  
  // A switch was changed due to a /grail command
  SWITCH_CHANGED,
  
  // A player entered a cart with a code
  PLAYER_ENTERED_CART,
  
  // A player exited a cart with a code
  PLAYER_EXITED_CART,
  
  // A cart with a code moved
  CART_MOVED,
  
  // A cart with a code is destroyed
  CART_DESTROYED,
}

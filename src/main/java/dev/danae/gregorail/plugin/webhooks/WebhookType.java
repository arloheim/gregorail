package dev.danae.gregorail.plugin.webhooks;


public enum WebhookType
{
  // The code of a cart has been changed
  CART_CODE_CHANGED,
  
  // The speed multiplier of a cart has been changed
  CART_SPEED_MULTIPLIER_CHANGED,
  
  // The shape of a block has been changed
  BLOCK_SHAPE_CHANGED,
  
  // The material of a block has been changed
  BLOCK_MATERIAL_CHANGED,
  
  // A sound is played
  SOUND_PLAYED,
  
  // A player entered a cart with a code
  CART_ENTERED,
  
  // A player exited a cart with a code
  CART_EXITED,
  
  // A cart with a code moved
  CART_MOVED,
  
  // A cart with a code has been destroyed
  CART_DESTROYED,
}

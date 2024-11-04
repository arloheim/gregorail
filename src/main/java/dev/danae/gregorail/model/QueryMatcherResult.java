package dev.danae.gregorail.model;


public class QueryMatcherResult<T>
{
  // The value of the result
  private final T value;
    
  // The detected cart of the result
  private final Minecart cart;
    
    
  // Constructor
  public QueryMatcherResult(T value, Minecart cart)
  {
    this.value = value;
    this.cart = cart;
  }
    
    
  // Return the value of the result
  public final T getValue()
  {
    return this.value;
  }
    
  // Return the detected cart of the result
  public final Minecart getCart()
  {
    return this.cart;
  }

  // Return the code of the detected cart
  public final Code getCartCode()
  {
    return this.cart != null ? cart.getCode() : Code.empty();
  }
}

package dev.danae.gregorail.util.minecart;


@FunctionalInterface
public interface Query
{
  // Match the query against a code
  public boolean matches(Code code);
}

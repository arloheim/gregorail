package dev.danae.gregorail.util.query;


@FunctionalInterface
public interface Query
{
  // Match the query against a string
  public boolean matches(String input);
}

package dev.danae.gregorail.plugin.commands;

import java.util.function.Supplier;
import dev.danae.common.commands.arguments.ArgumentException;
import dev.danae.common.commands.arguments.ArgumentType;
import dev.danae.common.commands.arguments.Scanner;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.Minecart;
import dev.danae.gregorail.model.QueryMatcherResult;


public abstract class ManagerQueryCommand extends ManagerCommand
{  
  // The query type of the command
  private final ManagerQueryCommandType type;
  
  
  // Constructor
  public ManagerQueryCommand(Manager manager, ManagerQueryCommandType type, String... permissions)
  {
    super(manager, permissions);
    
    this.type = type;
  }
  
  // Constructor without permissions
  public ManagerQueryCommand(Manager manager, ManagerQueryCommandType type)
  {
    super(manager);
    
    this.type = type;
  }
  
  
  // Return the query type of the command
  public ManagerQueryCommandType getType()
  {
    return this.type;
  }
  
  
  // Evaluate a query read from the scanner with the specified cart function
  protected QueryMatcherResult<Boolean> matchQuery(Scanner scanner, Supplier<Minecart> cartFunction) throws ArgumentException
  {
    if (this.getType() == ManagerQueryCommandType.CONDITIONAL)
    {
      var query = scanner.nextQuery();
      var cart = cartFunction.get();
      return new QueryMatcherResult<>(query.match(cart), cart);
    }
    else
    {
      
      var cart = cartFunction.get();
      return new QueryMatcherResult<>(true, cart);
    }
  }
  
  // Evaluate a query matcher read from the scanner with the specified cart function
  protected <T> QueryMatcherResult<T> matchQueryMatcher(Scanner scanner, ArgumentType<T> resultType, Supplier<Minecart> cartFunction) throws ArgumentException
  {
    if (this.getType() == ManagerQueryCommandType.CONDITIONAL)
    {
      var matcher = scanner.wrapInMatcher(resultFunction);
      var cart = cartFunction.get();
      return matcher.match(cart);
    }
    else
    {
      var value = resultFunction.get();
      var cart = cartFunction.get();
      return new QueryMatcherResult<>(value, cart);
    }
  }
}

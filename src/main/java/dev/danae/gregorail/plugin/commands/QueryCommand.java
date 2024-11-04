package dev.danae.gregorail.plugin.commands;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import dev.danae.common.commands.CommandContext;
import dev.danae.common.commands.arguments.ArgumentException;
import dev.danae.common.commands.arguments.Scanner;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.Minecart;
import dev.danae.gregorail.model.QueryMatcherResult;


public abstract class QueryCommand extends ManagerCommand
{
  // The query type of the command
  private final QueryType type;
  
  
  // Constructor
  public QueryCommand(Manager manager, QueryType type, String... permissions)
  {
    super(manager, permissions);
    
    this.type = type;
  }
  
  // Constructor without permissions
  public QueryCommand(Manager manager, QueryType type)
  {
    super(manager);
    
    this.type = type;
  }
  
  
  // Return the query type of the command
  public QueryType getType()
  {
    return this.type;
  }
  
  
  // Evaluate a query read from the scanner with the specified cart function
  protected QueryMatcherResult<Boolean> matchQuery(Scanner scanner, Supplier<Minecart> cartFunction) throws ArgumentException
  {
    var cart = cartFunction.get();
    if (this.getType() == QueryType.CONDITIONAL)
    {
      var query = this.getManager().getQueryArgumentType().parse(scanner);
      return new QueryMatcherResult<>(query.match(cart), cart);
    }
    else
    {
      return new QueryMatcherResult<>(true, cart);
    }
  }


  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    switch (this.getType())
    {
      case ALWAYS:
      {
        if (context.hasAtLeastArgumentsCount(1))
          return this.suggestAfterQuery(context, 0).toList();
        else
          return List.of();
      }
        
      case CONDITIONAL:
      {
        if (context.hasAtLeastArgumentsCount(2))
          return this.suggestAfterQuery(context, 1).toList();
        else if (context.hasArgumentsCount(1))
        return this.getManager().getCodeListArgumentType().suggestFromString(context.getArgument(0)).toList();
        else
          return List.of();
      }
        
      default:
        return List.of();
    }
  }

  // Return suggestions for the specified command context and argument after the query arguments
  public Stream<String> suggestAfterQuery(CommandContext context, int argumentIndex)
  {
    if (this.getType() == QueryType.CONDITIONAL)
      return Stream.of("||");
    else
      return Stream.empty();
  }
}

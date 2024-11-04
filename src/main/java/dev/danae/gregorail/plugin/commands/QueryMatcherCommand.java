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
import dev.danae.gregorail.model.arguments.QueryMatcherArgumentType;


public abstract class QueryMatcherCommand<T> extends ManagerCommand
{
  // The query type of the command
  private final QueryType type;

  // Argument types
  protected final QueryMatcherArgumentType<T> queryMatcherArgumentType;
  
  
  // Constructor
  public QueryMatcherCommand(Manager manager, QueryType type, QueryMatcherArgumentType<T> queryMatcherArgumentType, String... permissions)
  {
    super(manager, permissions);

    this.type = type;
    this.queryMatcherArgumentType = queryMatcherArgumentType;
  }
  
  // Constructor without permissions
  public QueryMatcherCommand(Manager manager, QueryType type, QueryMatcherArgumentType<T> queryMatcherArgumentType)
  {
    super(manager);
    
    this.type = type;
    this.queryMatcherArgumentType = queryMatcherArgumentType;
  }
  
  
  // Return the query type of the command
  public QueryType getType()
  {
    return this.type;
  }
  
  
  // Evaluate a query matcher read from the scanner with the specified cart function
  protected QueryMatcherResult<T> matchQueryMatcher(Scanner scanner, Supplier<Minecart> cartFunction) throws ArgumentException
  {
    var cart = cartFunction.get();
    if (this.getType() == QueryType.CONDITIONAL)
    {
      var matcher = this.queryMatcherArgumentType.parse(scanner);
      return matcher.match(cart);
    }
    else
    {
      var value = this.queryMatcherArgumentType.getResultArgumentType().parse(scanner);
      return new QueryMatcherResult<>(value, cart);
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
        if (context.hasAtLeastArgumentsCount(2))
          return this.suggestAfterQueryMatcher(context, 1).toList();
        else if (context.hasArgumentsCount(1))
          return this.queryMatcherArgumentType.getResultArgumentType().suggestFromString(context.getArgument(0)).toList();
        else
          return null;
      }
        
      case CONDITIONAL:
      {
        var separatorIndex = context.findLastArgumentIndex("||");
        if (separatorIndex == 2)
          return this.queryMatcherArgumentType.getResultArgumentType().suggestFromString(context.getLastArgument(0)).toList();
        else if (separatorIndex == 1)
          return this.queryMatcherArgumentType.getQueryArgumentType().suggestFromString(context.getLastArgument(0)).toList();
        else if (context.hasAtLeastArgumentsCount(3))
          return this.suggestAfterQueryMatcher(context, separatorIndex >= 0 ? context.getArgumentsCount() - separatorIndex + 2 : 2).toList();
        else if (context.hasArgumentsCount(2))
          return this.queryMatcherArgumentType.getResultArgumentType().suggestFromString(context.getArgument(1)).toList();
        else if (context.hasArgumentsCount(1))
          return this.queryMatcherArgumentType.getQueryArgumentType().suggestFromString(context.getArgument(0)).toList();
        else
          return List.of();
      }
        
      default:
        return List.of();
    }
  }

  // Return suggestions for the specified command context and argument after the query matcher arguments
  public Stream<String> suggestAfterQueryMatcher(CommandContext context, int argumentIndex)
  {
    if (this.getType() == QueryType.CONDITIONAL)
      return Stream.of("||");
    else
      return Stream.empty();
  }
}

package dev.danae.gregorail.plugin.commands;

import dev.danae.common.commands.Command;
import dev.danae.gregorail.model.Manager;


public abstract class ManagerCommand extends Command
{    
  // The manager of the command
  private final Manager manager;
  
  
  // Constructor
  public ManagerCommand(Manager manager, String... permissions)
  {
    super(permissions);
    
    this.manager = manager;
  }
  
  // Constructor without permissions
  public ManagerCommand(Manager manager)
  {    
    this.manager = manager;
  }
  
  
  // Return the manager of the command
  protected Manager getManager()
  {
    return this.manager;
  }
}

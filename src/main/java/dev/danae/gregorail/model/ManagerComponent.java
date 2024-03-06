package dev.danae.gregorail.model;


public abstract class ManagerComponent
{
  // The manager of the component
  private final Manager manager;
  
  
  // Constructor
  public ManagerComponent(Manager manager)
  {
    this.manager = manager;
  }
  
  
  // Return the manager of the component
  protected Manager getManager()
  {
    return this.manager;
  }
}

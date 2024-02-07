package dev.danae.gregorail.commands.cart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.InventoryUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.minecart.CodeUtils;
import dev.danae.gregorail.util.minecart.InvalidCodeException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;


public class CartPromptSetCommand extends CartCommand
{  
  // Namespaced key for storing the code of a minecart on an item stack
  private static final NamespacedKey codeKey = new NamespacedKey(RailPlugin.getInstance(), "cart_promptset_code");
  
  // Namespaced key for storing the location of a minecart on an item stack
  private static final NamespacedKey locationKey = new NamespacedKey(RailPlugin.getInstance(), "cart_promptset_location");
  
  
  // The options for the cart prompt
  private final CartPromptOptions options;
  
  
  // Constructor
  public CartPromptSetCommand(CartPromptOptions options)
  {
    super("gregorail.cart.promptset");
    
    this.options = options;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {
      // Assert that the command sender has a location
      context.assertSenderHasLocation();
      
      // Assert the the command sender is a player, or get the nearest player to the sender
      var player = context.nearestPlayerOrSender();
      if (player == null)
        throw new CommandException("No player found");
      
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(1))
        throw new CommandUsageException();
      
      var codes = CodeUtils.createCodes(context.getArgument(0));
      
      var locationString = context.getJoinedArguments(1);
      
      // Create the inventory
      var inventory = InventoryUtils.createInventory(player, codes, code -> {
        var itemStack = new ItemStack(this.options.getItemMaterial());
        var itemMeta = itemStack.getItemMeta();
      
        var displayName = CodeUtils.getDisplayName(code);
        itemMeta.getPersistentDataContainer().set(codeKey, PersistentDataType.STRING, code.getId());
        itemMeta.setDisplayName(displayName != null ? displayName : code.getId());
        
        itemMeta.getPersistentDataContainer().set(locationKey, PersistentDataType.STRING, locationString);
        
        itemStack.setItemMeta(itemMeta);
        return itemStack;
      }, this.options.getTitle());
      
      // Open the inventory
      player.openInventory(inventory);
    }
    catch (InvalidCodeException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.hasAtLeastArgumentsCount(2))
      return CommandUtils.handleLocationTabCompletion(context, 1);
    else if (context.hasArgumentsCount(1))
      return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
    else
      return null;
  }
  
  
  // Event listener for when an inventory item is clicked
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e)
  { 
    // Check if the click type was a left click
    if (!e.getClick().isLeftClick())
      return;
      
    // Check if the sender is a player
    if (!(e.getWhoClicked() instanceof Player player))
      return;
      
    // Check if there is a current item of the item material
    if (e.getCurrentItem() == null || e.getCurrentItem().getType() != options.getItemMaterial())
      return;
      
    // Validate the item
    var itemMeta = e.getCurrentItem().getItemMeta();
    
    var code = itemMeta.getPersistentDataContainer().get(codeKey, PersistentDataType.STRING);
    if (code == null)
      return;
    
    var locationString = itemMeta.getPersistentDataContainer().get(locationKey, PersistentDataType.STRING);
      
    // Perform the /gcart set <code> command
    if (locationString != null && !locationString.isEmpty())
      Bukkit.dispatchCommand(player, String.format("gcart set %s %s", code, locationString));
    else
      Bukkit.dispatchCommand(player, String.format("gcart set %s", code));
    
    // Cancel the event and close the inventory
    e.setCancelled(true);
    Bukkit.getScheduler().runTask(RailPlugin.getInstance(), () -> e.getWhoClicked().closeInventory());
  }
}

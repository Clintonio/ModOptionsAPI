package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 


// Import everything, lazy.
import net.minecraft.src.modoptionsapi.*;
import java.util.Random;

/**
* ModOptionsAPI Example file for helping use the API
* This mod will randomly make dirt decay (all dirt)
* into either: air, or a TNT/Sapling item drop.
* 
* It is aimed to help teach mod builders to use the ModOptionsAPI
*
* @author	Clinton Alexander
* @version 	0.1
* @since 	0.1
*
* IF YOU INSTALL/ COMPILE THIS JAVA FILE I AM NOT
* RESPONSIBLE FOR YOUR WORLD BEING ABSOLUTELY RUINED
*/
public class BlockDirt extends Block
{

    protected BlockDirt(int i, int j)
    {
        super(i, j, Material.ground);
        setTickOnLoad(true);
		// This is the simple and complex interfaces
		initModOptionsAPIExample();
		//This is the super simple interface
		initModOptionsFileAPIExample();
		
		// Both can be used in unison
    }
	
	/**
	* This method will initialise our mod options API
	* settings code
	*
	* The point of this example mod is to make dirt randomly 
	* disappear. It will have these configurables:
	*
	* An On/Off toggle for the whole mod
	* An On/Off toggle for "item drop"
	* A slider to determine how often a block it to die
	* A submenu called "Item Drop Settings" containing
	* - A multi-text option to decide WHAT to drop
	* - A slider to decide how high to fire the entity
	*
	* I will allow only two options for the multi-text to save time
	*/
	private void initModOptionsAPIExample() {
		// This first section will use the "flat" API from the ModOptions class
		// This allows you to not deal with many classes and just focus on 
		// your code
		
		// First up, let's create our mod's options menu labelled "Dirt Decay"
		ModOptions mainMenu = new ModOptions("Dirt Decay");
		// Now, let's add the menu to the main store, you MUST do this:
		ModOptionsAPI.addMod(mainMenu);
		
		// This is the GUI controller for the main menu:
		ModOptionsGuiController gui = mainMenu.getGuiController();
		
		// We now want to add our "enable/disable" button
		mainMenu.addToggle("EnableMod");
		// Let's set the default to off, as it could destroy someone's
		// favourite world
		mainMenu.setOptionValue("EnableMod", false);
		
		// Now let's add item drop toggle
		mainMenu.addToggle("ItemDrop");
		
		// Now let's add a slider to determine how fast, on average, to die, in ticks
		// from 0 to 10
		mainMenu.addSlider("DecayRate", 0, 20);
		mainMenu.setOptionValue("DecayRate", 10);
		ModSliderOption fuckyoucunt = (ModSliderOption) mainMenu.getOption("DecayRate");
		
		// Let's make the DecayRate slider wide:
		mainMenu.setWideOption("DecayRate");
		
		// Let's also give it a suffix (text at the end)
		// This allows you to format the text in your own way.
		// You will be shown how to create your own formatter later
		mainMenu.setOptionStringFormat("DecayRate", new MOFormatters.SuffixFormat(" Suffix"));
		
		// And let's add a random multi text label to show you how they work:
		String[] values = new String[3];
		values[0] = "Label1";
		values[1] = "Label2";
		values[2] = "Label3";
		
		mainMenu.addMultiOption("TestMulti", values);
		
		// Let's add a multi option where the values and the text are different,
		// such as for a switch case usage, a practical usage will be shown later
		
		// Note that order matters
		String[] labels = {"Label 1", "Label 2", "Label 4", "Label 3"};
		// Note that you can use ANY integers, not just continuous ones
		Integer[] keys  = {0       ,  1       ,   2       ,  4       };
		mainMenu.addMappedMultiOption("MappedMultiOption", keys, labels);
		
		// At this point let's use the more Object Oriented approach for more
		// advanced/ picky users
		
		// Callback example, will be used on most settings from here on
		ExampleCallback callback = new ExampleCallback((ModBooleanOption) mainMenu.getOption("EnableMod"));
		
		// Create a new subMenu called "Drop Settings" with Ditty Decay as it's parent
		ModOptions subMenu = new ModOptions("Drop Settings");
		mainMenu.addSubOptions(subMenu);
		
		// This is the GUI controller for the sub menu
		ModOptionsGuiController subGui = subMenu.getGuiController();
		
		// Let's create a new multi-text option:
		ModMappedMultiOption multi = new ModMappedMultiOption("Drop Type");
		multi.addValue(46, "TNT"); // We'll make it spawn a little TNT entity randomly
		multi.addValue(6, "Sapling"); // Make it spawn a sapling
		multi.addValue(352, "Bone"); // Make it spawn a bone
		// We'll leave it at two for brevity's sake
		
		// Let's add a new handler for the display string that we create
		// (go to the bottom of this file)
		// This formatter will set the text of the selector to "DISABLED"
		// if "ItemDrop" is set to Off
		subGui.setFormatter(multi, new MOExampleDisabledFormatter("DISABLED"));
		multi.setCallback(callback);
		subGui.setWide(multi.getName());
		
		// Now add to the menu
		subMenu.addOption(multi);
		
		// We'll add a slider now:
		ModSliderOption slider = new ModSliderOption("Drop height", -10, 10);
		// Let's set it to 4 by default
		slider.setGlobalValue(slider.getFloatValue(4));
		// Add the callback to the slider, view bottom of this file for the class for
		// the callback
		slider.setCallback(callback);
		
		subMenu.addOption(slider);
		
		// Add a random boolean (just to show you how to create one with on/off labels)
		ModBooleanOption bool = new ModBooleanOption("Bool", "On Text", "Off Text");
		bool.setCallback(callback);
		
		subMenu.addOption(bool);
		
		// New in 0.4!
		// == LOCAL/ WORLD VALUES ==
		// The loading/ saving and management of world and
		subMenu.setSingleplayerMode(false);
		// It's that simple to enable or disable sub menus and mod menus
		// for world and server specific options. 
		// All properties from the global options such as values
		// and the like are the same.
		// If you want to show two different sets of values for the
		// different worlds, you'll need to do it with a formatter
		// or making two different sets of options.
		// When a menu is disabled for world specific options
		// it will default use the GLOBAL value.
		
		// == Defaults and Menus ==
		
		// IMPORTANT: You must now load your config values from the disk:
		// If none exist, this method will simply do nothing.
		// It will also load the values of ALL submenus
		mainMenu.loadValues();
		
		// Since we set some default values earlier, if the user had no previous
		// config, then you will want to save these defaults. If they already had values
		// then this will just re-save them. No harm done.
		// This will NOT save the values of all submenus.
		mainMenu.save();
		
		// So now let's save the submenu too
		subMenu.save();
		
		// Now, when your user presses escape, they'll see your info.
		// Proceed to the below method to see it in action:
	
	}
	
	/**
	* This is the super simple interface to the mod options api
	* It loads a structure from a .modoptions file to remove
	* the need for you to code anything.
	*
	* Essentially, you load the mod by name, and then it's accessable
	* through the usual commands, which will be exampled in this method too
	* 
	* To see the structure examples go to the file in examples called Test.modoptions
	*/
	private void initModOptionsFileAPIExample() {
		try {
			// It is named in a string, so it will be loaded from a file
			ModOptions mo = ModOptionsAPI.addMod("Test");
		
			// Let's just output a piece of text to the terminal:
			System.out.println("Forest Growth Value: " + mo.getOptionValue("ForestGrowth"));
		} catch (MOMissingModException e) {
			System.out.println(e.getMessage());
		}
	}
	

    public int tickRate()
    {
        return 20;
    }
	
	public void updateTick(World world, int x, int y, int z, Random random) {
		// We're going to test every tick for a magical explosion of dirt
		// Let's get our options: 
		// (You can store your options directly in your classes if you want to.
		// The retrieving from the store is just to remove the need to store in classes)
		ModOptions options = ModOptionsAPI.getModOptions("Dirt Decay");
		
		// Now let's get the drop settings options too
		ModOptions dropOptions = options.getSubOption("Drop Settings");
		
		// As above, we will first use the "flat"/ less object oriented approach:
		if(Boolean.valueOf(options.getOptionValue("EnableMod"))) {
			// Let's get our random value from the slider:
			int decayRate = Integer.parseInt(options.getOptionValue("DecayRate"));
			
			// Decay rate ranges from 0 to 10, so it's a max of 1/10 chance
			// of decaying
			if(random.nextInt(100) < decayRate) {
				if(Boolean.valueOf(options.getOptionValue("ItemDrop"))) {
					// We will now return to an object oriented style for obtaining 
					// values:
					// Here is the slider usage, and the boolean too:
					ModSliderOption height = (ModSliderOption) dropOptions.getOption("Drop height");
					float f = height.getValue(); // Returns a float from 0F to 1F, avoid using, this is
					// the internal representation of the value
					int i2 = height.getIntValue(); // Returns the integer value of the slider, please
					// use this, it will be from the low to high value you specify (or 0 to 100 if you 
					// did not specify a high and low in the constructor)
					// New height to drop at
					int y2 = y + i2;
					
					ModMappedMultiOption item = (ModMappedMultiOption) dropOptions.getOption("Drop Type");
					if(item.getValue() == 46) {
					
						EntityItem entityitem = new EntityItem(world, x, y2, z, 
															   new ItemStack(Block.tnt));
						world.entityJoinedWorld(entityitem);
					} else if(item.getValue() == Block.sapling.blockID) {
						EntityItem entityitem = new EntityItem(world, x, y2, z, 
															   new ItemStack(Block.sapling));
						world.entityJoinedWorld(entityitem);
					} else if(item.getValue() == 352) {
						EntityItem entityitem = new EntityItem(world, x, y2, z, 
															   new ItemStack(Item.bone));
						world.entityJoinedWorld(entityitem);
					} else {
					}
					
					
					// Boolean:
					ModBooleanOption bool = (ModBooleanOption) dropOptions.getOption("Bool");
					boolean b = bool.getValue();
				}
				world.setBlockWithNotify(x, y, z, 0);
			}
		} else {
			// Mod is disabled. 
		}
	}
	
}

/**
* Will set the string to DISABLED if ItemDrop is off
*
* This is for the purpose of demonstration of only, practically
* If you want to disable an option under this API based on another
* there will be a better way to do it (currently there is none).
* This could be extended in infinite possible ways and gives you
* absolute control over the format. Using the constructor, this
* behaves as a completely normal class
*/
class MOExampleDisabledFormatter implements MODisplayString {
	String disabledText;
	
	/**
	* Showing that we can use this in a normal way
	*/
	public MOExampleDisabledFormatter(String disabledText) {
		this.disabledText = disabledText;
	}
	
	/**
	* This class will be called upon update and generation
	* 
	* @param	name	Name of Option being edited
	* @param	value	String representation of option
	* @return	Formatted text
	*/
	public String manipulate(String name, String value) {
		ModOptions mo = ModOptionsAPI.getModOptions("Dirt Decay");
		ModBooleanOption enabled = (ModBooleanOption) mo.getOption("ItemDrop");
		ModBooleanOption enabled2 = (ModBooleanOption) mo.getOption("EnableMod");
		
		// If enabled, use the normal pattern:
		if((enabled.getValue()) && (enabled2.getValue())) {
			return name + ": " + value;
		} else {
			return disabledText;
		}
	}
}

/**
* This is the callback example. Will lock the value of the drop rate slider 
*
* @author	Clinton Alexander
*/
class ExampleCallback extends MOCallback {
	private ModBooleanOption enabled;
	
	/**
	* Constructor
	*
	* @param	enabled		Option determining enabled status
	*/
	public ExampleCallback(ModBooleanOption enabled) {
		this.enabled = enabled;
	}
	
	/**
	* What to do upon clicking a button. This method in particular
	* will cancel the click if the mod is not enabled
	*
	* @param	option		The option being clicked
	* @return	True if to accept the click or to cancel
	*/
	public boolean onClick(ModOption option) {
		if(!enabled.getValue()) {
			return false;
		}
		
		return true;
	}
	
	/**
	* What to do upon setting a global value
	*
	* @param	newValue	New value of global setting
	* @param	option		Option being set
	* @return	True if to accept the change
	*/
	public boolean onGlobalChange(boolean newValue, ModOption option) {
		return true;
	}
}
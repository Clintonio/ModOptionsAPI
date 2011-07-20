package modoptionsapi;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import net.minecraft.client.Minecraft;

/**
* An abstract representation of a set of options for a single mod
*
* @author	Clinton Alexander
* @version	0.1	
* @since	0.1
*/
public class ModOptions {
	/**
	* Map of option selectors
	*/ 
	private LinkedHashMap<String, ModOption> options = new LinkedHashMap<String, ModOption>();
	/**
	* Map of sub option menus for this menu
	*/
	private LinkedHashMap<String, ModOptions> subOptions = new LinkedHashMap<String, ModOptions>();
	/**
	* Display details
	*/
	private ModOptionsGuiController gui = new ModOptionsGuiController(this);
	
	/**
	* Name of this menu
	*/
	private String name;
	/**
	* Parent of this set of options (null implies it's a mod package)
	*/
	private ModOptions	parent = null;
	/**
	* Whether this set of options can have server specific options
	* @note If false, all children will act as false
	*/
	private boolean multiplayer = false;
	/**
	* Whether this set of options can have single player world options
	* @note If set false, all children will act as false
	*/
	private boolean singleplayer = true;
	
	/**
	* Create a new set of options with no parent. A mod package
	*
	* @note		DO NOT USE : in the name
	* @param	name	Name of option list (menu name)
	*/
	public ModOptions(String name) {
		this.name = name;
	}
	
	/**
	* Create a sub options menu for a mod package
	*
	* @param	name	Name of this option list
	* @param	p		Parent of this option list
	*/
	public ModOptions(String name, ModOptions p) {
		this.name = name;
		parent = p;
	}
	
	//=========================
	// Adding Options Methods
	//=========================
	
	/**
	* Add a manually created option to this menu
	*
	* @param	option		Option selector to add
	* @return	Returns the option just added for further operations
	*/
	public ModOption addOption(ModOption option) {
		options.put(option.getName(), option);
		return option;
	}
	
	/**
	* Add a text option with infinite max length
	*
	* @param	name	Name of text input
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name) {
		ModTextOption option = new ModTextOption(name);
		return addOption(option);
	}	
	
	/**
	* Add a text option with infinite max length
	* with a default value
	* 
	* @param	name	Name of text input
	* @param	value	Default value of input
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, String value) {
		ModTextOption option = new ModTextOption(name);
		option.setGlobalValue(value);
		return addOption(option);
	}
	
	/**
	* Add a text option with infinite max length
	* 
	* @param	name	Name of text input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, int maxlen) {
		ModTextOption option = new ModTextOption(name, maxlen);
		return addOption(option);
	}
	
	/**
	* Add a text option with infinite max length with a default
	* value
	* 
	* @param	name	Name of text input
	* @param	value	Default value for input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, String value, int maxlen) {
		ModTextOption option = new ModTextOption(name, maxlen);
		option.setGlobalValue(value);
		return addOption(option);
	}
	
	/**
	* Add a text option with infinite max length with a default
	* value
	* 
	* @param	name	Name of text input
	* @param	value	Default value for input
	* @param	maxlen	Maximum length the user can input. 0 or less is infinite
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addTextOption(String name, String value, Integer maxlen) {
		return addTextOption(name, value, (int) maxlen);
	}
	
	/**
	* Adds a character binding which is unique and will not class with any
	* other character binding. This default will not set a binding. If 
	* a clash occurs during loading of your player's saved preferences
	* then the later of the two mods will have their bindings undone.
	* Please use this method and avoid setting a default vaue
	*
	* @param	name	Name of character binding
	* @return	Returns the option just added for further operations
	* @since	0.7
	*/
	public ModOption addKeyBinding(String name) {
		ModKeyOption option = new ModKeyOption(name);
		return addOption(option);
	}
	
	/**
	* Add a multiple selector
	*
	* @param	name	Name of selector
	* @param	values	Set of values to display
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMultiOption(String name, String[] values) {
		ModMultiOption option = new ModMultiOption(name, values);
		return addOption(option);
	}
	
	/**
	* Adds a mapped multi option
	*
	* @throws 	IndexOutOfBoundsException
	* @param	name	Name of selector
	* @param	keys	Keys for selector
	* @param	values	Values for selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMappedMultiOption(String name, Integer[] keys, String[] values) 
		throws IndexOutOfBoundsException {
		if(keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			ModMappedMultiOption option = new ModMappedMultiOption(name);
			for(int x = 0; x < keys.length; x++) {
				option.addValue(keys[x], values[x]);
			}
			
			return addOption(option);
		}
	}
	
	/**
	* Adds a mapped multi option
	*
	* @throws 	IndexOutOfBoundsException
	* @param	name	Name of selector
	* @param	keys	Keys for selector
	* @param	values	Values for selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addMappedMultiOption(String name, int[] keys, String[] values) 
		throws IndexOutOfBoundsException {
		if(keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			ModMappedMultiOption option = new ModMappedMultiOption(name);
			for(int x = 0; x < keys.length; x++) {
				option.addValue(new Integer(keys[x]), values[x]);
			}
			
			return addOption(option);
		}
	}
	
	/**
	* Add a toggle/boolean selector
	*
	* @param	name		Name of boolean selector
	* @return	Returns the option just added for further operations
	*/
	public ModOption addToggle(String name) {
		ModBooleanOption option = new ModBooleanOption(name);
		return addOption(option);
	}
	
	/**
	* Add a numeric slider ranging from 0 to 100
	*
	* @param	name		Name of slider
	* @return	Returns the option just added for further operations
	*/
	public ModOption addSlider(String name) {
		ModSliderOption option = new ModSliderOption(name);
		return addOption(option);
	}
	
	/**
	* Add a numeric slider with a range
	*
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	* @return	Returns the option just added for further operations
	*/
	public ModOption addSlider(String name, int low, int high) {
		ModSliderOption option = new ModSliderOption(name, low, high);
		return addOption(option);
	}
	
	//=========================
	// SubOptions Methods
	//=========================
	
	/**
	* Add a sub menu of options
	*
	* @param	m		Set of sub-options
	*/
	public void addSubOptions(ModOptions m) {
		m.setParent(this);
		subOptions.put(m.getName(), m);
	}
	
	/**
	* Check if this mod options has a given sub options menu
	*
	* @param	name	Name of sub options
	* @return	true if sub options exist in this menu
	*/
	public boolean containsSubOptions(String name) {
		return subOptions.containsKey(name);
	}
	
	/**
	* Get all sets of sub-options for this set
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = subOptions.entrySet();
		ModOptions m[] = new ModOptions[s.size()];
		int i = 0;
		for(Map.Entry<String, ModOptions> e : s) {
			m[i] = e.getValue();
			i++;
		}
		
		return m;
	}
	
	/**
	* Get all sets of sub-options for this set for multiplayer
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getMultiplayerSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = subOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<ModOptions>();
		
		for(Map.Entry<String, ModOptions> e : s) {
			if(e.getValue().isMultiplayerMod()) {
				m.add(e.getValue());
			}
		}
		
		return m.toArray(new ModOptions[0]);
	}
	
	/**
	* Get all sets of sub-options for this set for singleplayer
	*
	* return	Array of all sub options for this set
	*/
	public ModOptions[] getSingleplayerSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = subOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<ModOptions>();
		
		for(Map.Entry<String, ModOptions> e : s) {
			if(e.getValue().isSingleplayerMod()) {
				m.add(e.getValue());
			}
		}
		
		return m.toArray(new ModOptions[0]);
	}
	
	/**
	* Get a named set of sub-options
	*
	* @param	name		Name of sub-options
	* @return	A sub-set of options
	*/
	public ModOptions getSubOption(String name) {
		return subOptions.get(name);
	}
	
	//=========================
	// Mass Option Methods
	//=========================
	
	/**
	* Sets all values global values to the parameter
	*
	* @param	global	New value
	*/
	public void globalReset(boolean global) {
		if(((ModOptionsAPI.isMultiplayerWorld()) && (multiplayer)) 
			|| ((!ModOptionsAPI.isMultiplayerWorld()) && (singleplayer))) {
			// Set all local options
			ModOption[] options = getOptions();
			for(ModOption option : options) {
				option.setGlobal(global);
			}
			// If we're in single player, reset all single player ones
			ModOptions[] subMenus = new ModOptions[0];
			if((!ModOptionsAPI.isMultiplayerWorld()) && (singleplayer)) {
				subMenus = getSingleplayerSubOptions();
				for(ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}
			// If we're in multiplayer, reset all multiplayer ones
			if((ModOptionsAPI.isMultiplayerWorld()) && (multiplayer)) {
				subMenus = getMultiplayerSubOptions();
				for(ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}
		}
	}
	
	//=========================
	// Direct Option Methods
	//=========================
	
	/**
	* Return a single named option
	*
	* @param	name	Name of option selector
	* @return	Option selector
	*/
	public ModOption getOption(String name) {
		return options.get(name);
	}
	
	/**
	* Returns a single named option's internal value
	*
	* @return	Value of option, as an object
	*/
	public String getOptionValue(String name) {
		ModOption option = options.get(name);
		if(option instanceof ModSliderOption) {
			return  Integer.toString(((ModSliderOption) option)
						   .getIntValue(((ModSliderOption) option).getValue()));
		} else {
			return option.getValue().toString();
		}
	}
	
	/**
	* Get the value of a text input field
	*
	* @since	0.7
	* @throws	NoSuchOptionException	When no option is present
	* @throws	IncompatibleOptionTypeException
	* @param	name					Name of option
	* @return	Value of a toggle option
	*/
	public String getTextValue(String name) throws NoSuchOptionException {
		ModOption option = options.get(name);
		
		if(option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if(!(option instanceof ModTextOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a text option");
		} else {
			return ((ModTextOption) option).getValue();
		}
	}
	
	/**
	* Returns a single named toggle value
	*
	* @since	0.6.1
	* @throws	NoSuchOptionException	When no option is present
	* @throws	IncompatibleOptionTypeException
	* @param	name					Name of the option
	* @return	Value of a toggle option
	*/
	public boolean getToggleValue(String name) throws NoSuchOptionException {
		ModOption option = options.get(name);
		
		if(option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if(!(option instanceof ModBooleanOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a toggle option");
		} else {
			return ((ModBooleanOption) option).getValue();
		}
	}
	
	/**
	* Returns a single named slider option's value
	*
	* @since	0.6.1
	* @throws	NoSuchOptionException	When no option is present
	* @throws	IncompatibleOptionTypeException
	* @param	name					Name of the option
	* @return	Value of a slider option
	*/
	public float getSliderValue(String name) throws NoSuchOptionException {
		ModOption option = options.get(name);
		
		if(option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if(!(option instanceof ModSliderOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a slider option");
		} else {
			return ((ModSliderOption) option).getValue().floatValue();
		}
	}
	
	/**
	* Returns a single named mapped multi option's value
	*
	* @since	0.6.1
	* @throws	NoSuchOptionException	When no option is present
	* @throws	IncompatibleOptionTypeException
	* @param	name					Name of the option
	* @return	Value of a mapped multi option
	*/
	public int getMappedValue(String name) throws NoSuchOptionException {
		ModOption option = options.get(name);
		
		if(option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if(!(option instanceof ModMappedMultiOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a mapped multi option");
		} else {
			return ((ModMappedMultiOption) option).getValue();
		}
	}
	
	/**
	* Set a single named boolean options global value
	*
	* @throws	IncompatibleOptionTypeException
	* @throws	NoSuchOptionException				When the option doesn't already exist
	* @param	name		Name of boolean toggle to change
	* @param	value		New value of toggle
	*/
	public void setOptionValue(String name, boolean value) {
		ModOption m = this.getOption(name);
		if(m == null) {
			throw new NoSuchOptionException();
		} else if(m instanceof ModBooleanOption) {
			ModBooleanOption bo = (ModBooleanOption) m;
			bo.setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}
	}
	
	/**
	* Set a single slider or mapped multi option's global value
	*
	* @throws	IncompatibleOptionTypeException
	* @throws	NoSuchOptionException				When the option doesn't already exist
	* @param	name		Name of slider option to change
	* @param	value		New value of toggle
	*/
	public void setOptionValue(String name, int value) {
		ModOption m = this.getOption(name);
		if(m == null) {
			throw new NoSuchOptionException();
		} else if(m instanceof ModSliderOption) {
			ModSliderOption so = (ModSliderOption) m;
			so.setGlobalValue(so.getFloatValue(value));
		} else if(m instanceof ModMappedMultiOption) {
			((ModMappedMultiOption) m).setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}
	}
	
	/**
	* Set a single multi option's global value
	*
	* @throws	IncompatibleOptionTypeException
	* @throws	NoSuchOptionException				When the option doesn't already exist
	* @param	name		Name of multi toggle to change
	* @param	value		New value of toggle
	*/
	public void setOptionValue(String name, String value) {
		ModOption m = this.getOption(name);
		if(m == null) {
			throw new NoSuchOptionException();
		} else if(m instanceof ModMultiOption) {
			ModMultiOption mo = (ModMultiOption) m;
			mo.setGlobalValue(value);
		} else if(m instanceof ModTextOption) {
			((ModTextOption) m).setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}
	}
	
	/**
	* Set a key binding option value
	*
	* @throws	IncompatibleOptionTypeException
	* @throws	KeyAlreadyBoundException
	* @throws	NoSuchOptionException				When the option doesn't already exist
	* @param	name	Name of option to set
	* @param	key		Key to bind
	*/
	public void setOptionValue(String name, char value)
		throws IncompatibleOptionTypeException, KeyAlreadyBoundException {
		setOptionValue(name, new Character(value));
	}
	
	/**
	* Set a key binding option value
	*
	* @throws	IncompatibleOptionTypeException		When the name doesn't match option type
	* @throws	KeyAlreadyBoundException			When the key is already bound
	* @throws	NoSuchOptionException				When the option doesn't already exist
	* @param	name	Name of option to set
	* @param	key		Key to bind
	*/
	public void setOptionValue(String name, Character value) throws KeyAlreadyBoundException {
		ModOption m = this.getOption(name);
		
		if(m == null) {
			throw new NoSuchOptionException();
		} else if(m instanceof ModKeyOption) {
			((ModKeyOption) m).setValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}
	}
	
	/**
	* Return all option selectors for this menu
	*
	* @return	Array of all option selectors for this menu
	*/
	public ModOption[] getOptions() {
		Set<Map.Entry<String, ModOption>> s = options.entrySet();
		ModOption m[] = new ModOption[s.size()];
		int i = m.length - 1;
		for(Map.Entry<String, ModOption> e : s) {
			m[i] = e.getValue();
			i--;
		}
		
		return m;
	}
	
	//=========================
	// GUI Related
	//=========================
	
	/**
	* Set a named Option to wide
	*
	* @param name	 Name of option
	*/
	public void setWideOption(String name) {
		gui.setWide(name);
	}
	
	/**
	* Set a given option to wide
	*
	* @param	option	Option object
	*/
	public void setWideOption(ModOption option) {
		setWideOption(option.getName());
	}
	
	/**
	* Set an option's string format, will remove any other formatters
	*
	* @param name			Name of option
	* @param formatter		Formatter
	*/
	public void setOptionStringFormat(String name, MODisplayString formatter) {
		gui.setFormatter(getOption(name), formatter);
	}
	
	/**
	* Add a formatter to the set of formatters for the given option
	*
	* @since	0.6.1
	* @param	name		Name of option
	* @param	formatter	Formatter to add
	*/
	public void addOptionFormatter(String name, MODisplayString formatter) {
		gui.addFormatter(getOption(name), formatter);
	}
	
	
	//=========================
	// Getters and Setters
	//=========================
	
	/**
	* Return name for this menu
	*
	* @return	Name of this options menu
	*/
	public String getName() {
		return name;
	}
	
	/**
	* Get parent for this menu
	*
	* @return	Parent for this menu (or null if no parent)
	*/
	public ModOptions getParent() {
		return parent;
	}
	
	/**
	* Set the parent for this menu
	*
	* @param	o		Parent menu
	*/
	public void setParent(ModOptions o) {
		parent = o;
	}
	
	/**
	* Return the GUI controller for this menu
	*
	* @return	Gui controller
	*/
	public ModOptionsGuiController getGuiController() {
		return gui;
	}
	
	/**
	* Set multiplayer value
	*
	* @param	multi	True if visible in multiplayer
	*/
	public void setMultiplayerMode(boolean multi) {
		multiplayer = multi;
	}
	
	/**
	* Set singleplayer value
	*
	* @param	single	True if visible in singleplayer
	*/
	public void setSingleplayerMode(boolean single) {
		singleplayer = single;
	}
	
	//=========================
	// Verification
	//=========================
	
	/**
	* Checks if this mod is available in single player world menus
	*
	* @return	True if in single player world menus
	*/
	public boolean isSingleplayerMod() {
		return singleplayer;
	}
	
	/**
	* Checks if this mod is available in multiplayer server menus
	*
	* @return	True if in a multiplayer world menu
	*/
	public boolean isMultiplayerMod() {
		return multiplayer;
	}
	
	//=========================
	// Storage
	//=========================
	
	/**
	* Loads global values from disk into memory for this
	* and all sub-menus
	*/
	public void loadValues() {
		loadValues("", false);
	}
	
	/**
	* Loads values from disk into memory for this
	* and all sub-menus
	*
	* @param	worldName	Name of world/ server to load for
	* @param	multi		True if  multiplayer word
	*/
	public void loadValues(String worldName, boolean multi) {
		String line;
		// Also load all children
		for(ModOptions child : this.getSubOptions()) {
			child.loadValues(worldName, multi);
		}
		
		File file = getFile(worldName, multi);
		try {
			BufferedReader reader 	= new BufferedReader(new FileReader(file));
			HashMap map 			= new HashMap<String, String>();
			while((line = reader.readLine()) != null) {
				String[] 	parts 	= line.split(":", 2);
				String 		name 	= parts[0];
				String 		value 	= parts[1].replace(":", "");
				map.put(name, value);
			}
			
			for(ModOption o : this.getOptions()) {
				try {
					// If the option is saved to the disk
					if(map.containsKey(o.getName())) {
						String 	val 	= (String) map.get(o.getName());
						boolean global	= worldName.length() == 0;
						
						if(o instanceof ModSliderOption) {
							ModSliderOption s = (ModSliderOption) o;
							s.setValue(Float.parseFloat(val), global);
						} else if(o instanceof ModMultiOption) {
							ModMultiOption t = (ModMultiOption) o;
							t.setValue(val, global);
						} else if(o instanceof ModBooleanOption) {
							ModBooleanOption b = (ModBooleanOption) o;
							b.setValue(Boolean.valueOf(val), global);
						} else if(o instanceof ModMappedMultiOption) {
							ModMappedMultiOption t = (ModMappedMultiOption) o;
							t.setValue(Integer.parseInt(val), global);
						} else if(o instanceof ModKeyOption) {
							ModKeyOption k = (ModKeyOption) o;
							
							if(val.length() > 0) {
								k.setValue(val.charAt(0), global);
							}
						} else {
							o.setValue(val, global);
						}
						// Turn off global default for this option
						o.setGlobal(global);
					}
				} catch (NumberFormatException e) {
					System.err.println("(ModOptionsAPI): Could not load option value");
				} 
			}
		} catch (FileNotFoundException e) {
			// Ignore, this is expected
		} catch (IOException e) {
			System.out.println("(ModOptionsAPI): IOException occured: " + e.getMessage());
		}
	}
	
	/**
	* Save options to disk for a particular world
	*
	* @param	name			worldname
	* @param	multiplayer		Save to a multiplayer file if true
	*/
	public void save(String name, boolean multiplayer) {
		boolean global = (name.length() == 0);
		// Delete old file, write new
		File file = getFile(name, multiplayer);
		file.delete();
		try {
			PrintWriter printwriter = new PrintWriter(new FileWriter(file));
			for(ModOption o : this.getOptions()) {
				Object obj = o.getValue(global);
				
				// Only record if it's a global value or a local non-global reference value
				if((obj != null) && (global || (!o.useGlobalValue()))) {
					// Remove all ":" in the name. 
					printwriter.println(o.getName().replace(":", "") + ":" + obj.toString());
				}
			}
			
			printwriter.close();
		} catch (IOException e) {
			System.err.println("(ModOptionsAPI): Could not save options to " + name);
		}
	}
	
	/**
	* Saves options to disk
	*/
	public void save() {
		save("", false);
	}
	
	/**
	* Get directory for this set of options
	*
	* @return	Directory for this set of options
	*/
	private String getDir() {
		String		subDir 		= name;
		ModOptions 	p			= parent;
		String 		fileName;
		while(p != null) {
			subDir = p.getName() + "/" + subDir;
			p = p.getParent();
		}
		
		return Minecraft.getMinecraftDir() + "/ModOptions/" + subDir;
	}
	
	/**
	* Get file associated with this set of options
	*
	* @param	name			worldname
	* @param	multiplayer		Save to a multiplayer file if true
	*/
	private File getFile(String name, boolean multiplayer) {
		String prefix = "";
		// Add a prefix for world specifics
		if(name.length() > 0) {
			if(multiplayer) {
				prefix = name + ".server.";
			} else {
				prefix = name + ".world.";
			}
		} 
		
		String subDir = getDir();
		// Ensure dir exists
		File dir = new File(subDir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		//System.out.println(subDir + "/" + prefix + "settings.ini");
		return new File(subDir + "/" + prefix + "settings.ini");
	}
}
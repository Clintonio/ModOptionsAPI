package moapi;

import java.util.Set;
import java.util.TreeMap;
import java.util.Map;
import java.util.LinkedList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import net.minecraft.client.Minecraft;

/**
* Controller class for the API. 
*
* @author Clinton Alexander
* @version 0.1
*/
public class ModOptionsAPI {
	private static TreeMap<String, ModOptions> modOptions = new TreeMap<String, ModOptions>();
	
	/**
	* True when player has loaded a world
	*/
	private static boolean ingame	 		= false;
	/**
	* True if the player is on a multiplayer server
	*/
	private static boolean multiplayerWorld = false;
	
	//=========================
	// Checking and Validation
	//=========================
	
	/**
	* Check if we are ingame
	*
	* @return	true if we are ingame
	*/
	public static boolean worldLoaded() {
		return ingame;
	}
	
	/**
	* Check if the world is a multiplayer world
	*
	* @return	True if multiplayer world
	*/
	public static boolean isMultiplayerWorld() {
		return multiplayerWorld;
	}
	
	//=========================
	// Setters
	//=========================
	
	/**
	* Set that we are in a multiplayer world
	*
	* @param	s	 Server name
	*/
	public static void joinedMultiplayerWorld(String s) {
		// Load all local values for all multi player mods
		ModOptions[] m = ModOptionsAPI.getMultiplayerMods();
		for(ModOptions op : m) {
			op.loadValues(s, true);
		}
		
		multiplayerWorld = true;
		ingame = true;
	}
	
	/**
	* Set that we are in a singleplayer world
	*
	* @param	s	World name
	*/
	public static void selectedWorld(String s) {
		// Load all local values for all single player mods
		ModOptions[] m = ModOptionsAPI.getSingleplayerMods();
		for(ModOptions op : m) {
			op.loadValues(s, false);
		}
		
		multiplayerWorld = false;
		ingame = true;
	}
	
	/**
	* Set that we are in the main menu
	*/
	public static void viewingMainMenu() {
		multiplayerWorld = false;
		ingame = false;
	}
	
	//=========================
	// Getters
	//=========================
	
	/**
	* Gets all mods that have been added
	*
	* @author	Clinton Alexander
	* @version	0.1
	* @since	0.1
	* @return	Array of all sets of options for all mods
	*/
	public static ModOptions[] getAllMods() {
		Set<Map.Entry<String, ModOptions>> s = modOptions.entrySet();
		ModOptions m[] = new ModOptions[s.size()];
		int i = 0;
		for(Map.Entry<String, ModOptions> e : s) {
			m[i] = e.getValue();
			i++;
		}
		
		return m;
	}
	
	/**
	* Returns all mods that identify as a multiplayer mod
	*
	* @author	Clinton Alexander
	* @return	Array of all sets of options for multiplayer world mods
	*/
	public static ModOptions[] getMultiplayerMods() {
		Set<Map.Entry<String, ModOptions>> s = modOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<ModOptions>();
		
		for(Map.Entry<String, ModOptions> e : s) {
			if(e.getValue().isMultiplayerMod()) {
				m.add(e.getValue());
			}
		}
		
		return m.toArray(new ModOptions[0]);
	}
	
	/**
	* Returns all mods that identify as a singleplayer mod
	*
	* @author	Clinton Alexander
	* @return	Array of all sets of options for singleplayer world mods
	*/
	public static ModOptions[] getSingleplayerMods() {
		Set<Map.Entry<String, ModOptions>> s = modOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<ModOptions>();
		
		for(Map.Entry<String, ModOptions> e : s) {
			if(e.getValue().isSingleplayerMod()) {
				m.add(e.getValue());
			}
		}
		
		return m.toArray(new ModOptions[0]);
	}
	
	/**
	* Returns a set of options for a mod by the name of the mod.
	* 
	* @author	Clinton Alexander
	* @version	0.1
	* @since	0.1
	* @param	name		Name of the mod
	* @return	Set of options for the mod named in the parameters
	*/
	public static ModOptions getModOptions(String name) {
		return modOptions.get(name);
	}
	
	//=========================
	// Adder Methods
	//=========================
	
	/**
	* Adds a mods set of options to a menu in the ModOptions
	* menu. (Add your mod's options here)
	*
	* @author	Clinton Alexander
	* @version	0.1
	* @since	0.1
	* @param	o		A set of options for a mod
	*/
	public static void addMod(ModOptions o) {
		modOptions.put(o.getID(), o);
	}
	
	/**
	* Adds a mod by name and attempts to load it
	*
	* @throws 	MOMissingModException
	* @param	name	name of mod being loaded
	* @return	Mod object
	*/
	public static ModOptions addMod(String name) throws MOMissingModException{
		File file = getFile(name);
		ModOptions mod = new ModOptions(name);
		
		if(file == null) {
			throw new MOMissingModException(name + " mod is missing");
		} else {
			try {
				applyModFile(mod, file);
			} catch (FileNotFoundException e) {
				throw new MOMissingModException(name + " mod is missing");
			} catch (IOException e) {
				System.out.println("(ModOptionsAPI): IOException occured: " + e.getMessage());
				throw new MOMissingModException(name + " mod is missing due to an IOException");
			}
		}
		
		addMod(mod);
		return mod;
	}
	
	//=========================
	// Private Methods
	//=========================
	
	/**
	* Applies a given mod file to a mod
	*
	* @throws	IOException
	* @param	mod		Mod to be edited
	* @param	file	File to be parsed
	*/
	private static void applyModFile(ModOptions mod, File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		/* Current options we are reading */
		ModOptions cur = mod;
		String line;
		while((line = reader.readLine()) != null) {
			// Read a section and parse into a mod
			if(isSection(line)) {
				cur = parseSection(line, mod);
			} else if(isOption(line)) {
				try {
					parseOption(line, cur);
				} catch (IncompatibleOptionTypeException e) {
					// Do not add option
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	/**
	* Check if a line is an option declaration
	*
	* @param	String line
	* @return	true if lien is an option declaration
	*/
	private static boolean isOption(String line) {
		return (line.contains(":") && (line.split(":").length > 1));
	}
	
	/**
	* Add an option to a set of options
	*
	* @throws	IncompatibleOptionTypeException 
	* @param	line		Line to parse
	* @return	Option to add
	*/
	private static void parseOption(String line, ModOptions mod) throws IncompatibleOptionTypeException {
		ModOption 	mo			= null;
		// Parse line
		String[] 	sections 	= line.trim().split(":");
		String 		name 		= sections[0].trim();
		String 		type 		= sections[1].trim().toLowerCase();
		String[] 	params		= new String[0];
		if(sections.length > 2) {
			sections[2] = sections[2].trim();
			params = sections[2].substring(1, sections[2].length() - 1).split(",");
			if((sections.length > 3) && (sections[3].trim().toUpperCase().equals("WIDE"))) {
				mod.setWideOption(name);
			}
		}
		
		if(type.equals("boolean")) {
			if(params.length > 1) {
				mo = (ModOption) new ModBooleanOption(name, params[0].trim(), params[1].trim());
			} else {
				mo = (ModOption) new ModBooleanOption(name);
			}
		} else if(type.equals("multi")) {
			mo = (ModOption) new ModMultiOption(name, params);
		} else if(type.equals("mappedmulti")) {
			mo = (ModOption) parseMappedOption(name, params, mod);
		} else if(type.equals("slider")) {
			mo = parseSliderOption(name, params, mod);
		} else if(type.equals("text")) {
			mo = parseTextOption(name, params, mod);
		} else if(type.equals("keybinding")) {
			mo = parseKeyBinding(name, params, mod);
		} else {
			throw new IncompatibleOptionTypeException(type + " is an invalid option type in mod " + mod.getName());
		}
		
		mod.addOption(mo);
	}
	
	/**
	* Parse a mapped multi option
	*
	*/
	private static ModMappedOption parseMappedOption(String name, String[] params, ModOptions mod) {
		ModMappedOption mo = new ModMappedOption(name);
		// add the sections
		for(int i = 0; i < params.length; i++) {
			String[] tmp = params[i].trim().split("=");
			//System.out.println("PARAM("+i+"): " +params[i]);
			try {
				if(tmp.length == 2) {
					String value  = tmp[1];
					Integer key   = Integer.parseInt(tmp[0]);
					
					mo.addValue(key,value);
				}
			} catch (NumberFormatException e) {
				System.out.println("Number format for key value ("+tmp[0]+") invalid for option " 
									+ name + " in mod " + mod.getName());
			}
		};
		
		return mo;
	}
		
	/**
	* Parse a slider option into memory
	*
	* @param	line	line to parse
	* @param	params	Parameters
	* @param	mod		Mod being parsed
	* @return	New current mod
	*/
	private static ModOption parseSliderOption(String name, String[] params, ModOptions mod) {
		ModSliderOption mo;
		try {
			if(params.length > 1) {
				mo = new ModSliderOption(name, Integer.parseInt(params[0].trim()), 
													 Integer.parseInt(params[1].trim()));
			} else {
				mo = new ModSliderOption(name);
			}
		} catch (NumberFormatException e) {
			System.out.println("Number format for high or low value invalid for option " 
								+ name + " in mod " + mod.getName());
			mo = new ModSliderOption(name);
		}
		
		return mo;
	}
	
	/**
	* Parse a text option into memory
	*
	* @param	name	Name of option
	* @param	params	Parameters for option
	* @param	mod		Mod we are parsing into
	*/
	private static ModOption parseTextOption(String name, String[] params, ModOptions mod) {
		ModOption mo;
		try {
			if(params.length > 1) {
				mo = new ModTextOption(name, Integer.parseInt(params[0].trim()));
			} else {
				mo = new ModTextOption(name);
			}
		} catch (NumberFormatException e) {
			System.out.println("Number must be a valid integer for " + name + 
							   " in mod " + mod.getName() + ". Using infinite");
			mo = new ModTextOption(name);
		}
		
		return mo;
	}
	
	/**
	* Parse a key option into memory
	*
	* @param	 name	Name of option
	* @param	params	Parameters for options
	* @param	mod 	Mod we are parsing into
	*/
	private static ModOption parseKeyBinding(String name, String[] params, ModOptions mod) {
		return new ModKeyOption(name);
	}
	
	
	/**
	* Parses a section declaration into a mod
	*
	* @param	line	line to parse
	* @param	mod		Root mod option
	* @return	New current mod
	*/
	private static ModOptions parseSection(String line, ModOptions mod) {
		// Set cur to the root again
		ModOptions cur = mod;
		
		line = line.trim();
		line = line.substring(1, line.length() - 1);
		
		String[] parts = line.split(":");
		String name = parts[0].trim();
		// Set multiplayer and single player options
		boolean multi = false;
		boolean single = false;
		// apply modifiers
		if(parts.length > 1) {
			 String[] modParts = parts[1].split(",");
			 for(String modifier : modParts) {
				modifier = modifier.trim().toUpperCase();
				if(modifier.equals("MULTIPLAYER")) {
					multi = true;
				} else if(modifier.equals("SINGLEPLAYER")) {
					single = true;
				}
			 }
		}
		
		String[] path = name.split("/");
		for(int i = 0; i < path.length; i++) {
			String tmp = path[i].trim();
			if(cur.containsSubOptions(tmp)) {
				cur = mod.getSubOption(tmp);
			} else {
				ModOptions parent = cur;
				cur = new ModOptions(tmp);
				parent.addSubOptions(cur);
			}
		}
		
		// Now set up modifiers
		cur.setSingleplayerMode(single);
		cur.setMultiplayerMode(multi);
		
		return cur;
	}

	
	/**
	* Check if the line is a section
	*
	* @param	line	Line
	* @return	true if is a section
	*/
	private static boolean isSection(String line) {
		return ((line.length() > 0) && (line.startsWith("[")) && (line.endsWith("]")));
	}
	
	/**
	* Get file associated with this mod
	*
	* @param	name			Mod name
	* @return	File associated with this mod
	*/
	private static File getFile(String name) {
		File file = new File(Minecraft.getMinecraftDir() + "/ModOptions/" 
							 + name + "/" + name + ".modoptions");
		if(!file.exists()) {
			return null;
		} else {
			return file;
		}
	}
}

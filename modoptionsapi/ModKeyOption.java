package modoptionsapi;

import net.minecraft.src.GameSettings;
import net.minecraft.src.ModLoader;
import net.minecraft.src.KeyBinding;

import java.util.Hashtable;
import java.util.prefs.InvalidPreferencesFormatException;

/**
* An option for keybindings
* Enforces the concept of only one key per binding
* type.
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since 	0.7
*/
public class ModKeyOption extends ModOption<Character> {
	/**
	* Current key bindings, implemented as a single value due to the
	* fact that keys can only have one configuration per world
	*/
	private static Hashtable<Character, ModOption> bindings = new Hashtable<Character, ModOption>();
	
	/**
	* Constructor for key binding option
	*
	* @param	name	Name of option
	*/
	public ModKeyOption(String name) {
		this.name = name;
	}
	/**
	* Set the current used value of this option selector
	* 
	* @param	value		New value
	*/
	public void setValue(char value) {
		setValue(new Character(value));
	}
	
	/**
	* Set the current used value of this option selector
	* 
	* @throws	KeyAlreadyBoundException	When attempting to remind a key
	* @param	value		New value
	*/
	public void setValue(Character value) {
		if((getLocalValue() == value && !global) 
				|| (getGlobalValue() == value && global) 
				|| (!isKeyBound(value))) {
			if(!global) {
				this.value = value;
			} else {
				localValue = value;
			}
			
			bindings.put(value, this);
		} else {
			throw new KeyAlreadyBoundException(value);
		}
	}
	
	/**
	* Check if a key is already bound
	*
	* @param	c	Character to check
	* @return	True if already bound
	*/
	public boolean isKeyBound(Character c) {
		GameSettings settings = ModLoader.getMinecraftInstance().gameSettings;
		
		boolean found = false;
		for(KeyBinding binding : settings.keyBindings) {
			if((char) binding.keyCode == c.charValue()) {
				found = true;
			}
		}
		
		return(found || bindings.containsKey(c));
	}
}
package modoptionsapi;

import org.lwjgl.input.Keyboard;

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
	* The default character
	*/
	private static Character defaultChar = new Character((char) 14);
	
	/**
	* Constructor for key binding option
	*
	* @param	name	Name of option
	*/
	public ModKeyOption(String name) {
		this.name = name;
		
		setGlobalValue(defaultChar);
		setLocalValue(defaultChar);
	}
	/**
	* Set the current used value of this option selector
	* 
	* @param	value		New value
	*/
	public void setValue(char value) {
		setValue(new Character(value), global);
	}
	
	/**
	* Set the current used value of this option selector fr a given 
	* scope
	* 
	* @throws	KeyAlreadyBoundException	When attempting to remind a key
	* @param	value		New value
	*/
	public void setValue(Character value) {
		setValue(value, global);
	}
	
	/**
	* Set the current used value of this option selector for a given
	* scope
	* 
	* @throws	KeyAlreadyBoundException	When attempting to remind a key
	* @param	value		New value
	*/
	public void setValue(char value, boolean scope) {
		setValue(new Character(value), scope);
	}
	
	/**
	* Set the current used value of this option selector
	* 
	* @throws	KeyAlreadyBoundException	When attempting to remind a key
	* @param	value		New value
	*/
	public void setValue(Character value, boolean scope) {
		Character curVal = getValue(scope);
		value = Character.toUpperCase(value);
		if(value == defaultChar) {
			// Dead branch (CBA to refactor)
			bindings.remove(value);
			super.setValue(value, global);
		} else if((getLocalValue() == value && !global) 
				|| (getGlobalValue() == value && global) 
				|| (!isKeyBound(value))) {
			// Remove old value if it exists
			if((curVal != null) && (bindings.containsKey(curVal))) {
				bindings.remove(curVal);
			}
			super.setValue(value, scope);
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
	public static boolean isKeyBound(Character c) {
		c = Character.toUpperCase(c);
		
		return ((!c.equals(defaultChar)) && bindings.containsKey(c));
	}
}
package net.minecraft.src.modoptionsapi;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;

/**
* Multiple Selector API with Integer -> String mappings
*
* @author	Clinton Alexander
* @version	0.1
* @since	0.1
*/
public class ModMappedMultiOption extends ModOption<Integer> {
	/**
	* Collection of possible values of this selector
	*/
	private LinkedHashMap<Integer, String> values = new LinkedHashMap<Integer, String>();
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	public ModMappedMultiOption(String name) {
		this.name = name;
	}
	
	/**
	* Creates a multiple selector with given values
	*
	* @throws	IndexOutOfBoundsException
	* @param	name	Name of selector
	* @param	values	Values for selector
	* @param	def		Default value, must exist
	*/
	private ModMappedMultiOption(String name, LinkedHashMap<Integer, String> values, Integer def) 
		throws IndexOutOfBoundsException {
		this.name = name;
		// Set current/ first/ default value
		
		if(!values.containsKey(def)) {
			throw new IndexOutOfBoundsException("Default value does not exist");
		} else {
			value = def;
			this.values = (LinkedHashMap) values.clone();
		}
	}
	
	/**
	* Add a single value to this selector
	* 
	* @param	key		Key to add value to
	* @param	value	Value to add
	*/
	public void addValue(Integer key, String value) {
		if(values.size() == 0) {
			this.value = key;
			localValue = key;
		} 
		
		this.values.put(key, value);
	}
	
	/**
	* Gets the string representation
	*
	* @param	key	Key to get string rep of
	* @return	String representation of a value
	*/
	public String getStringValue(Integer key) {
		return values.get(key);
	}
	
	/**
	* Gets the next value in this selector
	*
	* @param	i	Current value
	* @return	Next value
	*/
	public Integer getNextValue(Integer i) {
		Integer cur			= null;
		boolean found 		= false;
		boolean written		= false;
		// We need the first key due to this being circular
		boolean firstFound 	= false;
		Integer firstKey 	= null;
		
		// Find next value
		Set<Map.Entry<Integer, String>> s = values.entrySet();
		for(Map.Entry<Integer, String> entry : s) {
			// Ensure we have the first key incase of looparouns
			if(!firstFound) {
				firstKey = entry.getKey();
				firstFound = true;
			}
			
			if(!written) {
				if(found) {
					cur 	= entry.getKey();
					written = true;
				}
				
				if(entry.getKey().equals(i)) {
					found = true;
				}
			}
		}
		
		// Looparound back to first key
		if(!written) {
			cur = firstKey;
		}
		
		return cur;
	}
}
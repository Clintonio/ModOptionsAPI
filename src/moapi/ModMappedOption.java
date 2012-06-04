package moapi;

import java.util.*;

/**
* Multiple Selector API with Integer -> String mappings
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.7
*/
public class ModMappedOption extends ModOption<Integer> {
	/**
	* Collection of possible values of this selector
	*/
	private LinkedHashMap<Integer, String> values = new LinkedHashMap<Integer, String>();
	
	//==============
	// Constructors
	//==============
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	public ModMappedOption(String name) {
		this(name, name);
	}
	
	/**
	* Create a multiple selector with the given keys and labels
	*
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	keys		Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String name, Integer[] keys, String[] labels) {
		this(name, name, keys, labels);
	}
	
	/**
	* Create a multiple selector with the given keys and labels
	*
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	keys		Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String name, int[] keys, String[] labels) {
		this(name, name, keys, labels);
	}
	
	/**
	* Create a multiple selector with the given keys and labels and ID/name
	*
	* @since	0.8
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	id		ID of selector
	* @param	name	Name of selector
	* @param	keys		Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String id, String name, Integer[] keys, String[] labels) {
		this(id, name);
		if(keys.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			
			for(int x = 0; x < keys.length; x++) {
				addValue(keys[x], labels[x]);
			}
		}
	}
	
	/**
	* Create a multiple selector with the given keys and labels
	*
	* @since	0.8
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	keys		Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String id, String name, int[] keys, String[] labels) {
		this(id, name);
		if(keys.length != labels.length) {
			throw new IndexOutOfBoundsException("Keys and labels must have same # of entries");
		} else {
			
			for(int x = 0; x < keys.length; x++) {
				addValue(new Integer(keys[x]), labels[x]);
			}
		}
	}
	
	/**
	* Creates a multi selector with the given name/ id and no values
	*
	* @since	0.8
	* @param	id		ID of this option
	* @param	name	Name of this option
	*/
	public ModMappedOption(String id, String name) {
		super(id, name);
	}
	
	//==============
	// Adders
	//==============
	
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
	* Add a single value to this selector
	* 
	* @param	intKey	Key to add value to
	* @param	value	Value to add
	*/
	public void addValue(int intKey, String value) {
		addValue(new Integer(intKey), value);
	}
	
	//==============
	// Getters
	//==============
	
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
	* Gets the string representation
	*
	* @param	key	Key to get string rep of
	* @return	String representation of a value
	*/
	public String getStringValue(int key) {
		return values.get(new Integer(key));
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
	
	/**
	* Gets the next value in this selector
	*
	* @param	i	Current value
	* @return	Next value
	*/
	public Integer getNextValue(int i) {
		return getNextValue(new Integer(i));
	}
}

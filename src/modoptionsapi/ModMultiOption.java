package net.minecraft.src.modoptionsapi;

import java.util.LinkedList;

/**
* Multiple Selector API
*
* @author	Clinton Alexander
* @version	0.1
* @since	0.1
*/
public class ModMultiOption extends ModOption<String> {
	/**
	* Collection of possible values of this selector
	*/
	private LinkedList<String> values = new LinkedList<String>();
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	public ModMultiOption(String name) {
		this.name = name;
	}
	
	/**
	* Creates a multiple selector with given values
	*
	* @param	name	Name of selector
	* @param	values	Values for selector
	*/
	public ModMultiOption(String name, String[] values) {
		this.name = name;
		// Set current/ first/ default value
		
		if(values.length > 0) {
			value = values[0];
			localValue = values[0];
		
			for(String value : values) {
				this.values.add(value);
			}
		}
	}
	
	/**
	* Add a single value to this selector
	* 
	* @param	value	Value to add
	*/
	public void addValue(String value) {
		if(values.size() == 0) {
			this.value = value;
			this.localValue = value;
		}
		
		this.values.add(value);
	}
	
	/**
	* Gets the next value in this selector
	*
	* @param	s	Current value
	* @return	Next value
	*/
	public String getNextValue(String s) {
		int index = 0;
		for(int x = 0; x < values.size(); x++) {
			if(values.get(x).equals(s)) {
				index = x;
			}
		}
		return values.get((index + 1) % values.size());
	}
}
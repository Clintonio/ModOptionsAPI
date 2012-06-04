package moapi;

import java.util.LinkedList;

/**
* Multiple Selector API
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.1
*/
public class ModMultiOption extends ModOption<String> {
	/**
	* Collection of possible values of this selector
	*/
	private LinkedList<String> values = new LinkedList<String>();
	
	//==============
	// Constructors
	//==============
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	public ModMultiOption(String name) {
		this(name, name);
	}
	
	/**
	* Creates a multiple selector with given values
	*
	* @param	name	Name of selector
	* @param	values	Values for selector
	*/
	public ModMultiOption(String name, String[] values) {
		this(name, name, values);
	}
	
	/**
	* Creates a multiple selector with given values and given name/id
	*
	* @param	id		ID of the selector
	* @param	name	Name of selector
	* @param	values	Values for selector
	*/
	public ModMultiOption(String id, String name, String[] values) {
		this(id, name);
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
	* Creates a multiple selector with no values and the given name/id
	*
	* @since	0.8
	* @param	id		ID of selector
	* @param	name	Name of selector
	*/
	public ModMultiOption(String id, String name) {
		super(id, name);
	}
	
	//==============
	// Adders
	//==============
	
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
	
	//==============
	// Getters
	//==============
	
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
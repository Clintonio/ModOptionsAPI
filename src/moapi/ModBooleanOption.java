package moapi;

/**
* Boolean Option API
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.1
*/
public class ModBooleanOption extends ModOption<Boolean> {
	private String onVal 	= "On";
	private String offVal 	= "Off";
	
	//==============
	// Constructors
	//==============
	
	/**
	* Creates an On/Off toggle
	*
	* @param	name		Name of this toggle option
	*/
	public ModBooleanOption(String name) {
		this(name, name);
	}
	
	/**
	* Creates an On/Off toggle with specified labels
	*
	* @param	name		Name of this toggle option
	* @param	onVal		Value to display when this option is "on"
	* @param	offVal		Value to display when this option is "off"
	*/
	public ModBooleanOption(String name, String onVal, String offVal) {
		this(name, name, onVal, offVal);
	}
	
	/**
	* Creates an On/Off toggle with the specified labels and ID
	*
	* @since	0.8
	* @param	id			ID for this toggle to use
	* @param	name		Name of this toggle option
	* @param	onVal		Value to display when this option is "on"
	* @param	offVal		Value to display when this option is "off"
	*/
	public ModBooleanOption(String id, String name, String onVal, String offVal) {
		this(id, name);
		this.onVal = onVal;
		this.offVal = offVal;
	}
	
	/**
	* Creates an On/Off toggle with the given ID
	*
	* @since	0.8
	*/
	public ModBooleanOption(String id, String name) {
		super(id, name);
		this.value = true;
		this.localValue = true;
	}
	
	//==============
	// Getters
	//==============
	
	/**
	* Returns the string value of boolean based on this option
	*
	* @param	value	Value to get string for
	* @return	String representation
	*/
	public String getStringValue(boolean value) {
		if(value) {
			return onVal;
		} else {
			return offVal;
		}
	}
}
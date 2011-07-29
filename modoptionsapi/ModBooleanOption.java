package moapi;

/**
* Boolean Option API
*
* @author	Clinton Alexander
* @version	0.1
* @since	0.1
*/
public class ModBooleanOption extends ModOption<Boolean> {
	private String onVal 	= "On";
	private String offVal 	= "Off";
	
	/**
	* Creates an On/Off toggle
	*
	* @param	name		Name of this toggle option
	*/
	public ModBooleanOption(String name) {
		this.name = name;
		this.value = true;
		this.localValue = true;
	}
	
	/**
	* Creates an On/Off toggle with specified labels
	*
	* @param	name		Name of this toggle option
	* @param	onVal		Value to display when this option is "on"
	* @param	offVal		Value to display when this option is "off"
	*/
	public ModBooleanOption(String name, String onVal, String offVal) {
		this(name);
		this.onVal = onVal;
		this.offVal = offVal;
	}
	
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
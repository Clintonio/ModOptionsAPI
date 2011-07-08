package modoptionsapi;

/**
* Provides an interface to create a simple bounded slider
*
* @author	Clinton Alexander
* @version	0.1
* @since	0.1
*/
public class ModSliderOption extends ModOption<Float> {
	/**
	* Lowest value of slider
	*/
	private int low = 0;
	/**
	* Highest value of slider
	*/
	private int high = 100;
	
	/**
	* Create a slider identified by given name
	*
	* @param	name	Name of this slider
	*/
	public ModSliderOption(String name) {
		this.name = name;
		value = 1.0F;
		localValue = 1.0F;
	}
	
	/**
	* Create a bounded slider
	*
	* @param	name	Name of slider
	* @param	low		Lowest value of slider
	* @param	high	Highest value of slider
	*/
	public ModSliderOption(String name, int low, int high) {
		this(name);
		this.low = low;
		this.high = high;
	}
	
	/**
	* Get the highest value of the slider
	*
	* @return	Highest value of the slider
	*/
	public int getHighVal() {
		return high;
	}
	
	/**
	* Get the lowest value of the slider
	*
	* @return	Lowest value of the slider
	*/
	public int getLowVal() {
		return low;
	}
	
	/**
	* Set the float value
	*
	* @param	value	Value being set
	*/
	public void setValue(Float value) {
		super.setValue(getBoundedValue(value, low, high));
	}
	
	/**
	* Set the integer value
	*
	* @param	value	Value being set
	*/
	public void setValue(int value) {
		setValue((float) value);
	}
	
	/**
	* Set the float value
	*
	* @param	value	Value being set
	*/
	public void setLocalValue(Float value) {
		super.setLocalValue(getBoundedValue(value, low, high));
	}
	
	/**
	* Set the local value using an int
	*
	* @since	0.6.1
	* @param	value	Value being set
	*/
	public void setLocalValue(int value) {
		setLocalValue((float) value);
	}
	
	/**
	* Set the global value
	*
	* @param	value	Value being set
	*/
	public void setGlobalValue(Float value) {
		super.setGlobalValue(getBoundedValue(value, low, high));
	}
	
	/**
	* Set the global value using an int
	*
	* @since	0.6.1
	* @param	value	Value being set
	*/
	public void setGlobalValue(int value) {
		setGlobalValue((float) value);
	}
	
	/**
	* Transforms the given value from it's current upper and lower bounds
	* to the corresponding place in the ones provided
	*
	* @since	0.6.1
	* @param	value	The value to transform
	* @param	lower	Lower bound to transform to
	* @param	upper	Upper bound to transform to
	* @return	Value transformed from between low to high to between lower to upper
	*/
	public float transformValue(float value, int lower, int upper) {
		value = getBoundedValue(value, low, high);
		// Return it to the 0 to 1 base
		float base = (value - low) / (high - low);
		// Now put it into the new range
		float out = (base * (upper - lower)) + lower;
		return out;
	}
	
	/**
	* Transforms a value back from a given range into the local range
	*
	* @since	0.6.1
	* @param	value	Transformed value
	* @param	lower	Lower bound to transform from
	* @param	upper	Upper bound to transform from
	* @return 	Value in the range of this slider
	*/
	public float untransformValue(float value, int lower, int upper) {
		value = getBoundedValue(value, lower, upper);
		// Return to the 0 to 1 base
		float base = (value - lower) / (upper - lower);
		// Now put into the original range
		float out = (value * (high - low)) + low;
		
		return out;
	}
	
	/**
	* Returns a bounded value
	*
	* @since	0.6.1
	* @param	value	Unbounded value
	* @param	lower	Lower bound
	* @param	upper	Upper bound
	* @return	Bounded value
	*/
	private float getBoundedValue(float value, int lower, int upper) {
		if(value < lower) {
			return (float) lower;
		} else if(value > upper) {
			return (float) upper;
		} else {
			return value;
		}
	}
	
	/**
	* Get the rounded and bounded integer value of the slider
	*
	* @deprecated	This is depreciated code and will be removed in 0.7
	*				Float values are automatically generated now, you can just do (int) obj.getValue();
	* @param	value	value to convert
	* @return	Rounded integer value of the slider
	*/
	@Deprecated
	public int getIntValue(float value) {
		return (int) value;
	}
	
	/**
	* Sets the value of this slider by integer
	*
	* @deprecated	No need for this method now that the internal representation has
	*				been abstracted out. You will now get a value between low and high from getValue in float 
	*				form, use (int) obj.getValue();
	* @param	val		Integer value of slider
	*/
	@Deprecated
	public void setIntValue(int val) {
		setValue(val);
	}
	/**
	* Get the bounded float value of the slider
	* for the current scope value
	*
	* @deprecated	This is depreciated code and will be removed in 0.7 use getValue instead
	* @return	Bounded float value of slider
	*/
	@Deprecated
	public float getFloatValue() {
		return getValue();
	}
	
	/**
	* Get the bounded float value of the slider
	*
	* @deprecated	This is depreciated code and will be removed in 0.7
	*				Float values are automatically generated now.
	* @param	value	value to convert
	* @return	Bounded float value of slider
	*/
	@Deprecated
	public float getFloatValue(int value) {
		return (float) value;
	}
	
	/**
	* Get the rounded and bounded integer value of the slider#
	* for current scope
	*
	* @deprecated	This is depreciated code and will be removed in 0.7
	*				Float values are automatically generated now, you can just do (int) obj.getValue();
	* @param	value	value to convert
	* @return	Rounded integer value of the slider
	*/
	@Deprecated
	public int getIntValue() {
		return (int) getValue().intValue();
	}
}
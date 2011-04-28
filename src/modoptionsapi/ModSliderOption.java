package net.minecraft.src.modoptionsapi;

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
		if(value < 0.0F) {
			value = 0.0F;
		}
		if(value > 1.0F) {
			value = 1.0F;
		}
		
		super.setValue(value);
	}
	
	/**
	* Set the integer value
	*
	* @param	value	Value being set
	*/
	public void setValue(int value) {
		setValue(getFloatValue(value));
	}
	
	/**
	* Set the float value
	*
	* @param	value	Value being set
	*/
	public void setLocalValue(Float value) {
		if(value < 0.0F) {
			value = 0.0F;
		}
		if(value > 1.0F) {
			value = 1.0F;
		}
		
		super.setLocalValue(value);
	}
	
	/**
	* Set the global value
	*
	* @param	value	Value being set
	*/
	public void setGlobalValue(Float value) {
		if(value < 0.0F) {
			value = 0.0F;
		}
		if(value > 1.0F) {
			value = 1.0F;
		}
		
		super.setGlobalValue(value);
	}
	
	/**
	* Get the bounded float value of the slider
	* for the current scope value
	*
	* @return	Bounded float value of slider
	*/
	public float getFloatValue() {
		return ((getValue() - low) / (high - low));
	}
	
	/**
	* Get the bounded float value of the slider
	*
	* @param	value	value to convert
	* @return	Bounded float value of slider
	*/
	public float getFloatValue(int value) {
		return (((float) value - low) / (high - low));
	}
	
	/**
	* Get the rounded and bounded integer value of the slider#
	* for current scope
	*
	* @param	value	value to convert
	* @return	Rounded integer value of the slider
	*/
	public int getIntValue() {
		return (int) (getValue() * (high - low) + low);
	}
	
	/**
	* Get the rounded and bounded integer value of the slider
	*
	* @param	value	value to convert
	* @return	Rounded integer value of the slider
	*/
	public int getIntValue(float value) {
		return (int) (value * (high - low) + low);
	}
	
	/**
	* Sets the value of this slider by integer
	*
	* @param	val		Integer value of slider
	*/
	public void setIntValue(int val) {
		setValue(val);
	}
}
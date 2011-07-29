package moapi;

import moapi.gui.GuiController;

/**
* A text input field option for modoptionsapi
* with customisable maximum length
*
* @author	Clinton Alexander
* @version	1.0.0.0
*/
public class ModTextOption extends ModOption<String> {
	/**
	* Maximum length of text specified
	* 0 = No limit
	*/
	private int maxLength = 0;
	
	/**
	* Constructor for the text option
	*
	* @param	name	Name of option
	*/
	public ModTextOption(String name) {
		this(name, 0);
	}
	
	/**
	* Constructor for the text option specifying max length
	*
	* @param	name	Name of option
	* @param	maxlen	Maximum length of text entered
	*/
	public ModTextOption(String name, Integer maxLen) {
		this(name, (int) maxLen);
	}
	
	/**
	* Constructor for the text option specifying max length
	*
	* @param	name	Name of option
	* @param	maxlen	Maximum length of text entered
	*/
	public ModTextOption(String name, int maxLen) {
		this.name 	= name;
		setGlobalValue("");
		setMaxLength(maxLen);
	}
	
	/**
	* Set maximum length for the input
	*
	* @param	maxlen	Maximum length inputtable. 0 or less is infinite
	*/
	public void setMaxLength(int maxlen) {
		if(maxlen < 0) { 
			maxlen = 0;
		}
		
		maxLength = maxlen;
	}
	
	/**
	* Set maximum length for the input
	*
	* @param	maxlen	Maximum length inputtable
	*/
	public void setMaxLength(Integer maxlen) {
		if(maxlen < 0) {
			maxlen = new Integer(0);
		}
		
		maxLength = maxlen.intValue();
	}
	
	/**
	* Get the maximum value for the input length
	*
	* @return	Maximum length value
	*/
	public int getMaxLength() {
		return maxLength;
	}
}
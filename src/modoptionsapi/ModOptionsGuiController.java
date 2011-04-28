package net.minecraft.src.modoptionsapi;

import java.util.LinkedList;
import java.util.Hashtable;

/**
* Controller class for a Mod's Option menu
*
* @author	Clinton Alexander
* @version	0.1
* @since	0.1
*/
public class ModOptionsGuiController {
	/** 
	* The options that this controller is displaying
	*/
	private ModOptions options;
	/**
	* List defining which options are to be displayed wide
	*/
	private LinkedList<String> wide = new LinkedList<String>();
	/**
	* List defining string manipulation callbacks
	*/
	private Hashtable<ModOption, MODisplayString> formatters;
	
	
	/**
	* Create the controller for a given set of options
	*
	* @param	o	Set of options
	*/
	public ModOptionsGuiController(ModOptions o) {
		formatters = new Hashtable<ModOption, MODisplayString>();
		options = o;
	}
	
	/**
	* Sets the named option to show as a full width bar
	* instead of the default half-width
	*
	* @param	name	Name of option to set wide
	*/
	public void setWide(String name) {
		wide.add(name);
	}
	
	/**
	* Check if the given option is in a wide bar format
	*
	* @param	o	Check if this option is wide
	*/
	public boolean isWide(ModOption o) {
		return wide.contains(o.getName());
	}
	
	/**
	* Set the text formatting class for a specific option's
	* output string
	*
	* @param	option		Option to compare
	* @param	formatter	String formatter
	*/
	public void setFormatter(ModOption option, MODisplayString formatter) {
		formatters.put(option, formatter);
	}
	
	/**
	* Get the display string for an option, which will use
	* a string formatter to decide the output of the text
	* for a global value
	*
	* @param	o			Get display string
	* @return	display string
	*/
	public String getDisplayString(ModOption o) {
		return getDisplayString(o, false);
	}
	
	/**
	* Get the display string for an option, which will use
	* a string formatter to decide the output of the text
	* Value is local value if localMode is true, otherwise global
	*
	* @param	o			Get display string
	* @param	localMode	True if use local valu
	* @return	display string
	*/
	public String getDisplayString(ModOption o, boolean localMode) {
		// String formatter
		MODisplayString s		= null;
		String 			value 	= "Problem loading value";
		
		if(!formatters.containsKey(o)) {
			// Select a default object
			s = MOFormatters.defaultFormat;
		} else {
			s = formatters.get(o);
		}
		
		if((localMode) && (o.useGlobalValue())) {
			value = "GLOBAL";
		} else {
			if(localMode) {
				if(o instanceof ModSliderOption) {
					ModSliderOption o2 = (ModSliderOption) o;
					value = Integer.toString(o2.getIntValue(o2.getLocalValue()));
				} else if(o instanceof ModMappedMultiOption) {
					ModMappedMultiOption o2 = (ModMappedMultiOption) o;
					value = o2.getStringValue(o2.getLocalValue());
				} else if(o instanceof ModMultiOption) {
					value = ((ModMultiOption) o).getLocalValue();
				} else if(o instanceof ModBooleanOption) {
					ModBooleanOption o2 = (ModBooleanOption) o;
					value = o2.getStringValue(o2.getLocalValue());
				}
			} else {
				if(o instanceof ModSliderOption) {
					ModSliderOption o2 = (ModSliderOption) o;
					value = Integer.toString(o2.getIntValue(o2.getGlobalValue()));
				} else if(o instanceof ModMappedMultiOption) {
					ModMappedMultiOption o2 = (ModMappedMultiOption) o;
					value = o2.getStringValue(o2.getGlobalValue());
				} else if(o instanceof ModMultiOption) {
					value = ((ModMultiOption) o).getGlobalValue();
				} else if(o instanceof ModBooleanOption) {
					ModBooleanOption o2 = (ModBooleanOption) o;
					value = o2.getStringValue(o2.getGlobalValue());
				}
			}
		}
		
		return s.manipulate(o.getName(), value);
	}
}
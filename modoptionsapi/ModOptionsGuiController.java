package modoptionsapi;

import modoptionsapi.gui.*;

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
	private Hashtable<ModOption, LinkedList<MODisplayString>> formatters;
	
	
	/**
	* Create the controller for a given set of options
	*
	* @param	o	Set of options
	*/
	public ModOptionsGuiController(ModOptions o) {
		formatters = new Hashtable<ModOption, LinkedList<MODisplayString>>();
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
	* Sets the named option to show as a full width bar
	* instead of the default half-width
	*
	* @param	option	Option to set wide
	*/
	public void setWide(ModOption option) {
		setWide(option.getName());
	}
	
	/**
	* Check if the given option is in a wide bar format
	*
	* @param	o	Check if this option is wide
	*/
	public boolean isWide(ModOption o) {
		// Text options are always wide.
		if(o instanceof ModTextOption) {
			return true;
		} else {
			return wide.contains(o.getName());
		}
	}
	
	/**
	* Set the text formatting class for a specific option's
	* output string and removes all other formatters
	*
	* @param	option		Option to set a single formatter for
	* @param	formatter	String formatter
	*/
	public void setFormatter(ModOption option, MODisplayString formatter) {
		LinkedList<MODisplayString> newList = new LinkedList<MODisplayString>();
		newList.add(formatter);
		formatters.put(option, newList);
	}
	
	/**
	* Add a new formatter for this specific option's output string
	*
	* @since	0.6.1
	* @param	option		Option to add a formatter to
	* @param	formatter	Formatter to add to this options format queue
	*/
	public void addFormatter(ModOption option, MODisplayString formatter) {
		LinkedList<MODisplayString> list;
		if(formatters.containsKey(option)) {
			list = formatters.get(option);
		} else {
			list = new LinkedList<MODisplayString>();
			formatters.put(option, list);
		}
		
		list.add(formatter);
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
		String 			value 	= "Problem loading value";
		
		if((localMode) && (o.useGlobalValue())) {
			value = "GLOBAL";
		} else {
			if(o instanceof ModSliderOption) {
				ModSliderOption o2 = (ModSliderOption) o;
				value = Float.toString(o2.getValue(!localMode));
			} else if(o instanceof ModMappedMultiOption) {
				ModMappedMultiOption o2 = (ModMappedMultiOption) o;
				value = o2.getStringValue(o2.getValue(!localMode));
			} else if(o instanceof ModMultiOption) {
				value = ((ModMultiOption) o).getValue(!localMode);
			} else if(o instanceof ModBooleanOption) {
				ModBooleanOption o2 = (ModBooleanOption) o;
				value = o2.getStringValue(o2.getValue(!localMode));
			} else if(o instanceof ModTextOption) {
				value = ((ModTextOption) o).getValue(!localMode);
			} else if(o instanceof ModKeyOption) {
				value = ((ModKeyOption) o).getValue(!localMode).toString();
			} else {
				value = o.getValue(!localMode).toString();
			}
		}
		
		// If it has no formatters or is using a global value then show
		// a label: Value format
		if((!formatters.containsKey(o)) || ((localMode) && (o.useGlobalValue()))) {
			// Select a default object
			MODisplayString s = MOFormatters.defaultFormat;
			return s.manipulate(o.getName(), value);
		} else {
			for(MODisplayString s : formatters.get(o)) {
				value = s.manipulate(o.getName(), value);
			}
			
			return value;
		}
	}
}
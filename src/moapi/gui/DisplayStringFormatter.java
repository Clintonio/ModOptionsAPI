package moapi.gui;

import moapi.ModOption;

/**
* Used to manipulate a display string for a Mod Option.
* Moved from MODisplayStrng to gui.DisplayStringFormatter in v0.8
* 
* @author	Clinton Alexander
* @version	1.5
* @since	0.8
*/
public interface DisplayStringFormatter {
	/**
	* Manipulates the display string according
	*
	* @param	option	The option we are manipulating
	* @param	value	Current value of button
	* @return	Display string, such as with a suffix, prefix, etc
	*/
	public String manipulate(ModOption option, String value);
}
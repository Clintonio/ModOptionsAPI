package moapi.gui;

import moapi.ModOption;
import moapi.ModOptions;

import net.minecraft.src.GuiButton;

/**
* A MOAPI button representation to allow
* the storage of the option's ID inside
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.8
*/
public class Button extends GuiButton {
	/**
	* The option for this button
	*
	* @since	0.8
	*/
	private ModOption option = null;
	/**
	* The options menu for this button
	*
	* @since	0.8
	*/
	private ModOptions options = null;
	
	/**
	* Create button with the given option
	* 
	* @since	0.8
	* @param	op		Option
	*/
    public Button(int i, int j, int k, ModOption op, 
				  GuiController gui, boolean worldMode) {
        super(i, j, k, 200, 20, gui.getDisplayString(op, worldMode));
		
		this.option = op;
    }

	/**
	* Create button with the given option
	*
	* @since	0.8
	* @param	op		Option
	*/
    public Button(int i, int j, int k, int l, int i1, ModOption op, 
				  GuiController gui, boolean worldMode) {
        super(i, j, k, l, i1, gui.getDisplayString(op, worldMode));
		
		this.option = op;
    }
	
	/**
	* Create button with the given options menu
	* 
	* @since	0.8
	* @param	op		Options menu
	*/
    public Button(int i, int j, int k, ModOptions op) {
        super(i, j, k, 200, 20, op.getName());
		
		this.options = op;
    }

	/**
	* Create button with the given options menu
	*
	* @since	0.8
	* @param	op		Options menu
	*/
    public Button(int i, int j, int k, int l, int i1, ModOptions op) {
        super(i, j, k, l, i1, op.getName());
		
		this.options = op;
    }
	
	/**
	* Get the ID of the option this button represents
	*
	* @since	0.8
	* @return	ID this button represents
	*/
	public String getID() {
		if(option != null) {
			return option.getID();
		} else {
			return options.getID();
		}
	}
}
package moapi.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.FontRenderer;
import net.minecraft.client.Minecraft;

import moapi.ModOption;

/**
* A part of the inheritance chain for buttons
* for text input fields to be group identified
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since	0.7
*/
public abstract class TextInputField extends GuiButton { 
	/**
	* Option relevant to this getText() field
	*/
	protected ModOption option;
	/**
	* Whether to use global mode or not
	*/
	protected boolean global;
	/**
	* For rendering text
	*/
	protected final FontRenderer fontRenderer;
	/**
	* For determining current focus status
	*/
	private boolean isFocused					= false;
	/**
	* For tabbing functionality
	*/
	protected GuiScreen parentGuiScreen;
	/** 
	* For allowing cursor to blink
	*/
    private int cursorCounter;
	/**
	* The GUI for controlling MOAPI output
	*/
	protected GuiController gui;

	/**
	* To interface child with parent
	*/
    public TextInputField(int i, int j, int k, FontRenderer r, GuiController gui) {
		super(i, j, k, "");
		this.gui		= gui;
		enabled 		= true;
		fontRenderer 	= r;
	}
	
	/**
	* Method for handling keyboard input
	*
	* @param	c	Character types
	* @param	i	Integer representation of c
	*/
	public abstract void textboxKeyTyped(char c, int i);
	
	/**
	* Draw a textarea with a label inside and an editable text space at i,j
	*/
	public void drawButton(Minecraft minecraft, int i, int j) {
		super.drawButton(minecraft, i, j);
	}
	
	/**
	* Get the current cursor counter value
	*
	* @return	Current cursor counter value
	*/
	protected int getCursorCounter() {
		return cursorCounter;
	}
	
	/**
	* Update the cursor counter value
	*/
	public void updateCursorCounter() {
		cursorCounter++;
	}
	
	/**
	* Set whether the textarea is in focus or not
	*
	* @param	flag	True when focused
	*/
    public void setFocused(boolean flag) {
        if(flag && !isFocused) {
            cursorCounter = 0;
        }
        isFocused = flag;
    }
	
	/**
	* Check whether this text input field is focused or not
	*
	* @return	True when focused
	*/
	public boolean isFocused() {
		return isFocused;
	}
	
	/**
	* Get the option related to this getText() field
	*/
	public ModOption getOption() {
		return option;
	}
}
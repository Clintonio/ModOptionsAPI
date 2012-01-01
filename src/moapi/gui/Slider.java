package moapi.gui; 

import moapi.ModSliderOption;

import net.minecraft.src.GuiButton;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/**
* A replacement for the default MC Slider. 
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since	0.5
*/
public class Slider extends GuiButton {
	// CONSTANTS
	private static final int SMALL_WIDTH = 150;
	private static final int WIDE_WIDTH  = 200;
    public boolean dragging;
	
	private ModSliderOption			option;
	private GuiController gui;
	
	/**
	* Value determining whether we are in the local/world scope
	* for option values or not
	*/
	private boolean worldMode;
	
	/**
	* Construct a gui slider
	*
	* @param	op			Option Object this represents
	* @param	gui			Gui representing this object
	* @param	worldMode	True when using world values
	*/
    public Slider(int i, int j, int k, ModSliderOption op, 
					   GuiController gui, boolean worldMode) {
		super(i, j, k, 150, 20,  gui.getDisplayString(op, worldMode));
		this.gui		= gui;
		this.worldMode	= worldMode;
		dragging 		= false;
		option		= op;
		displayString 	= gui.getDisplayString(op, worldMode);
    }
	
	/**
	* Return the name for this slider button
	*
	* @return	Slider name
	*/
	public String getName() {
		return option.getName();
	}
	
	/**
	* Set this slider to "wide" mode
	*
	* @param	wide	Whether to be wide or not
	*/
	public void setWide(boolean wide) {
		if(wide) {
			width = WIDE_WIDTH;
		} else {
			width = SMALL_WIDTH;
		}
	}
	
	/**
	* Get the value of the slider object for this scope
	*
	* @param	option	Option we want the value for
	* @return	Value
	*/
	private float getInternalValue(ModSliderOption option) {
		float val = 0F;
		if(worldMode) {
			val = option.getLocalValue();
		} else {
			val = option.getGlobalValue();
		}
		
		val = option.transformValue(val, 0, 1);
		return val;
	}
	
	/**
	* Perform action on mouse drag
	*
	* @param	minecraft	Game instance
	* @param	i			x position of drag end
	* @param	j			y position of drag end
	*/
    protected void mouseDragged(Minecraft minecraft, int i, int j) {
		float sliderValue;
		if(drawButton) {
			if(dragging) {
				float value = (float)(i - (xPosition + 4)) / (float)(width - 8);
				value = option.untransformValue(value, 0, 1); 
				if(worldMode) {
					if(option.useGlobalValue()) {
						option.setGlobal(false);
					}
					option.setLocalValue(value);
				} else {
					option.setGlobalValue(value);
				}
				updateDisplayString();
			}
			
			sliderValue = getInternalValue(option);
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
		}
    }
	
	/**
	* Alternate Mouse press action to avoid issues with the obfuscator
	* and to allow right click checks
	*
	* @param	minecraft	Game instance
	* @param	i			x position of click
	* @param	j			y position of click
	* @param	rightClick	True if a right click
	* @return	Keep changes
	*/
    public boolean altMousePressed(Minecraft minecraft, int i, int j, boolean rightClick) {
		// Avoid the dragging action for right click
		if((rightClick) && (super.mousePressed(minecraft, i, j))) {
			return true;
		} else if(!rightClick) {
			return mousePressed(minecraft, i, j);
		} else {
			return false;
		}
	}
	
	/**
	* Mouse press action
	*
	* @param	minecraft	Game instance
	* @param	i			x position of click
	* @param	j			y position of click
	* @return	Keep changes
	*/
    public boolean mousePressed(Minecraft minecraft, int i, int j) {
		// Only accept a left click, using callbacks
		if(((!option.hasCallback()) || (option.getCallback().onClick(option)))
			&& (super.mousePressed(minecraft, i, j))) {
			float value = (float)(i - (xPosition + 4)) / (float)(width - 8);
			value = option.untransformValue(value, 0, 1); 
			if(worldMode) {
				if(((!option.hasCallback()) || (option.getCallback().onGlobalChange(false, option))) 
					&& (option.useGlobalValue())) {
					option.setGlobal(false);
				}
				option.setLocalValue(value);
			} else {
				option.setGlobalValue(value);
			}
			
			updateDisplayString();
            dragging = true;
            return true;
        } else  {
            return false;
        }
    }
	
	/**
	* Update the display string
	*
	*/
	public void updateDisplayString() {
		displayString 	= gui.getDisplayString(option, worldMode);
	}

    public void mouseReleased(int i, int j) {
        dragging = false;
    }
	
	protected int getHoverState(boolean flag) {
		return 0;
	}
}

package net.minecraft.src.modoptionsapi;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/**
* A replacement for the default MC Slider. 
*/
public class MOGuiSlider extends GuiSlider {
	// CONSTANTS
	private static final int SMALL_WIDTH = 150;
	private static final int WIDE_WIDTH  = 200;
	
	private ModSliderOption			option;
	private ModOptionsGuiController gui;
	
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
    public MOGuiSlider(int i, int j, int k, ModSliderOption op, 
					   ModOptionsGuiController gui, boolean worldMode) {
		// Just to maintain compatibility, shove MUSIC in
        super(i, j, k, EnumOptions.MUSIC, op.getName(), op.getGlobalValue()); 
		this.gui		= gui;
		this.worldMode	= worldMode;
        dragging 		= false;
		option			= op;
		displayString 	= gui.getDisplayString(op, worldMode);
		
		// Ignore the original constructor and override it
		if(worldMode) {
			sliderValue = op.getLocalValue();
		} else {
			sliderValue = op.getGlobalValue();
		}
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
	* Perform action on mouse drag
	*
	* @param	minecraft	Game instance
	* @param	i			x position of drag end
	* @param	j			y position of drag end
	*/
    protected void mouseDragged(Minecraft minecraft, int i, int j) {
		float sliderValue;
        if(enabled2) {
			if(dragging) {
				if(worldMode) {
					if(option.useGlobalValue()) {
						option.setGlobal(false);
					}
					option.setLocalValue((float)(i - (xPosition + 4)) / (float)(width - 8));
				} else {
					option.setGlobalValue((float)(i - (xPosition + 4)) / (float)(width - 8));
				}
				displayString 	= gui.getDisplayString(option, worldMode);
			}
			
			if(worldMode) {
				sliderValue = option.getLocalValue();
			} else {
				sliderValue = option.getGlobalValue();
			}
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int)(sliderValue * (float)(width - 8)) + 4, yPosition, 196, 66, 4, 20);
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
		if(((!option.hasCallback()) || (option.getCallback().onClick(option)))
			&& (super.mousePressed(minecraft, i, j))) {
			if(worldMode) {
				if(((!option.hasCallback()) || (option.getCallback().onGlobalChange(false, option))) 
					&& (option.useGlobalValue())) {
					option.setGlobal(false);
				}
				option.setLocalValue((float)(i - (xPosition + 4)) / (float)(width - 8));
			} else {
				option.setGlobalValue((float)(i - (xPosition + 4)) / (float)(width - 8));
			}
			
			displayString 	= gui.getDisplayString(option, worldMode);
            dragging = true;
            return true;
        } else  {
            return false;
        }
    }
}

package moapi.gui;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

import net.minecraft.client.Minecraft;

import moapi.ModOption;
import moapi.ModTextOption;

/**
* GUI for getText() field within MOAPI
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since	0.7
*/
public class TextField extends TextInputField {
	
    public TextField(int id, GuiScreen guiscreen, FontRenderer fontrenderer, int i, int j,
						ModTextOption op, GuiController gui, boolean global) {
		super(id, i, j, fontrenderer, gui);
        parentGuiScreen = guiscreen;
        xPosition = i - 50;
        yPosition = j;
        width = 300;
        height = 20;
		option = op;
		this.global = global;
    }

	/**
	* Sets the getText() for this option and button
	*
	* @param	s		New string to display and use for option value
	*/
    protected void setText(String s) {
		int maxlen = ((ModTextOption) option).getMaxLength();
		if((s.length() > maxlen) && (maxlen > 0)) {
			s = s.substring(0, maxlen - 1);
		}
		
		((ModTextOption) option).setValue(s, global);
	}
	
	/**
	* Return getText() value for this field
	*
	* @return 	Current getText() value
	*/
    public String getText() {
		return ((ModTextOption) option).getValue(global);
    }

    public void textboxKeyTyped(char c, int i) {
		String 	text = getText();
		String 	s 	= GuiScreen.getClipboardString();
		int 	max = ((ModTextOption) option).getMaxLength();
		int 	j;
		
        if(enabled && isFocused()) {
			/* Method no longer in 1.2.4 - if(c == '\t') {
				parentGuiScreen.selectNextField();
			} else */ if(c == '\026') {
				if(s == null) {
					s = "";
				}
				j = 32 - text.length();
				if(j > s.length()) {
					j = s.length();
				}
				if(j > 0) {
					text += s.substring(0, j);
				}
			} else if(i == 14 && text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			} else if(ChatAllowedCharacters.allowedCharacters.indexOf(c) >= 0 && 
				((text.length() < max) || (max == 0))) {
				text += c;
			}
			
			setText(text);
		}
    }

	/**
	* Draw a textarea with a label inside and an editable text space
	*/
    public void drawButton(Minecraft minecraft, int i, int j) {
		String 	text		= getText();
		String	name		= option.getName();
		
		int 	maxlen		= ((ModTextOption) option).getMaxLength();
		int 	len			= text.length();
		int		padding		= 30;
		
		String 	counterStr	= (maxlen > 0) ?  "(" + len + "/" + maxlen + ")" : "";
		
		int 	nameWidth	= fontRenderer.getStringWidth(name);
		int 	textWidth	= fontRenderer.getStringWidth(text);
		int		counterWidth= fontRenderer.getStringWidth(counterStr);
		
		// Remove excess padding when there is no counter
		if(maxlen <= 0) {
			padding = padding - 10;
		}
		
		// Reduce string until it is a decent length.
		// Don't worry about optimising too much, can optimise
		// if it becomes an issue
		if(nameWidth + textWidth + counterWidth + padding > width) {
			while(nameWidth + textWidth + counterWidth + padding > width) {	
				text = text.substring(1, len - 1);
				len--;
				textWidth = fontRenderer.getStringWidth(text);
			}
		}
		
        drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0xffa0a0a0);
        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xff000000);
        if(enabled) {
			drawString(fontRenderer, option.getName(), xPosition + 4, yPosition + (height - 8) / 2, 0x707070);
            boolean flag = isFocused() && (getCursorCounter() / 6) % 2 == 0;
			
            drawString(fontRenderer, (new StringBuilder()).append(text).append(flag ? "_" : "").toString(), 
					   xPosition + nameWidth + 10, yPosition + (height - 8) / 2, 0xe0e0e0);
			
		
			// Add a max length counter
			if(maxlen > 0) {
				drawString(fontRenderer, counterStr, 
				   xPosition + 300 - 5 - counterWidth,
				   yPosition + (height - 8) / 2, 0x707070);
			}
        } else {
            drawString(fontRenderer, getText(), xPosition + 4, yPosition + (height - 8) / 2, 0x707070);
        }
    }
}

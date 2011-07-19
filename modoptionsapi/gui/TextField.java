package modoptionsapi.gui;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

import net.minecraft.client.Minecraft;

import modoptionsapi.ModOption;
import modoptionsapi.ModTextOption;
import modoptionsapi.ModOptionsGuiController;

/**
* GUI for getText() field within MOAPI
*
* @author	Clinton Alexander
* @version	1.0.0.0
* @since	0.7
*/
public class TextField extends GuiButton {
	/**
	* Option relevant to this getText() field
	*/
	private ModTextOption option;
	/**
	* Whether to use global mode or not
	*/
	private boolean global;
	
    public TextField(int id, GuiScreen guiscreen, FontRenderer fontrenderer, int i, int j,
						ModTextOption op, ModOptionsGuiController gui, boolean global) {
		super(id, i, j, "");
        isFocused = false;
        enabled = true;
        parentGuiScreen = guiscreen;
        fontRenderer = fontrenderer;
        xPosition = i;
        yPosition = j;
        width = 200;
        height = 20;
		option = op;
		this.global = global;
    }
	
	/**
	* Get the option related to this getText() field
	*/
	public ModOption getOption() {
		return option;
	}

	/**
	* Sets the getText() for this option and button
	*
	* @param	s		New string to display and use for option value
	*/
    protected void setText(String s) {
		if(s.length() > option.getMaxLength()) {
			s = s.substring(0, option.getMaxLength());
		}
		if(global) {
			option.setGlobalValue(s);
		} else {
			option.setLocalValue(s);
		}
	}
	
	/**
	* Return getText() value for this field
	*
	* @return 	Current getText() value
	*/
    public String getText() {
		if(global) {
			return option.getGlobalValue();
		} else {
			return option.getLocalValue();
		}
    }

    public void updateCursorCounter() {
        cursorCounter++;
    }

    public void textboxKeyTyped(char c, int i) {
		String 	text = getText();
		String 	s 	= GuiScreen.getClipboardString();
		int 	j;
		
        if(enabled && isFocused) {
			if(c == '\t') {
				parentGuiScreen.selectNextField();
			} else if(c == '\026') {
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
				(text.length() < option.getMaxLength() 
				|| option.getMaxLength() == 0)) {
				text += c;
			}
			
			setText(text);
		}
    }

    public void setFocused(boolean flag) {
        if(flag && !isFocused) {
            cursorCounter = 0;
        }
        isFocused = flag;
    }

    public void drawButton(Minecraft minecraft, int i, int j) {
        drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, 0xffa0a0a0);
        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xff000000);
        if(enabled) {
            boolean flag = isFocused && (cursorCounter / 6) % 2 == 0;
            drawString(fontRenderer, (new StringBuilder()).append(getText()).append(flag ? "_" : "").toString(), xPosition + 4, yPosition + (height - 8) / 2, 0xe0e0e0);
        } else {
            drawString(fontRenderer, getText(), xPosition + 4, yPosition + (height - 8) / 2, 0x707070);
        }
    }

    private final FontRenderer fontRenderer;
    private int cursorCounter;
    public boolean isFocused;
    private GuiScreen parentGuiScreen;
}

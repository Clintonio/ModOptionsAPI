package moapi.gui;

import moapi.*;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
* A scrollable screen for modoptionsapi
*
* @author	Clinton Alexander (with credit to Mojang for the drawscreen code)
* @since	0.6
*/
public class ModMenu extends GuiScreen {
	/**
	* Currently selected button
	*/
	protected GuiButton curButton = null;
	/**
	* Slider positional value
	*/
	private int sliderMiddle = -1;
	/**
	* True if slider dragging in progress
	*/
	private boolean draggingSlider = false;
	/**
	* Title of this menu
	*/
    protected String screenTitle;
    private GuiScreen parentScreen;
    private GameSettings options;
	
	/**
	* Current set of options that we are displaying
	*/
	private ModOptions 				modOptions 	= null;
	private GuiController 			gui 		= null;
	
	/**
	* Whether we are in world specific mode or not
	*/
	private boolean	worldMode = false;
	/**
	* Whether we are in multiplayer mode
	*/
	private boolean multiplayerWorld = false;
	/**
	* World name
	*/
	private String worldName;
	
	/**
	* Initilise options menu gui screen
	*/
    public ModMenu(GuiScreen guiscreen) {
		this(guiscreen, "Mod Options List", false, false);
    }
	
	/**
	* Initialise world options menu with a named world
	*/
	public ModMenu(GuiIngameMenu guiscreen, String name, boolean mult) {
		this(guiscreen, "World Specific Mod Options", true, mult);
		worldName = name;
	}
	
	/**
	* Initialise a particular set of options gui
	* 
	* @param	name	Name of world loaded
	* @param	multi	True if in a multiplayer world
	*/
	public ModMenu(GuiScreen guiscreen, ModOptions options, String name, boolean multi) {
        this(guiscreen, options.getName() + " Options", true, multi);
		modOptions 		= options;
		worldName		= name;
		gui 			= modOptions.getGuiController();
	}
	
	/**
	* Initialise a particular set of options gui
	*/
	public ModMenu(GuiScreen guiscreen, ModOptions options) {
        this(guiscreen, options.getName() + " Options", false, false);
		modOptions 		= options;
		gui 			= modOptions.getGuiController();
	}
	
	/**
	* Initialise settings
	*/
	private ModMenu(GuiScreen parent, String title, 
						  boolean world, boolean mult) {
		super();
		parentScreen 		= parent;
		worldMode 			= world;
		screenTitle			= title;
		multiplayerWorld	= mult;
		
		if(!Keyboard.areRepeatEventsEnabled()) {
			Keyboard.enableRepeatEvents(true);
		}
	}
	
    public void initGui() {
		if(modOptions == null) {
			// Load only relevant mods
			ModOptions[] options = new ModOptions[0];
			if(multiplayerWorld) {
				options = ModOptionsAPI.getMultiplayerMods();
			} else if(worldMode) {
				options = ModOptionsAPI.getSingleplayerMods();
			} else {
				options = ModOptionsAPI.getAllMods();
			}
			loadModList(options);
		} else {
			loadModOptions();
		}
    }
	
	/**
	* Loads the initial list of mods
	*
	* @param	options		List of mods to display
	*/
	private void loadModList(ModOptions[] options) {
		int xPos = width / 2 - 100;
		int yPos;
		
		for(int i = 0; i < options.length; i++) {
			yPos = height / 6 + (i * 24) + 12;
			controlList.add(new Button(i, xPos, yPos, options[i]));
		}
		
		yPos = height / 6 + 168;
		controlList.add(new GuiButton(200, xPos, yPos, "Done"));
	}
	
	/**
	* Loads a set of mod options buttons
	*/
	private void loadModOptions() {
		screenTitle 				= modOptions.getName();
		ModOption[] ops 			= modOptions.getOptions();
		ModOptions[] subOps;
		
		// Pick a set of sub options based on the menu
		if(!worldMode) {
			subOps = modOptions.getSubOptions();
		} else if(multiplayerWorld) {
			subOps = modOptions.getMultiplayerSubOptions();
		} else {
			subOps = modOptions.getSingleplayerSubOptions();
		}
		
		// ID offset for an option
		int id = 0;
		// This is for positioning the elements in two columns
		int pos = 2;
		for(ModOption op : ops) {
			addModOptionButton(op, id, pos);
			
			if(gui.isWide(op)) {
				pos = pos + 2 + (pos % 2);
			} else {
				pos++;
			}
			
			id++;
		}
		
		int xPos, yPos;
		// Add the sub options
		for(int x = 0; x < subOps.length; x++) {
			// Calculate position of full width bar
			xPos = width / 2 - 100;
			yPos = height / 6 + 24 * ((pos + 1) >> 1);
			controlList.add(new Button(x + 101, xPos, yPos, subOps[x]));
			pos = pos + 2 + (pos % 2);
		}
		
		controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, "Done"));
	}
	
	/**
	* Adds an option button to the GUI
	*
	* @param	op 	Option
	* @param	id	id for buttn
	* @param	pos	Used for positioning a button
	*/
	private void addModOptionButton(ModOption op, int id, int pos) {
		int xPos, yPos;
		// Display string for option
		boolean isWide 	= gui.isWide(op);
		
		if(!isWide) {
			// Calculate position of half width bar. Takes up one position
			xPos = (width / 2 - 155) + (pos % 2) * 160;
			yPos = height / 6 + 24 * (pos >> 1);
		} else {
			// Calculate position of full width bar
			xPos = width / 2 - 100;
			yPos = height / 6 + 24 * ((pos + 1) >> 1);
		}
		
		if(op instanceof ModSliderOption) {
			Slider btn = new Slider(id, xPos, yPos, (ModSliderOption) op, gui, worldMode);
			btn.setWide(isWide);
			controlList.add(btn);
		} else if(op instanceof ModTextOption) {
			controlList.add(new TextField(id, this, fontRenderer, xPos, yPos, 
						   (ModTextOption) op, gui, !worldMode));
		} else if(op instanceof ModKeyOption) {
			KeyBindingField btn = new KeyBindingField(id, this, fontRenderer, xPos, yPos,
								  (ModKeyOption) op, gui, !worldMode);	
			controlList.add(btn);
			
			btn.setWide(isWide);
		} else if(!isWide) {
			// 150, 20 is the width/ height for a small button
			controlList.add(new Button(id, xPos, yPos, 150, 20, op, gui, worldMode));
		} else {
			controlList.add(new Button(id, xPos, yPos, op, gui, worldMode));
		}
	}
	
	/**
	* Draw our slider based options screen
	*/
    public void drawScreen(int i, int j, float f)  {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
		
		if(sliderMiddle == -1) {
			setInitialSlider();
		} else if(getSliderTop() > getSliderAreaTop()) {
			sliderMiddle = getUpperSliderBound();
		} else if(getSliderBottom() < getSliderAreaBottom()) {
			setInitialSlider();
		}
		
		addButtons(i, j);
		
		int sliderTop 		= getSliderTop();
		int sliderBottom 	= getSliderBottom();
		int sliderLeft 		= getSliderLeft();
		int sliderRight 	= getSliderRight();
		
		// Draw the container
		drawRect(sliderLeft, getSliderAreaBottom(), sliderRight, getSliderAreaTop(), 0x80000000);
		// Draw the slider
		drawRect(sliderLeft, sliderBottom, sliderRight, sliderTop, 0xffcccccc);
		sliderDragged(i, j);
	}
	
	/**
	* Adds the buttons to the screen
	*
	* @param	i
	* @param	j
	*/
	private void addButtons(int i, int j) {
		// Boundes for content
		int contentTop 		= getContentTop();
		int contentBottom 	= getContentBottom();
		int bottom 			= getSliderAreaBottom();
		int top				= getSliderAreaTop();
		
		for(int k = 0; k < controlList.size(); k++) {
			GuiButton btn = (GuiButton) controlList.get(k);
			if(btn.id != 200) {
				int y = btn.yPosition;
				btn.yPosition = y - contentBottom;
				if((btn.yPosition > bottom) && ((btn.yPosition + 20) < top)) {
					btn.drawButton(mc, i, j);
				}
				btn.yPosition = y;
			} else {
				btn.drawButton(mc, i, j);
			}
        }
	}
	
	/**
	* Control the mouse clicks so that we can let right
	* click turn on "global" mode
	*
	* @param	i	x pos
	* @param	j	y pos
	* @param	k	1 for right click, 0 for left
	*/
    protected void mouseClicked(int i, int j, int k) {
		setCurrentButton(null);
		// left click
		if(k == 0) {
			if((i > getSliderLeft()) && (i < getSliderRight()) 
				&& (j > getSliderBottom()) && (j < getSliderTop())) {
				setSliderMiddle(j);
			} else if((i > getSliderLeft()) && (i < getSliderRight()) 
				&& (j > getSliderAreaBottom()) && (j < getSliderAreaTop())) {
				setSliderMiddle(j);
			} else {
				// Pick a button
				for(int l = 0; l < controlList.size(); l++) {
					GuiButton guibutton = (GuiButton)controlList.get(l);
					if(buttonPressed(guibutton, i, j, false)) {
						setCurrentButton(guibutton);
					}
				}
			}
		}
		// Right click.
        if(k == 1) {
			for(int l = 0; l < controlList.size(); l++) {
				GuiButton guibutton = (GuiButton)controlList.get(l);
				if(buttonPressed(guibutton, i, j, true)) {
					altActionPerformed(guibutton);
                    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					setCurrentButton(guibutton);
				}
			}
		}
    }
	
	/**
	* Ensures that when escape is pressed
	* that the changed options are saved.
	*/
    protected void keyTyped(char c, int i) {
        if(i == 1) {
			changeScreen(null);
        } else if(curButton instanceof TextInputField) {
			handleTextAction((TextInputField) curButton, c, i);
		}
    }
	
	/**
	* Handle a text input action to the given button
	*
	* @param	txt		Text area being updated
	* @param	c		Character input
	* @param	i		Integer value of input
	*/
	private void handleTextAction(TextInputField txt, char c, int i) {
		txt.textboxKeyTyped(c, i);
	}
		
	/**
	* Mouse has left clicked the given button
	*
	* @param	guibutton	Button left clicked
	*/
    protected void actionPerformed(GuiButton guibutton) {
		// We have clicked the "Done" button
        if(guibutton.id == 200) {
			changeScreen(parentScreen);
		// We are in the mod menu and picking a mod
        } else if(modOptions == null) {
			Button btn 			= (Button) guibutton;
			// Choose the type of mod options
			ModOptions modOp 	= ModOptionsAPI.getModOptions(btn.getID());
			
			if(worldMode) {
				mc.displayGuiScreen(new ModMenu(this, modOp, worldName, multiplayerWorld));
			} else {
				mc.displayGuiScreen(new ModMenu(this, modOp));
			}
		// A slider has been dragged
        } else if((guibutton.id < 100 && (guibutton instanceof Slider))) {	
			// Ignore, handled internally
		// A sub options button has been pressed
		} else if((guibutton.id > 100) && (guibutton.id < 200)) {
			Button btn 			= (Button) guibutton;
			ModOptions modOp 	= modOptions.getSubOption(btn.getID());
			
			if(worldMode) {
				mc.displayGuiScreen(new ModMenu(this, modOp, worldName, multiplayerWorld));
			} else {
				mc.displayGuiScreen(new ModMenu(this, modOp));
			}
		// A button has been pressed
		} else if(guibutton.id < 100) {
			optionButtonPressed(guibutton);
		}
    }
	
	/**
	* Button right clicked on the given button
	*
	* @param	guibutton	button pressed
	*/
	protected void altActionPerformed(GuiButton guibutton) {
		if((modOptions != null) && (guibutton.id < 100) && (worldMode)) {
			ModOption[] ops = modOptions.getOptions();
			ModOption option = ops[guibutton.id];
		
			option.setGlobal(!option.useGlobalValue());
			
			updateDisplayString(option, guibutton);
		// User right clicks menu - resets all values to global
		} else if((worldMode) && ((modOptions == null) || (guibutton.id < 200))) {
			ModOptions modOp = null;
			if(modOptions == null) { 
				modOp = ModOptionsAPI.getModOptions(guibutton.displayString);
			} else {
				modOp = modOptions.getSubOption(guibutton.displayString);
			}
			
			if(modOp != null) {
				// Sets all values to global
				// in applicable menus
				modOp.globalReset(true);
			}
		}
	}
	
	/**
	* More control over the mouse
	* Called when mouse is unclicked
	*
	* @param	i	x pos
	* @param	j	y pos
	* @param	k	1 for right click, 0 for left
	*/
    protected void mouseMovedOrUp(int i, int j, int k) {
		// Text fields need constant focus, so do not set curbutton to null
		// when a textbox is focused
		if(!(curButton instanceof TextInputField) && curButton != null
				 && k == 0) {
            curButton.mouseReleased(i, j);
            curButton = null;
        } else if((draggingSlider) && (k == 0)){
			draggingSlider = false;
		}
    }
	
	/**
	* What to do on screen updates
	*/
	public void updateScreen() {
		super.updateScreen();
		
		// Update all text field cursor blinking
		for(Object obj : controlList) {
			if(obj instanceof TextInputField) {
				((TextInputField) obj).updateCursorCounter();
			}
		}
	}
	
	/**
	* Change the screen and perform cleanup actions
	*
	* @param	screen
	*/
	public void changeScreen(GuiScreen screen) {
		saveChanges();
		mc.displayGuiScreen(screen);
		// Re-disable repeat keypress
		if(!(screen instanceof ModMenu)) {
			Keyboard.enableRepeatEvents(false);
		}
		// If ingame, set to ingame focus
		if(worldMode && (screen == null)) {
			mc.setIngameFocus();
		}
	}
	
	/**
	* Drag the slider about
	*
	* @param	i	x pos
	* @param	j	y pos
	*/
	private void sliderDragged(int i, int j) {
		if(draggingSlider) {
			setSliderMiddle(j);
		}
	}
	
	/**
	* Set the currently selected button
	*
	* @param	btn		Button
	*/
	private void setCurrentButton(GuiButton btn) {
		if(curButton instanceof TextInputField) {
			((TextInputField) curButton).setFocused(false);
		}
		
		curButton = btn;
		
		if(curButton instanceof TextInputField) {
			((TextInputField) curButton).setFocused(true);
		}
		
		if(curButton != null) {
			mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
			actionPerformed(curButton);
		}
	}

	
	/**
	* Check if a button has been pressed
	*
	* @param	btn		Button
	* @return	true if pressed
	*/
	protected boolean buttonPressed(GuiButton btn, int i, int j) {
		return buttonPressed(btn, i, j, false);
	}
	
	/**
	* Check if a button has been pressed
	*
	* @param	btn			Button
	* @param	rightClick	True if right click
	* @return	true if pressed
	*/
	protected boolean buttonPressed(GuiButton btn, int i, int j, boolean rightClick) {
		int contentBottom 	= getContentBottom();
		int bottom 			= getSliderAreaBottom();
		int top				= getSliderAreaTop();
		boolean flag 		= false;
		
		if(btn.id != 200) {
			int y = btn.yPosition;
			btn.yPosition = y - contentBottom;
			if((btn.yPosition > bottom) && ((btn.yPosition + 20) < top)) {
				// Need to send the type of click to the slider due to
				// dragging behaviour
				if(btn instanceof Slider) {
					flag = ((Slider) btn).altMousePressed(mc, i, j, rightClick);
				} else {
					if(btn.mousePressed(mc, i, j)) {
						flag = true;
					}
				}
			}
			btn.yPosition = y;
		} else {
			flag = btn.mousePressed(mc, i, j);
		}
		
		return flag;
	}
	
	/**
	* Set the initial slider value
	*/
	private void setInitialSlider() {
		sliderMiddle = getLowerSliderBound();
	}
	
	/**
	* Set the slider middle value
	*
	* @param	val		New val
	*/
	private void setSliderMiddle(int val) {
		if(val > getUpperSliderBound()) {
			sliderMiddle = getUpperSliderBound();
		} else if(val < getLowerSliderBound()) {
			sliderMiddle = getLowerSliderBound();
		} else {
			sliderMiddle = val;
		}
		
		draggingSlider = true;
		setCurrentButton(curButton);
	}
	
	/**
	* Gets the upper slider bound
	*
	* @return	Upper slider bound
	*/
	private int getUpperSliderBound() {
		return getSliderAreaTop() - (getSliderHeight() / 2);
	}
	
	/**
	* Gets the lower slider bound
	*
	* @return	Lower slider bound
	*/
	private int getLowerSliderBound() {
		return getSliderAreaBottom() + (getSliderHeight() / 2);
	}
		
	
	/**
	* Returns the top of the slider
	* 
	* @return	top of slider
	*/
	private int getSliderTop() {
		return sliderMiddle + (getSliderHeight() / 2);
	}
	
	/**
	* Returns the left of the slider
	*
	* @return	left of the slider
	*/
	private int getSliderBottom() {
		return sliderMiddle - (getSliderHeight() / 2);
	}
	
	/**
	* Returns the height of the slider
	*
	* @return	slider height
	*/
	private int getSliderHeight() {
		int contHeight = getContentHeight();
		int areaHeight = getSliderAreaHeight();
		
		if(contHeight < areaHeight) {
			return areaHeight;
		} else {
			return (int) (((double) areaHeight / (double) contHeight) * (double) areaHeight);
		}
	}
	
	/**
	* Returns the content height
	*
	* @return	Content height
	*/
	private int getContentHeight() {
		int height = 1;
		int bottom = getSliderAreaBottom();
		for(int k = 0; k < controlList.size(); k++) {
            GuiButton guibutton = (GuiButton)controlList.get(k);
			
			if(guibutton.id != 200) {
				if(guibutton.yPosition - bottom > height) {
					height = guibutton.yPosition - bottom;
				}
			}
        }
		// An extra 100 for tolerance
		return height + 100;
	}
	
	/**
	* Get the top of the content area
	*
	* @return	top of the content area
	*/
	private int getContentTop() {
		int top = getSliderTop() - getSliderAreaBottom();
		int contHeight = getContentHeight();
		int areaHeight = getSliderAreaHeight();
		
		double prop;
		if(contHeight < areaHeight) {
			prop = 1;
		} else {
			prop = (double) getContentHeight() / (double) getSliderAreaHeight();
		}
		
		return (int) ((double) top * prop);
	}
	
	/**
	* Get the bottom of the content area
	*
	* @return	bottom of the content area
	*/
	private int getContentBottom() {
		int bot = getSliderBottom() - getSliderAreaBottom();
		int contHeight = getContentHeight();
		int areaHeight = getSliderAreaHeight();
		
		double prop;
		if(contHeight < areaHeight) {
			prop = 1;
		} else {
			prop = getContentHeight() / getSliderAreaHeight();
		}
		
		return (int) ((double) bot * prop);
	}
	
	/**
	* Returns the left of the slider
	*
	* @return	left of the slider
	*/
	private int getSliderLeft() {
		return width - 20;
	}
	
	/**
	* Returns the left of the slider
	*
	* @return	left of the slider
	*/
	private int getSliderRight() {
		return width - 10;
	}
	
	/**
	* Returns the slider area top
	*
	* @return	slider area top
	*/
	private int getSliderAreaTop() {
		return height - 40;
	}
	
	/**
	* Returns the slider area bottom
	*
	* @return	slider area bottom
	*/
	private int getSliderAreaBottom() {
		return 40;
	}
	
	/**
	* Returns the slider area height
	*
	* @return	Slider area height
	*/
	private int getSliderAreaHeight() {
		int height = (getSliderAreaTop() - getSliderAreaBottom());
		return (height > 0) ? height : 1;
	}
	
	/**
	* Update a value upon an option button press
	*
	* @param	btn		Button that was pressed
	*/
	private void optionButtonPressed(GuiButton btn) {
		// The order of the array is always the same. The ID
		// is used above to identify. This is a bit of a hack.
		ModOption[] ops = modOptions.getOptions();
		ModOption option = ops[btn.id];
		
		// Callback for when a button is clicked
		if((!option.hasCallback()) || (option.getCallback().onClick(option))) {
			// If option is global on, turn it to a local enabled value
			if((worldMode) && (option.useGlobalValue()) 
				&& ((!option.hasCallback()) 
					|| (option.getCallback().onGlobalChange(false, option)))) {
				option.setGlobal(false);
			}
			
			updateDisplayString(option, btn);
		}
	}
	
	/**
	* Updates the display string for a given button
	*
	* @param	btn		Button pressed
	* @param	option	Option represented by button
	*/
	private void updateDisplayString(ModOption option, GuiButton btn) {
		if(option instanceof ModMultiOption) {
			btn.displayString = optionPressed((ModMultiOption) option);
		} else if(option instanceof ModBooleanOption) {
			btn.displayString = optionPressed((ModBooleanOption) option);
		} else if(option instanceof ModMappedOption) {
			btn.displayString = optionPressed((ModMappedOption) option);
		} else if(btn instanceof Slider) {
			((Slider) btn).updateDisplayString();
		}
	}
	
	/**
	* A multi option has been pressed, update the value
	*
	* @param	op	Option pressed
	* @return	New text value
	*/
	private String optionPressed(ModMultiOption op) {
		// Use local or global values
		if(!worldMode) {
			String nextVal = op.getNextValue(op.getGlobalValue());
			op.setGlobalValue(nextVal);
		} else {
			String nextVal = op.getNextValue(op.getLocalValue());
			op.setLocalValue(nextVal);
		}
		
		return gui.getDisplayString(op, worldMode);
	}
	
	/**
	* A boolean option has been pressed, update the value
	*
	* @param	op	Option pressed
	* @return	New text value
	*/
	private String optionPressed(ModBooleanOption op) {
		if(!worldMode) {
			boolean nextVal = !op.getGlobalValue();
			op.setGlobalValue(nextVal);
		} else {
			boolean nextVal = !op.getLocalValue();
			op.setLocalValue(nextVal);
		}
		
		return gui.getDisplayString(op, worldMode);
	}
	
	/**
	* A maped multi option has been pressed, update the value
	*
	* @param	op	Option pressed
	* @return	New text value
	*/
	private String optionPressed(ModMappedOption op) {		
		if(!worldMode) {
			Integer nextVal = op.getNextValue(op.getGlobalValue());
			op.setGlobalValue(nextVal);
		} else {
			Integer nextVal = op.getNextValue(op.getLocalValue());
			op.setLocalValue(nextVal);
		}
		
		return gui.getDisplayString(op, worldMode);
	}
	
	/**
	* Save changes when a menu is exited if necessary
	*/
	private void saveChanges() {	
		if(modOptions != null) {
			if(worldMode) {
				modOptions.save(worldName, multiplayerWorld);
			} else {
				modOptions.save();
			}
		}
	}
}

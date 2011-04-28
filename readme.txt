ModOptionsAPI

Author: Clinton Alexander
Version: 0.6
Description: An API for allowing simple mod options menus in Minecraft Options menu.

For non-developers, please go to "Installation"
For developers, please go to Developers

-----------------
Installation
-----------------

1) Find your minecraft.jar (check note below)*

2) Open the minecraft.jar file in WinRAR or 7zip (do not extract it)

3) Extract the contents of the ModOptionsAPI ZIP file

4) Copy the files into your minecraft.jar 

6) Delete META-INF folder IF it exists in the minecraft.jar

7) Run Minecraft


* Windows: <your user folder>/AppData/Roaming/.minecraft/bin/
* Mac: Home -> Library -> Application Support -> Minecraft -> bin
* Linux: ~/.minecraft/bin/

------------------
Compatibility
------------------

This is not a ModLoader mod but is compatible and recommended.
Known issues: GuiAPI requires a patch. To patch copy the files in the "guiapi patch" folder
into the Minecraft.jar file AFTER following steps 1-7 of the installation.

------------------
Developers
------------------

The sourcecode for mcp v212 is included in /src/ if you downloaded the developer version

There are two ways to begin using this mod in your mod:
1) Use a modloader mod and put the initialisation code in your modloader mod constructor
2) Place the initialisation code else where in your code that you know it will run before the user opens the options menu

(Number 1 is obviously prefered, and thus this is mod loader compatible, but does not NEED modloader to run)

An example source is provided in /examples/. It will not compile, it is merely example code.

JavaDoc is in /src/doc/, navigate to index.html for the JavaDoc index.

If you wish to begin understanding the ModOptionsAPI source, first check src/modoptionsapi/ModOptionsAPI.java.

API Features:
- Boolean/ Toggle Options
- List/ Multi-value options
- Saves conveniently to disk in consistent save file
- Well designed and consistent API with both a simple and more advanced interface available.
- Includes all source and no files are obfuscated. 
- Fully JavaDoc commented where appropriate.
- Simple GUI API interface; no complex widgets or unexpected behaviour.
- Individual Server and World values
- Super simple API using a new ModOptions file format!
- Scrolling menus are always available
- Callbacks so you have more control over value changes

------------------
Version History
------------------

0.6
(Overview)
- Added callbacks
- Updated for MCP212/Minecraft Beta 1.5_01
- Added scrolling in menus

(API Additions)
- MOCallback abstract class

(Improvements)
- Some bug fixes, details forgotten due to 1.5 release (>.>)

(File Changes)
- GuiModOptions.java -> GuiModScrollOptions.java (not a part of the public API, part of implementation)

0.5.0.2
- Fixed bug in sliders preventing local mode from activating 

0.5.0.1
- Fixed a bug in sliders preventing them from working and saving values

0.5
(Overview)
- Added a new file format called a .modoptions to easily write your own options for this API with only 5 lines of actual Java, which before would have used 50+
- Made 1.4_01 compatible 

(Files)
- Added new example file called Test.modoptions
- Added new exception type

(Additions)
- New exception type: MOMissingModException

- Added ModOptionsAPI.addMod(String) to load your mod options from a new .modoptions file format

(Improvements)
- Improved IncompatibleOptionTypeException

0.4
(Overview)
- Added world specific and server specific values
- Moved user files to a separate ZIP file
- Moved dev files to a separate ZIP file

(Files)
- Added 3 new files to files and compatiblity patch for world/server options

(Additions)
- Added ModOptions.getMultiplayerSubOptions() for getting submenus that can use server specific options
- Added ModOptions.getSingleplayerSubOptions() for getting submenus that can use world specific options
- Added ModOptions.setMultiplayerMode(boolean) for setting if the menu can use server specific options
- Added ModOptions.setSingleplayerMode(boolean) for setting if the menu can use world specific options
- Added ModOptions.isMultiplayerMod() for checking if the menu can use server specific options
- Added ModOptions.isSingleplayerMod() for checking if the menu can use world specific options
- Added ModOptions.loadValues(String, boolean) for compatibility with new save options
- Added ModOptions.save(String, boolean) for compatibility with new save options

- Added ModOption.localValue for representing the current local value
- Added ModOption.global to flag whether to use global value or not
- Added ModOption.setLocalValue(E)
- Added ModOption.getLocalValue()
- Added ModOption.getGlobalValue()
- Added ModOption.setLocalValue(E)
- Added ModOption.useGlobalValue() for checking if getValue() is to use the global value or not
- Added ModOption.setGlobal(boolean) for settng whether getValue() is to use the global value

- Added ModSliderOption.setGlobalValue(Float) override
- Added ModSliderOption.setLocalValue(Float) override
- Added ModSliderOption.getFloatValue(float) for checking a given value
- Added ModSliderOption.getIntValue(float) for checking a given value 

- Added ModOptionsAPI.getMultiplayerMods() for getting mods that have server specific options enabled
- Added ModOptionsAPI.getSingleplayerMos() for getting mods that have world specific options enabled

- Added ModOptionsGuiController.getDisplayString(ModOption, boolean) to denote whether to use local value or not

(Non Breaking Changes)
- Changed ModOptions.loadValues() to load only global values
- Changed ModOptions.save() to save only global values

- Changed ModOption.setValue(E) to set the current scope value
- Changed ModOption.getValue() to use the global value only when ModOptions.global is true

- Changed ModSliderOption.getFloatValue() to use current scope value
- Changed ModSliderOption.getIntValue() to use current scope value
- Changed ModSliderOption.setIntValue(int) to only current scope value to 1st param

- Changed ModOptionsGuiController.getDisplayString(ModOption) to use global value only

0.3.3.2
- Removed bug causing options to not be saved when "esc" is pressed.

0.3.3.1
- Improved ordering of elements in API. Ordered in opposite direction to the way they are added. This will be flipped later. Previous order was Chaotic due to use of Hashtable. Using more ordered LinkedHashMap.
- Removed output to terminal by ModOptionsAPI (hi and hid were being output)

0.3.3
- Compatible with beta 1.4
- Compatibility with GUIAPI probably temporarily lost.

0.3.2
- Fixed a menu display bug
- Set a constructor for ModMappedMultiOption to private. It was not meant to be public in the first place.

0.3.1
- Fixed a menu display bug

0.3
- Added ModMappedMultiOption for mapping strings to integer keys for more advanced case/ arithmetic comparisons
- Re-implemented backend to ensure ordering of options is personalised and not alphabetical.
- Added GUI controller API to allow for more control over display of your options
- Added a SuffixFormat in the new MOFormatter class for those wishing to append suffixes to their values.
- Improved general backend
- Fixed slider bugs
- Improved entire slider class

0.2
- Changed getOptionValue return type from Object to String for easier parsing
- Added new constructor for ModBooleanOption of: (String name, String OnVal, String OffVal)
- Bug fixes
-- Improved saving - no null values stored when constructing a new ModSliderOption with a high/low value
-- Fixed subMenus not actually displaying due to an incorrect id check.

0.1
- Initial Version
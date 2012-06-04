package moapi.gui;

import moapi.ModOption;

/**
* Standard formatters which are used by the API as defaults
* Moved from StdFormatters to gui.StdFormatters on version 0.8
*
* @author 	Clinton Alexander
* @version	1.1
* @since	0.8
*/
public class StdFormatters {
	/**
	* Default formatter for adding the name label
	*/
	public final static DefaultFormat defaultFormat = new StdFormatters.DefaultFormat();
	/**
	* Integer formatter for sliders
	*
	* @since	0.6.1
	*/
	public final static IntegerSliderFormat integerSlider = new StdFormatters.IntegerSliderFormat();
	/**
	* Formatter for an option that only displays the option value
	*
	* @since	0.7
	*/
	public final static NoFormat noFormat = new StdFormatters.NoFormat();
	
	/**
	* Default manipulation class
	*
	* @author	Clinton Alexander
	* @version	1.0
	* @since	0.6
	*/
	private static final class DefaultFormat implements DisplayStringFormatter {
		/**
		* Format the standard input into a readable string
		*/
		public String manipulate(ModOption option, String value) {
			return option.getName() + ": " + value;
		}
	}
	
	/**
	* A formatted with no format, which outputs just the text given.
	* 
	* @author	Clinton Alexander
	* @version	1.0
	* @since	0.7
	*/
	private static final class NoFormat implements DisplayStringFormatter {
		/**
		* Simply return the value
		*
		* @since	0.7
		* @return	Same value as input
		*/
		public String manipulate(ModOption option, String value) {
			return value;
		}
	}
	
	/**
	* Formatting class for a given suffix with a space between
	*
	* @author	Clinton Alexander
	* @version	1.0
	* @since	0.6
	*/
	public static final class SuffixFormat implements DisplayStringFormatter {
		/**
		* Suffix value
		*/
		private String suffix;
		
		/**
		* Creates a string formatter with a suffix
		*
		* @param	suffix	Suffix to add to all values
		*/
		public SuffixFormat(String suffix) {
			this.suffix = suffix;
		}
		
		/**
		* Format the input into a readable string
		*/
		public String manipulate(ModOption option, String value) {
			return value + " " + suffix;
		}
	}
	
	/**
	* Formatting class for turning a float numeric string to an int one
	*
	* @author	Clinton Alexander
	* @version	1.0
	* @since	0.6.1
	*/
	public static final class IntegerSliderFormat implements DisplayStringFormatter {
		/**
		* Creates a string formatter which formats slider values to integer values
		*
		*/
		public IntegerSliderFormat() {
		}
		
		/**
		* Format the input into a readable string
		*/
		public String manipulate(ModOption option, String value) {
			try {
				float f = Float.parseFloat(value);
				int i = (int) f;
				return "" + i;
			} catch (NumberFormatException e) {		
				System.out.println("(MdoOptionsAPI) Could not format " + value + " into an integer");
				return option.getName() + ": " + value;
			}
		}
	}
}
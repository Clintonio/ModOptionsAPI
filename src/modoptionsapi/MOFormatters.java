package net.minecraft.src.modoptionsapi;

/**
* Container class for formatters
*
* @author 	Clinton Alexander
* @version	1.0
* @since	0.3
*/
public class MOFormatters {
	/**
	* Default formatter
	*/
	public final static DefaultFormat defaultFormat = new MOFormatters.DefaultFormat();
	
	/**
	* Default manipulation class
	*
	* @author	Clinton Alexander
	* @version	1.0
	* @since	0.6
	*/
	private static final class DefaultFormat implements MODisplayString {
		/**
		* Format the standard input into a readable string
		*/
		public String manipulate(String name, String value) {
			return name + ": " + value;
		}
	}
	
	/**
	* Formatting class for a given suffix with a space between
	*
	* @author	Clinton Alexander
	* @version	1.0
	* @since	0.6
	*/
	public static final class SuffixFormat implements MODisplayString {
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
		public String manipulate(String name, String value) {
			return name + ": " + value + " " + suffix;
		}
	}
}
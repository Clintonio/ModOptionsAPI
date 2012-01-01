package moapi;

/**
* This exception will occur when an option was not found
* or the types mismatch
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.7
*/
public class NoSuchOptionException extends RuntimeException {

	/**
	* Construct a no such option exception with a null message
	*/
	public NoSuchOptionException() {}
	
	/**
	* Construct a no such option exception with a message
	*
	* @param	message		Message for programmer
	*/
	public NoSuchOptionException(String message) {
		super(message);
	}
}
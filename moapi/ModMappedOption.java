package moapi;

/**
* Multiple Selector API with Integer -> String mappings
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.7
* @deprecated	Moved to ModMappedOption
*/
public class ModMappedOption extends ModMappedMultiOption {
	
	/**
	* Create a multiple selector with no values
	*
	* @param	name	Name of selector
	*/
	public ModMappedOption(String name) {
		super(name, name);
	}
	
	/**
	* Create a multiple selector with the given keys and labels
	*
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	values	Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String name, Integer[] keys, String[] labels) {
		super(name, name, keys, labels);
	}
	
	/**
	* Create a multiple selector with the given keys and labels
	*
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	values	Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String name, int[] keys, String[] labels) {
		super(name, name, keys, labels);
	}
	
	/**
	* Create a multiple selector with the given keys and labels and ID/name
	*
	* @since	0.8
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	id		ID of selector
	* @param	name	Name of selector
	* @param	values	Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String id, String name, Integer[] keys, String[] labels) {
		super(id, name, keys, labels);
	}
	
	/**
	* Create a multiple selector with the given keys and labels
	*
	* @since	0.8
	* @throws	IndexOutOfBoundsException	Thrown when keys and labels differ in length
	* @param	name	Name of selector
	* @param	values	Values for the selector
	* @param	labels	Labels for values
	*/
	public ModMappedOption(String id, String name, int[] keys, String[] labels) {
		super(id, name, keys, labels);
	}
	
	/**
	* Creates a multi selector with the given name/ id and no values
	*
	* @since	0.8
	* @param	id		ID of this option
	* @param	name	Name of this option
	*/
	public ModMappedOption(String id, String name) {
		super(id, name);
	}
}
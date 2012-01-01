package moapi;

/**
* Abstract base class for individual option classes
*
* @author	Clinton Alexander
* @version	1.0
* @since	0.1
*/
abstract public class ModOption<E> {
	/**
	* The identifier for this option.
	* 
	* @since	0.8
	*/
	private String id;
	/**
	* Given name for this option selector
	*/
	protected String name;
	/**
	* Global Value of this option selector
	*/
	protected E value;
	/**
	* Local/ server value f this option
	*/
	protected E localValue;
	/**
	* If we should use the global value
	*/
	protected boolean global = true;
	/**
	* The callback object
	*/
	protected MOCallback callback = null;
	
	//==============
	// Constructor
	//==============
	
	/**
	* Default constructor, requires an ID
	*
	* @since	0.8
	* @param	id		Identifier for this option
	*/
	protected ModOption(String id) {
		setID(id);
		setName(id);
	}
	
	/**
	* Default constructor with a name
	*
	* @since	0.8
	* @param	id		Identified for this option
	* @param	name	Name for this option
	*/
	protected ModOption(String id, String name) {
		setID(id);
		setName(name);
	}
	
	//==============
	// Setters
	//==============
	
	/**
	* Sets the ID of this option
	*
	* @since	0.8
	*/
	private final void setID(String id) {
		this.id = id;
	}
	
	/**
	* Set the name of this option
	*
	* @param	name	New name for this option
	*/
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	* Set the current used value of this option selector
	* 
	* @param	value		New value
	*/
	public void setValue(E value) {
		if(global) {
			this.value = value;
		} else {
			localValue = value;
		}
	}
	
	/**
	* Set the current value used for the given scope
	*
	* @param	value	New value for scope
	* @param	scope	Scope value. True for global
	* @since	0.7
	*/
	public void setValue(E value, boolean scope) {
		if(scope) {
			this.value = value;
		} else {
			localValue = value;
		}
	}
	
	/**
	* Sets the local value of this option
	*
	* @param	value		New value
	*/
	public void setLocalValue(E value) {
		setValue(value, false);
	}
	
	/**
	* Sets global value of this option
	*
	* @param	value		New value
	*/
	public void setGlobalValue(E value) {
		setValue(value, true);
	}
	
	/**
	* Set the scope of the value
	*
	* @param	global	True if use global value only
	*/
	public void setGlobal(boolean global) {
		this.global = global;
	}
	
	/**
	* Set the callback for this option
	*
	* @param	callback	The callback to set
	*/
	public void setCallback(MOCallback callback) {
		this.callback = callback;
	}
	
	//==============
	// Getters
	//==============
	
	/**
	* Get the ID of this option selector
	*
	* @since	0.8
	* @return	ID of this option
	*/
	public final String getID() {
		return id;
	}
	
	/**
	* Return the name of this option selector
	*
	* @return	Name of option selector
	*/
	public String getName() {
		return name;
	}
	
	
	/**
	* Get value of this option selector
	*
	* @return	Value of this option selector
	*/
	public E getValue() {
		if(global) {
			return value;
		} else {
			return localValue;
		}
	}
	
	/**
	* Get the value of this selector from the scope
	* given in the first parameter
	*
	* @param	scope	True for global value
	* @return	Value of this option in the given scope
	*/
	public E getValue(boolean scope) {
		if(global) {
			return getGlobalValue();
		} else {
			return getLocalValue();
		}
	}
	
	/**
	* Returs the global value of this option
	*
	* @return	Global value of this option
	*/
	public E getGlobalValue() {
		return value;
	}
	
	/**
	* Get the local value
	*
	* @return	Local value of this option selector
	*/
	public E getLocalValue() {
		return localValue;
	}
	
	/**
	* Set this option to only use the global value
	*
	* @return	True if only using the global value
	*/
	public boolean useGlobalValue() {
		return global;
	}
	
	/**
	* Returns the callback
	*
	* @return	callback object
	*/
	public MOCallback getCallback() {
		return callback;
	}
	
	/**
	* Check if this option has a callback
	*
	* @return	True if has callback
	*/
	public boolean hasCallback() {
		return (callback instanceof MOCallback);
	}
}
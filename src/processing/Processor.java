package processing;

import data.Data;

/**
 * This interface represents processing objects that take a <code>Data</code> object as
 * input, processes it, and returns a new, processed <code>Data</code> object.
 */
public interface Processor {
	
	/**
	 * Processes the specified <code>Data</code> object and returns a new, processed
	 * <code>Data</code> object.
	 *
	 * @param data The <code>Data</code> object to process.
	 * @return A new, processed <code>Data</code> object
	 */
	Data process(Data data);
	
	/**
	 * Returns the name of this <code>Processor</code> object. This should be a
	 * human-readable string representation.
	 *
	 * @return The name.
	 */
	String getName();
	
}

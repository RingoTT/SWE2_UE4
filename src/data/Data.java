package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * This class represents an immutable data object whose content are double values, i.e.,
 * it is basically an immutable double array. New <code>Data</code> objects can be created
 * either by using the constructor {@link #Data(double[])} or the static factory method
 * {@link #readFromFile(String)}.
 * <p>
 * The individual double values can be accessed only via an iterator, for example, by
 * using the foreach-loop:
 * <pre>
 *     for (double d: myDataObject) {
 *         ...
 *     }
 * </pre>
 * Exactly {@link #size()} values will be returned by this iteration.
 */
public class Data implements Iterable<Double> {
	
	private final double[] values;
	
	/**
	 * Creates a new immutable <code>Data</code> object using the specified
	 * <code>values</code>.
	 *
	 * @param values The double values of this <code>Data</code> object.
	 */
	public Data(double[] values) {
		this.values = values;
	}
	
	/**
	 * Returns the size of the stored values, i.e., the number of elements this
	 * <code>Data</code> object contains. The iterator will have exactly this many
	 * iterations.
	 *
	 * @return The size of the stored values.
	 */
	public int size() {
		return values.length;
	}
	
	/**
	 * Returns an iterator for iterating over the double values of this <code>Data</code>
	 * object.
	 *
	 * @return The iterator for the double values.
	 */
	@Override
	public Iterator<Double> iterator() {
		return new DataIterator();
	}
	
	@Override
	public String toString() {
		return toCsvString();
	}
	
	/**
	 * Creates a new <code>Data</code> object from the contents of the CSV-file specified
	 * by the given <code>path</code>.
	 *
	 * @param path The path of the file whose contents should be used for the new
	 *             <code>Data</code> object.
	 * @return A new <code>Data</code> object.
	 * @throws IOException Thrown when anything goes wrong when reading the file.
	 */
	public static Data readFromFile(String path) throws IOException {
		String[] parts = Files.readString(Path.of(path)).split(",");
		double[] values = new double[parts.length];
		for (int i = 0; i < parts.length; i++) {
			values[i] = Double.parseDouble(parts[i]);
		}
		return new Data(values);
	}
	
	/**
	 * Write the double values of this <code>Data</code> object as comma separated values
	 * to a file.
	 *
	 * @param path The path for the output file.
	 * @throws IOException Thrown when anything goes wrong when writing to the file.
	 */
	public void writeToFile(String path) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write(toCsvString());
		}
	}
	
	/**
	 * Returns a string containing the double values of this <code>Data</code> object
	 * separated by commas.
	 */
	private String toCsvString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length - 1; i++) {
			sb.append(values[i]).append(",");
		}
		sb.append(values[values.length - 1]);
		return sb.toString();
	}
	
	private class DataIterator implements Iterator<Double> {
		
		private int i = 0;
		
		@Override
		public boolean hasNext() {
			return i < values.length;
		}
		
		@Override
		public Double next() {
			double d = values[i];
			i++;
			return d;
		}
		
	}
	
}

package put.your.package.name.here;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import com.demo.example.dto.ExampleDTO;

/**
 * The Class CSVReportGenerator.
 * 
 * @author <a href="mailto:d.synchronized@gmail.com">Dishant Anand</a>
 */
public class CSVReportGenerator {

	/** The Constant DEFAULT_SEPARATOR. */
	private static final char DEFAULT_SEPARATOR = ',';

	/** The headers. */
	private Set<String> headers;

	/**
	 * Write line.
	 *
	 * @param w
	 *            the w
	 * @param values
	 *            the values
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeLine(final Writer w, final List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	/**
	 * Write line.
	 *
	 * @param w
	 *            the w
	 * @param values
	 *            the values
	 * @param separators
	 *            the separators
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void writeLine(final Writer w, final List<String> values, final char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	/**
	 * Write line.
	 *
	 * @param w
	 *            the w
	 * @param values
	 *            the values
	 * @param separators
	 *            the separators
	 * @param customQuote
	 *            the custom quote
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeLine(final Writer w, final List<String> values, char separators, final char customQuote) throws IOException {
		boolean first = true;
		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}
		final StringBuilder sb = new StringBuilder();
		for (final String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());
	}

	/**
	 * Follow CV sformat.
	 *
	 * @param value
	 *            the value
	 * @return the string
	 */
	private String followCVSformat(final String value) {
		String result = value;
		if (result.contains("\"")) {
			result = result.replace("\"", "\"\"");
		}
		return result;

	}

	/**
	 * Prepare CSV data.
	 *
	 * @param <T>
	 *            the generic type
	 * @param <S>
	 *            the generic type
	 * @param objects
	 *            the objects
	 * @param clazz
	 *            the clazz
	 * @return the list
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
	private <T extends Object, S extends Object> List<List<String>> prepareCSVData(final List<T> objects, final Class<T> clazz)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		final Set<String> headers = new LinkedHashSet<String>();
		final List<List<String>> rows = new ArrayList<>();
		for (final T object : objects) {
			final List<String> row = new ArrayList<>();
			for (final Field field : clazz.getDeclaredFields()) {
				final String fieldName = field.getName();
				// Add field headers
				headers.add(fieldName);

				field.setAccessible(true);
				final Object value = PropertyUtils.getProperty(object, field.getName());
				row.add(String.valueOf(value));
			}
			rows.add(row);
		}
		this.headers = headers;
		return rows;
	}

	/**
	 * Generate CSV.
	 *
	 * @param <T>
	 *            the generic type
	 * @param <S>
	 *            the generic type
	 * @param csvFilePath
	 *            the csv file path
	 * @param objects
	 *            the objects
	 * @param clazz
	 *            the clazz
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public <T extends Object, S extends Object> void generateCSV(final String csvFilePath, final List<T> objects, final Class<T> clazz)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		final FileWriter fileWriter = new FileWriter(csvFilePath);
		try {
			final List<List<String>> rows = prepareCSVData(objects, clazz);
			writeLine(fileWriter, new ArrayList<>(headers));
			for (final List<String> row : rows) {
				writeLine(fileWriter, new ArrayList<>(row));
			}
		} finally {
			fileWriter.flush();
			fileWriter.close();
		}
	}
	
	public static void main(final String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		final String csvFilePath = "C:\\temp\\DemoCSV.csv";
		
		final List<
    > sendEmailRequests = new ArrayList<>();
		final ExampleDTO exampleDTO = new exampleDTO("Dishant", null, null, "demo", null);
		sendEmailRequests.add(sendEmailRequest);
		
		new CSVReportGenerator().generateCSV(csvFilePath, sendEmailRequests, SendEmailRequest.class);
	}

}

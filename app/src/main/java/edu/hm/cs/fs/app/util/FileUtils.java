package edu.hm.cs.fs.app.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Eine Hilfsklasse für das schreiben und lesen aus dem Dateisystem von Android.
 * 
 * @author Fabio
 */
public final class FileUtils {
	private static final String FS07 = "fs07";
	private static final String FS07_PATH = "/" + FS07 + "/";
	private static final File FS07_DIR = new File(Environment.getExternalStorageDirectory(), FS07_PATH);

	static {
		if (!FS07_DIR.exists()) {
			FS07_DIR.mkdirs();
		}
	}

	private FileUtils() {
	}

	/**
	 *
	 * @param fileName
	 * @return
	 */
	public static File createFile(String fileName) {
		return new File(FS07_DIR, fileName);
	}

	/**
	 * Schreibt einen String in eine Datei.
	 * 
	 * @param filename
	 *            der Datei.
	 * @param filecontent
	 *            der in die Datei geschrieben werden soll.
	 * @throws java.io.IOException
	 */
	public static void writeToFile(final String filename, final String filecontent) throws IOException {
		if (canWriteSD()) {
			FileWriter out = null;
			try {
				final File file = new File(FS07_DIR, filename);
				if (!file.exists()) {
					file.createNewFile();
				}

				out = new FileWriter(file);
				out.write(filecontent);
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
	}

	/**
	 * Öffnet einen {@link java.io.InputStream} für eine Datei.
	 * 
	 * @param filename
	 *            der Datei.
	 * @return den {@link java.io.InputStream} oder <code>null</code>.
	 * @throws java.io.IOException
	 */
	public static InputStream openFileStream(final String filename) throws IOException {
		if (canReadSD()) {
			return new FileInputStream(new File(FS07_DIR, filename));
		}
		return null;
	}

	/**
	 * Ließt den Inhalt einer Datei in einen String.
	 * 
	 * @param filename
	 *            der Datei.
	 * @return den Inhalt der Datei in einem String.
	 * @throws java.io.IOException
	 */
	public static String readFileToString(final String filename) throws IOException {
		final StringBuilder sb = new StringBuilder();

		if (canReadSD()) {
			BufferedReader reader = null;
			try {
				final File file = new File(FS07_DIR, filename);
				reader = new BufferedReader(new FileReader(file));

				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Überprüft die Leserechte auf der externen Speichereinheit.
	 * 
	 * @return <code>true</code> wenn der Lesezugriff gewährt ist.
	 */
	public static boolean canReadSD() {
		return canWriteSD() || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState());
	}

	/**
	 * Überprüft die Schreibrechte auf der externen Speichereinheit.
	 * 
	 * @return <code>true</code> wenn der Schreibzugriff gewährt ist.
	 */
	public static boolean canWriteSD() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
}

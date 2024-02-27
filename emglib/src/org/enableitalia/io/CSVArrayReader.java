package org.enableitalia.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVArrayReader {
	private String filename;

	public CSVArrayReader(String filename) {
		this.filename = filename;
	}

	public List<long[]> read() {
		List<long[]> longs = new ArrayList<>();
		try {
			// Crea uno scanner per leggere il file CSV
			Scanner scanner = new Scanner(new File(filename));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] values = line.split(",");

				if (values.length == 8) {
					long[] longValues = new long[8];

					for (int i = 0; i < 8; i++) {
						try {
							longValues[i] = Long.parseLong(values[i]);
						} catch (NumberFormatException e) {
							longValues[i] = 0;
							System.out.println("Warning: Skipping non-integer value '" + line + "'");
						}
					}

					longs.add(longValues);
				} else {
					System.out.println("La riga non contiene 8 valori.");
				}
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File non trovato: " + filename);
			e.printStackTrace();
		}

		return longs;
	}
}

package org.enableitalia.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private String filename;

    public CSVReader(String filename) {
        this.filename = filename;
    }
    
    public List<Double> read() {
        List<Double> doubles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Double value = Double.parseDouble(line.trim());
                    doubles.add(value);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Skipping non-integer value '" + line + "'");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return doubles;
    }
}

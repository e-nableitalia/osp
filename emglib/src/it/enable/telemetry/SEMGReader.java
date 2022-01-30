package it.enable.telemetry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SEMGReader {
	BufferedReader r;
	
	boolean eof;
	
	public SEMGReader(String name)  {
		try {
			r = new BufferedReader(new FileReader(name));
			// empty readline
			r.readLine();
			eof = false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int read() {
		String line;
		try {
			line = r.readLine();
			
			if (line == null) {
				eof = true;
				return 0;
			} else
				return Integer.parseInt(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean isEof() {
		return eof;
	}
 
}

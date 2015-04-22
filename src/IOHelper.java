import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class IOHelper {
	public BufferedReader getReader(String filename){
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reader;
	}
	
	
	public BufferedWriter getWriter(String filename){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(filename)));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer;
	}
}

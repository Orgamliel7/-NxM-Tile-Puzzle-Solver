import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is incharge of parsing the input.txt file
 * @author danie
 *
 */
public class InputParser {

	private String _path;
	private BufferedReader _br;

	public InputParser(String path) {
		_path = new String(path);
		try {
			_br = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			System.out.println("could not create InputParser Object \n");
			e.printStackTrace();
		}
	}

	public Input parseFile() {
		
		String algo = null;
		boolean withTime = false;
		String[][] puzzle = null;
		
		try {
		
			String currentLine = _br.readLine();
			algo = currentLine;
			currentLine = _br.readLine();
			withTime = currentLine.contains("with") ? true : false;
			currentLine = _br.readLine();
			String[] sizeOfpuzzle = currentLine.replace(" ", "").split("x"); //sizeOfPuzzle[0]=rows, sizeOfPuzzle[1]=columns
			int numOfRows = Integer.parseInt(sizeOfpuzzle[0]);
			int numOfColumns = Integer.parseInt(sizeOfpuzzle[1]);
			puzzle = new String[numOfRows][numOfColumns];
			
			//iterate over puzzle rows in txt file
			for (int i = 0; i < numOfRows; i++) {
				currentLine = _br.readLine();
				puzzle[i] = currentLine.replace(" ", "").split(",");
			}
			
		} catch (IOException e) {
			System.out.println("Could not read line while parsing input.txt\n");
			e.printStackTrace();
		}
		
		finally {
			try {
				_br.close();
			} catch (IOException e) {
				System.out.println("Could not close BufferReader\n");
				e.printStackTrace();
			}
		}		
		return new Input(algo, withTime, new PuzzleState(puzzle));
	}

}

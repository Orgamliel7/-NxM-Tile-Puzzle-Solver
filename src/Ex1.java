import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Ex1
{

	final static String input = "input.txt";
	final static String output = "output.txt";
	
	public static void main(String[] args) {
		
		InputParser parser = new InputParser(input);
		Input input = parser.parseFile();
		Output ans = Solver.solve(input);
		PrintWriter outs = null;
		try {
			outs = new PrintWriter(new FileWriter(output));
		} catch (IOException e) {
			System.out.println("Problem creating PrintWriter object from output path");
			e.printStackTrace();
		}
		
		outs.print(ans.toString());
		outs.close();

	}

}

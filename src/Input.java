/**
 * This class represents an input from the input.txt
 * @author daniel
 *
 */
public class Input {
	
	private String _algorithm;
	private boolean withTime;
	private PuzzleState _startPuzzle;
	
	public Input(String _algorithm, boolean withTime, PuzzleState _startPuzzle) {
		this._algorithm = _algorithm;
		this.withTime = withTime;
		this._startPuzzle = new PuzzleState(_startPuzzle);
	}

	public String get_algorithm() {
		return _algorithm;
	}

	public boolean isWithTime() {
		return withTime;
	}

	public PuzzleState get_startPuzzle() {
		return new PuzzleState(_startPuzzle);
	}

	@Override
	public String toString() {
		return "Algorithm = " + _algorithm + "\nwithTime = " + withTime + "\nStartPuzzle = \n" + _startPuzzle;
	}
	
	
	
	
	
	
}

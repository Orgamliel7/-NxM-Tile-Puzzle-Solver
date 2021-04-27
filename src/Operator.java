
/**
 * This class is the operator done an a puzzle
 * @author danie
 *
 */
public class Operator {

	public static final byte TWO_LEFT = 0;
	public static final byte TWO_UP = 1;
	public static final byte TWO_RIGHT = 2;
	public static final byte TWO_DOWN = 3;
	public static final byte SINGLE_LEFT_FIRST = 4;
	public static final byte SINGLE_UP_FIRST = 5;
	public static final byte SINGLE_RIGHT_FIRST = 6;
	public static final byte SINGLE_DOWN_FIRST = 7;
	public static final byte SINGLE_LEFT_SECCOND = 8;
	public static final byte SINGLE_UP_SECCOND = 9;
	public static final byte SINGLE_RIGHT_SECCOND = 10;
	public static final byte SINGLE_DOWN_SECCOND = 11;
	private static final byte END = 12;

	private byte _currentOperation;
	private PuzzleState _puzzle;
	private String _lastOperationDescription;
	private int _lastOperationCost;
	private int _lastOperationRank;
	
	public Operator(PuzzleState _puzzle) {
		this._puzzle = new PuzzleState(_puzzle);
		_currentOperation = TWO_LEFT;
		_lastOperationDescription = "";
		_lastOperationCost = 0;
		_lastOperationRank = -1;
	}

	/**
	 * This function generates the next PuzzleState from the father
	 * @return PuzzleState after applying next operation. returns null if there is none
	 */
	public PuzzleState generateNextPuzzleState() {

		PuzzleState puzzleAfterOperation = new PuzzleState(_puzzle);

		while(true) {

			if(!_puzzle.canOperate(_currentOperation)) { 
				if(_currentOperation == END)
					return null;
			}
			else {
				_lastOperationDescription = puzzleAfterOperation.operate(_currentOperation);
				_lastOperationRank = _currentOperation;
				if(_lastOperationDescription.contains("&")){
					if(_lastOperationDescription.contains("U") || _lastOperationDescription.contains("D"))
						_lastOperationCost = 7;
					else
						_lastOperationCost = 6;
				}
				else
					_lastOperationCost = 5;
				_currentOperation++;
				break;
			}
			_currentOperation++;
		}
		return puzzleAfterOperation;
	}

	public byte get_currentOperation() {
		return _currentOperation;
	}

	public String get_lastOperationDescription() {
		return _lastOperationDescription;
	}

	public int get_lastOperationCost() {
		return _lastOperationCost;
	}

	public int get_lastOperationRank() {
		return _lastOperationRank;
	}
	
	

}

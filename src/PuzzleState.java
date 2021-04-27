import java.util.Arrays;

/**
 * This class represents a state of the puzzle
 * @author daniel
 *
 */
//TODO: try to improve hashCode for this
public class PuzzleState {

	public static final String EMPTY_CELL = "_";

	private enum Move {
		LEFT,
		UP,
		RIGHT,
		DOWN
	}

	private int _numOfRows;
	private int _numOfColumns;
	private String[][] _puzzle;
	private Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> _indexesOfEmptySpaces;
	private boolean _needToHashash;
	private int _puzzleMatrixHash;

	public PuzzleState(String[][] puzzle) {
		_puzzle = copyMatrix(puzzle);
		_numOfRows = _puzzle.length;
		_numOfColumns = _puzzle[0].length;
		updateEmptySpacesIndexes();
		_needToHashash = true;
		_puzzleMatrixHash = 0;
	}

	public PuzzleState(PuzzleState other) {
		_puzzle = copyMatrix(other._puzzle);
		_numOfRows = other._numOfRows;
		_numOfColumns = other._numOfColumns;
		Pair left = new Pair(other._indexesOfEmptySpaces.getLeft().getLeft(), other._indexesOfEmptySpaces.getLeft().getRight());
		Pair right = new Pair(other._indexesOfEmptySpaces.getRight().getLeft(), other._indexesOfEmptySpaces.getRight().getRight());
		_indexesOfEmptySpaces = new Pair(left, right);
		_needToHashash = false;
		_puzzleMatrixHash = other._puzzleMatrixHash;
	}

	/**
	 * This function checks if *this* is a goal(sorted)
	 * @return
	 */
	public boolean isGoal() {
		int counter = 1;
		int lastNum = _numOfRows * _numOfColumns - 2;
		if(_indexesOfEmptySpaces.getLeft().getLeft() != _numOfRows-1 || 
				_indexesOfEmptySpaces.getRight().getLeft() != _numOfRows-1 || 
				_indexesOfEmptySpaces.getRight().getRight() != _numOfColumns-2 ||
				_indexesOfEmptySpaces.getLeft().getRight() != _numOfColumns-1)
			return false;
		for (int i = 0; i < _numOfRows; i++) {
			for (int j = 0; j < _numOfColumns; j++) {
				if((_puzzle[i][j].matches("^[0-9]+$") && Integer.parseInt(_puzzle[i][j]) != counter) || _puzzle[i][j].equals(PuzzleState.EMPTY_CELL)) 
					return false;
				if(counter == lastNum)
					return true;
				counter++;
			}
		}
		return false;
	}

	/**
	 * This function checks if *this* can do a given operation
	 * @param operation
	 * @return true if can false otherwise
	 */
	public boolean canOperate(byte operation) {

		switch (operation) {

		case Operator.TWO_LEFT:
			//make sure two empty spaces are on same column
			if(_indexesOfEmptySpaces.getRight().getRight() != _indexesOfEmptySpaces.getLeft().getRight())
				return false;
			//make sure two empty spaces aren't on the right column
			if(_indexesOfEmptySpaces.getRight().getRight() == _numOfColumns-1)
				return false;
			//make sure two empty spaces are close to each other
			if(Math.abs(_indexesOfEmptySpaces.getRight().getLeft() - _indexesOfEmptySpaces.getLeft().getLeft()) != 1)
				return false;
			return true;

		case Operator.TWO_UP:
			//make sure two empty spaces are on same row
			if(_indexesOfEmptySpaces.getRight().getLeft() != _indexesOfEmptySpaces.getLeft().getLeft())
				return false;
			//make sure two empty spaces aren't on the bottom row
			if(_indexesOfEmptySpaces.getRight().getLeft() == _numOfRows-1)
				return false;
			//make sure two empty spaces are close to each other
			if(Math.abs(_indexesOfEmptySpaces.getRight().getRight() - _indexesOfEmptySpaces.getLeft().getRight()) != 1)
				return false;
			return true;

		case Operator.TWO_RIGHT:
			//make sure two empty spaces are on same column
			if(_indexesOfEmptySpaces.getRight().getRight() != _indexesOfEmptySpaces.getLeft().getRight())
				return false;
			//make sure two empty spaces aren't on the left colomn
			if(_indexesOfEmptySpaces.getRight().getRight() == 0)
				return false;
			//make sure two empty spaces are close to each other
			if(Math.abs(_indexesOfEmptySpaces.getRight().getLeft() - _indexesOfEmptySpaces.getLeft().getLeft()) != 1)
				return false;
			return true;

		case Operator.TWO_DOWN:
			//make sure two empty spaces are on same row
			if(_indexesOfEmptySpaces.getRight().getLeft() != _indexesOfEmptySpaces.getLeft().getLeft())
				return false;
			//make sure two empty spaces aren't on the top row
			if(_indexesOfEmptySpaces.getRight().getLeft() == 0)
				return false;
			//make sure two empty spaces are close to each other
			if(Math.abs(_indexesOfEmptySpaces.getRight().getRight() - _indexesOfEmptySpaces.getLeft().getRight()) != 1)
				return false;
			return true;

		case Operator.SINGLE_LEFT_FIRST:
			return canMove(_indexesOfEmptySpaces.getRight(), Move.LEFT);

		case Operator.SINGLE_UP_FIRST:
			return canMove(_indexesOfEmptySpaces.getRight(), Move.UP);

		case Operator.SINGLE_RIGHT_FIRST:
			return canMove(_indexesOfEmptySpaces.getRight(), Move.RIGHT);

		case Operator.SINGLE_DOWN_FIRST:
			return canMove(_indexesOfEmptySpaces.getRight(), Move.DOWN);

		case Operator.SINGLE_LEFT_SECCOND:
			return canMove(_indexesOfEmptySpaces.getLeft(), Move.LEFT);

		case Operator.SINGLE_UP_SECCOND:
			return canMove(_indexesOfEmptySpaces.getLeft(), Move.UP);

		case Operator.SINGLE_RIGHT_SECCOND:
			return canMove(_indexesOfEmptySpaces.getLeft(), Move.RIGHT);

		case Operator.SINGLE_DOWN_SECCOND:
			return canMove(_indexesOfEmptySpaces.getLeft(), Move.DOWN);

		default:
			break;
		}

		return false;
	}

	private void swap(int i1, int j1, int i2, int j2) throws IndexOutOfBoundsException {
		String temp = _puzzle[i1][j1];
		_puzzle[i1][j1] = _puzzle[i2][j2];
		_puzzle[i2][j2] = temp;
		_needToHashash = true;
	}

	/**
	 * This function does an operation(TWO_LEFT, TWO_RIGHT etc.) on *this* puzzle
	 * This function does not check if the operation is possible or not. There is a separate function for it
	 * @param operation
	 * @return
	 */
	public String operate(byte operation) throws IndexOutOfBoundsException {

		String output = "";
		int i_emptyRight = _indexesOfEmptySpaces.getRight().getLeft();
		int j_emptyRight = _indexesOfEmptySpaces.getRight().getRight();
		int i_emptyLeft = _indexesOfEmptySpaces.getLeft().getLeft();
		int j_emptyLeft = _indexesOfEmptySpaces.getLeft().getRight();

		switch (operation) {
		case Operator.TWO_LEFT:
			swap(i_emptyRight, j_emptyRight, i_emptyRight, j_emptyRight+1);
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft, j_emptyLeft+1);
			output += _puzzle[i_emptyRight][j_emptyRight] + "&" + _puzzle[i_emptyLeft][j_emptyLeft] + "L";
			_indexesOfEmptySpaces.getRight().setRight(j_emptyRight+1);
			_indexesOfEmptySpaces.getLeft().setRight(j_emptyLeft+1);
			break;

		case Operator.TWO_UP:
			swap(i_emptyRight, j_emptyRight, i_emptyRight+1, j_emptyRight);
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft+1, j_emptyLeft);
			output += _puzzle[i_emptyRight][j_emptyRight] + "&" + _puzzle[i_emptyLeft][j_emptyLeft] + "U";
			_indexesOfEmptySpaces.getRight().setLeft(i_emptyRight+1);
			_indexesOfEmptySpaces.getLeft().setLeft(i_emptyLeft+1);
			break;

		case Operator.TWO_RIGHT:
			swap(i_emptyRight, j_emptyRight, i_emptyRight, j_emptyRight-1);
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft, j_emptyLeft-1);
			output += _puzzle[i_emptyRight][j_emptyRight] + "&" + _puzzle[i_emptyLeft][j_emptyLeft] + "R";
			_indexesOfEmptySpaces.getRight().setRight(j_emptyRight-1);
			_indexesOfEmptySpaces.getLeft().setRight(j_emptyLeft-1);
			break;
		case Operator.TWO_DOWN:
			swap(i_emptyRight, j_emptyRight, i_emptyRight-1, j_emptyRight);
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft-1, j_emptyLeft);
			output += _puzzle[i_emptyRight][j_emptyRight] + "&" + _puzzle[i_emptyLeft][j_emptyLeft] + "D";
			_indexesOfEmptySpaces.getRight().setLeft(i_emptyRight-1);
			_indexesOfEmptySpaces.getLeft().setLeft(i_emptyLeft-1);
			break;
		case Operator.SINGLE_LEFT_FIRST:
			swap(i_emptyRight, j_emptyRight, i_emptyRight, j_emptyRight+1);
			output += _puzzle[i_emptyRight][j_emptyRight] + "L";
			_indexesOfEmptySpaces.getRight().setRight(j_emptyRight+1);
			break;
		case Operator.SINGLE_UP_FIRST:
			swap(i_emptyRight, j_emptyRight, i_emptyRight+1, j_emptyRight);
			output += _puzzle[i_emptyRight][j_emptyRight] + "U";
			_indexesOfEmptySpaces.getRight().setLeft(i_emptyRight+1);
			break;
		case Operator.SINGLE_RIGHT_FIRST:
			swap(i_emptyRight, j_emptyRight, i_emptyRight, j_emptyRight-1);
			output += _puzzle[i_emptyRight][j_emptyRight] + "R";
			_indexesOfEmptySpaces.getRight().setRight(j_emptyRight-1);
			break;
		case Operator.SINGLE_DOWN_FIRST:
			swap(i_emptyRight, j_emptyRight, i_emptyRight-1, j_emptyRight);
			output += _puzzle[i_emptyRight][j_emptyRight] + "D";
			_indexesOfEmptySpaces.getRight().setLeft(i_emptyRight-1);
			break;
		case Operator.SINGLE_LEFT_SECCOND:
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft, j_emptyLeft+1);
			output += _puzzle[i_emptyLeft][j_emptyLeft] + "L";
			_indexesOfEmptySpaces.getLeft().setRight(j_emptyLeft+1);
			break;
		case Operator.SINGLE_UP_SECCOND:
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft+1, j_emptyLeft);
			output += _puzzle[i_emptyLeft][j_emptyLeft] + "U";
			_indexesOfEmptySpaces.getLeft().setLeft(i_emptyLeft+1);
			break;
		case Operator.SINGLE_RIGHT_SECCOND:
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft, j_emptyLeft-1);
			output += _puzzle[i_emptyLeft][j_emptyLeft] + "R";
			_indexesOfEmptySpaces.getLeft().setRight(j_emptyLeft-1);
			break;
		case Operator.SINGLE_DOWN_SECCOND:
			swap(i_emptyLeft, j_emptyLeft, i_emptyLeft-1, j_emptyLeft);
			output += _puzzle[i_emptyLeft][j_emptyLeft] + "D";
			_indexesOfEmptySpaces.getLeft().setLeft(i_emptyLeft-1);
			break;

		default:
			break;
		}

		sortEmptySpaces();
		_needToHashash = true;
		return output;
	}

	@Override
	public String toString() {
		String rtn="";
		for (int i = 0; i < _puzzle.length; i++) {
			rtn+=Arrays.toString(_puzzle[i]);
			if(i!=_puzzle.length-1)
				rtn+="\n";
		}
		return rtn;
	}

	//A function to update the current empty spaces in the puzzle
	private void updateEmptySpacesIndexes() {
		if(_indexesOfEmptySpaces == null) 
			_indexesOfEmptySpaces = new Pair();
		for (int i = 0; i < _numOfRows; i++) {
			for (int j = 0; j < _numOfColumns; j++) {
				if(_puzzle[i][j].equals(PuzzleState.EMPTY_CELL)) {
					if(_indexesOfEmptySpaces.getRight() == null)
						_indexesOfEmptySpaces.setRight(new Pair<Integer, Integer>(i,j));
					else
						_indexesOfEmptySpaces.setLeft(new Pair<Integer, Integer>(i,j));
				}
			}
		}
	}

	//This function checks if you can move(LEFT, UP, RIGHT or DOWN) to an emptySpace
	private boolean canMove(Pair<Integer, Integer> emptySpace, Move move) {

		switch (move) {
		case LEFT:
			//check that empty space isent on the most right column
			if(emptySpace.getRight() == _numOfColumns-1 || _puzzle[emptySpace.getLeft()][emptySpace.getRight()+1].equals(EMPTY_CELL))
				return false;
			return true;

		case UP:
			//check that empty space isent on the most bottom row
			if(emptySpace.getLeft() == _numOfRows-1 || _puzzle[emptySpace.getLeft()+1][emptySpace.getRight()].equals(EMPTY_CELL))
				return false;
			return true;

		case RIGHT:
			//check that empty space isent on the most left column
			if(emptySpace.getRight() == 0 || _puzzle[emptySpace.getLeft()][emptySpace.getRight()-1].equals(EMPTY_CELL))
				return false;
			return true;

		case DOWN:
			//check that empty space isent on the most top row
			if(emptySpace.getLeft() == 0 || _puzzle[emptySpace.getLeft()-1][emptySpace.getRight()].equals(EMPTY_CELL))
				return false;
			return true;

		default:
			break;
		}

		return false;
	}

	//A function to swap right and left empty spaces
	private void swapEmptyPlaces() {
		Pair<Integer, Integer> temp = _indexesOfEmptySpaces.getRight();
		_indexesOfEmptySpaces.setRight(_indexesOfEmptySpaces.getLeft());
		_indexesOfEmptySpaces.setLeft(temp);
	}

	//A function to sort the empty places in the pair such that the first is the most top left
	private void sortEmptySpaces() {
		if(_indexesOfEmptySpaces.getRight().getLeft() > _indexesOfEmptySpaces.getLeft().getLeft())
			swapEmptyPlaces();
		else if(_indexesOfEmptySpaces.getRight().getLeft() == _indexesOfEmptySpaces.getLeft().getLeft())
			if(_indexesOfEmptySpaces.getRight().getRight() > _indexesOfEmptySpaces.getLeft().getRight())
				swapEmptyPlaces();
	}

	public String[][] get_puzzle() {
		return _puzzle;
	}

	private String[][] copyMatrix(String[][] mat) {
		String[][] rtn = new String[mat.length][mat[0].length];
		for (int i = 0; i < rtn.length; i++) {
			for (int j = 0; j < rtn[0].length; j++) {
				rtn[i][j] = mat[i][j];
			}
		}
		return rtn;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_indexesOfEmptySpaces == null) ? 0 : _indexesOfEmptySpaces.hashCode());
		result = prime * result + _numOfColumns;
		result = prime * result + _numOfRows;
		result = prime * result + create2DArrayHashCode();
		return result;
	}


	private int create2DArrayHashCode() {
		if (_needToHashash) {
			int hash = 0;
			for (int i = 0; i < _numOfRows; i++)
				for (int j = 0; j < _numOfColumns; j++)
					hash += _puzzle[i][j].hashCode() * (i + _numOfColumns * j);

			_puzzleMatrixHash = hash;
			_needToHashash = false;
		}

		return _puzzleMatrixHash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PuzzleState other = (PuzzleState) obj;
		if (_indexesOfEmptySpaces == null) {
			if (other._indexesOfEmptySpaces != null)
				return false;
		} else if ( _numOfColumns != other._numOfColumns)
			return false;
		if (_numOfRows != other._numOfRows)
			return false;
		if (!_indexesOfEmptySpaces.equals(other._indexesOfEmptySpaces))
			return false;
		if (!Arrays.deepEquals(_puzzle, other._puzzle))
			return false;
		return true;
	}



}

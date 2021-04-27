/**
 * This class is an abstract class of an algorithm that solves the puzzle problem
 * @author daniel
 *
 */
public abstract class Algorithm {

	protected PuzzleNode _startPuzzleNode;
	protected long _startTimeInNannoSecconds;
	protected long _endTimeInNannoSecconds;
	protected int _numOfNodesCreated;
	protected int _cost;
	protected String _path;
	protected boolean _withTime;

	public Algorithm(PuzzleState startPuzzle, boolean withTime) {
		this._startPuzzleNode = new PuzzleNode(startPuzzle);
		this._startTimeInNannoSecconds = 0;
		this._endTimeInNannoSecconds = 0;
		this._numOfNodesCreated = 0;
		this._cost = 0;
		this._path = "no path";
		this._withTime = withTime;
	}

	public abstract Output solve();

	//return path from goal to root
	protected void getPathFromGoal(PuzzleNode goal) {
		_path = "";
		PuzzleNode current = goal;
		while(true) {
			if(current.get_parent() == null)
				break;
			if(_path.isEmpty())
				_path = current.get_operateCreatorString();
			else
				_path = current.get_operateCreatorString() + "-" + _path;
			current = current.get_parent();
		}

	}
	
	//generate output object from *this* vars
	protected Output generateOutput() {
		Output out;
		if(_withTime) {
			long time = _endTimeInNannoSecconds - _startTimeInNannoSecconds;
			out = new Output(_path, _numOfNodesCreated, _cost, time);
		}
		else
			out = new Output(_path, _numOfNodesCreated, _cost);
		return out;
	}
}

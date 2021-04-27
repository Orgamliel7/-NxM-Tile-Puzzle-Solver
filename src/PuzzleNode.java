import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This claa represnts a node in a graph of puzzle states.
 * it contains the parent of each node, its children and the operator that created it
 * @author daniel
 *
 */
public class PuzzleNode {

	private PuzzleState _data;
	private List<PuzzleNode> _children;
	private PuzzleNode _parent;
	private String _operatetCreatorString;
	private int _nodeCost;
	private Operator _op;
	private int _operatorCreatorRank;

	public PuzzleNode(PuzzleState _data, List<PuzzleNode> _children, PuzzleNode _parent) {
		this._data = new PuzzleState(_data);
		this._children = _children;
		this._parent = _parent;
		this._operatetCreatorString = "";
		_nodeCost = 0;
		this._op = new Operator(_data);
		_operatorCreatorRank = -1;
	}

	public PuzzleNode(PuzzleState _data) {
		this._data = new PuzzleState(_data);
		this._children = new ArrayList<>();
		this._parent = null;
		this._operatetCreatorString = "";
		_nodeCost = 0;
		this._op = new Operator(_data);
		_operatorCreatorRank = -1;
	}

	public void set_Parent(PuzzleNode parent, String operateCreatorString, int operateCreatorCost) {
		_parent = parent;
		_operatetCreatorString = operateCreatorString;
		_nodeCost = operateCreatorCost;
	}

	public void addChild(PuzzleNode child, String operateCreator, int operateCreatorCost, int operatorRank) {
		_children.add(child);
		child.set_Parent(this, operateCreator, operateCreatorCost);
		child._operatorCreatorRank = operatorRank;
	}

	public PuzzleNode generateChild() {
		PuzzleState child = _op.generateNextPuzzleState();
		if((get_parent() != null && get_parent().get_data().equals(child))) {
			child = _op.generateNextPuzzleState();
		}
		if(child == null)
			return null;
		PuzzleNode childNode = new PuzzleNode(child);
		addChild(childNode, _op.get_lastOperationDescription(), _nodeCost + _op.get_lastOperationCost(), _op.get_lastOperationRank());
		return childNode;		
	}

	public PuzzleState get_data() {
		return _data;
	}

	public List<PuzzleNode> get_children() {
		return _children;
	}

	public PuzzleNode get_parent() {
		return _parent;
	}

	public String get_operateCreatorString() {
		return _operatetCreatorString;
	}
	

	public int get_nodeCost() {
		return _nodeCost;
	}

	public void set_nodeCost(int _nodeCost) {
		this._nodeCost = _nodeCost;
	}

	public void set_operateCreatorString(String _operatCreator) {
		this._operatetCreatorString = _operatCreator;
	}

	public int get_operatorCreatorRank() {
		return _operatorCreatorRank;
	}

	/**
	 * This only compares the actual puzzleState and not other vars
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_data == null) ? 0 : _data.hashCode());
		return result;
	}

	/**
	 * This only compares the actual puzzleState and not other vars
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PuzzleNode other = (PuzzleNode) obj;
		if (_data == null) {
			if (other._data != null)
				return false;
		} else if (!_data.equals(other._data))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PuzzleNode [_data=" + _data + ", _operatetCreatorString=" + _operatetCreatorString + ", _nodeCost="
				+ _nodeCost+ "]";
	}
	
	
	
	








}

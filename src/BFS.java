import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue; 

public class BFS extends Algorithm {

	public BFS(PuzzleState startPuzzle, boolean withTime) {
		super(startPuzzle, withTime);
	}

	@Override
	public Output solve() {
		boolean breakFlag = false;
		if(_withTime)
			_startTimeInNannoSecconds = System.nanoTime();
		if(_startPuzzleNode.get_data().isGoal()) {
			_path = "";
			if(_withTime)
				_endTimeInNannoSecconds = System.nanoTime();
			return generateOutput();		
		}
		Queue<PuzzleNode> q = new LinkedList<>();
		HashMap<PuzzleState, Boolean> openList = new HashMap<>();
		HashMap<PuzzleState, Boolean> closedList = new HashMap<>();
		Operator op;
		q.add(_startPuzzleNode);
		openList.put(_startPuzzleNode.get_data(), true);
		
		while(!q.isEmpty()) {
			PuzzleNode n = q.poll();
			openList.remove(n.get_data());
			_numOfNodesCreated++;
			//op = new Operator(n.get_data());
			closedList.put(n.get_data(), true);
			PuzzleNode currentChild = n.generateChild();
			while(currentChild != null) {
				//PuzzleNode childNode = new PuzzleNode(nextOp);
				//n.addChild(childNode, op.get_lastOperationDescription());	
				if(!closedList.containsKey(currentChild.get_data()) && !openList.containsKey(currentChild.get_data())) {
					if(currentChild.get_data().isGoal()) {
						getPathFromGoal(currentChild);
						_cost = currentChild.get_nodeCost();
						breakFlag = true;
						break;
					}
					q.add(currentChild);
					openList.put(currentChild.get_data(), true);
				}
				currentChild = n.generateChild();
			}
			if(breakFlag)
				break;
		}
		if(_withTime)
			_endTimeInNannoSecconds = System.nanoTime();

		return generateOutput();
	}

}

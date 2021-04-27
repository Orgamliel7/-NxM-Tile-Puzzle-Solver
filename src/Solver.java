
public class Solver {
	
	public static Output solve(Input _input) {
		Algorithm algo = null;
		
		switch (_input.get_algorithm()) {
		case "BFS":
			algo = new BFS(_input.get_startPuzzle(), _input.isWithTime());			
			break;
		case "DFID":
			algo = new DFID(_input.get_startPuzzle(), _input.isWithTime());			
			break;
		case "A*":
			algo = new A_Star(_input.get_startPuzzle(), _input.isWithTime());			
			break;
		case "IDA*":
			algo = new IDA_Star(_input.get_startPuzzle(), _input.isWithTime());			
			break;
		case "DFBnB":
			algo = new DFBnB(_input.get_startPuzzle(), _input.isWithTime());			
			break;
		default:
			break;
		}
		
		Output out = algo.solve();
		return out;
	}

}

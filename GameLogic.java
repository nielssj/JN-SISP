import java.util.LinkedList;
import java.util.Queue;


public class GameLogic implements IGameLogic {
    private int n = 0;
    private int m = 0;
    private int playerID;
    private GameState state;
    private Queue<int[]> newMoves;
    
    public GameLogic() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int n, int m, int playerID) {
        this.n = n;
        this.m = m;
        this.playerID = playerID;
        this.state = new GameState(n, m);
        this.newMoves = new LinkedList<int[]>();
    }
	
    public Winner gameFinished() {
        // Is it a tie?
    	boolean tie = true;
    	for(int t = 0; t < n; t++) {
    		tie = tie && !(state.coinsInColumn(t) < m);
    	}
    	if(tie) return Winner.TIE;

    	// Did anyone win?
    	while(!newMoves.isEmpty()) {
    		int[] move = newMoves.poll();
    		int x = move[0];
			int y = move[1];
    		int p = state.ps(x, y);
    		if (p > 0) {
				if ((p == state.ps(x, y+1) && p == state.ps(x, y+2) && p == state.ps(x, y+3))
    				|| (p == state.ps(x+1, y) && p == state.ps(x+2, y) && p == state.ps(x+3, y))
    				|| (p == state.ps(x-1, y) && p == state.ps(x-2, y) && p == state.ps(x-3, y))
					|| (p == state.ps(x, y+1) && p == state.ps(x, y+2) && p == state.ps(x, y+3))
    				|| (p == state.ps(x, y-1) && p == state.ps(x, y-2) && p == state.ps(x, y-3))
    				|| (p == state.ps(x+1, y+1) && p == state.ps(x+2, y+2) && p == state.ps(x+3, y+3))
    				|| (p == state.ps(x+1, y-1) && p == state.ps(x+2, y-2) && p == state.ps(x+3, y-3))
					|| (p == state.ps(x-1, y+1) && p == state.ps(x-2, y+2) && p == state.ps(x-3, y+3))
					|| (p == state.ps(x-1, y-1) && p == state.ps(x-2, y-2) && p == state.ps(x-3, y-3)))
				{
					return GameState.winnerFromInt(p);
				}
			}
    	}
    	
        return Winner.NOT_FINISHED;
    }

    public void insertCoin(int column, int playerID) {
    	int[] move = state.madeMove(playerID, column);
    	newMoves.add(move);
    }

    public int decideNextMove() {
        // Put coin in left-most column, that isn't full already.
    	for(int i = 0; i < n; i++) {
    		if(state.coinsInColumn(i) < m) {
    			return i;
    		}
    	}
    	
        return 0;
    }
}

public class GameState {
	private int n;
	private int m;
	private int currentPlayer;
	private int[] cic;
	private int[][] board;
	private int[] lastMove;
	
	
	public GameState(int n, int m) {
		this.n = n;
		this.m = m;
		this.currentPlayer = 1;
		this.cic = new int[n];
		this.board = new int[n][m];
		this.lastMove = new int[] {-1,-1};
	}
	
	public GameState(int n, int m, int currentPlayer, int[] cic, int[][] board, int[] lastMove)
	{
		this.n = n;
		this.m = m;
		this.cic = cic;
		this.board = board;
		this.currentPlayer = currentPlayer;
		this.lastMove = lastMove;
	}
	
	public void madeMove(int c) {
		int r = cic[c]++;
		board[c][r] = currentPlayer;
		
		if (currentPlayer == 1) currentPlayer = 2;
		else currentPlayer = 1;
		
		lastMove = new int[] {c,r};
	}
	
	public int coinsInColumn(int column)
	{
		return cic[column];
	}
	
	public int getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	public int[] getLastMove()
	{
		return lastMove;
	}
	
	public int ps(int x, int y)
	{
		if(x >= n || x < 0 || y >= m || y < 0) return -1;
		return board[x][y];
	}
	
	public GameState result(int column)
	{
		int[][] newboard = new int[n][1];
		for(int i=0; i < n; i++)
		{
			newboard[i] = this.board[i].clone();
		}
		
		GameState newState = new GameState(this.n, this.m, this.currentPlayer, this.cic.clone(), newboard, this.lastMove.clone());
		newState.madeMove(column);
		return newState;
	}
	
	public static IGameLogic.Winner winnerFromInt(int playerID)
	{
		if (playerID == 1) return IGameLogic.Winner.PLAYER1;
		else return IGameLogic.Winner.PLAYER2;
	}
	
	public static int opponent(int playerID) 
	{
		if (playerID == 1) return 2;
		else return 1;
	}
}

public class GameState {
	private int n;
	private int m;
	private int[] cic;
	private int[][] board;
	
	
	public GameState(int n, int m) {
		this.n = n;
		this.m = m;
		this.cic = new int[n];
		this.board = new int[n][m];
	}
	
	public int[] madeMove(int player, int c) {
		int r = cic[c]++;
		board[c][r] = player;
		return new int[] {c,r};
	}
	
	public int coinsInColumn(int column)
	{
		return cic[column];
	}
	
	public int ps(int x, int y)
	{
		if(x >= n || x < 0 || y >= m || y < 0) return -1;
		return board[x][y];
	}
	
	public static IGameLogic.Winner winnerFromInt(int playerID)
	{
		if (playerID == 1) return IGameLogic.Winner.PLAYER1;
		else return IGameLogic.Winner.PLAYER2;
	}
}
/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;

import net.sf.javabdd.*;

public class QueensLogic {
    private int x = 0;
    private int y = 0;
    private int size = 0;
    private int tiles = 0;
    private int[][] board;
    private BDD bdd;
    private BDDFactory factory;


    public QueensLogic() {
       //constructor
    }

    public void initializeGame(int size) {
        this.x = size;
        this.y = size;
        this.size = size;
        this.tiles = size*size;
        this.board = new int[x][y];
                
        this.factory = JFactory.init(2000000, 200000);
        this.factory.setVarNum(size*size);
        
        this.bdd = this.factory.one();
        
        // Used to check whether only one solution exists so the rest of the queens can be placed automagically
        for (int column = 0 ; column < size ; column++) {
        	BDD colBDD = this.factory.zero();
        	for (int row = 0 ; row < size ; row++) {
        		colBDD = colBDD.or(this.factory.ithVar(this.varNum(column, row)));
        	}
        	
        	this.bdd = this.bdd.and(colBDD);
        }
        
        // Used to ensure that no two queens can capture each other
        for (int column = 0 ; column < size ; column++) {
        	BDD colBDD = this.factory.zero();
        	for (int row = 0 ; row < size ; row++) {
        		colBDD = colBDD.or(this.negateOthers(column, row));
        	}
        	
        	this.bdd = this.bdd.and(colBDD);
        }
    }
    
    /*
     * Given a variable at column, row; make that one positive, and
     * negate all other variables in the row, the column and diagonally
     */
    private BDD negateOthers(int column, int row) {
    	BDD ret = this.factory.ithVar(this.varNum(column, row));
   
    	// Negate all variables to the right in this column
    	ret = this.negateDirection(ret, column, row, 0, 1);
    	// Negate all variables to the left in this column
    	ret = this.negateDirection(ret, column, row, 0, -1);
    	// Negate all variables upwards in this row
    	ret = this.negateDirection(ret, column, row, 1, 0);
    	// Negate all variables downwards in this row
    	ret = this.negateDirection(ret, column, row, -1, 0);
    	
    	// Negate diagonally
        ret = this.negateDirection(ret, column, row, 1, 1);
        ret = this.negateDirection(ret, column, row, -1, -1);
        ret = this.negateDirection(ret, column, row, 1, -1);
        ret = this.negateDirection(ret, column, row, -1, 1);
    	
    	return ret;
    }
    
    /* 
     * Negate the variable at column + cDelta, row + rDelta if it exists, 
     * do a logic AND of the result with the given BDD and do a recursive call
     * to continue to negate in the direction given på cDelta, rDelta
     */
    private BDD negateDirection(BDD bdd, int column, int row, int cDelta, int rDelta) {
    	int newColumn = column + cDelta;
    	int newRow = row + rDelta;
    	
    	if (!this.outsideBoard(newColumn, newRow)) {
    		bdd = bdd.and(this.factory.nithVar(this.varNum(newColumn, newRow)));
    		return this.negateDirection(bdd, newColumn, newRow, cDelta, rDelta);
    	} else {
    		return bdd;
    	}
    }
    
    /*
     * Returns true if a column, row position is outside the board
     */
    private boolean outsideBoard(int column, int row) {
    	return column < 0 || row < 0 || column >= size || row >= size;
    }

    /*
     * Returns the variable number for a tile placed in a given column and row.
     * Variable numbering starts at 0.
     * 
     * The first column is assigned variable numbers 0 ... size-1, the second 
     * column is assigned size ... (2*size)-1 etc.
     */
    private int varNum(int column, int row) {
    	return (size * column) + row;
    }
   
    public int[][] getGameBoard() {
        return board;
    }

    /*
     * Insert a queens
     * @returns Sometimes false, other times true ...
     */
    public boolean insertQueen(int column, int row) {
        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        board[column][row] = 1;
        
        BDD restriction = this.factory.ithVar(this.varNum(column, row));
        this.bdd = this.bdd.restrict(restriction);
        
        int blocked = 0;
        
        for (int _column = 0 ; _column < this.size ; _column++) {
        	for (int _row = 0 ; _row < this.size ; _row++) {
        		if (this.board[_column][_row] == 0) {
        			BDD restricted = this.bdd.restrict(this.factory.ithVar(this.varNum(_column, _row)));
            		
            		if (restricted.isZero()) {
            			this.board[_column][_row] = -1;
            			blocked++;
            		}
        		} else if (this.board[_column][_row] == -1) {
        			blocked++;
        		}
        	}
        }
        
        // If the number of queens (i.e. the size of the board) plus the number of blocked tiles equals the total number of tiles there is only one configuration left, so we place the queens
        if (size + blocked == this.tiles) {
        	for (int _column = 0 ; _column < this.size ; _column++) {
            	for (int _row = 0 ; _row < this.size ; _row++) {
            		if (this.board[_column][_row] == 0) {
            			this.board[_column][_row] = 1;
            		}
            	}
            }
        }

        return Math.random() > 0.5 ? true : false; // Never used ...
    }
}

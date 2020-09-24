package com.example.matrix;

public class TicTacToe {

	private int[][] board;
	private int n;
	private int winner;

	public TicTacToe(int n) {
		this.n = n;
		this.board = new int[n][n];
	}

	/**
	 * Makes a move on the board and returns the winner if that move is a winning move
	 * @param playerId is either -1 or +1
	 * @param row is move's row index
	 * @param column is move's column index
	 * @return -1 if first player wins, +1 if second player wins, 0 if no winner in that move
	 */
	private int move(int playerId, int row, int column) {
		if(winner != 0) {
			throw new IllegalArgumentException("This game is over already. Winner is " + winner);
		}
		if (row < 0 || column < 0 || row >= n || column >= n) {
			throw new IllegalArgumentException("Move is out of board");
		}
		if (board[row][column] != 0) {
			throw new IllegalArgumentException("Square is alreay occupied");
		}
		if (playerId != -1 && playerId != 1) {
			throw new IllegalArgumentException("Invalid Player");
		}
	
		
		board[row][column] = playerId;
		printBoard();
		boolean winRow = true, winColumn = true, winDiag = true, winRevDiag = true;

		for (int i = 0; i < n; i++) {
			if (board[row][i] != playerId) {
				winRow = false;
			}
			if (board[i][column] != playerId) {
				winColumn = false;
			}
			if (board[i][i] != playerId) {
				winDiag = false;
			}
			if (board[i][n - 1 - i] != playerId) {
				winRevDiag = false;
			}
		}
		
		if(winRow || winColumn || winDiag || winRevDiag) {
			setWinner(playerId);
			return playerId;
		}
		return 0;
	}

	public static void main(String[] args) {
		TicTacToe game = new TicTacToe(3);
		System.out.println(game.move(-1, 0, 0));
		System.out.println(game.move(-1, 1, 1));
		System.out.println(game.move(-1, 2, 2));
		System.out.println(game.move(-1, 0, 2));
	}

	public void printBoard() {
		System.out.println("-------------------------");
		for (int[] col : board) {

			for (int spot : col) {
				System.out.print(spot + " \t| ");
	
			}
			System.out.println();
			System.out.println("-------------------------");
		}
	}
	
	public int getWinner() {
		return this.winner;
	}
	
	public void setWinner(int winner) {
		this.winner = winner;
	}
}

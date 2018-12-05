import static java.lang.System.out;
import java.util.ArrayList;

public class Board{
	
	private int[][] board;
	private int advantage, same, other;
	
	public Board(int board[][]) {
		this.board = board;
		advantage = getAdvantage();
		if (getChips() % 2 == 0) {
			same = -1;
		} else {
			same = 1;
		}
		other =  0 - same;
	}
		
	public int[][] getState() {
		return board;
	}

	public void setState(int[][] newState) {
		board = newState;
	}
	
	public int getSame() {
		return same;
	}
	
	public int getChips() {
		int chipNum = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] != 0) {
					chipNum++;
				}
			}
		}
		return chipNum;
	}
	
	public int getAdvantage() {//returns white chips minus black chips (effectively by how much the computer is winning; positive is good, negative is bad)
		advantage = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == -1) {
					advantage--;
				} else if (board[row][col] == 1) {
					advantage++;
				}
			}
		}
		return advantage;
	}
	
	//converts int coordinates to string coordinates
	public String convertIS (int row, int col) {
		return "" + (char) (row + 97) + col;
	}
	
	//converts string coordinates to int coordinates
	public int[] convertSI (String input) {
		int[] output = {(int) (input.charAt(0) - 97), (int) (input.charAt(1) - 48)};		
		return output;
	}
	
	public ArrayList<String> getAllMoves() {
		ArrayList<String> moves = new ArrayList<String>();
		
		//looks through each square on the board; if it is of the color to move, check in each of the 6 directions it could close off
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == same) {
					int checkR = 0, checkC = 0;
					boolean isTile = false, cont = true;
					
					while (row - checkR > -1 && col - checkC > -1 && cont) {//checking diagonally up and to the left
						if (board[row - checkR][col - checkC] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row - checkR][col - checkC] == 0) {
								moves.add(convertIS(row - checkR, col - checkC));
							}
						}
						
						checkR++;
						checkC++;
					}
					
					checkR = 1;
					checkC = 1;
					isTile = false;
					cont = true;
					
					while (row - checkR > -1 && col + checkC < 8 && cont) {//checking diagonally up and to the right
						if (board[row - checkR][col + checkC] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row - checkR][col + checkC] == 0) {
								moves.add(convertIS(row - checkR, col + checkC));
							}
						}
						
						checkR++;
						checkC++;
					}
					
					checkR = 1;
					checkC = 1;
					isTile = false;
					cont = true;
					
					while (row + checkR < 8 && col + checkC < 8 && cont) {//checking diagonally down and to the right
						if (board[row + checkR][col + checkC] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row + checkR][col + checkC] == 0) {
								moves.add(convertIS(row + checkR, col + checkC));
							}
						}
						
						checkR++;
						checkC++;
					}
					
					checkR = 1;
					checkC = 1;
					isTile = false;
					cont = true;
					
					while (row + checkR < 8 && col - checkC > -1 && cont) {//checking diagonally down and to the left
						if (board[row + checkR][col - checkC] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row + checkR][col - checkC] == 0) {
								moves.add(convertIS(row + checkR, col - checkC));
							}
						}
						
						checkR++;
						checkC++;
					}
					
					checkR = 1;
					isTile = false;
					cont = true;
					
					while (row - checkR > -1 && cont) {//checking straight up
						if (board[row - checkR][col] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row - checkR][col] == 0) {
								moves.add(convertIS(row - checkR, col));
							}
						}
						
						checkR++;
					}
					
					checkR = 1;
					isTile = false;
					cont = true;
					
					while (row + checkR < 8 && cont) {//checking straight down
						if (board[row + checkR][col] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row + checkR][col] == 0) {
								moves.add(convertIS(row + checkR, col));
							}
						}
						
						checkR++;
					}
					
					checkC = 1;
					isTile = false;
					cont = true;
					
					while (col + checkC < 8 && cont) {//checking straight right
						if (board[row][col + checkC] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row][col + checkC] == 0) {
								moves.add(convertIS(row, col + checkC));
							}
						}
						
						checkC++;
					}
					
					checkC = 1;
					isTile = false;
					cont = true;
					
					while (col - checkC > -1 && cont) {//checking straight left
						if (board[row][col - checkC] == other) {
							isTile = true;
						} else {
							cont = false;
							if (isTile && board[row][col - checkC] == 0) {
								moves.add(convertIS(row, col - checkC));
							}
						}
						
						checkC++;
					}
				}
			}
		}//end of loop through all squares
		
		return moves;
	}
	
	public void makeMove(String move) {

		int row = convertSI(move)[0];
		int col = convertSI(move)[1];
		
		board[row][col] = same;
		
		ArrayList<String> endSquares = new ArrayList<String>();
		
		//the following six loops start at the specified square and add the ending square of the flip lines to the endSquares list for each of the six potential flip lines
		int checkR = 1, checkC = 1;
		boolean isTile = false, cont = true;
		while (row - checkR > -1 && col - checkC > -1 && cont) {//checking diagonally up and to the left
			if (board[row - checkR][col - checkC] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row - checkR][col - checkC] == same) {
					endSquares.add(convertIS(row - checkR, col - checkC));
				}
			}
			
			checkR++;
			checkC++;
		}
		
		checkR = 1;
		checkC = 1;
		isTile = false;
		cont = true;
		
		while (row - checkR > -1 && col + checkC < 8 && cont) {//checking diagonally up and to the right
			if (board[row - checkR][col + checkC] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row - checkR][col + checkC] == same) {
					endSquares.add(convertIS(row - checkR, col + checkC));
				}
			}
			
			checkR++;
			checkC++;
		}
		
		checkR = 1;
		checkC = 1;
		isTile = false;
		cont = true;
		
		while (row + checkR < 8 && col + checkC < 8 && cont) {//checking diagonally down and to the right
			if (board[row + checkR][col + checkC] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row + checkR][col + checkC] == same) {
					endSquares.add(convertIS(row + checkR, col + checkC));
				}
			}
			
			checkR++;
			checkC++;
		}
		
		checkR = 1;
		checkC = 1;
		isTile = false;
		cont = true;
		
		while (row + checkR < 8 && col - checkC > -1 && cont) {//checking diagonally down and to the left
			if (board[row + checkR][col - checkC] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row + checkR][col - checkC] == same) {
					endSquares.add(convertIS(row + checkR, col - checkC));
				}
			}
			
			checkR++;
			checkC++;
		}
		
		checkR = 1;
		isTile = false;
		cont = true;
		
		while (row - checkR > -1 && cont) {//checking straight up
			if (board[row - checkR][col] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row - checkR][col] == same) {
					endSquares.add(convertIS(row - checkR, col));
				}
			}
			
			checkR++;
		}
		
		checkR = 1;
		isTile = false;
		cont = true;
		
		while (row + checkR < 8 && cont) {//checking straight down
			if (board[row + checkR][col] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row + checkR][col] == same) {
					endSquares.add(convertIS(row + checkR, col));
				}
			}
			
			checkR++;
		}
		
		checkC = 1;
		isTile = false;
		cont = true;
		
		while (col + checkC < 8 && cont) {//checking straight right
			if (board[row][col + checkC] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row][col + checkC] == same) {
					endSquares.add(convertIS(row, col + checkC));
				}
			}
			
			checkC++;
		}
		
		checkC = 1;
		isTile = false;
		cont = true;
		
		while (col - checkC > -1 && cont) {//checking straight left
			if (board[row][col - checkC] == other) {
				isTile = true;
			} else {
				cont = false;
				if (isTile && board[row][col - checkC] == same) {
					endSquares.add(convertIS(row, col - checkC));
				}
			}
			
			checkC++;
		}
		//goes through the end squares of the flip lines and flips the tiles
		for (int i = 0; i < endSquares.size(); i++) {
			int rowE = convertSI(endSquares.get(i))[0], colE = convertSI(endSquares.get(i))[1], incR = 0, incC = 0;
			
			if (rowE < row) {
				incR = -1;
			} else if (rowE > row) {
				incR = 1;
			}
			
			if (colE < col) {
				incC = -1;
			} else if (colE > col) {
				incC = 1;
			}
			do {
				board[row + incR][col + incC] = same;
				
				if (rowE < row) {
					incR--;
				} else if (rowE > row) {
					incR++;
				}
				
				if (colE < col) {
					incC--;
				} else if (colE > col) {
					incC++;
				}
			} while (!(row + incR == rowE && col + incC == colE));//while the current square is not the end square
		}
		
		same = 0 - same;
		other = 0 - other;

	}
	
	public void switchSides() {
		other = 0 - other;
		same = 0 - same;
	}
	
	

}

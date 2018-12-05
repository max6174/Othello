import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import static java.lang.System.out;

public class OthelloDriver extends Applet implements MouseListener {
	

	private final int SIZE = 60;//width/height of one box -- change this to make board bigger or smaller
	private final int MAX_BRANCH = 6;//increase to make the program calculate farther, at the expense of runtime
	private Board currentBoard;
	private ArrayList<String> highlights;
	private String status;

	public void init() {
				
		setSize(8 * SIZE, 8 * SIZE + 60);
		setBackground(new Color(60, 130, 60));//dark green
		addMouseListener(this);

		
		int[][] newBoard = new int[8][8];

		//setting first 3 rows to be empty
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 8; col++) {
				newBoard[row][col] = 0;
			}
		}
		
		//setting up initial newBoard state of the middle two rows
		newBoard[3][0] = 0;
		newBoard[3][1] = 0;
		newBoard[3][2] = 0;		
		newBoard[3][3] = 1;
		newBoard[3][4] = -1;
		newBoard[3][5] = 0;
		newBoard[3][6] = 0;
		newBoard[3][7] = 0;
		newBoard[4][0] = 0;
		newBoard[4][1] = 0;
		newBoard[4][2] = 0;
		newBoard[4][3] = -1;
		newBoard[4][4] = 1;
		newBoard[4][5] = 0;
		newBoard[4][6] = 0;
		newBoard[4][7] = 0;
		
		//setting last 3 rows to be empty
		for (int row = 5; row < 7; row++) {
			for (int col = 0; col < 8; col++) {
				newBoard[row][col] = 0;
			}
		}
		
		currentBoard = new Board(newBoard);
		
		status = "Your move.";
		
		highlights = new ArrayList<String>();
		highlights.addAll(currentBoard.getAllMoves());
	}
	
	public void paint(Graphics page) {
		
		//drawing lines on board
		page.setColor(Color.black);
		page.drawLine(SIZE, 0, SIZE, SIZE * 8);
		page.drawLine(SIZE * 2, 0, SIZE * 2, SIZE * 8);
		page.drawLine(SIZE * 3, 0, SIZE * 3, SIZE * 8);
		page.drawLine(SIZE * 4, 0, SIZE * 4, SIZE * 8);
		page.drawLine(SIZE * 5, 0, SIZE * 5, SIZE * 8);
		page.drawLine(SIZE * 6, 0, SIZE * 6, SIZE * 8);
		page.drawLine(SIZE * 7, 0, SIZE * 7, SIZE * 8);
		page.drawLine(0, SIZE, SIZE * 8, SIZE);
		page.drawLine(0, SIZE * 2, SIZE * 8, SIZE * 2);
		page.drawLine(0, SIZE * 3, SIZE * 8, SIZE * 3);
		page.drawLine(0, SIZE * 4, SIZE * 8, SIZE * 4);
		page.drawLine(0, SIZE * 5, SIZE * 8, SIZE * 5);
		page.drawLine(0, SIZE * 6, SIZE * 8, SIZE * 6);
		page.drawLine(0, SIZE * 7, SIZE * 8, SIZE * 7);

		//drawing circles for the chips
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (currentBoard.getState()[row][col] == -1) {
					page.setColor(Color.black);
					page.fillOval((int) (col * SIZE + SIZE * 0.1), (int) (row * SIZE + SIZE * 0.1), (int) (0.8 * SIZE), (int) (0.8 * SIZE));
				} else if (currentBoard.getState()[row][col] == 1) {
					page.setColor(Color.white);
					page.fillOval((int) (col * SIZE + SIZE * 0.1), (int) (row * SIZE + SIZE * 0.1), (int) (0.8 * SIZE), (int) (0.8 * SIZE));
				}
			}
		}
		
		//highlighting squares for possible moves
		for (String highlight : highlights) {
			int x = convertSI(highlight)[1] * SIZE;
			int y = convertSI(highlight)[0] * SIZE;
			page.setColor(Color.red);
			page.drawRect(x, y, SIZE, SIZE);
		}
		
		//blank bar at the bottom for the game state info
		page.setColor(Color.white);
		page.fillRect(0, SIZE * 8, SIZE * 8, 60);
		page.setColor(Color.black);
		page.drawRect(0, SIZE * 8, SIZE * 8 - 1, 59);

		page.setFont(new Font("Monospaced", Font.BOLD, 20));
		page.drawString(status, SIZE * 3, SIZE * 8 + 35);
		int blackChips = 0, whiteChips = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (currentBoard.getState()[row][col] == -1) {
					blackChips++;
				} else if (currentBoard.getState()[row][col] == 1) {
					whiteChips++;
				}
			}
		}
		page.drawString("Black: " + blackChips, 25, SIZE * 8 + 35);
		page.drawString("White: " + whiteChips, SIZE * 6, SIZE * 8 + 35);
	}
	
	public String findMove(Board boardInit) {
		
		Board copied = copy(boardInit);

		ArrayList<String> moves = new ArrayList<String>();
		moves.addAll(copied.getAllMoves());
		int currentMax = -1000, maxIndex = 0;
		
		for (int i = 0; i < moves.size(); i++) {//don't change this to a for:each loop -- the index is important to know
			String move = moves.get(i);
			ArrayList<Board> endBoards = new ArrayList<Board>();
			endBoards.addAll(findBoards(copied));
			
			int advantage = 0;
			for (Board endBoard : endBoards) {
				if (endBoard.getChips() == 64) {
					advantage += endBoard.getAdvantage();
				}
			}
			if (advantage > currentMax) {
				currentMax = advantage;
				maxIndex = i;
			}
		}
		return moves.get(maxIndex);
	}
	
	public ArrayList<Board> findBoards(Board boardInit) {

		//int maxBranch = 64 - boardInit.getChips();
		int maxBranch = 64 - boardInit.getChips();
		if (maxBranch > MAX_BRANCH) {
			maxBranch = MAX_BRANCH;
		}
		ArrayList<Board> tree = new ArrayList<Board>();
		tree.add(copy(boardInit));

		
		int branchNum = 1;
		while (branchNum < maxBranch) {
			ArrayList<Board> newBoards = new ArrayList<Board>();
			for (int i = 0; i < tree.size(); i++) {
				out.println("branchNum: " + branchNum);
				final Board branchBoard = copy(tree.get(i));
				ArrayList<String> newMoves = branchBoard.getAllMoves();
				for (String newMove : newMoves) {
					Board newBoard = copy(branchBoard);
					newBoard.makeMove(newMove);
					newBoards.add(newBoard);
				}
			}
			tree.clear();
			tree.addAll(newBoards);
			branchNum++;
		}
		
		return tree;
	}
	
	//converts int coordinates to string coordinates
	public String convertIS(int row, int col) {
		return "" + (char) (row + 97) + col;
	}
	
	//converts string coordinates to int coordinates
	public int[] convertSI(String input) {
		int[] output = {(int) (input.charAt(0) - 97), (int) (input.charAt(1) - 48)};		
		return output;
	}
	
	public Board copy(Board original) {
		int[][] matrixCopy = copyMatrix(original.getState());
		Board copy = new Board(matrixCopy);
		return copy;
	}
	
	public int[][] copyMatrix(int[][] original) {
		int[][] copy = new int[original.length][original[0].length];
		for (int row = 0; row < original.length; row++) {
			for (int col = 0; col < original[row].length; col++) {
				copy[row][col] = original[row][col];
			}
		}
		return copy;
	}
	
	public void mouseClicked(MouseEvent e) {
		
		int row = e.getPoint().y / SIZE;
		int col = e.getPoint().x / SIZE;
		String click = convertIS(row, col);
		
		if (currentBoard.getAllMoves().contains(click)) {
			currentBoard.makeMove(click);
			highlights.clear();
			status = "Thinking...";
			repaint();
			move();
		}			
	}
	
	public void move() {
		Board cloned = copy(currentBoard);
		String move = findMove(cloned);
		currentBoard.makeMove(move);
		highlights = currentBoard.getAllMoves();
		status = "Your move.";
		
	}
	
	
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	

}

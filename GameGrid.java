import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

// A class used for modelling the game grid
public class GameGrid {
   // data fields
   private int gridHeight, gridWidth; // the size of the game grid
   private Tile[][] tileMatrix; // to store the tiles locked on the game grid
   // the tetromino that is currently being moved on the game grid
   private Tetromino currentTetromino = null;
   // the gameOver flag shows whether the game is over or not
   private boolean gameOver = false;
   private Color emptyCellColor; // the color used for the empty grid cells
   private Color lineColor; // the color used for the grid lines
   private Color boundaryColor; // the color used for the grid boundaries
   private double lineThickness; // the thickness used for the grid lines
   private double boxThickness; // the thickness used for the grid boundaries

   private Tetromino nextTetromino = null;
   private int score;
   
   public int getScore() {
       return score;
   }
   
   
   // A constructor for creating the game grid based on the given parameters
   public GameGrid(int gridH, int gridW) {
      // set the size of the game grid as the given values for the parameters
      gridHeight = gridH;
      gridWidth = gridW;
      // create the tile matrix to store the tiles locked on the game grid
      tileMatrix = new Tile[gridHeight][gridWidth];
      // set the color used for the empty grid cells
      emptyCellColor = new Color(0, 0, 58);
      // set the colors used for the grid lines and the grid boundaries
      lineColor = new Color(0, 100, 200);
      boundaryColor = new Color(0, 100, 200);
      // set the thickness values used for the grid lines and the grid boundaries
      lineThickness = 0.002;
      
      score=0;
      boxThickness = 10 * lineThickness;
   }

   // A setter method for the currentTetromino data field
   public void setCurrentTetromino(Tetromino currentTetromino) {
      this.currentTetromino = currentTetromino;
   }

   // A setter method for the nextTetromino data field
   public void setNextTetromino(Tetromino nextTetromino) {
      this.nextTetromino = nextTetromino;
   }
   
   // A method used for displaying the game grid
   public void display() {
      // clear the background to emptyCellColor
      StdDraw.clear(emptyCellColor);
      // draw the game grid
      drawGrid();
      // draw the current/active tetromino if it is not null (the case when the
      // game grid is updated)
      if (currentTetromino != null)
         currentTetromino.draw();
      // draw a box around the game grid
      drawBoundaries();
      drawSidebar();
   
      // show the resulting drawing with a pause duration = 50 ms
      StdDraw.show();
      StdDraw.pause(50);
   }
   
   public void drawSidebar() {
	    // colors used for the menu
	    Color backgroundColor = new Color(42, 69, 99);
	    Color buttonColor = new Color(250, 250, 58);
	    Color textColor = new Color(31, 160, 239);
	    double totalGridWidth = gridWidth + gridWidth / 3.0;
	    double sidebarCenterX = totalGridWidth - (totalGridWidth / 3.0) / 2.0;
	    double sidebarCenterY = gridHeight - (gridHeight / 3.0) / 2.0;
	    StdDraw.setPenColor(buttonColor);
	    // display the score text on top right
	    Font font = new Font("Arial", Font.PLAIN, 25);
	    StdDraw.setFont(font);
	    StdDraw.setPenColor(buttonColor);
	    String textToDisplay = "Score:";
	    StdDraw.text(sidebarCenterX, sidebarCenterY, textToDisplay);
	    textToDisplay = String.valueOf(score);
	    StdDraw.text(sidebarCenterX, sidebarCenterY - 0.5, textToDisplay);
	    textToDisplay = "Next";
	    StdDraw.text(sidebarCenterX, sidebarCenterY - 5, textToDisplay);
	    textToDisplay = "Tetromino:";
	    StdDraw.text(sidebarCenterX, sidebarCenterY - 5.5, textToDisplay);
	 // Draw the next tetromino
	    if (nextTetromino != null) {
	        double tetrominoDrawX = sidebarCenterX - 1;
	        double tetrominoDrawY = sidebarCenterY - 8;
	        nextTetromino.draw(tetrominoDrawX, tetrominoDrawY);
	    }

	}

   // A method for drawing the cells and the lines of the game grid
   public void drawGrid() {
	  deleteTile(); 
      // for each cell of the game grid
      for (int row = 0; row < gridHeight; row++)
         for (int col = 0; col < gridWidth; col++)
            // draw the tile if the grid cell is occupied by a tile
            if (tileMatrix[row][col] != null)
               tileMatrix[row][col].draw(new Point(col, row));
      // draw the inner lines of the grid
      StdDraw.setPenColor(lineColor);
      StdDraw.setPenRadius(lineThickness);
      // x and y ranges for the game grid
      double startX = -0.5, endX = gridWidth - 0.5;
      double startY = -0.5, endY = gridHeight - 0.5;
      for (double x = startX + 1; x < endX; x++) // vertical inner lines
         StdDraw.line(x, startY, x, endY);
      for (double y = startY + 1; y < endY; y++) // horizontal inner lines
         StdDraw.line(startX, y, endX, y);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
   }

   // A method for drawing the boundaries around the game grid
   public void drawBoundaries() {
      // draw a bounding box around the game grid as a rectangle
      StdDraw.setPenColor(boundaryColor); // using boundaryColor
      // set the pen radius as boxThickness (half of this thickness is visible
      // for the bounding box as its lines lie on the boundaries of the canvas)
      StdDraw.setPenRadius(boxThickness);
      // the center point coordinates for the game grid
      double centerX = gridWidth / 2 - 0.5, centerY = gridHeight / 2 - 0.5;
      StdDraw.rectangle(centerX, centerY, gridWidth / 2, gridHeight / 2);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
   }

   // A method for checking whether the grid cell with given row and column
   // indexes is occupied by a tile or empty
   public boolean isOccupied(int row, int col) {
      // considering newly entered tetrominoes to the game grid that may have
      // tiles out of the game grid (above the topmost grid row)
      if (!isInside(row, col))
         return false;
      // the cell is occupied by a tile if it is not null
      return tileMatrix[row][col] != null;
   }

   // A method for checking whether the cell with given row and column indexes
   // is inside the game grid or not
   public boolean isInside(int row, int col) {
      if (row < 0 || row >= gridHeight)
         return false;
      if (col < 0 || col >= gridWidth)
         return false;
      return true;
   }

   // A method that locks the tiles of the landed tetromino on the game grid while
   // checking if the game is over due to having tiles above the topmost grid row.
   // The method returns true when the game is over and false otherwise.
   public boolean updateGrid(Tile[][] tilesToLock, Point blcPosition) {
      // necessary for the display method to stop displaying the tetromino
      currentTetromino = null;
      // lock the tiles of the current tetromino (tilesToLock) on the game grid
      int nRows = tilesToLock.length, nCols = tilesToLock[0].length;
      for (int col = 0; col < nCols; col++) {
         for (int row = 0; row < nRows; row++) {
            // place each tile onto the game grid
            if (tilesToLock[row][col] != null) {
               // compute the position of the tile on the game grid
               Point pos = new Point();
               pos.setX(blcPosition.getX() + col);
               pos.setY(blcPosition.getY() + (nRows - 1) - row);
               if (isInside(pos.getY(), pos.getX()))
                  tileMatrix[pos.getY()][pos.getX()] = tilesToLock[row][col];
               // the game is over if any placed tile is above the game grid
               else
                  gameOver = true;
            }
         }
      } 
      checkGrid();
      merge();
      // return the value of the gameOver flag
      return gameOver;
     
   }
   
   
   public void checkGrid() {
       for (int row = 0; row < gridHeight; row++) {
           if (!Arrays.asList(tileMatrix[row]).contains(null)) {
               deleteRow(row);
               moveRow(row);
               checkGrid();
           }
       }
   }
   
   
   
   public void merge() {
       for (int row_i = 0; row_i < gridHeight - 1; row_i++) {
           for (int col_i = 0; col_i < gridWidth; col_i++) {
               if (tileMatrix[row_i][col_i] != null && tileMatrix[row_i + 1][col_i] != null) {
                   if (tileMatrix[row_i][col_i].getNumber() == tileMatrix[row_i + 1][col_i].getNumber()) {
                       score += tileMatrix[row_i][col_i].getNumber() * 2;
                       tileMatrix[row_i][col_i].doubleNumber();
                       tileMatrix[row_i + 1][col_i] = null;
                       moveColumn(col_i, row_i + 1);
                       merge();
                   }
               }
           }
       }

   }
   // Method for deleting a row when it is full
   private void deleteRow(int row) {
       for (int i = 0; i < gridWidth; i++) {
           int newScore = tileMatrix[row][i].getNumber();
           score += newScore;
       }

       tileMatrix = removeRow(tileMatrix, row);
       tileMatrix = addEmptyRow(tileMatrix);
   }

   // Helper method to remove a row from the tile matrix
   private Tile[][] removeRow(Tile[][] matrix, int row) {
       Tile[][] newMatrix = new Tile[matrix.length - 1][matrix[0].length];

       for (int i = 0; i < row; i++) {
           newMatrix[i] = matrix[i];
       }
       for (int i = row + 1; i < matrix.length; i++) {
           newMatrix[i - 1] = matrix[i];
       }
       return newMatrix;
   }

   // Helper method to add an empty row to the top of the tile matrix
   private Tile[][] addEmptyRow(Tile[][] matrix) {
       Tile[][] newMatrix = new Tile[matrix.length + 1][matrix[0].length];
       for (int i = 0; i < matrix.length; i++) {
           newMatrix[i] = matrix[i];
       }
       newMatrix[matrix.length] = new Tile[matrix[0].length];
       return newMatrix;
   }

   // Method for moving tiles down when a row is deleted
// Method to move a row down when a row is deleted
   private void moveRow(int row) {
	    for (int row_i = row; row_i < gridHeight - 1; row_i++) {
	        for (int col_i = 0; col_i < gridWidth; col_i++) {
	            if (tileMatrix[row_i + 1][col_i] != null) {
	                tileMatrix[row_i][col_i] = tileMatrix[row_i + 1][col_i];
	                tileMatrix[row_i + 1][col_i] = null;
	            }
	        }
	    }
	}

   // Method for moving tiles down in a column when a tile is removed
   private void moveColumn(int col, int startRow) {
       for (int row = startRow; row < gridHeight - 1; row++) {
           tileMatrix[row][col] = tileMatrix[row + 1][col];
           tileMatrix[row + 1][col] = null;
       }
   }

   // Method for deleting a tile and updating the score when no tiles are around it
   public void deleteTile() {
       for (int row_i = 1; row_i < gridHeight - 1; row_i++) {
           for (int col_i = 1; col_i < gridWidth - 1; col_i++) {
               if (tileMatrix[row_i][col_i] != null) {
                   if (tileMatrix[row_i + 1][col_i] == null &&
                           tileMatrix[row_i - 1][col_i] == null &&
                           tileMatrix[row_i][col_i + 1] == null &&
                           tileMatrix[row_i][col_i - 1] == null) {
                	   int newScore = tileMatrix[row_i][col_i].getNumber();
                	   score += newScore;
                	   tileMatrix[row_i][col_i] = null;
                	   //deleteTile();
                   }
               }              
           }
       }
   }
}
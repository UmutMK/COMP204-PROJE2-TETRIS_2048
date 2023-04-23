import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw
import java.util.Random;

// The main class to run the Tetris 2048 game
public class Tetris2048 {

   public static void main(String[] args) {
	  boolean gamePaused = false; // Add this flag for game pause status
      // set the size of the game grid
      int gridH = 12, gridW = 8;
      // set the size of the drawing canvas
      int canvasH = 80 * gridH, canvasW = 80 * gridW + gridW/3 * 80;
      StdDraw.setCanvasSize(canvasW, canvasH);
      // set the scale of the coordinate system
      StdDraw.setXscale(-0.5, gridW + ((double) gridW / 3) - 0.5);
      StdDraw.setYscale(-0.5, gridH - 0.5);
      // double buffering enables computer animations, creating an illusion of
      // motion by repeating four steps: clear, draw, show and pause
      StdDraw.enableDoubleBuffering();

      // set the dimension values stored and used in the Tetromino class
      Tetromino.gridHeight = gridH;
      Tetromino.gridWidth = gridW;

      // create the game grid
      GameGrid grid = new GameGrid(gridH, gridW);
      // create the first tetromino to enter the game grid
      // by using the createTetromino method defined below
      Tetromino currentTetromino = createTetromino();
      grid.setCurrentTetromino(currentTetromino);

      // display a simple menu before opening the game
      // by using the displayGameMenu method defined below
      displayGameMenu(gridH, gridW);
      // display the how to play menu before starting the game
      displayHowToPlayMenu(gridH, gridW);
      // the main game loop (using some keyboard keys for moving the tetromino)
      // -----------------------------------------------------------------------
      int iterationCount = 0; // used for the speed of the game
      while (true) {
         // check user interactions via the keyboard
         // --------------------------------------------------------------------
         // if the left arrow key is being pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            // move the active tetromino left by one
            currentTetromino.move("left", grid);
         // if the right arrow key is being pressed
         }else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            // move the active tetromino right by one
            currentTetromino.move("right", grid);
         // if the down arrow key is being pressed
         }else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
            // move the active tetromino down by one
            currentTetromino.move("down", grid);
            
         }else if (StdDraw.isKeyPressed(KeyEvent.VK_D)) {
            // move the active tetromino rotateCounterclockwise by one
        	currentTetromino.rotateCounterclockwise(grid);
         	StdDraw.pause(50);
         
         }else if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
            // move the active tetromino rotateClockwise by one
            currentTetromino.rotateClockwise(grid);
         	StdDraw.pause(50);
         }
         else if (StdDraw.isKeyPressed(KeyEvent.VK_P)) {
        	 gamePaused = !gamePaused;
             if (gamePaused) {
                displayPauseMenu(gridH, gridW);
                StdDraw.show();
             }
             StdDraw.pause(200);
          }

          // If the game is paused, don't process the game loop further
          if (gamePaused) {
             continue;
          }
          	StdDraw.pause(50);
          
         // move the active tetromino down by 1 once in 10 iterations (auto fall)
         boolean success = true;
         if (iterationCount % 10 == 0)
            success = currentTetromino.move("down", grid);
         iterationCount++;

         // place the active tetromino on the grid when it cannot go down anymore
         if (!success) {
            // get the tile matrix of the tetromino without empty rows and columns
            currentTetromino.createMinBoundedTileMatrix();
            Tile[][] tiles = currentTetromino.getMinBoundedTileMatrix();
            Point pos = currentTetromino.getMinBoundedTileMatrixPosition();
            // update the game grid by locking the tiles of the landed tetromino
            boolean gameOver = grid.updateGrid(tiles, pos);
            // end the main game loop if the game is over
            if (gameOver)
               break;
            // create the next tetromino to enter the game grid
            // by using the createTetromino function defined below
            currentTetromino = createTetromino();
            grid.setCurrentTetromino(currentTetromino);
         }

         // display the game grid and the current tetromino
         grid.display();

      }

      // print a message on the console that the game is over
      System.out.println("Game over!");
      // Replace the "System.out.println("Game over!");" line with a call to displayGameOver
      displayGameOver(gridH, gridW, grid);
   }
   

   // A method for creating a random shaped tetromino to enter the game grid
   public static Tetromino createTetromino() {
      // the type (shape) of the tetromino is determined randomly
      char[] tetrominoTypes = { 'I', 'O', 'Z' , 'J', 'L', 'S' , 'T' };
      Random random = new Random();
      int randomIndex = random.nextInt(tetrominoTypes.length);
      char randomType = tetrominoTypes[randomIndex];
      // create and return the tetromino
      Tetromino tetromino = new Tetromino(randomType);
      return tetromino;
   }
   
   public static void displayHowToPlayMenu(int gridHeight, int gridWidth) {
	   // set colors and font for the how to play menu
	   Color backgroundColor = new Color(42, 69, 99);
	   Color textColor = new Color(255, 255, 255);
	   Font font = new Font("Arial", Font.PLAIN, 20);

	   // clear the background canvas to background_color
	   StdDraw.clear(backgroundColor);
	   
	   // display the title of the menu
	   StdDraw.setFont(new Font("Arial", Font.BOLD, 30));
	   StdDraw.setPenColor(textColor);
	   StdDraw.text(gridWidth/2.0, gridHeight-2, "How to Play");

	   // display the instructions for how to play the game
	   StdDraw.setFont(font);
	   StdDraw.setPenColor(textColor);
	   String[] instructions = {
	      "1-)Use the left and right arrow keys to move the tetromino",
	      "2-)Use the down arrow key to drop the tetromino faster",
	      "3-)Use the 'A' and 'D' keys to rotate the tetromino",
	      "4-)Complete a row to make it disappear and earn points",
	      "5-)The game is over when the tetrominos reach the top of the grid"
	   };
	   double startY = gridHeight - 5;
	   for (String instruction : instructions) {
	      StdDraw.text(gridWidth/2.0, startY, instruction);
	      startY += -0.5;
	   }

	   // display a button to return to the main menu
	   double buttonW = gridWidth/3.0, buttonH = 2;
	   double buttonX = gridWidth/2.0, buttonY = 2;
	   StdDraw.setPenColor(Color.GREEN);
	   StdDraw.filledRectangle(buttonX, buttonY, buttonW/2, buttonH/2);
	   StdDraw.setPenColor(textColor);
	   StdDraw.text(buttonX, buttonY, "go to play");

	   // menu interaction loop
	   while (true) {
	      // display the menu and wait for a short time (50 ms)
	      StdDraw.show();
	      StdDraw.pause(50);

	      // check if the mouse is being pressed on the button
	      if (StdDraw.isMousePressed()) {
	         // get the x and y coordinates of the position of the mouse
	         double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
	         // check if these coordinates are inside the button
	         if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
	            if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
	               break; // break the loop to end the method and return to the main menu
	      }
	   }
	}


   // A method for displaying a simple menu before starting the game
   public static void displayGameMenu(int gridHeight, int gridWidth) {
      // colors used for the menu
      Color backgroundColor = new Color(42, 69, 99);
      Color buttonColor = new Color(25, 255, 228);
      Color textColor = new Color(31, 160, 239);
      // clear the background canvas to background_color
      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/menu_image.png";
      // center coordinates to display the image
      double imgCenterX = (gridWidth - 1) / 2.0, imgCenterY = gridHeight - 5;
      // display the image
      StdDraw.picture(imgCenterX, imgCenterY, imgFile);
      // the width and the height of the start game button
      double buttonW = gridWidth - 1.5, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 2;
      // display the start game button as a filled rectangle
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW / 2, buttonH / 2);
      // display the text on the start game button
      Font font = new Font("Arial", Font.PLAIN, 25);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String textToDisplay = "Click Here to Start the Game";
      StdDraw.text(buttonX, buttonY, textToDisplay);
      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
                  break; // break the loop to end the method and start the game
         }
      }
   }
   
	// Add the displayGameOver method to the Tetris2048 class
	public static void displayGameOver(int gridHeight, int gridWidth, GameGrid grid) {
	   Color buttonColor = new Color(194, 24, 27);
	   Color textColor = new Color(255, 255, 255);
	   String imgFile = "images/game_over_menu.png";
	   double imgCenterX = (gridWidth - 1) / 1.5, imgCenterY = gridHeight - 5;
	
	   double imgWidth = 10;
	   double imgHeight = 5;
	   StdDraw.picture(imgCenterX, imgCenterY, imgFile, imgWidth, imgHeight);
	
	   // New Game Button
	   double newGameButtonW = gridWidth - 1.5, newGameButtonH = 2;
	   double newGameButtonX = imgCenterX, newGameButtonY = 4;
	   StdDraw.setPenColor(buttonColor);
	   StdDraw.filledRectangle(newGameButtonX, newGameButtonY, newGameButtonW / 2, newGameButtonH / 2);
	
	   // Exit Game Button
	   double exitGameButtonW = gridWidth - 1.5, exitGameButtonH = 2;
	   double exitGameButtonX = imgCenterX, exitGameButtonY = 1;
	   StdDraw.setPenColor(buttonColor);
	   StdDraw.filledRectangle(exitGameButtonX, exitGameButtonY, exitGameButtonW / 2, exitGameButtonH / 2);
	
	   Font font = new Font("Helvetica Neue", Font.PLAIN, 25);
	   StdDraw.setFont(font);
	   StdDraw.setPenColor(textColor);
	
	   StdDraw.text(imgCenterX, 5.5, "Score : " + grid.getScore());
	   StdDraw.text(newGameButtonX, newGameButtonY, "New Game");
	   StdDraw.text(exitGameButtonX, exitGameButtonY, "Exit Game");
	
	   while (true) {
	       StdDraw.show();
	       StdDraw.pause(50);
	
	       if (StdDraw.isMousePressed()) {
	           double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
	
	           // New Game button clicked
	           if (mouseX >= newGameButtonX - newGameButtonW / 2 && mouseX <= newGameButtonX + newGameButtonW / 2) {
	               if (mouseY >= newGameButtonY - newGameButtonH / 2 && mouseY <= newGameButtonY + newGameButtonH / 2) {
	                  displayGameMenu(gridHeight, gridWidth);
	                  main(null); // break the loop to end the method and start the game
	               }
	            }
	
	           // Exit Game button clicked
	           if (mouseX > exitGameButtonX - exitGameButtonW / 2 && mouseX < exitGameButtonX + exitGameButtonW / 2
	                   && mouseY > exitGameButtonY - exitGameButtonH / 2 && mouseY < exitGameButtonY + exitGameButtonH / 2) {
	               System.exit(0);
	           }
	       }
	   }
	}
	 public static void displayPauseMenu(int gridHeight, int gridWidth) {
	      // Set colors and font for the pause menu
	      Color backgroundColor = new Color(42, 69, 99, 150);
	      Color textColor = new Color(255, 255, 255);
	      Font font = new Font("Arial", Font.PLAIN, 25);

	      // Draw the background for the pause menu
	      StdDraw.setPenColor(backgroundColor);
	      StdDraw.filledRectangle(gridWidth / 2.0, gridHeight / 2.0, gridWidth / 2.0, gridHeight / 2.0);

	      // Draw the text for the pause menu
	      StdDraw.setFont(font);
	      StdDraw.setPenColor(textColor);
	      StdDraw.text(gridWidth / 2.0, gridHeight / 2.0, "PAUSED");
	      StdDraw.text(gridWidth / 2.0, gridHeight / 2.0 - 2, "Press 'P' to resume");
	   }
}
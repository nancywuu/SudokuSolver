/** LAB FIVE
 * @author NANCY WU
 * VERSION 2.0
 **/

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.TimerTask;
import javafx.stage.Modality;
import javafx.scene.shape.Line;
import java.util.Timer;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Animation;

public class sudokuSolver extends Application{

	private GridPane gridPane = new GridPane();
	private Stage errorWindow = new Stage();
	private Scene gridScene = new Scene(gridPane, 600, 600);
	private Button [][] puzzle = new Button[9][9];
	private boolean [][] track = new boolean[9][9];
	private Button okButton = new Button("OK");
	private String [][] nons = new String[9][9];
	private boolean error = false;
	private boolean incomplete = true;
	private boolean initError = true;
	private ArrayList<String> steps = new ArrayList<String>();
	private int index = 0;
	private Timer timer = new Timer();

	/**
	* NAME: buildGrid
	* FUNCTION: builds up buttons for the sudoku array and adds them into the GridPane
	* @param GridPane gridPane: this is the gridpane that the buttons will be inserted into
	**/

	private void buildGrid(GridPane gridPane){
		for(int x = 0; x < 9; x++){
    		ColumnConstraints column = new ColumnConstraints(50);
    		gridPane.getColumnConstraints().add(column);

    		for(int y = 0; y < 9; y++){
    			RowConstraints row = new RowConstraints(50);
	    		gridPane.getRowConstraints().add(row);
    			Button button = new Button();
    			button.setStyle("-fx-background-color: #b76e79; ");
	    		button.setStyle("-fx-border-color: #280947; -fx-border-width: 2px;");
	    		button.setMaxWidth(Double.MAX_VALUE);
	    		button.setMaxHeight(Double.MAX_VALUE);

	    		final int indexX = x;
	    		final int indexY = y;
	    		
    			puzzle[x][y] = button;
    			track[x][y] = false;

	    		puzzle[indexX][indexY].setOnAction(new EventHandler<ActionEvent>() { 
	    			@Override
		            public void handle(ActionEvent event) {
		                if(puzzle[indexX][indexY].getText().equals("")){
		                	puzzle[indexX][indexY].setText("1");
		                	button.setStyle("-fx-border-color: #5c64c9; -fx-border-width: 3px;");
		                } else if(puzzle[indexX][indexY].getText().equals("9")){
		                	puzzle[indexX][indexY].setText("");
		                	puzzle[indexX][indexY].setStyle("-fx-border-color: #280947; -fx-border-width: 2px;");
		                } else {
		                	puzzle[indexX][indexY].setText(String.valueOf(Integer.parseInt(button.getText()) + 1));
		                	puzzle[indexX][indexY].setStyle("-fx-border-color: #5c64c9; -fx-border-width: 3px;");
		                }
		                
		                track[indexX][indexY] = true;
		            }
	    		});
    			GridPane.setConstraints(button,x,y);
				gridPane.getChildren().add(button);
    		}
    	}
	}

	/**
	* NAME: resetArray
	* FUNCTION: builds up buttons for the sudoku array and adds them into the GridPane
	**/

	private void resetArray(){
		for(int x = 0; x < 9; x++){
			for(int y = 0; y < 9; y++){
				puzzle[x][y].setText("");
				puzzle[x][y].setStyle("-fx-border-color: #280947; -fx-border-width: 2px;");
				track[x][y] = false;
			}
		}
		gridPane.getChildren().clear();
		steps.clear();
		for(int ycoor = 0; ycoor < 9; ycoor++){
			for(int xcoor = 0; xcoor < 9; xcoor++){
				track[xcoor][ycoor] = false;
				puzzle[xcoor][ycoor] = null;
			}

			//puzzle.remove(xcoor);
		}
		buildGrid(gridPane);
	}

    /**
	 * NAME: validCheck 
	 * FUNCTION: checks if the sudoku is valid with existing inputed numbers
	 * @return boolean is the sudoku currently valid
	 */

    private boolean validCheck() { //checks if the sudoku is valid thru row snad columns
	    for (int i = 0; i < 9; i++) {
	        int[] row = new int[9];
	        int[] square = new int[9];
	        int[] column = new int[9];

	        for (int j = 0; j < 9; j ++) {
	      		if(!puzzle[j][i].getText().equals("")){
	            	row[j] = Integer.parseInt(puzzle[j][i].getText());
		        } else {
		        	row[j] = 0;
		        }

		        if(!puzzle[i][j].getText().equals("")){
		            column[j] = Integer.parseInt(puzzle[i][j].getText());
		        } else {
		        	column[j] = 0;
		        }

		        if(!puzzle[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3].getText().equals("")){
		            square[j] = Integer.parseInt(puzzle[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3].getText());
		        } else {
		        	square[j] = 0;
		        }
	        }

	        if (!(buttonCheck(column) && buttonCheck(row) && buttonCheck(square))) {
	        	return false;
	        }
	    }

	    return true;
	}

	/**
	 * NAME: buttonCheck 
	 * FUNCTION: checks if the row/column/button is valid
	 * @param check the row/button/column being checked
	 * @return boolean does it contain that same number
	 */

	private boolean buttonCheck(int[] check) { 
	    ArrayList<Integer> nums = new ArrayList<>();
	    for (int i : check) {
	        if (nums.contains(i) && i != 0) {
	            return false;
	        }
	        nums.add(i);
	    }
	    return true;
	}

	/**
	 * NAME: findNumbers
	 * FUNCTION: solves the sudoku and finds the right numbers
	 * @return boolean is the sudoku solved
	 */

	private boolean findNumbers() { 
	    for (int x = 0; x < 9; x++) {
	        for (int y = 0; y < 9; y++) {
	            if (puzzle[x][y].getText().toString().equals("")){
	                for (int i = 1; i <= 9; i++) {
	                	puzzle[x][y].setText(String.valueOf(i));
	                    String step = x +"," + y +","+ i;
	                    steps.add(step);

	                    if (validCheck() && findNumbers()){
	                        return true;
	                    }
	                }

	                puzzle[x][y].setText("");
	                String step = x +"," + y +","+ " ";
	                steps.add(step);

	                return false;
	            }
	        }
	    }

	    return true;
	}

	private void errorPopUp() {
		if(initError){
			errorWindow.initModality(Modality.APPLICATION_MODAL);
			initError = false;
		}
		errorWindow.setTitle("ERROR"); 
		Label descrip = new Label("This puzzle is unsolvable. Please enter another puzzle.");	     

        VBox vBoxError = new VBox(10);      
		vBoxError.getChildren().addAll(descrip, okButton);      

		Scene errorScene = new Scene(vBoxError, 700, 250);	      
		errorWindow.setScene(errorScene);		      
		errorWindow.showAndWait();
	}

	private void animation(){
		index = 0;

		for (int x = 0; x < 9; x++) {
			for (int i = 0; i < 9; i++) {
				if (!track[x][i]) {
					puzzle[x][i].setText("");
				}
			}
		}

		Timeline timeLine = new Timeline (new KeyFrame(Duration.millis(1),
			new EventHandler<ActionEvent>() {
				@Override 
				public void handle(ActionEvent e) {
					if(index < steps.size()) {
						String[] parse = steps.get(index).split(",", 3);
						puzzle[Integer.parseInt(parse[0])][Integer.parseInt(parse[1])].setText(parse[2]);

						if (!parse[2].equals(" ")) {
							for (int x = 1; x <= Integer.valueOf(parse[2]); x++) {
								puzzle[Integer.parseInt(parse[0])][Integer.parseInt(parse[1])].setText(String.valueOf(x));
							}
						}

						index++;
					}
				}

			}
		));

		timeLine.setCycleCount(Animation.INDEFINITE);
		timeLine.setOnFinished(e -> {
			System.out.println("timeline halt triggered");
			boolean incomplete = false;
        	for(int xcoord = 0; xcoord < 9; xcoord++){
        		for(int ycoord = 0; ycoord < 9; ycoord++){
        			if(puzzle[xcoord][ycoord].getText().equals("")){
        				incomplete = true;
        			}
         		}
        	}
        	if(incomplete){
        		errorPopUp();
        	}
		});
		timeLine.play();
	}



	/**
	* NAME: main
	* FUNCTION: launches application
	**/

	public static void main(String[] args) {
    	Application.launch(args);
	}

	/**
	* NAME: start
	* FUNCTION: initiates the program and calls menu scene
	**/

    @Override
    public void start(Stage primaryStage) {
    	for(int i = 0; i < 9; i++){
    		for(int j = 0; j < 9; j++){
    			nons[i][j] = "";
    		}
    	}
    	buildGrid(gridPane);

    	Text welcome = new Text();
		welcome.setText("Enter Numbers for a Valid Sudoku Puzzle!");
		welcome.setFont(new Font(20));
		GridPane.setRowIndex(welcome, 9);
		GridPane.setColumnIndex(welcome, 0);
		gridPane.getChildren().add(welcome);

		Button solveButton = new Button("  SOLVE  ");
		solveButton.setStyle("-fx-background-color: #d9f7d9; ");
		solveButton.setStyle("-fx-border-color: #60c960; -fx-border-width: 2px;");
		GridPane.setConstraints(solveButton,2,10,6,1);

		Button resetButton = new Button("  RESET  ");
		resetButton.setStyle("-fx-background-color: #fcdee4; ");
		resetButton.setStyle("-fx-border-color: #ba2e4a; -fx-border-width: 2px;");
		GridPane.setConstraints(resetButton,0,10,6,1);

		solveButton.setOnAction(new EventHandler<ActionEvent>() { 
			@Override
            public void handle(ActionEvent event) {
            	if(validCheck()){
            		findNumbers();
            		animation();
            	} else {
            		errorPopUp();
            	}
        	}
		});

		resetButton.setOnAction(new EventHandler<ActionEvent>() { 
			@Override
            public void handle(ActionEvent event) {
       			resetArray();  
       			gridPane.getChildren().addAll(welcome, resetButton, solveButton);       
            }
		});

		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				errorWindow.close();
				resetArray();
				gridPane.getChildren().addAll(welcome, resetButton, solveButton);
			}
		});
    	
		gridPane.getChildren().addAll(resetButton, solveButton);

    	primaryStage.setScene(gridScene);
        primaryStage.show();

		primaryStage.setOnCloseRequest(event -> {
		    primaryStage.setTitle(null);
		});
    }
}






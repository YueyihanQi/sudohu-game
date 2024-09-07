package org.example.sudokugame_javafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import static org.example.sudokugame_javafx.SudokuLogic.sudoku;


public class SudokuGame extends Application {

    private GridPane sudokuGrid; // Declare sudokuGrid as a field
    private long startTime = 0L; // Timer start time
    private AnimationTimer timer; // Timer object
    private int level = 1; // Current level
    private boolean musicOn = true; // Music toggle
    MusicPlayer musicPlayer = new MusicPlayer("src/main/resource/bgMusic.wav");

    @Override
    public void start(Stage primaryStage) {
        try {
            SudokuLogic.readSudoku(); // read sudoku
            SudokuLogic.readAnswer(); // read answer
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create the root layout
        VBox root = new VBox();
        root.setPadding(new Insets(10)); // Set padding
        root.setAlignment(Pos.CENTER); // Center align the children
        root.setSpacing(20); // Add vertical spacing between elements

        // Create the title label
        Label titleLabel = new Label("Sudoku Game");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black; -fx-font-weight: bold;"); // Set styles

        // Timer label
        Label timerLabel = new Label("Timer: ");
        timerLabel.setAlignment(Pos.CENTER); // Set center alignment for the timer label

        // Level label
        Label levelLabel = new Label("Level: " + level);
        levelLabel.setAlignment(Pos.CENTER); // Set center alignment for the level label

        // Create an HBox for timerLabel and levelLabel
        HBox labelsBox = new HBox(10);
        labelsBox.setAlignment(Pos.CENTER);
        labelsBox.getChildren().addAll(timerLabel, levelLabel);

        // Start updating the level label
        startLevelUpdate(levelLabel);

        // Create the Sudoku grid
        sudokuGrid = createSudokuGrid(); // Call the method to create the Sudoku grid

        // Control buttons
        HBox controlButtons = new HBox(10); // Create an HBox for the control buttons with spacing of 10 pixels
        controlButtons.setAlignment(Pos.CENTER); // Center align the control buttons

        Button resetButton = new Button("Reset"); // Create a Reset button
        Button submitButton = new Button("Submit"); // Create a Submit button
        Button helpButton = new Button("Help"); // Create a Help button
        Button musicToggleButton = new Button("Music: ON"); // Create a Music toggle button

        // Add CSS styles to the labels
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: black; -fx-font-weight: bold;");
        timerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
        levelLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");

        // Add CSS styles to the control buttons
        resetButton.setStyle("-fx-font-size: 16px;");
        submitButton.setStyle("-fx-font-size: 16px;");
        helpButton.setStyle("-fx-font-size: 16px;");
        musicToggleButton.setStyle("-fx-font-size: 16px;");

        submitButton.setOnAction(e -> {
            if (SudokuLogic.checkSudoku()) {
                System.out.println("Sudoku is correct!");
            } else {
                System.out.println("Sudoku is incorrect!");
            }
        });

        // Add button actions
        resetButton.setOnAction(e -> resetGame());
        submitButton.setOnAction(e -> submitGame());
        helpButton.setOnAction(e -> showHelp(sudokuGrid));
        musicToggleButton.setOnAction(e -> toggleMusic(musicToggleButton));

        controlButtons.getChildren().addAll(resetButton, submitButton, helpButton, musicToggleButton); // Add buttons to the HBox

        // Add elements to root layout
        root.getChildren().addAll(titleLabel, new HBox(10, timerLabel, levelLabel), sudokuGrid, controlButtons);

        // Scene setup
        Scene scene = new Scene(root, 550, 550); // Create a scene with the root layout and size of 500x500 pixels
        scene.setFill(javafx.scene.paint.Color.rgb(0, 51, 102)); // Set the background color of the scene
        primaryStage.setTitle("Sudoku Game"); // Set the title of the stage
        primaryStage.setScene(scene); // Set the scene for the stage
        primaryStage.show(); // Display the stage

        // Start the timer
        startTimer(timerLabel);
    }




    // Method to create the Sudoku grid
    private GridPane createSudokuGrid() {
        GridPane sudokuGrid = new GridPane(); // Create a new Sudoku grid
        sudokuGrid.setHgap(2); // Set horizontal gap
        sudokuGrid.setVgap(2); // Set vertical gap
        sudokuGrid.setAlignment(Pos.CENTER); // Center align the Sudoku grid

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int regionRow = i / 3; // Calculate the region row
                int regionCol = j / 3; // Calculate the region column

                StackPane cellPane = new StackPane(); // Create a StackPane for the cell
                cellPane.setPrefSize(40, 40); // Set the size of the cell

                // Create a rectangle with different background color for each region
                Rectangle backgroundRect = new Rectangle(40, 40);
                if ((regionRow + regionCol) % 2 == 0) {
                    backgroundRect.setFill(Color.LIGHTGRAY); // Set light gray background for even regions
                } else {
                    backgroundRect.setFill(Color.GRAY); // Set gray background for odd regions
                }

                int value = sudoku[i][j]; // Get the value from SudokuLogic
                if (value != 0) {
                    // If the value is not 0, display it as a label
                    Label label = new Label(String.valueOf(value));
                    label.setPrefSize(40, 40);
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;"); // Set label style
                    cellPane.getChildren().addAll(backgroundRect, label); // Add label and background to cell pane
                } else {
                    // If the value is 0, create an editable TextField
                    TextField textField = new TextField();
                    textField.setPrefSize(40, 40); // Set text field size
                    textField.setStyle("-fx-background-color: transparent; -fx-border-width: 0; -fx-text-fill: blue;-fx-font-weight: bold;"); // Set text field style
                    textField.setAlignment(Pos.CENTER); // Center align the text
                    textField.setEditable(true); // Allow user to edit the text field
                    final int row = i;
                    final int col = j;
                    textField.setOnKeyReleased(event -> handleTextFieldInput(event, row, col));

                    // Add the rectangle and text field to the cell pane
                    cellPane.getChildren().addAll(backgroundRect, textField);
                }

                sudokuGrid.add(cellPane, j, i); // Add cell pane to the specified position in the Sudoku grid
            }
        }

        return sudokuGrid; // Return the created Sudoku grid
    }

    // Method to handle text field input
    private void handleTextFieldInput(javafx.scene.input.KeyEvent event, int row, int col) {
        TextField textField = (TextField) event.getSource(); // Get the source of the event
        String input = textField.getText(); // Get the input from the text field

        // Check if the input is a digit
        if (input.matches("[1-9]")) {
            int value = Integer.parseInt(input); // Convert input to integer
            SudokuLogic.setSudokuValue(row, col, value); // Update the sudoku array with the new value
            updateSudokuGrid(sudokuGrid); // Update the Sudoku grid to reflect the changes
        } else {
            // If the input is not a digit, clear the text field
            textField.clear();
        }
    }

    // Method to update the Sudoku grid with the current values in the sudoku array
    private void updateSudokuGrid(GridPane sudokuGrid) {
        // Traverse the sudoku array and update the text fields or labels in the grid pane
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                StackPane cellPane = (StackPane) sudokuGrid.getChildren().get(i * 9 + j); // Get the StackPane at position (i, j)
                Node node = cellPane.getChildren().get(1); // Get the second child node from the StackPane
                if (node instanceof TextField) { // Check if the node is a TextField
                    TextField textField = (TextField) node; // Cast the node to TextField
                    int value = SudokuLogic.getSudokuValue(i, j); // Get the value from the sudoku array
                    if (value == 0) {
                        textField.setText(""); // If the value is 0, clear the text field
                    } else {
                        textField.setText(String.valueOf(value)); // Set the text field with the value
                    }
                } else if (node instanceof Label) { // Check if the node is a Label
                    Label label = (Label) node; // Cast the node to Label
                    int value = SudokuLogic.getSudokuValue(i, j); // Get the value from the sudoku array
                    if (value == 0) {
                        label.setText(""); // If the value is 0, clear the label
                    } else {
                        label.setText(String.valueOf(value)); // Set the label with the value
                    }
                }
            }
        }
    }

    // Method to start the timer
    private void startTimer(Label timerLabel) {
        startTime = System.currentTimeMillis(); // Get the current time
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long currentTime = System.currentTimeMillis(); // Get the current time
                long elapsedTime = (currentTime - startTime) / 1000; // Calculate elapsed time in seconds
                timerLabel.setText("Timer: " + elapsedTime + "s"); // Update the timer label text
            }
        };
        timer.start(); // Start the timer
    }

    // Method to reset the game

    private void resetGame() {
        startTime = System.currentTimeMillis(); // Reset start time
        SudokuLogic.resetSudoku(); // Reset sudoku
        clearTextFields(sudokuGrid); // Clear text fields
        updateSudokuGrid(sudokuGrid); // Update Sudoku grid to reflect changes
    }



    // Method to submit the game
    private void submitGame() {
        Alert alert;
        if (SudokuLogic.checkSudoku()) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Submit Result");
            alert.setHeaderText(null);
            alert.setContentText("Sudoku is correct!");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Submit Result");
            alert.setHeaderText(null);
            alert.setContentText("Sudoku is incorrect. Please try again.");
        }

        // Display the alert dialog and wait for user response
        alert.showAndWait()
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // User clicked the OK button
                        System.out.println("User clicked the OK button");
                    }
                });
    }



    // Method to show help
    private void showHelp(GridPane sudokuGrid) {
        boolean foundEmptyCell = false;
        int emptyRow = -1;
        int emptyCol = -1;

        // Iterate through the sudoku array to find the next empty cell
        for (int i = 0; i < 9 && !foundEmptyCell; i++) {
            for (int j = 0; j < 9 && !foundEmptyCell; j++) {
                if (sudoku[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                    foundEmptyCell = true;
                }
            }
        }

        // If an empty cell is found, fill it with the answer value
        if (foundEmptyCell) {
            try {
                // Read the answer from the file
                int answerValue = SudokuLogic.readAnswerValue(emptyRow, emptyCol);
                if (answerValue != 0) {
                    sudoku[emptyRow][emptyCol] = answerValue; // Fill the empty cell with the answer value

                    // Update the UI to reflect the change
                    for (Node node : sudokuGrid.getChildren()) {
                        if (GridPane.getRowIndex(node) == emptyRow && GridPane.getColumnIndex(node) == emptyCol) {
                            if (node instanceof StackPane) {
                                StackPane cellPane = (StackPane) node;
                                for (Node innerNode : cellPane.getChildren()) {
                                    if (innerNode instanceof TextField) {
                                        TextField textField = (TextField) innerNode;
                                        textField.setText(Integer.toString(answerValue));
                                        textField.setEditable(false); // Set the text field to not editable
                                        textField.setStyle("-fx-text-fill: darkgreen; -fx-background-color: transparent; -fx-font-weight: bold; -fx-background-insets: 0; -fx-padding: 0;"); // Set text color to green
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("No answer found for the empty cell.");
                }
            } catch (IOException e) {
                System.out.println("Error reading answer file: " + e.getMessage());
            }
        } else {
            // If no empty cell is found, show a message indicating that there are no empty cells
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Help Information");
            alert.setHeaderText(null);
            alert.setContentText("Sudoku is completed. There are no blank cells to fill.");

            // Show the alert dialog
            alert.showAndWait()
                    .ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // User clicked the OK button
                            System.out.println("User clicked the OK button");
                        }
                    });
        }
    }



    // Method to toggle music
    private void toggleMusic(Button musicToggleButton) {
        // Implement music toggle logic here
        musicPlayer.togglePlayPause();
        musicOn = !musicOn; // Toggle music state
        if (musicOn) {
            musicToggleButton.setText("Music: ON");
            System.out.println("Music: ON");
        } else {
            musicToggleButton.setText("Music: OFF");
            System.out.println("Music: OFF");
        }
    }

    // Method to start updating the level label
    private void startLevelUpdate(Label levelLabel) {
        // Set initial level
        levelLabel.setText("Level: " + level); // Set the initial level text

        // You can add logic here to update the level label based on game progress, if needed
    }

    public static void main(String[] args) {
        launch(args);
    }
    // Method to clear all text fields in the Sudoku grid
    private void clearTextFields(GridPane sudokuGrid) {
        for (Node node : sudokuGrid.getChildren()) {
            if (node instanceof StackPane) {
                StackPane cellPane = (StackPane) node;
                for (Node innerNode : cellPane.getChildren()) {
                    if (innerNode instanceof TextField) {
                        TextField textField = (TextField) innerNode;
                        textField.clear(); // Clear the text field content
                    }
                }
            }
        }
    }


}

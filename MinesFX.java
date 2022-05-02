package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

	public class MinesFX extends Application {
		private Mines mineBoard;
		private Button buttons[][];
		private int height, width, numOfMines;
		private HBox hbox;
		private Controller controller;
		private Stage stage;
		private GridPane grid;
		private Button resetBTN;
		private ProgressIndicator progress;
		private boolean isEmpty = false, gameOver = false;

		@Override
		public void start(Stage stage) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("MinesPlayGround.fxml"));
				hbox = loader.load();
				controller = loader.getController();
				this.stage = stage;
				progress = controller.prog();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			resetBTN = controller.GetResetBTN();
			Scene scene = new Scene(hbox);
			stage.setTitle("Minesweeper Game");
			stage.setScene(scene);
			stage.show();
			isEmpty = true;
			resetBTN.setOnAction(new Reset(hbox));
		}

		public static void main(String[] args) {
			launch(args);
		}

		/* creates a grid of buttons for the game */
		public GridPane GetGrid(int height, int width) {
			GridPane gridp = new GridPane();
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					buttons[i][j] = new Button();
					buttons[i][j].setPrefSize(50, 50);
					buttons[i][j].setMaxSize(50, 50);
					buttons[i][j].setMinSize(50, 50);
					buttons[i][j].setOnMouseClicked(new MouseClick(i, j));
					gridp.add(buttons[i][j], i, j);
				}
			}
			gridp.setPadding(new Insets(90));
			return gridp;
		}

		public double GetProgress() {
			double game = 0, total = height * width - numOfMines;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (mineBoard.Board[i][j].open && !mineBoard.Board[i][j].mine) {
						game++;
					}
				}
			}
			return (game / total);
		}

		//reset the game 
		class Reset implements EventHandler<ActionEvent> {
			private HBox root;
			public Reset(HBox root) {
				this.root = root;
			}
			@Override
			public void handle(ActionEvent event) { 
				if (!isEmpty)
					root.getChildren().remove(grid);
				isEmpty = false;
				gameOver = false;
				height = controller.AppHeight();
				width = controller.AppWidth();
				numOfMines = controller.InputMines();
				if (height == 0 || width == 0) {
					return;
				}
				mineBoard = new Mines(height, width, numOfMines); 
				buttons = new Button[height][width]; 
				grid = GetGrid(height, width);
				root.getChildren().add(grid);
				progress.setProgress(0); 
				stage.setHeight(width * 60 + 300); 
				stage.setWidth(height * 65 + 300);
				for (int i = 0; i < height; i++) { 
					for (int j = 0; j < width; j++) {
						buttons[i][j].setText(mineBoard.get(i, j));
					}
				}
			}
		}

		//creates the mouse role in the game
		class MouseClick implements EventHandler<MouseEvent> { 
			private int i, j;
			public MouseClick(int i, int j) {
				this.i = i;
				this.j = j;
			}
			@Override
			public void handle(MouseEvent event) { 
				MouseButton clickType = event.getButton(); 
				if (gameOver) { return; }
				if (clickType == MouseButton.PRIMARY && mineBoard.get(i, j) != "F") { 
					if (!mineBoard.isDone()) { 
						if (!mineBoard.open(i, j)) {
							mineBoard.setShowAll(true);
							gameOver = true;
						}
					}
				}
				for (int x = 0; x < controller.AppHeight(); x++) {
					for (int y = 0; y < controller.AppWidth(); y++) {
						buttons[x][y].setText(mineBoard.get(x, y));
					}
				}
				if (clickType == MouseButton.SECONDARY) { 
					mineBoard.toggleFlag(i, j);
					buttons[i][j].setText(mineBoard.get(i, j));
				}
				progress.setProgress(GetProgress());
			}
		}
}
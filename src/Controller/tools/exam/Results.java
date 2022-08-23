package Controller.tools.exam;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Results {
	public static VBox getSummarizes(int score,QA[] qaAr) {
		 
		VBox sumarizeVbox = new VBox();
		Label finalScore = new Label("FinalScore "+Integer.toString(score));
		finalScore.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
		sumarizeVbox.getChildren().add(finalScore);
		sumarizeVbox.setSpacing(20);
		for(int i=0;i<qaAr.length;i++) {

			Label lbel = new Label("Question"+Integer.toString(i+1)+":"+(qaAr[i].isCorrect()?"Right":"Wrong"));
			lbel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			sumarizeVbox.getChildren().add(lbel );
			
		}
		sumarizeVbox.setAlignment(Pos.CENTER_LEFT);
		return sumarizeVbox;
//
//		ExamController.getStage().getScene().getRoot();
//		((BorderPane)  ExamController.getStage().getScene().getRoot().lookup("#container")).setCenter(sumarizeVbox);
	}
}

package Controller.tools.exam;

import Controller.tools.ExamController;
import Controller.user.LoginController;
import Model.user.User;
import home.AdminClass;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.List;


public class QAPane {
   private RadioButton[] rbAr;
   private VBox qaPane;
   public static int finalScore;

   public QAPane(QA qa){
	   qaPane = new VBox();
	   
	   Button submit= new Button("OK");
	   submit.setPrefWidth(200);
	   Label question = new Label(qa.getQuestion());
	   question.setPrefWidth(800);
	   question.setWrapText(true);
	   question.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	   

	   String judge = "Wrong";
	   Text judgement = new Text(judge);
	   judgement.setFill(Color.RED);
	   judgement.setVisible(false);
	   judgement.setFont(Font.font("Verdana", 20));

	   Text finalScoreTxt = new Text();
	   finalScoreTxt.setFill(Color.GREEN);
	   finalScoreTxt.setFont(Font.font("Verdana", 40));
	   
	    VBox answersBox= new VBox();
	   answersBox= getAnswerPane(qa.getAnswers());
	   
	   qaPane.setPadding(new Insets(0,0,0,70));
	   qaPane.setMargin(question, new Insets(0,0,20,0));
	   qaPane.setMargin(submit, new Insets(20,0,20,500));
	   qaPane.setSpacing(10);
	   
	   rbAr= new RadioButton[answersBox.getChildren().size()];
	   for(int i=0;i<answersBox.getChildren().size();i++) {
		   rbAr[i]= (RadioButton) answersBox.getChildren().get(i);
	   }
	   
	   submit.setOnAction(actionEvent->{
		   if(getRadioButtonSelected(rbAr)>=0) {
		   for(RadioButton rb:rbAr) {
			   rb.setDisable(true);
		   }
		   submit.setDisable(true);
		   
		  if(qa.getCorrectAnswerNumber()==getRadioButtonSelected(rbAr)) {
			  qa.setResult(true);
			  finalScore++;
			  judgement.setText("Right");
			  judgement.setFill(Color.GREEN);
		  }
		  judgement.setVisible(true);
		  if((FileUtils.getQAArray().length-1)== ExamController.currentQuestion) {
			VBox  sumarizeVbox = Results.getSummarizes(finalScore, FileUtils.getQAArray());
			ExamController.setResult(sumarizeVbox);
			  finalScoreTxt.setText("Final Score:"+finalScore);
			  qaPane.getChildren().add(finalScoreTxt);
			  AdminClass adminClass = new AdminClass();
			  try {
				  if(finalScore>adminClass.getScoreByName(LoginController.loginUsername)){
				  adminClass.updateUserScore(LoginController.loginUsername,finalScore);
				  }
			  } catch (SQLException throwables) {
				  throwables.printStackTrace();
			  }
			  System.out.println("output rank");
			  try {
				  List<User> allUser = adminClass.getAllUser();
				  String[] names = new String[allUser.size()];
				  int[] scores = new int[allUser.size()];
				  for (int i = 0; i < allUser.size(); i++) {
					  names[i] = allUser.get(i).getName();
					  scores[i] = allUser.get(i).getBestScore();
				  }
				 for(int i=0;i<names.length-1;i++){//外层循环控制排序趟数
				 for(int j=0;j<names.length-1-i;j++){//内层循环控制每一趟排序多少次
				 if(scores[j]>scores[j+1]){
				 int temp=scores[j];
					 scores[j]=scores[j+1];
					 scores[j+1]=temp;
					 String tempStr=names[j];
					 names[j]=names[j+1];
					 names[j+1]=tempStr;
				 }
				 }
				 }
				 String rank = "Rank Board\n";


				  for(int i=names.length-1;i>=0;i--){
					  rank+=names[i]+"--"+scores[i]+"\n";
					  System.out.println(names[i]+"--"+scores[i]);

				  }
				  Text tf = new Text (rank);
				  qaPane.getChildren().addAll(tf);
			  } catch (SQLException throwables) {
				  throwables.printStackTrace();
			  }


		  }


	   }});
	   
	   qaPane.getChildren().add(question);
	   qaPane.getChildren().add(answersBox);
	   qaPane.getChildren().add(submit);
	   qaPane.getChildren().add(judgement);
	   
   }
   VBox getAnswerPane(String[] answers) {
	   VBox answerPane = new VBox();
	   final ToggleGroup group = new ToggleGroup();
	    for(int i=0;i<answers.length;i++) {
	    	 RadioButton rb1 = new RadioButton(answers[i]);
	    	    rb1.setToggleGroup(group);
	    	    rb1.setUserData(i);
	    	    rb1.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	    	    answerPane.getChildren().add(rb1);
	    	    answerPane.setSpacing(10);
	    }
	   return answerPane;
   }
	int getRadioButtonSelected(RadioButton[] rbAr) {
		      int myAnswer =0;
		for(int i=0;i<rbAr.length;i++) {
			if(rbAr[i].isSelected()) {
				myAnswer=i;
				break;
			}
			else {
				myAnswer=-1;
			}
		}
		return myAnswer;
	}
   private void setQAPane(VBox vb) {this.qaPane = vb;}
   public VBox getQAPane() {return qaPane;}
}

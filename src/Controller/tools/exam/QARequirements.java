package Controller.tools.exam;


public abstract class QARequirements{

	public abstract String getQuestion();
	public abstract void setQuestion(String question);
	
	public abstract String[] getAnswers();
	public abstract void setAnswers(String[] answers);
	
	public abstract String getExplanation();
	public abstract void setExplanation(String question);
	
	public abstract String getCategory();
	public abstract void setCategory(String category);
	
	public abstract int getDifficulty();
	public abstract void setDifficulty(int difficulty);
	
	public abstract int getPoints();
	public abstract void setPoints(int points);
	
	public abstract int getCorrectAnswerNumber();
	public abstract void setCorrectAnswerNumber(int correctAnswer);
	
	public abstract boolean isCorrect();
	public abstract void setResult(boolean b);

}

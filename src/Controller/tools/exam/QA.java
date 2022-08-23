package Controller.tools.exam;

public class QA extends  QARequirements{
	   private String question;
	   private String category;
	   private String explanation;
	   private String[] answers;
	   private int difference;
	   private int points;
	   private int correctAns;
	   private boolean iscorrect;
	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public void setQuestion(String question) {
		this.question=question;
	}

	@Override
	public String[] getAnswers() {
		return answers;
	}

	@Override
	public void setAnswers(String[] answers) {
		this.answers=answers;
	}

	@Override
	public String getExplanation() {
		return explanation;
	}

	@Override
	public void setExplanation(String question) {
		this.explanation=question;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public void setCategory(String category) {
		this.category=category;
	}

	@Override
	public int getDifficulty() {
		return difference;
	}

	@Override
	public void setDifficulty(int difficulty) {
		this.difference=difficulty;
	}

	@Override
	public int getPoints() {
		return points;
	}

	@Override
	public void setPoints(int points) {
		this.points=points;
	}

	@Override
	public int getCorrectAnswerNumber() {
		return correctAns;
	}

	@Override
	public void setCorrectAnswerNumber(int correctAnswer) {
		this.correctAns=correctAnswer;
	}

	@Override
	public boolean isCorrect() {
		return iscorrect;
	}

	@Override
	public void setResult(boolean b) {
		this.iscorrect=b;
	}

}

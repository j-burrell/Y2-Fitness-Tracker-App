package Controller.tools.exam;

import java.io.File;

public class FileUtils {
	
	private static  QA[] qaAr;
		
	public static void setQAArray() {
		qaAr = new  QA[8];
		qaAr[0]= getQA("What are some things you can do to help support your brain health?",
				"Eating nutritious foods",
				"Regular physical activity",
				"Doing mental exercises like crossword puzzles and other games",
				" All of the above",3);
		qaAr[1]= getQA("How can you ensure that your wishes about your health care are known by your close family or friends??",
				"By writing down my wishes regarding end of life care and specific medical procedures.",
				"By choosing a decision maker I can trust.",
				"By making important health care decisions now",
				"A,B and C are correct.",
				3);

		qaAr[2]= getQA("Depression in older adults can be hard to detect. However the earlier it is detected, the easier it can be to treat. Which of the following are symptoms of depression?",
				"Decreased interest and pleasure in usual activities",
				"Reports of pain",
				" Complaints about memory problems",
				" All of the above",3);
		qaAr[3]= getQA("What is the recommended amount of physical activity for adults per week (as per the Canadian Physical Activity Guidelines)?",
				"60 minutes of moderate to vigorous intensity aerobic physical activity per week",
				"150 minutes of moderate to vigorous",
				"120 minutes of moderate to vigorous",
				" 90 minutes of moderate to vigorous",1);
		qaAr[4]= getQA("Eating a healthy diet can help reduce the risk of developing health problems, such as:",
				"Eating nutritious foods",
				"Regular physical activity",
				"Doing mental exercises like crossword puzzles and other games",
				" All of the above",3);
		qaAr[5]= getQA("You can help to prevent falls by:",
				"Some forms of cancer",
				"High blood pressure",
				"Heart & respiratory disease",
				" All of the above",4);
		qaAr[6]= getQA("What are some things you can do to help support your brain health?",
				"Scheduling regular vision checkups and correcting any vision problems",
				"Maintaining flexibility, balance and increasing muscle strength",
				"Reducing trip and slip hazards in your home and outdoors",
				" All of the above",4);
		qaAr[7]= getQA("Did you know that if everyone ate five to ten servings of vegetables and fruit each day, the current cancer rate could be reduced by as much as:",
				"20%",
				"15%",
				"10%",
				"5%",0);



	}

	public static QA getQA(String q, String a1,String a2,String a3,String a4,int num){
		QA qa= new QA();

		qa.setAnswers(new String[]{a1,a2,a3,a4});
		qa.setQuestion(q);
		qa.setCorrectAnswerNumber(num);
		return qa;
	}
	
	public static  QA[] getQAArray() {return qaAr;}

	public static boolean fileExists(File f) {
		return (f != null && f.exists() && f.isFile() && f.canRead() && (f.length() > 2));
	}

	public static boolean fileExists(String s) {
		return (fileExists(new File(s)));
	}
	
	public static String getAbsPath(File f) {
		return f.getAbsolutePath();
	}

}

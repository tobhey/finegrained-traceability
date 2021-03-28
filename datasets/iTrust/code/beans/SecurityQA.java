package edu.ncsu.csc.itrust.beans;

/**
 * A bean for storing data about a security question and answer.
 * 
 * A bean's purpose is to store data. Period. Little or no functionality is to be added to a bean 
 * (with the exception of minor formatting such as concatenating phone numbers together). 
 * A bean must only have Getters and Setters (Eclipse Hint: Use Source > Generate Getters and Setters 
 * to create these easily)
 */
public class SecurityQA {

	String question, answer, confirmAnswer;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getConfirmAnswer() {
		return confirmAnswer;
	}

	public void setConfirmAnswer(String confirmAnswer) {
		this.confirmAnswer = confirmAnswer;
	}

}

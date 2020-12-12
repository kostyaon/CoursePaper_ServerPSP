package Models;

import java.io.Serializable;

public class Answer implements Serializable {
    private int answerID;
    private String answer;
    private boolean correct;

    public Answer(String answer, boolean correct){
        this.answer = answer;
        this.correct = correct;
    }

    public int getAnswerID() {
        return answerID;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}

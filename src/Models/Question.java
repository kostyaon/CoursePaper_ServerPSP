package Models;

import java.io.Serializable;

public class Question implements Serializable {
    private int questID;
    private String theme;
    private int level;
    private String question;

    public Question(String theme, int level, String question){
        this.theme = theme;
        this.level = level;
        this.question = question;
    }
    public Question(String question, int id){
        this.questID = id;
        this.question = question;
    }

    public int getQuestID() {
        return questID;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getLevel() {
        return level;
    }

    public String getQuestion() {
        return question;
    }

    public String getTheme() {
        return theme;
    }
}

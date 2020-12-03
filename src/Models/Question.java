package Models;

public class Question {
    private String theme;
    private int level;
    private String question;

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

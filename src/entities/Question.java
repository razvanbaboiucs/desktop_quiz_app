package entities;

import utils.QuestionType;

public class Question implements HasID<String> {
    String id, description, correctAnswer;
    Integer points;
    String[] answers;
    QuestionType type;

    @Override
    public String toString() {
        return "Question number:" + id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Question(String id, String description, String correctAnswer, Integer points, String[] answers) {
        this.id = id;
        this.description = description;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.answers = answers;
        this.type = QuestionType.Active;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

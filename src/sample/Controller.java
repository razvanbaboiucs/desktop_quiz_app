package sample;

import entities.Answers;
import entities.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import services.Service;
import sun.plugin.perf.PluginRollup;
import utils.MemberType;
import utils.Observer;
import utils.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller implements Observer {
    static Question questionNow;
    Service service;
    MemberType type;
    String name;
    ObservableList<Question> remainingQuestionsModel = FXCollections.observableArrayList();
    ObservableList<Question> quizQuestionsModel = FXCollections.observableArrayList();
    ObservableList<Answers> answersModel = FXCollections.observableArrayList();

    @FXML
    TableView<Question> questionTableView;
    @FXML
    TableView<Question> quizTableView;
    @FXML
    TableView<Answers> answersTableView;
    @FXML
    Label maxPointsLabel;
    @FXML
    Label questionLabel;
    @FXML
    RadioButton answerAButton;
    @FXML
    RadioButton answerBButton;
    @FXML
    RadioButton answerCButton;
    @FXML
    Button sendAnswerButton;
    @FXML
    Button sendResults;
    @FXML
    Label nameLabel;
    @FXML
    Label pointsLabel;
    @FXML
    ComboBox<Question> questionComboBox;
    @FXML
    TableColumn<Question, String> idColumn1, idColumn2, descriptionColumn1, descriptionColumn2, correctAnswerColumn1, correctAnswerColumn2;
    @FXML
    TableColumn<Question, Integer> pointsColumn1, pointsColumn2;
    @FXML
    TableColumn<Answers, String> nameColumn, questionIdColumn;
    @FXML
    TableColumn<Answers, Integer> numberOfPointsColumn;
    @FXML
    Button sendQuestionButton;
    @FXML
    Label resultTextLabel;
    @FXML
    Label maxPointsTextLabel;


    public void setServices(Service service, MemberType type, String name) {
        this.service = service;
        this.type = type;
        this.name = name;
        this.service.addObserver(this);
        init();
    }

    private void init() {
        idColumn1.setCellValueFactory(new PropertyValueFactory<Question, String>("id"));
        idColumn2.setCellValueFactory(new PropertyValueFactory<Question, String>("id"));
        descriptionColumn1.setCellValueFactory(new PropertyValueFactory<Question, String>("description"));
        descriptionColumn2.setCellValueFactory(new PropertyValueFactory<Question, String>("description"));
        correctAnswerColumn1.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));
        correctAnswerColumn2.setCellValueFactory(new PropertyValueFactory<Question, String>("correctAnswer"));
        pointsColumn1.setCellValueFactory(new PropertyValueFactory<Question, Integer>("points"));
        pointsColumn2.setCellValueFactory(new PropertyValueFactory<Question, Integer>("points"));
        questionIdColumn.setCellValueFactory(new PropertyValueFactory<Answers, String>("questionId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Answers, String>("name"));
        numberOfPointsColumn.setCellValueFactory(new PropertyValueFactory<Answers, Integer>("points"));
        List<Question> questions = new ArrayList<>(service.getAllQuestions());
        questionTableView.setItems(remainingQuestionsModel);
        questionComboBox.setItems(remainingQuestionsModel);
        remainingQuestionsModel.setAll(questions);
        answersTableView.setItems(answersModel);
        quizTableView.setItems(quizQuestionsModel);
        questionLabel.setVisible(false);
        answerAButton.setVisible(false);
        answerBButton.setVisible(false);
        answerCButton.setVisible(false);
        pointsLabel.setVisible(false);
        nameLabel.setText(name);
        if(type == MemberType.Student) {
            answersTableView.setVisible(false);
            questionTableView.setVisible(false);
            quizTableView.setVisible(false);
            maxPointsLabel.setVisible(false);
            sendResults.setVisible(false);
            sendQuestionButton.setVisible(false);
            questionComboBox.setVisible(false);
            maxPointsTextLabel.setVisible(false);
        }
        else {
            sendAnswerButton.setVisible(false);
            maxPointsLabel.setText("0");
            resultTextLabel.setVisible(false);
        }
    }

    private static void showErrorMessage(String err){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }

    public void handleAddQuestion() {
        Question questionToAdd = questionComboBox.valueProperty().getValue();
        try {
            if(questionToAdd == null) {
                throw new ValueException("");
            }
            this.questionNow = questionToAdd;
            service.addQuestion(questionToAdd);
        } catch(ValueException e) {
            showErrorMessage("Please choose a question!");
        }

    }

    public void handleSendResults() {
        service.sendResults(maxPointsLabel.getText());
    }

    public void handleSelectAnswer() {
        try {
           if((answerAButton.isSelected() && answerBButton.isSelected()) || (answerAButton.isSelected() && answerCButton.isSelected()) || (answerCButton.isSelected() && answerBButton.isSelected()))
               throw new ValueException("");
            String answer;
            if(answerAButton.isSelected()) {
                answer = answerAButton.getText();
            } else if(answerBButton.isSelected()) {
                answer = answerBButton.getText();
            } else if(answerCButton.isSelected()){
                answer = answerCButton.getText();
            } else {
                throw new ValueException("");
            }
            Integer points = 0;
            if(answer.equals(questionNow.getCorrectAnswer())) {
                points = questionNow.getPoints();
            }
            Answers answers = new Answers(name, points, questionNow.getId());
            service.addAnswer(answers);
            questionLabel.setVisible(false);
            answerAButton.setVisible(false);
            answerCButton.setVisible(false);
            answerBButton.setVisible(false);
        } catch(ValueException e) {
            showErrorMessage("Choose one answer only!");
        }
    }

    @Override
    public void updateTablesAndQuestions() {
        if(type == MemberType.Student) {
            questionLabel.setText(questionNow.getId() + ". " + questionNow.getDescription());
            questionLabel.setVisible(true);
            answerAButton.setText(questionNow.getAnswers()[0]);
            answerAButton.setVisible(true);
            answerBButton.setText(questionNow.getAnswers()[1]);
            answerBButton.setVisible(true);
            answerCButton.setText(questionNow.getAnswers()[2]);
            answerCButton.setVisible(true);
        }
        List<Question> remainingQuestions = service.getAllQuestions().stream().filter((x) -> x.getType().equals(QuestionType.Active)).collect(Collectors.toList());
        remainingQuestionsModel.setAll(remainingQuestions);
        List<Question> quizQuestions = service.getAllQuestions().stream().filter((x) -> x.getType().equals(QuestionType.Inactive)).collect(Collectors.toList());
        quizQuestionsModel.setAll(quizQuestions);
        List<Answers> answers = (List<Answers>) service.getAllAnswers();
        answersModel.setAll(answers);
        Integer maxPoints = 0;
        for (Question q:
             service.getAllQuestions()) {
            if(q.getType().equals(QuestionType.Inactive)) {
                maxPoints += q.getPoints();
            }
        }
        maxPointsLabel.setText(String.valueOf(maxPoints));
    }

    @Override
    public void updateResult() {
        if(type == MemberType.Student) {
            pointsLabel.setText(service.getPointsForStudent(name));
            pointsLabel.setVisible(true);
        }
        if(type == MemberType.Prof) {
            questionComboBox.setVisible(false);
            sendQuestionButton.setVisible(false);
            sendResults.setVisible(false);
        }
    }
}

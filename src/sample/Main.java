package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repositories.AnswersRepository;
import repositories.QuestionRepository;
import repositories.Repository;
import services.Service;
import utils.MemberType;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        QuestionRepository questionRepo = new QuestionRepository("data/quizzes.txt");
        AnswersRepository answersRepository = new AnswersRepository("data/testResults.txt");
        Service questionService = new Service(questionRepo, answersRepository);

//        if(questionRepository instanceof Repository) {
//            System.out.println("True");
//        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setServices(questionService, MemberType.Prof, "Prof");

        Stage stage = new Stage();
        stage.setTitle("Prof window");
        stage.setScene(new Scene(root, 1200, 750));
        stage.show();

        List<String> args = getParameters().getRaw();
        for (String name:
             args) {
            FXMLLoader loaderStudent = new FXMLLoader();
            loaderStudent.setLocation(getClass().getResource("sample.fxml"));
            Parent rootStudent = loaderStudent.load();

            Controller controllerStudent = loaderStudent.getController();
            controllerStudent.setServices(questionService, MemberType.Student, name);

            Stage stageStudent = new Stage();
            stageStudent.setTitle(name + "'s window");
            stageStudent.setScene(new Scene(rootStudent, 1200, 750));
            stageStudent.show();
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}

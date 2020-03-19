package repositories;

import entities.Question;
import utils.QuestionType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestionRepository extends FileRepository<String, Question> {
    List<Question> questionList = new ArrayList<>();

    public QuestionRepository(String fileName) {
        super(fileName);
        getFromFile();
    }

    @Override
    protected void parseLine(String line) {
        String[] args = line.split(":");
        String id = args[0];
        String description = args[1];
        String[] answers = args[2].split(",");
        String correctAnswer = args[3];
        Integer points = Integer.valueOf(args[4]);
        Question question = new Question(id, description, correctAnswer, points, answers);
        questionList.add(question);
    }

    @Override
    protected void writeLine(Question entity, Path path) {

    }

    @Override
    public Question findOne(String id) {
        for (Question q:
             getAll()) {
            if(q.getId().equals(id)) {
                return q;
            }
        }
        return null;
    }

    @Override
    public Collection<Question> getAll() {
        return questionList;
    }

    public void addQuestion(Question questionToAdd) {
        questionToAdd.setType(QuestionType.Inactive);
    }
}

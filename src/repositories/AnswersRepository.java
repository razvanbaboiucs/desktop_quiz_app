package repositories;

import entities.Answers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnswersRepository extends FileRepository<String, Answers> {
    List<Answers> answersList = new ArrayList<>();

    public AnswersRepository(String fileName) {
        super(fileName);
    }

    public void addAnswer(Answers answers) {
        answersList.add(answers);
    }

    @Override
    protected void parseLine(String line) {

    }

    @Override
    protected void writeLine(Answers entity, Path path) {
        String answerString = entity.getQuestionId() + ":" + entity.getName() + ":" + entity.getPoints() + "\n";
        byte[] strToBytes = answerString.getBytes();
        try {
            Files.write(path, strToBytes, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Answers findOne(String id) {
        return null;
    }

    @Override
    public Collection<Answers> getAll() {
        return answersList;
    }

    public void sendResults(String points) {
        Path path = Paths.get(fileName);
        try {
            Files.write(path, "".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        points += "\n";
        byte[] strToBytes = points.getBytes();
        try {
            Files.write(path, strToBytes, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        writeToFile();

    }
}

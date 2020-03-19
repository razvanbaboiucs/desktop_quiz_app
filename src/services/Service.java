package services;

import entities.Answers;
import entities.Question;
import repositories.AnswersRepository;
import repositories.QuestionRepository;
import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Service implements Observable {
    List<Observer> observers = new ArrayList<>();
    QuestionRepository questionRepository;
    AnswersRepository answersRepository;

    public Service(QuestionRepository questionRepository, AnswersRepository answersRepository) {
        this.questionRepository = questionRepository;
        this.answersRepository = answersRepository;
    }

    public Collection<Question> getAllQuestions() {
        return questionRepository.getAll();
    }

    public Collection<Answers> getAllAnswers() {
        return answersRepository.getAll();
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::updateTablesAndQuestions);
    }

    public void addQuestion(Question questionToAdd) {
        questionRepository.addQuestion(questionToAdd);
        notifyObservers();
    }

    public void addAnswer(Answers answers) {
        answersRepository.addAnswer(answers);
        notifyObservers();
    }

    public void sendResults(String points) {
        answersRepository.sendResults(points);
        notifyObserversResults();
    }

    private void notifyObserversResults() {
        observers.forEach(Observer::updateResult);
    }

    public String getPointsForStudent(String name) {
        AtomicReference<Integer> points = new AtomicReference<>(0);
        answersRepository.getAll().stream().filter((x) -> x.getName().equals(name)).forEach((x) -> points.updateAndGet(v -> v + x.getPoints()));
        return points.toString();
    }
}

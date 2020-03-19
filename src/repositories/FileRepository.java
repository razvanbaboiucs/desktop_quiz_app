package repositories;

import entities.HasID;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class FileRepository<T, E extends HasID<T>> implements Repository<T, E> {
    String fileName;

    FileRepository(String fileName) {
        this.fileName = fileName;
    }

    protected void getFromFile() {
        try{
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void parseLine(String line);

    protected void writeToFile(){
        Path path = Paths.get(fileName);
        getAll().forEach(x->writeLine(x, path));
    }

    protected abstract void writeLine(E entity, Path path);
    public abstract E findOne(T id);
}

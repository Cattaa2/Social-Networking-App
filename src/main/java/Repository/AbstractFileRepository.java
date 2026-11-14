package Repository;

import domain.Entity;
import domain.Validator;
import exceptions.RepoException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    protected String filename;

    public AbstractFileRepository(String filename, Validator<E> validator) {
        super(validator);
        this.filename = filename;
        loadData();
    }

    protected abstract E extractEntity(String line);

    protected abstract String createEntityAsString(E entity);

    protected void loadData(){
        Path path = Paths.get(filename);
        if(!Files.exists(path)){
            return; //daca fisierul nu exista aplicatia porneste cu repo gol
        }
        try(Stream<String> lines = Files.lines(path)){
            lines.filter(line -> !line.trim().isEmpty()).forEach(line -> {
               try{
                   E entity = extractEntity(line);
                   super.save(entity);
               }catch (Exception e){
                   System.err.println("Linie corupta ignorata in " + filename + ": " + e.getMessage());
               }
            });
        }catch (IOException e){
            throw new RepoException("Eroare la citirea fisierului: "+ filename, e);
        }
    }

    protected void writeToFile(){
        Path path = Paths.get(filename);
        if (path.getParent() != null && !Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RepoException("Eroare la crearea folderului", e);
            }
        }

        try {
            List<String> linii = StreamSupport.stream(findAll().spliterator(), false)
                    .map(this::createEntityAsString)
                    .collect(Collectors.toList());
            Files.write(path, linii);
        } catch (IOException e) {
            throw new RepoException("Eroare la scrierea in fisierul " + filename, e);
        }
    }

    @Override
    public E save(E entity) {
        E result = super.save(entity);
        if(result == null){
            writeToFile();
        }
        return result;
    }

    @Override
    public Optional<E> delete(ID id){
        Optional<E> result = super.delete(id);
        if(result.isPresent()){
            writeToFile();
        }
        return result;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> result = super.update(entity);
        if(result.isEmpty()){
            writeToFile();
        }
        return result;
    }
}

package Repository;

import domain.Prietenie;
import domain.Tuple;
import domain.Validator;
import exceptions.RepoException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class PrietenieFileRepository extends AbstractFileRepository<Tuple<Integer,Integer>,Prietenie> {

    public PrietenieFileRepository(String filename, Validator<Prietenie> validator) {
        super(filename,validator);
    }

    @Override
    protected Prietenie extractEntity(String line) {
        String[] parti = line.split(";",-1);
        if(parti.length != 3){
            throw new RepoException("Numar invalid de campuri (trebuie 3: id1,id2,data)");
        }
        try{
            int id1 = Integer.parseInt(parti[0].trim());
            int id2 = Integer.parseInt(parti[1].trim());
            LocalDateTime data = LocalDateTime.parse(parti[2].trim());
            return new Prietenie(id1,id2,data);
        }catch(NumberFormatException e){
            throw new RepoException("ID-ul utilizatorilor trebuie sa fie un numar intreg valid",e);
        }catch(DateTimeParseException e){
            throw new RepoException("Formatul datei e invalid (trebuie format ISO)",e);
        }
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getIdUser1()+";"+entity.getIdUser2()+";"+entity.getData().toString();
    }
}

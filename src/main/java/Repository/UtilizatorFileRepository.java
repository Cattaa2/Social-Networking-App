package Repository;

import domain.Utilizator;
import domain.Validator;
import exceptions.RepoException;

public class UtilizatorFileRepository extends AbstractFileRepository<Integer, Utilizator> {

    public UtilizatorFileRepository(String fileName, Validator<Utilizator> validator) {
        super(fileName, validator);
    }

    @Override
    protected Utilizator extractEntity(String line) {
        String[] parti = line.split(";",-1);
        if(parti.length != 3){
            throw new RepoException("Numar invalid de campuri (trebuie 3: id;prenume;nume)");
        }
        try{
            int id = Integer.parseInt(parti[0].trim());
            String prenume = parti[1].trim();
            String nume = parti[2].trim();
            return new Utilizator(id, prenume, nume);
        }catch(NumberFormatException e){
            throw new RepoException("ID-ul utilizatorului trebuie sa fie un numar intreg valid",e);
        }
    }

    @Override
    protected String createEntityAsString(Utilizator entity) {
        return entity.getId()+";"+entity.getPrenume()+";"+entity.getNume();
    }
}
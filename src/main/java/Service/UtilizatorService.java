package Service;

import Repository.Repository;
import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.ServiceException;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorService {
    private Repository<Integer, Utilizator> repoUtilizatori;
    private Repository<Tuple<Integer,Integer>, Prietenie> repoPrietenii;

    public UtilizatorService(Repository<Integer, Utilizator> repoUtilizatori,  Repository<Tuple<Integer,Integer>, Prietenie> repoPrietenii) {
        this.repoUtilizatori = repoUtilizatori;
        this.repoPrietenii = repoPrietenii;
    }

    public void addUtilizator(String nume, String prenume){
        int maxID = StreamSupport.stream(repoUtilizatori.findAll().spliterator(),false).mapToInt(Utilizator::getId).max().orElse(0);
        int currentId = maxID + 1;
        Utilizator u = new Utilizator(currentId,nume,prenume);
        if(repoUtilizatori.save(u) != null){
            throw new ServiceException("Utilizatorul exista deja!");
        }
    }

    public void removeUtilizator(int id){
        Utilizator toDelete = repoUtilizatori.findOne(id).orElseThrow(() -> new ServiceException("Utilizatorul nu exista"));
        toDelete.getPrieteni().forEach(prieten -> prieten.removePrieten(toDelete));
        StreamSupport.stream(repoPrietenii.findAll().spliterator(), false)
                .filter(p -> p.getIdUser1() == id || p.getIdUser2() == id )
                .map(Prietenie::getId)
                .collect(Collectors.toList())
                .forEach(repoPrietenii::delete);
        repoUtilizatori.delete(id);
    }

    public Iterable<Utilizator> getAllUtilizatori(){
        return repoUtilizatori.findAll();
    }
}

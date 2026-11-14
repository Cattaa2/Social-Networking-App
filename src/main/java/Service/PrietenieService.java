package Service;

import Repository.Repository;
import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.ServiceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService {
    private Repository<Integer, Utilizator> repoUtilizatori;
    private Repository<Tuple<Integer,Integer>, Prietenie> repoPrietenii;

    public PrietenieService(Repository<Tuple<Integer,Integer>, Prietenie> repoPrietenii, Repository<Integer, Utilizator> repoUtilizatori) {
        this.repoUtilizatori = repoUtilizatori;
        this.repoPrietenii = repoPrietenii;
        sincronizeazaPrietenii();
    }

    //adauga prietenii fiecarui utilizator in lista lui de prieteni
    private void sincronizeazaPrietenii() {
        List<Tuple<Integer,Integer>> prieteniiInvalide = StreamSupport.stream(repoPrietenii.findAll().spliterator(),false)
                .filter(p -> {
                    Optional<Utilizator> optU1 = repoUtilizatori.findOne(p.getIdUser1());
                    Optional<Utilizator> optU2 = repoUtilizatori.findOne(p.getIdUser2());

                    if(optU1.isPresent() && optU2.isPresent()){
                        optU1.get().addPrieten(optU2.get());
                        optU2.get().addPrieten(optU1.get());
                        return false;
                    } else{
                        System.err.println("Prietenie ingorata (lipeste un utilizator): " + p.getId());
                        return true;
                    }
                }).map(Prietenie::getId).collect(Collectors.toList());
        prieteniiInvalide.forEach(repoPrietenii::delete);
    }

    public void addPrietenie(int idUser1, int idUser2){
        Utilizator u1 = repoUtilizatori.findOne(idUser1).orElseThrow(() -> new ServiceException("Utilizatorul 1 nu exista!"));
        Utilizator u2 = repoUtilizatori.findOne(idUser2).orElseThrow(() -> new ServiceException("Utilizatorul 2 nu exista!"));

        Prietenie p = new Prietenie(idUser1,idUser2, LocalDateTime.now());
        if(repoPrietenii.save(p) != null){
            throw new ServiceException("Prietenia exista deja intre acesti utilizatori!");
        }
        u1.addPrieten(u2);
        u2.addPrieten(u1);
    }

    public void removePrietenie(int idUser1, int idUser2){
        Tuple<Integer,Integer> id = new Tuple<>(Math.min(idUser1,idUser2),Math.max(idUser1,idUser2));
        repoPrietenii.delete(id).orElseThrow(() -> new ServiceException("Prietenia nu exista!"));
        repoUtilizatori.findOne(idUser1).ifPresent(u1 -> repoUtilizatori.findOne(idUser2).ifPresent(u1::removePrieten));
        repoUtilizatori.findOne(idUser2).ifPresent(u2 -> repoUtilizatori.findOne(idUser1).ifPresent(u2::removePrieten));
    }

    public Iterable<Prietenie> getAllPrietenii() {
        return repoPrietenii.findAll();
    }
}

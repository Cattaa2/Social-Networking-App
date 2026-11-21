package Service;

import Repository.Repository;
import Repository.PagingRepository;
import domain.Prietenie;
import domain.Tuple;
import domain.Utilizator;
import exceptions.ServiceException;
import util.paging.Page;
import util.paging.Pageable;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorService {
    private Repository<Integer, Utilizator> repoUtilizatori;
    private Repository<Tuple<Integer,Integer>, Prietenie> repoPrietenii;

    public UtilizatorService(Repository<Integer, Utilizator> repoUtilizatori,  Repository<Tuple<Integer,Integer>, Prietenie> repoPrietenii) {
        this.repoUtilizatori = repoUtilizatori;
        this.repoPrietenii = repoPrietenii;
    }

    public Utilizator addUtilizator(String nume, String prenume){
        Utilizator u = new Utilizator(prenume,nume);
        if(repoUtilizatori.save(u) != null){
            throw new ServiceException("Utilizatorul exista deja!");
        }
        return u;
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

    /**
     * Retrieves a page of users. Works only with database repo
     *
     * @param pageable the pagination parameters
     * @return a Page of users
     * @throws UnsupportedOperationException if the repository is not database
     */
    public Page<Utilizator> getUtilizatoriOnPage(Pageable pageable){
        if(repoUtilizatori instanceof PagingRepository<Integer, Utilizator> pagingRepo){
            return pagingRepo.findAllOnPage(pageable);
        }
        throw new UnsupportedOperationException("Paginarea functioneaza doar pentru database");
    }
}

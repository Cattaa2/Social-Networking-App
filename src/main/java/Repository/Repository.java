package Repository;

import domain.Entity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Interfata pentru operatiile CRUD
 * @param <ID> tipul ID-ului a entitatii
 * @param <E> tipul entitatii
 */
public interface Repository<ID,E extends Entity<ID>> {

    /**
     * Cauta o entitate dupa ID
     * @param id ID-ul entitatii
     * @return entitatea gasita sau null daca nu exista
     */
    Optional<E> findOne(ID id);

    /**
     * Returneaaza toate entitatile
     * @return un obiect Iterable
     */
    Iterable<E> findAll();

    /**
     * Salveaza o entitate in repository
     * @param entity entitatea care trebuie salvata
     * @return null daca entitatea a fost salvata, sau entitatea daca exista deja
     * @throws exceptions.ValidationException daca entitatea nu e valida
     */
    E save(E entity);

    /**
     * Sterge o entitate dupa ID
     * @param id ID-ul entitatii de sters
     * @return entitatea stearsa sau null daca nu exista
     * @throws exceptions.RepoException daca ID ul e null
     */
    Optional<E> delete(ID id);

    /**
     * Modifica o entitate
     * @param entity entitatea cu datele noi
     * @return null daca a fost modificata sau entitatea daca nu exista
     */
    Optional<E> update(E entity);

    default List<E> findAll(Predicate<E> filter) {
        return StreamSupport.stream(findAll().spliterator(), false).filter(filter).collect(Collectors.toList());
    }
}

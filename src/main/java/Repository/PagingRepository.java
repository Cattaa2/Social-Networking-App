package Repository;

import domain.Entity;
import util.paging.Page;
import util.paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID,E> {

    /**
     * Finds all entites on a page
     *
     * @param pageable
     * @return a Page with its elements and total count
     */
    public Page<E> findAllOnPage(Pageable pageable);
}

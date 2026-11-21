package util.paging;

public class Pageable {
    private final int pageNumber;
    private final int pageSize;

    /**
     * Creates a new Pageable object
     *
     * @param pageNumber the page index (must be >= 0)
     * @param pageSize   the maximum number of elements per page (must be > 0)
     * @throws IllegalArgumentException if pageNumber < 0 or pageSize <= 0
     */
    public Pageable(int pageNumber, int pageSize) {
        if(pageNumber < 0){
            throw new IllegalArgumentException("Page number can't be negative");
        }
        if(pageSize <= 0){
            throw new IllegalArgumentException("Page size must be > 0");
        }
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}

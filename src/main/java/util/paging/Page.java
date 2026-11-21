package util.paging;

public class Page<E> {
    private final int totalNumberOfElements;
    private final Iterable<E> elementsOnPage;

    /**
     * Constructs a new Page
     *
     * @param elementsOnPage the elements on the current Page
     * @param totalNumberOfElements total number of elements from all pages
     */
    public Page(Iterable<E> elementsOnPage, int totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
        this.elementsOnPage = elementsOnPage;
    }

    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    public Iterable<E> getElementsOnPage() {
        return elementsOnPage;
    }
}

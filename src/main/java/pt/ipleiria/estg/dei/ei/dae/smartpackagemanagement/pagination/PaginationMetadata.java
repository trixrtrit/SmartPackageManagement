package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination;

public class PaginationMetadata {
    private final int currentPage;
    private final int pageSize;
    private final long totalItems;
    private final long totalPages;
    private final long totalItemsOnPage;

    public PaginationMetadata(int currentPage, int pageSize, long totalItems, long totalPages, long totalItemsOnPage) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.totalItemsOnPage = totalItemsOnPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalItemsOnPage() {
        return totalItemsOnPage;
    }
}

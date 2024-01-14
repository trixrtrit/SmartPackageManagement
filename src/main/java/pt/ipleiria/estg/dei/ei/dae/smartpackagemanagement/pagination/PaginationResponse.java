package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.pagination;

import java.util.List;

public class PaginationResponse<T> {
    private final List<T> data;
    private final PaginationMetadata metadata;

    public PaginationResponse(List<T> data, PaginationMetadata metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    public List<T> getData() {
        return data;
    }

    public PaginationMetadata getMetadata() {
        return metadata;
    }
}

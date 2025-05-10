package org.example.model;

import java.util.List;

public record ExtractorData(
        List<GProduct> products,
        boolean hasNextPage
) {
}

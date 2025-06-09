package org.example.model.records;

import org.example.model.GProduct;

import java.util.List;

public record UpdateProcess(String message, List<GProduct> products) {}
package org.example.model.interfaces;

public record TreeItem(String label, String id) {
    @Override
    public String toString() {return label;}
}
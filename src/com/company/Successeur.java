package com.company;

import java.util.ArrayList;
import java.util.List;

public class Successeur {
    private int sommet;
    private List<Integer> successeurs = new ArrayList<>();

    public Successeur() {
    }

    public Successeur(int sommet) {
        this.sommet = sommet;
    }

    public Successeur(int sommet, List<Integer> successeurs) {
        this.sommet = sommet;
        this.successeurs = successeurs;
    }

    public int getSommet() {
        return sommet;
    }

    public List<Integer> getSuccesseurs() {
        return successeurs;
    }

    public void addSuccesseurs(int successeur) {
        this.successeurs.add(successeur);
    }

    @Override
    public String toString() {
        return String.format("\n%8s | %15s", sommet, successeurs);
    }
}

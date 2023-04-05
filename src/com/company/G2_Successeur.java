package com.company;

import java.util.ArrayList;
import java.util.List;

public class G2_Successeur {
    private int sommet;
    private List<Integer> successeurs = new ArrayList<>();

    public G2_Successeur(int sommet) {
        this.sommet = sommet;
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

package com.company;

import java.util.List;

public class G2_Tache {
    private int etiquette;
    private int delai;
    private List<Integer> contrainte;

    public G2_Tache(int etiquette, int delai, List<Integer> contrainte) {
        this.etiquette = etiquette;
        this.delai = delai;
        this.contrainte = contrainte;
    }

    public int getEtiquette() {
        return etiquette;
    }

    public List<Integer> getContrainte() {
        return contrainte;
    }

    @Override
    public String toString() {
        return String.format("\n%8s | %8d | %15s", etiquette, delai, contrainte);
    }
}

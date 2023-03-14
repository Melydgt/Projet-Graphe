package com.company;

import java.util.Arrays;
import java.util.List;

public class G2_Tache {
    private String etiquette;
    private int delai;
    private int[] contrainte;

    public G2_Tache(String etiquette, int delai) {
        this.etiquette = etiquette;
        this.delai = delai;
        this.contrainte = null;
    }

    public G2_Tache(String etiquette, int delai, int[] contrainte) {
        this.etiquette = etiquette;
        this.delai = delai;
        this.contrainte = contrainte;
    }

    @Override
    public String toString() {
        return String.format("\n%8s | %8d | %8s", etiquette, delai, Arrays.toString(contrainte));
    }
}

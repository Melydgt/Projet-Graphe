package com.company;

import java.util.ArrayList;
import java.util.List;

public class G2_Graphe {
    private List<G2_Tache> graph_tach = new ArrayList<>();

    public G2_Graphe() {
    }

    public G2_Graphe(List<G2_Tache> graph_tach) {
        this.graph_tach = graph_tach;
    }

    public List<G2_Tache> getGraph_tach() {
        return graph_tach;
    }

    public void appendGraph(G2_Tache tache) {
        this.graph_tach.add(tache);
    }

    @Override
    public String toString() {
        return String.format("%s\n%8s | %8s | %15s\n", "Graphe : ", "Tache", "délai", "contrainte") + graph_tach;
    }
}

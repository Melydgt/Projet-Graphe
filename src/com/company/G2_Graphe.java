package com.company;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class G2_Graphe {
    private List<G2_Tache> graph_tach = new ArrayList<G2_Tache>();

    public List<G2_Tache> getGraph_tach() {
        return graph_tach;
    }

    public void appendGraph(G2_Tache tache) {
        this.graph_tach.add(tache);
    }

    @Override
    public String toString() {
        return String.format("%s\n%8s | %8s | %8s\n", "Graphe :: ", "Tache", "délai", "contrainte") + graph_tach;
    }
}

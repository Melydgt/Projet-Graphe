package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class G2_Main {

    public static void main(String[] args) {
        try {
            // Variable
            Scanner sc = new Scanner(System.in);
            String name_file; // nom du fichier à ouvrir
            G2_Graphe graphe = new G2_Graphe(); // On créer le graphe
            String[] l; // tab Str qui va accuellir la ligne du graph temporairement
            List<Integer> i = new ArrayList<>(); // tab qui va recevoir les conraintes
            int j;
            int tache_final=1;

            System.out.println("Bonjour et bienvenue dans ce programme qui vous aides pour l'ordonnancement d'un graphe");
            System.out.println("Entrer le nom du fichier que vous voulez lire");
            name_file = sc.nextLine();
            name_file = "./fichier_test/" + name_file + ".txt";
            System.out.println("Nous ouvrons le fichier " + name_file);

            // Le fichier d'entrée par default
//            File file = new File(name_file);
            System.out.println("\n\n\tDefault table 1");
            File file = new File("./fichier_test/table 12.txt");

            // Créer l'objet File Reader
            FileReader fr = new FileReader(file);

            // Créer l'objet BufferedReader qui va lire chaque ligne
            BufferedReader br = new BufferedReader(fr);
//            StringBuffer sb = new StringBuffer();
            String line;

            // BOUCLE On lit les lignes et on les ajoutes au graphe
            while((line = br.readLine()) != null) {
//                ajoute la ligne au buffer
//                sb.append(line);
//                sb.append("\n");
                l = line.split(" ");

                    // Ajout de l'état début=0 pour les taches qui n'ont pas de contrainte
                if (l.length == 2) {
                    i.add(0);
                }
                    // Ajout des contraintes à la tab i
                for (j = 2; j<(l.length); j++) {
                    i.add(Integer.parseInt(l[j]));
                }

                    // Trouver l'état le plus grd pour déterminer le nom de l'état fin
                if (tache_final < Integer.parseInt(l[0])){
                    tache_final = Integer.parseInt(l[0]);
                }

                    // Ajout des new taches
                graphe.appendGraph(new G2_Tache(Integer.parseInt(l[0]), Integer.parseInt(l[1]), i));
                i = new ArrayList<>();
            }

            // Pour ajouter les contraintes de l'état final c-a-d les taches qui n'apparaissent pas dans les contraintes il faut boucler les contraintes
            for (G2_Tache ta : graphe.getGraph_tach()) {
                Iterator<G2_Tache> k = graphe.getGraph_tach().iterator();
                boolean existe = false;
                while(!existe && k.hasNext()) {
                    if (k.next().getContrainte().contains(ta.getEtiquette())) {
                        existe = true;
                    }
                }
                if (!existe) {
                    i.add(ta.getEtiquette());
                }
            }
            graphe.appendGraph(new G2_Tache(tache_final+1,0,i));

            // AFFICHAGE
            System.out.println("Contenu du graphe : \n début=0\tfin=" + (tache_final+1));
            System.out.println(graphe);
            fr.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

package com.company;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;

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
            System.out.println("Nous ouvrons le fichier " + name_file + ".txt");

            // Le fichier d'entrée par default
            File file = new File("./fichier_test/" + name_file + ".txt");
//            System.out.println("\n\n\tDefault table 1");
//            File file = new File("./fichier_test/table 1.txt");

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

            // BOUCLE Ajouter les predecesseurs de l'état final
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

            // SAVE sauvegarde du graphe à l'aide de GSON
            String jsonStrGraphe = new Gson().toJson(graphe);
            writeJsonToFile(jsonStrGraphe, name_file);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void writeJsonToFile(String jsonStr, String name_graphe) {
        try {
            String file = "./fichier_test/memoire.txt";
            String list_file = "./fichier_test/liste_en_mem.txt";

            String jsonStrFile = "";
            String jsonStrList = "";

            if (!fileEmpty(file)) { jsonStrFile = readJsonFromFile(file); }
            if (!fileEmpty(list_file)) { jsonStrList = readJsonFromFile(list_file); }

            PrintWriter jsonFile = new PrintWriter(file);
            PrintWriter jsonList = new PrintWriter(list_file);

            if (!jsonStrFile.equals("")) { jsonFile.print(jsonStrFile + "\n" + jsonStr); }
            else{ jsonFile.print(jsonStr); }
            if (!jsonStrList.equals("")) { jsonList.print(jsonStrList + "\n" + name_graphe); }
            else { jsonList.print(name_graphe); }


            jsonFile.close();
            jsonList.close();
            System.out.println("Saving successful");
        } catch (IOException ioe) {
            System.out.println("ERROR: " + ioe.getMessage());
        }
    }

    private static String readJsonFromFile(String file) {
        String str = "";
        try {
            Scanner jsonFile = new Scanner(new File(file));
            str = jsonFile.nextLine();
            jsonFile.close();
        }
        catch (IOException ioe) {
            System.out.println("ERROR: " + ioe.getMessage());
        }
        return str;
    }

    private static boolean fileEmpty(String name_file) {
        File file = new File(name_file);
        return file.length() == 0;
    }
}

package com.company;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;

public class G2_Main {

    public static void main(String[] args) {
        int choix=-1;
        while (choix != 999) {
            try {
// ------------ Variable
                Scanner sc = new Scanner(System.in);
                String name_file; // nom du fichier à ouvrir
                String list_file = "./fichier_test/liste_en_mem.txt";
                String mem_file = "./fichier_test/memoire.txt";
                int i=1;
                choix = -1;
                G2_Graphe graphe = null;

// ------------ Récuperer la liste des graphes en mémoire
                File fileList = new File(list_file);
                FileReader fr = new FileReader(fileList);   // Créer l'objet File Reader
                BufferedReader br = new BufferedReader(fr); // Créer l'objet BufferedReader qui va lire chaque ligne
                StringBuffer sb = new StringBuffer();
                String line;

                while((line = br.readLine()) != null) {
                    sb.append("\t("+ (i++) + ") " +line);
                    sb.append("\n");
                }
                fr.close();

// ------------ Accueil
                System.out.println("\nBonjour et bienvenue dans ce programme pour l'ordonnancement d'un graphe\ttape 999 pour exit");
                System.out.println("Graphe sauvegarder :");
                System.out.println(sb);
                while (0>choix || choix>i) {
                    try {
                        System.out.println("Vous pouvez ouvrir un nouveau graphe(0) ou lire un graphe déja sauvegarder(1-" + (i-1) + ")");
                        choix = sc.nextInt();
                        if (choix == 999){
                            System.exit(0);
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Saisie non valide, recommencez");
                        sc.nextLine(); // on libère la prochaine ligne
                    }
                }

                sc.nextLine(); // on libère la prochaine ligne

// ------------ Choix d'utilisation
                if (choix == 0) {
                    System.out.println("Entrer le nom du fichier que vous voulez lire");
                    name_file = sc.nextLine();
                    System.out.println("Nous ouvrons le fichier " + name_file + ".txt");

                    if (new File("./fichier_test/" + name_file + ".txt").exists()) {
                        graphe = lireGraphe(name_file);

                        // SAVE sauvegarde du graphe à l'aide de GSON
                        String jsonStrGraphe = new Gson().toJson(graphe);
                        writeJsonToFile(jsonStrGraphe, name_file);
                    }
                    else {
                        System.out.println("!!! Le fichier n'existe pas !!!");
                    }
                }
// ------------ Choix d'utilisation
                else {
                    if (!fileEmpty(list_file)) {
                        graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class);
                    }
                    else {
                        System.out.println("!!! Aucun graphe en mémoire !!!");
                    }
                }


// ------------ AFFICHAGE
                System.out.println("\t\tLoad Graphe");
                System.out.println(graphe);

// ------------ Matrice des valeurs
                AffichageMatrice(graphe);

// ------------ Vérifier les propriétés

// ------------ Date au plus tot

// ------------ Date au plus tard

// ------------ Calendrier

// ------------ Marge

// ------------ Chemin critique

            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static G2_Graphe lireGraphe(String name_file) {
        G2_Graphe graphe = new G2_Graphe();             // On créer le graphe
        String[] l;                                     // tab Str qui va accuellir la ligne du graphe temporairement
        List<Integer> i = new ArrayList<>();            // tab qui va recevoir les contraintes
        int j;                                          // index boucle for
        int tache_final=1;                              // numéros de la tache fin

        File file = new File("./fichier_test/" + name_file + ".txt");

        try {
            FileReader fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr); // Créer l'objet BufferedReader qui va lire chaque ligne
            String line;

            while((line = br.readLine()) != null) {
                l = line.split(" ");

                // Ajout état début=0 pour les taches qui n'ont pas de contrainte
                if (l.length == 2) {
                    i.add(0);
                }

                // Ajout des contraintes (chaque taches) à la tab i
                for (j = 2; j<(l.length); j++) {
                    i.add(Integer.parseInt(l[j]));
                }

                // Trouver l'état le plus grd pour le nom de l'état final
                if (tache_final < Integer.parseInt(l[0])) {
                    tache_final = Integer.parseInt(l[0]);
                }

                // Ajout des new taches
                graphe.appendGraph(new G2_Tache(Integer.parseInt(l[0]), Integer.parseInt(l[1]), i));
                i = new ArrayList<>();                  // RESET i
            }

            // BOUCLE Ajouter les predecesseurs de l'état final
            for (G2_Tache ta : graphe.getGraph_tach()) {
                Iterator<G2_Tache> k = graphe.getGraph_tach().iterator();
                boolean existe = false;
                while(!existe && k.hasNext()) {
                    if (k.next().getContrainte().contains(ta.getSommet())) {
                        existe = true;
                    }
                }
                if (!existe) {
                    i.add(ta.getSommet());
                }
            }
            graphe.appendGraph(new G2_Tache(tache_final+1,0,i));
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graphe;
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

            if (!jsonStrFile.equals("")) { jsonFile.print(jsonStrFile + jsonStr); }
            else{ jsonFile.print(jsonStr); }
            if (!jsonStrList.equals("")) { jsonList.print(jsonStrList + name_graphe); }
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
            while (jsonFile.hasNextLine()) {
                str += (jsonFile.nextLine() + "\n");
            }
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

    private static String readONElineFromFile(int n_line,String file) {
        String str = "";
        try {
            int i=0;
            Scanner jsonFile = new Scanner(new File(file));
            while (jsonFile.hasNextLine() && (i++) != n_line) {
                str = (jsonFile.nextLine());
            }
            jsonFile.close();
        }
        catch (IOException ioe) {
            System.out.println("ERROR: " + ioe.getMessage());
        }
        return str;
    }

    private static void AffichageMatrice(G2_Graphe graphe) {
        System.out.println("\nMatrice des valeur\n");

        // Entête de la matrice
        System.out.printf("%s%4d", "", 0);
        for (G2_Tache ta_li : graphe.getGraph_tach()) {
            System.out.printf("%4d", ta_li.getSommet());
        }
        System.out.println();

        // Sommet 0
        System.out.printf("%4d", 0);
        for (G2_Tache ta_co : graphe.getGraph_tach()) {
            if (ta_co.getContrainte().contains(0)) {
                System.out.printf("%4d", 0);
            } else {
                System.out.printf("%4s", '*');
            }
        }
        System.out.println();

        // Tous les autres sommets
        for (G2_Tache ta_li : graphe.getGraph_tach()) {
            System.out.printf("%4d", ta_li.getSommet());
            for (G2_Tache ta_co : graphe.getGraph_tach()) {
                if (ta_co.getContrainte().contains(ta_li.getSommet())) {
                    System.out.printf("%4d", ta_li.getSommet());
                }
                else {
                    System.out.printf("%4s", '*');
                }
            }
            System.out.println();
        }
    }
}

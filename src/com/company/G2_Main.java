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
                System.out.println("\n------------------------------------------------------------------------");
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

// ------------ Choix d'utilisation (Etape 1...)
                if (choix == 0) {
                    System.out.println("Entrer le nom du fichier que vous voulez lire");
                    name_file = sc.nextLine();
                    System.out.println("Nous ouvrons le fichier " + name_file + ".txt");

                    if (new File("./fichier_test/" + name_file + ".txt").exists()) {
                        graphe = lireGraphe(name_file);

                        // SAVE sauvegarde du graphe à l'aide de GSON
                        String jsonStrGraphe = new Gson().toJson(graphe);
                        writeJsonToFile(jsonStrGraphe, name_file);
                    } else {
                        System.out.println("!!! Le fichier n'existe pas !!!");
                    }
                }
// ------------ Choix d'utilisation (...Etape 1)
                else {
                    if (!fileEmpty(list_file)) {
                        graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class);
                    } else {
                        System.out.println("!!! Aucun graphe en mémoire !!!");
                    }
                }


// ------------ AFFICHAGE (Etape 2)
                System.out.println("\t\t** Load Graphe **");
                System.out.println(graphe);
                AffichageGraphe(graphe);

// ------------ Matrice des valeurs (Etape 2)
                AffichageMatrice(graphe);

// ------------ Vérifier les propriétés (Etape 3)
                System.out.printf("\nPoint d'entrée : %d\tPoint de sorties : %d\n\n", 0, graphe.getGraph_tach().size());
                System.out.println("Présence de circuit dans le graphe ?");
/*                detectionCircuit(graphe);
                System.out.println(graphe);

                if (!arcNeg(graphe)) {
                    System.out.println("Le circuit NE contient PAS d'arc négatif");
                } else {
                    System.out.println("Le circuit contient un ou plusieurs arc(s) négatif");
                }*/

// ------------ Calcule du rangs (Etape 4)
                if (!detectionCircuit(graphe) && !arcNeg(graphe)) {
                    System.out.println("Le circuit NE contient PAS d'arc négatif");
                    graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class); // on recup le graphe puisqu'on l'a changer dans la detection de circuit
                    // RANG ...
                    rangs(graphe);
                } else {
                    System.out.println(graphe);
                    if (arcNeg(graphe)) {
                        System.out.println("Le circuit contient un ou plusieurs arc(s) négatif");
                    }
                    // escape return au debut
                }

// ------------ Date au plus tot (Etape 5)

// ------------ Date au plus tard (Etape 5)

// ------------ Calendrier /affichage (Etape 5)

// ------------ Marge (Etape 6)

// ------------ Chemin critique (Etape 6)

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

                // Ajout des contraintes (chaque taches) à la tab i,
                // donc i est le tab de contrainte à ajouter à chaque taches, reset à chaque itération
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
            else { jsonFile.print(jsonStr); }
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


    private static void AffichageGraphe(G2_Graphe graphe) {
        System.out.printf("\n* Affichage chemin du graphe\n%11s = %-3s\n", "Chemins  ", "Délais");
        int countArc=0;
        for (G2_Tache sommet : graphe.getGraph_tach()) {
            for (int i = 0; i < sommet.getContrainte().size(); i++) {
                System.out.printf("%3d --> %-3d = %-3d\n", sommet.getContrainte().get(i), sommet.getSommet(), sommet.getDelai());
                countArc++;
            }
        }
        System.out.printf("Il y a %d sommets et %d arcs\n", graphe.getGraph_tach().size(), countArc);
    }

    private static void AffichageMatrice(G2_Graphe graphe) {
        System.out.println("\n* Matrice des valeur\n");

        // Entête de la matrice
        System.out.printf("%4s%4d", "", 0);
        for (G2_Tache ta_li : graphe.getGraph_tach()) {
            System.out.printf("%4d", ta_li.getSommet());
        }
        System.out.println();

        // Sommet 0
        System.out.printf("%4d%4s", 0, '*');
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
            System.out.printf("%4d%4s", ta_li.getSommet(), '*');
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


    private static boolean detectionCircuit(G2_Graphe graphe) {
//        System.out.println("Find 0 in contrainte");
//        G2_Graphe graphTemp = new G2_Graphe(graphe.getGraph_tach());
        for (G2_Tache ta_co : graphe.getGraph_tach()) {
            if (ta_co.getContrainte().contains(0)) {
                ta_co.getContrainte().remove((Integer)0);
            }
        }

        while (graphe.getGraph_tach().size() != 0 && contrainteExiste(graphe)) {
            System.out.println("* Detection de circuit\n* Méthode d'élimination des points d'entrée");
            for (int i=0; i<graphe.getGraph_tach().size(); i++) {
                System.out.println("\t\t\tTEST Sommet = " + graphe.getGraph_tach().get(i).getSommet());
                if (graphe.getGraph_tach().get(i).getContrainte().isEmpty()) {
                    System.out.println("Point d'entrée : " + graphe.getGraph_tach().get(i).getSommet());
                    for (G2_Tache ta_co : graphe.getGraph_tach()) {
                            if (ta_co.getContrainte().contains(graphe.getGraph_tach().get(i).getSommet())) {
//                                System.out.println("REMOVE - " + graphe.getGraph_tach().get(i).getSommet() + " FROM " + ta_co.getSommet());
                                ta_co.getContrainte().remove((Integer)graphe.getGraph_tach().get(i).getSommet());
                            }
    //                    }
                    }
                    System.out.println("Suppression des points d'entrée");
                    graphe.getGraph_tach().remove(i);
                    i--;
                    if (graphe.getGraph_tach().size() != 0) {
                        System.out.println("Il reste des sommets ...");
                    }
                }
            }
        }
        if (graphe.getGraph_tach().size() != 0) {
            System.out.println("Le graphe contient un circuit (boucle)");
            return true;
        }
        else {
            System.out.println("Le graphe NE contient PAS de circuit");
            return false;
        }
    }

    public static boolean contrainteExiste(G2_Graphe graphe) {
        for (G2_Tache tache : graphe.getGraph_tach()) {
            if (!tache.getContrainte().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean arcNeg(G2_Graphe graphe) {
        for (G2_Tache arc_neg : graphe.getGraph_tach()) {
            if (arc_neg.getDelai() < 0) {
                return true;
            }
        }
        return false;
    }

    private static void rangs(G2_Graphe graphe) {
        // on ne compte pas le sommet final
        int Sfinal = graphe.getGraph_tach().size();
        int[][] TabRang = new int[Sfinal-1][2];

        int i = 0; // la ligne ou on est dans le tableau

        for (G2_Tache ta_co : graphe.getGraph_tach()) {
            if (ta_co.getContrainte().contains(0)) {
                ta_co.getContrainte().remove((Integer)0);
                TabRang[i][0] = ta_co.getSommet();
                TabRang[i++][1] = 0;
            }
        }

        System.out.println("* Calcul de rang\n");
        int line_max = i;
        while (contrainteExiste(graphe)) {
            for (int j = 0; j < line_max; j++) { // Parcourt des sommets qui ont déja des rangs
                for (G2_Tache ta_co : graphe.getGraph_tach()) {

                    if (ta_co.getContrainte().contains(TabRang[j][0])) {
//                        System.out.print(TabRang[j][0] + " existe dans ");
//                        System.out.println(ta_co.getContrainte() + " ??? " + "sommet=" + ta_co.getSommet());
                        if (findSommetInGraphe(graphe,TabRang[j][0]).getContrainte().size() == 0) {
                            ta_co.getContrainte().remove((Integer) TabRang[j][0]);
                        }
                        // vérifier si le rang existe sinon ajouter +1 a la ligne max
                        int temp = findRangInTab(TabRang, ta_co.getSommet());
                        if (temp == -1) {
//                            System.out.println("on ajoute le sommet" + ta_co.getSommet() + " rang : " + (TabRang[j][1] + 1));
                            if (ta_co.getSommet() != Sfinal) {
                                TabRang[i][0] = ta_co.getSommet();
                                TabRang[i++][1] = (TabRang[j][1] + 1);
                            }
                        }
                        // vérifier si le rang actuel est supérieur à celui qu'on veut ajouter
                        else {
                            if (TabRang[temp][1] <= TabRang[j][1]+1) {
//                                System.out.println("on met à jour le rang de " + TabRang[temp][0] + " new rang : " + (TabRang[j][1] + 1));
                                TabRang[temp][1] = (TabRang[j][1] + 1);
                            }
                        }
                    }
                }
                line_max = i;
//                i = line_max;
            }
        }

        System.out.println("Liste des rangs des sommets");
        for (int k = 0; k < TabRang.length; k++) {
            System.out.printf("Sommet_%-2d  Rang_%-2d\n", TabRang[k][0], TabRang[k][1]);
        }
    }

    private static int findRangInTab (int[][] TabRang, int sommet) {
        int index=0;
        for (int[] ligneRang : TabRang) {
            if (ligneRang[0] == sommet) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static G2_Tache findSommetInGraphe (G2_Graphe graphe, int sommet) {
        for (int index=0; index<graphe.getGraph_tach().size(); index++) {
            if (sommet == graphe.getGraph_tach().get(index).getSommet()) {
                return graphe.getGraph_tach().get(index);
            }
        }
        return null;
    }
}

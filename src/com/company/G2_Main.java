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
                String name_file = null; // nom du fichier à ouvrir
                String list_file = "./fichier_test/liste_en_mem.txt";
                String mem_file = "./fichier_test/memoire.txt";
                String fileExe = ""; // fichier dans lequel on sauvegarde les traces d'exécution du programme

                int i=1;
                choix = -1;
                G2_Graphe graphe = null;

// ------------ Récuperer la liste des graphes en mémoire
                File fileList = new File(list_file);
                File fileMem = new File(mem_file);
                if (!fileList.exists()) {
                    fileList.createNewFile();
                }
                if (!fileMem.exists()) {
                    fileMem.createNewFile();
                }

                FileReader fr = new FileReader(fileList);   // Créer l'objet File Reader
                BufferedReader br = new BufferedReader(fr); // Créer l'objet BufferedReader qui va lire chaque ligne
                StringBuffer sb = new StringBuffer();
                String line;

                while((line = br.readLine()) != null) {
//                    sb.append("\t(").append(i++).append(") ").append(line);
                    sb.append("\t("+ (i++) + ") " +line);
                    sb.append("\n");
                }
                fr.close();

// ------------ Accueil
                System.out.println("\n------------------------------------------------------------------------");
                System.out.println("\nBonjour et bienvenue dans ce programme pour l'ordonnancement d'un graphe\ttape 999 pour exit");
                System.out.println("Graphe sauvegarder :");
                System.out.println(sb);
                while (0>choix || choix>i-1) {
                    choix = -1;
                    try {
                        if (i-1 == 0) {
                            System.out.println("Vous pouvez ouvrir un nouveau graphe(0) ou lire un graphe déja sauvegarder(0-" + (i-1) + ")");
                        } else {
                            System.out.println("Vous pouvez ouvrir un nouveau graphe(0) ou lire un graphe déja sauvegarder(1-" + (i-1) + ")");
                        }
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


// 1 ------------ Choix d'utilisation (Etape 1...)
                if (choix == 0) {
                    boolean file_existe = false;
                    while (!file_existe) {
                        System.out.println("Entrer le nom du fichier que vous voulez lire\ttape 999 pour exit");
                        name_file = sc.nextLine();
                        System.out.println("Nous ouvrons le fichier " + name_file + ".txt");
                        file_existe = new File("./fichier_test/" + name_file + ".txt").exists();
                        if (file_existe) {
                            graphe = lireGraphe(name_file);

                            // SAVE sauvegarde du graphe à l'aide de GSON
                            String jsonStrGraphe = new Gson().toJson(graphe);
                            writeJsonToFile(jsonStrGraphe, name_file);
                        } else {
                            System.out.println("!!! Le fichier n'existe pas !!!");
                        }
                        if (name_file.equals("999")) {
                            System.exit(0);
                        }
                    }
                    fileExe = name_file; // VAR Enregistrement des traces d'exécution
                }

// 1 ------------ Choix d'utilisation (...Etape 1)
                else {
                    if (!fileEmpty(list_file)) {
                        graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class);
                        fileExe = readONElineFromFile(choix,list_file);
                    } else {
                        System.out.println("!!! Aucun graphe en mémoire !!!");
                    }
                }


                if (!(graphe == null)) {
// 0 ------------ Enregistrement des traces d'exécution
                    fileExe = "Execution_" + fileExe + ".txt";
                    File fileExecution = new File(fileExe);
                    if (!fileExecution.exists()) {
                        fileExecution.createNewFile();
                    }

// 2 ------------ AFFICHAGE (Etape 2)
                    System.out.println("\t\t*** Load Graphe ***");
                    writeExecutionToFile(fileExe, "\t\t*** Load Graphe ***");
                    System.out.println(graphe);
                    writeExecutionToFile(fileExe, graphe.toString());
                    AffichageGraphe(graphe);

// 2 ------------ Matrice des valeurs (Etape 2)
                    AffichageMatrice(graphe);

// 3 ------------ Vérifier les propriétés (Etape 3)
                    System.out.printf("\nPoint d'entrée : %d\tPoint de sorties : %d\n\n", 0, graphe.getGraph_tach().size());
                    System.out.println("Présence de circuit dans le graphe ?");

                    if (!detectionCircuit(graphe) && !arcNeg(graphe)) {
                        System.out.println("Le circuit NE contient PAS d'arc négatif");
                        if (choix != 0) {
                            graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class); // on recup le graphe puisqu'on l'a changer dans la detection de circuit
                        } else {
                            graphe = lireGraphe(name_file);
                        }

// 4 ------------ Calcule du rangs (Etape 4)
                        rangs(graphe);
                        if (choix != 0) {
                            graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class); // on recup le graphe puisqu'on l'a changer dans la detection de circuit
                        } else {
                            graphe = lireGraphe(name_file);
                        }

// 5 ------------ Date au plus tot (Etape 5)
// 5 ------------ Date au plus tard (Etape 5)
// 5 ------------ Calendrier /affichage (Etape 5)
// 6 ------------ Marge (Etape 6)
                        // calendrier(graphe, choix, mem_file, name_file);

// 6 ------------ Chemin critique (Etape 6)
                        cheminCritique(graphe, choix, mem_file, name_file);

                    } else {
                        System.out.println(graphe);
                        if (arcNeg(graphe)) {
                            System.out.println("Le circuit contient un ou plusieurs arc(s) négatif");
                        }
                        // escape return au debut
                    }
                }
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

    private static void writeExecutionToFile(String file, String trace) {
        try {
            String Temp = "";

            if (!fileEmpty(file)) { Temp = readJsonFromFile(file); }

            PrintWriter writerExe = new PrintWriter(file);

            if (!Temp.equals("")) {
                writerExe.print(Temp + trace);
            }
            else {
                writerExe.print(trace);
            }

            writerExe.close();
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
        System.out.printf("\n\t\t*** Affichage chemin du graphe ***\n%11s = %-3s\n", "Chemins  ", "Délais");
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
        System.out.println("\n\t\t*** Matrice des valeur ***\n");

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

        System.out.println("\n\t\t*** Detection de circuit ***\n\t* Méthode d'élimination des points d'entrée *\n");
        while (graphe.getGraph_tach().size() != 0 && contrainteExiste(graphe)) {
            for (int i=0; i<graphe.getGraph_tach().size(); i++) {
                System.out.println("\t\t\t\t\t\tTEST Sommet = " + graphe.getGraph_tach().get(i).getSommet());
                if (graphe.getGraph_tach().get(i).getContrainte().isEmpty()) {
                    System.out.println("Point d'entrée : " + graphe.getGraph_tach().get(i).getSommet());
                    for (G2_Tache ta_co : graphe.getGraph_tach()) {
                            if (ta_co.getContrainte().contains(graphe.getGraph_tach().get(i).getSommet())) {
//                                System.out.println("REMOVE - " + graphe.getGraph_tach().get(i).getSommet() + " FROM " + ta_co.getSommet());
                                ta_co.getContrainte().remove((Integer)graphe.getGraph_tach().get(i).getSommet());
                            }
    //                    }
                    }
                    System.out.print("Suppression des points d'entrée. ");
                    graphe.getGraph_tach().remove(i);
                    i--;
                    if (graphe.getGraph_tach().size() != 0) {
                        System.out.println("Il reste des sommets ...");
                    }
                }
            }
        }
//        if (graphe.getGraph_tach().size() != 0) {
        if (!contrainteExiste(graphe) && graphe.getGraph_tach().size() != 0) {
            System.out.println("Le graphe contient un circuit (boucle)");
            return true;
        }
        else {
            System.out.println("Le graphe NE contient PAS de circuit");
            return false;
        }
    }

    // return true if just one contrainte is empty
    // return false else
    public static boolean contrainteExiste(G2_Graphe graphe) {
        for (G2_Tache tache : graphe.getGraph_tach()) {
            if (tache.getContrainte().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // return true if just one contrainte is not empty
    // return false else
    public static boolean contrainteExiste2(G2_Graphe graphe) {
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

    // Revoir ... dans le désordre
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

        System.out.println("\n\t\t*** Calcul de rang ***\n");
        int line_max = i;
        while (contrainteExiste2(graphe)) {
            for (int j = 0; j < line_max; j++) { // Parcourt des sommets qui ont déja des rangs
                for (G2_Tache ta_co : graphe.getGraph_tach()) {

                    if (ta_co.getContrainte().contains(TabRang[j][0])) {
                        if (findSommetInGraphe(graphe,TabRang[j][0]).getContrainte().size() == 0) {
                            System.out.printf("Le sommet %d est un prédécesseur de S%d et il n'a plus d'autre antécédents on supprime le sommet % des contraintes\n", TabRang[j][0], ta_co.getSommet(), TabRang[j][0]);
                            ta_co.getContrainte().remove((Integer) TabRang[j][0]);
                        }
                        // vérifier si le rang existe sinon ajouter +1 a la ligne max
                        int temp = findRangCalendarInTab(TabRang, ta_co.getSommet());
                        if (temp == -1) {
                            if (ta_co.getSommet() != Sfinal) {
                                System.out.printf("S%d prends le rang : %d\n", ta_co.getSommet(), (TabRang[j][1] + 1));
                                TabRang[i][0] = ta_co.getSommet();
                                TabRang[i++][1] = (TabRang[j][1] + 1);
                            }
                        }
                        // vérifier si le rang actuel est supérieur à celui qu'on veut ajouter
                        else {
                            if (TabRang[temp][1] <= TabRang[j][1]+1) {
                                System.out.printf("S%d prends le nouveaux rang : %d\n", TabRang[temp][0], (TabRang[j][1] + 1));
                                TabRang[temp][1] = (TabRang[j][1] + 1);
                            }
                        }
                    }
                }
                line_max = i;
//                i = line_max;
            }
        }

        System.out.println("\n* Liste des rangs des sommets *");
        System.out.printf("%-5s %-4s\n", "Sommet", "Rang");
        for (int k = 0; k < TabRang.length; k++) {
            System.out.printf("S%-5d %-4d\n", TabRang[k][0], TabRang[k][1]);
        }
    }

    private static int findRangCalendarInTab(int[][] TabRang, int sommet) {
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

    private static int[][] calendrier(G2_Graphe graphe, int choix, String mem_file, String name_file) {
        int[][] Calendrier = new int[graphe.getGraph_tach().size()+1][5];
        int i = 1;

        // Début
        Calendrier[0][0] = 0; // Sommet
        Calendrier[0][1] = 0; // Date au plus tot
        Calendrier[0][2] = 0; // Date au plus tard

        // dans le calendrier on met tous les sommets
        for (G2_Tache tache : graphe.getGraph_tach()) {
            Calendrier[i][0] = tache.getSommet();
            // on en profite si la tache à pour contrainte 0 on lui met date debut = 0
            if (tache.getContrainte().contains(0)) {
                Calendrier[i][1] = 0;
                tache.getContrainte().remove((Integer)0);
            }
            i++;
        }

        System.out.println("\n\t\t*** Calendrier ***\n");
        System.out.println("\t* Calcul des dates au plus tot *");

// ------------ Date au plus tot (Etape 5)
        while (contrainteExiste2(graphe)) {
            for (int[] ligneSommet : Calendrier) {
                System.out.printf("\n\t\t\t\t\t\tTEST Sommet %d\n", ligneSommet[0]);
                for (G2_Tache tache : graphe.getGraph_tach()) {
                    int Pred = findRangCalendarInTab(Calendrier, ligneSommet[0]);
                    int Actu = findRangCalendarInTab(Calendrier, tache.getSommet());
                    if (tache.getContrainte().contains(ligneSommet[0])) {
                        System.out.printf("S%d est un prédécesseur de S%d\n", ligneSommet[0], tache.getSommet());
                        int delaisPred = findSommetInGraphe(graphe,ligneSommet[0]).getDelai();
                        if (Calendrier[Actu][1] < (Calendrier[Pred][1] + delaisPred)) {
                            System.out.printf("S%d_Date+tot = %d < S%d_Date+tot %d + délais %d ", Calendrier[Actu][0], Calendrier[Actu][1], Calendrier[Pred][0], Calendrier[Pred][1], delaisPred);
                            System.out.printf("==> S%d_Date+tot = %d\n", Calendrier[Actu][0], (Calendrier[Pred][1] + delaisPred));
                            Calendrier[Actu][1] = Calendrier[Pred][1] + delaisPred;
                        }
                        if (findSommetInGraphe(graphe,Calendrier[Pred][0]).getContrainte().size() == 0) {
                            tache.getContrainte().remove((Integer) Calendrier[Pred][0]);
                        }
                    }
                }
            }
        }

        if (choix != 0) {
            graphe = new Gson().fromJson(readONElineFromFile(choix, mem_file), G2_Graphe.class); // on recup le graphe puisqu'on l'a changer dans la detection de circuit
        } else {
            graphe = lireGraphe(name_file);
        } // on recup le graphe puisqu'on l'a modifier dans le while précèdent
// 5 ------------ Date au plus tard (Etape 5)

        System.out.println("\n\n\t* Calcul des dates au plus tard *");
        System.out.println("\n!!! Pour des raisons de programmation les délais au plus tard sont initialisé à 999 !!!");

        // FIN
        Calendrier[Calendrier.length-1][2] = Calendrier[Calendrier.length-1][1]; // Date au plus tard = Date au plus tard;
        int m = 0;
        for (G2_Tache tache : graphe.getGraph_tach()) {
            // si la tache à pour contrainte 0 on lui met date debut = 0
            if (tache.getContrainte().contains(0)) {
                tache.getContrainte().remove((Integer)0);
            }
            if (Calendrier[m][0] != 0) {
                Calendrier[m][2] = 999;
                Calendrier[m][4] = 999;
            }
            m++;
        }

        List<G2_Successeur> successeurs = createSuccesseur(graphe);

        while (successeursExiste(successeurs)) {
            for (int j = successeurs.size() - 1; j >= 0; j--) {
                if (!successeurs.get(j).getSuccesseurs().isEmpty()) {
//                    System.out.println("on est dans le 1er if ----------");
                    int actu = findRangCalendarInTab(Calendrier, successeurs.get(j).getSommet());
                    int actuDelais = findSommetInGraphe(graphe, successeurs.get(j).getSommet()).getDelai();
                    for (int l = 0; l < successeurs.get(j).getSuccesseurs().size(); l++) {
//                        System.out.println("on est dans le 2eme for l=" + l);
                        int succ = findRangCalendarInTab(Calendrier, successeurs.get(j).getSuccesseurs().get(l));
                        int succINsucces = findSuccesseur(successeurs, successeurs.get(j).getSuccesseurs().get(l));
                        if (Calendrier[actu][2] > (Calendrier[succ][2] - actuDelais)) {
                            System.out.printf("\nS%d_Date+tard = %d > S%d_Date+tard %d - délais %d ", Calendrier[actu][0], Calendrier[actu][2], Calendrier[succ][0], Calendrier[succ][2], actuDelais);
                            System.out.printf("==> S%d_Date+tard = %d\n", Calendrier[actu][0], (Calendrier[succ][2] - actuDelais));
                            Calendrier[actu][2] = (Calendrier[succ][2] - actuDelais);
                        }
                        if (Calendrier[actu][4] > (Calendrier[succ][1]-Calendrier[actu][1]-actuDelais)) {
                            System.out.printf("\nS%d_Marge.L = %d > S%d_Date+tot.s %d - S%d_Date+tot.a %d - délais.a %d ", Calendrier[actu][0], Calendrier[actu][4], Calendrier[succ][0], Calendrier[succ][1], Calendrier[actu][0], Calendrier[actu][1], actuDelais);
                            System.out.printf("==> S%d_Marge.L = %d\n", Calendrier[actu][0], ((Calendrier[succ][1]-Calendrier[actu][1]-actuDelais)));
                            Calendrier[actu][4] = (Calendrier[succ][1]-Calendrier[actu][1]-actuDelais);
                        }
                        if (successeurs.get(succINsucces).getSuccesseurs().size() == 0) {
//                            System.out.println("DELETE");
                            successeurs.get(j).getSuccesseurs().remove((Integer) successeurs.get(j).getSuccesseurs().get(l--));
                        }
                    }
                }
            }
        }

// 5 ------------ Marge libre = min(date+tot du successeur - date+tôt de l'actuel - délai actuel) (Etape 5)
/*        successeurs = createSuccesseur(graphe);
        while (successeursExiste(successeurs)) {
            for (int j = successeurs.size() - 1; j >= 0; j--) {
                if (!successeurs.get(j).getSuccesseurs().isEmpty()) {
//                    System.out.println("on est dans le 1er if ----------");
                    int actu = findRangCalendarInTab(Calendrier, successeurs.get(j).getSommet());
                    int actuDelais = findSommetInGraphe(graphe, successeurs.get(j).getSommet()).getDelai();
                    for (int l = 0; l < successeurs.get(j).getSuccesseurs().size(); l++) {
//                        System.out.println("on est dans le 2eme for l=" + l);
                        int succ = findRangCalendarInTab(Calendrier, successeurs.get(j).getSuccesseurs().get(l));
                        int succINsucces = findSuccesseur(successeurs, successeurs.get(j).getSuccesseurs().get(l));
                        if (Calendrier[actu][2] > (Calendrier[succ][2] - actuDelais)) {
                            System.out.printf("\nS%d_Date+tard = %d > S%d_Date+tard %d - délais %d ", Calendrier[actu][0], Calendrier[actu][2], Calendrier[succ][0], Calendrier[succ][2], actuDelais);
                            System.out.printf("==> S%d_Date+tard = %d\n", Calendrier[actu][0], (Calendrier[succ][2] - actuDelais));
                            Calendrier[actu][2] = (Calendrier[succ][2] - actuDelais);
                        }
                        if (successeurs.get(succINsucces).getSuccesseurs().size() == 0) {
//                            System.out.println("DELETE");
                            successeurs.get(j).getSuccesseurs().remove((Integer) successeurs.get(j).getSuccesseurs().get(l--));
                        }
                    }
                }
            }
        }*/


        System.out.println("\n\t\t*** Calendrier final ***\n");
        System.out.printf("%6s %8s %9s %12s %11s\n", "Sommet", "Date+tot", "Date+tard", "Marge totale", "Marge libre");
        for (int k = 0; k < Calendrier.length; k++) {
            if (Calendrier[k][0] == 0) {
                Calendrier[k][3] = 0;   // Marge totale DEBUT
                Calendrier[k][4] = 0;   // Marge libre DEBUT
            } else {
                Calendrier[k][3] = (Calendrier[k][2] - Calendrier[k][1]); // Marge totale
            }
            System.out.printf("%6d %8d %9d %12d %11d\n", Calendrier[k][0], Calendrier[k][1], Calendrier[k][2], Calendrier[k][3], Calendrier[k][4]);
        }
        return Calendrier;
    }

    private static List<G2_Successeur> createSuccesseur(G2_Graphe graphe) {
        List<G2_Successeur> successeurs = new ArrayList<>();

        successeurs.add(new G2_Successeur(0));
        for (G2_Tache ta : graphe.getGraph_tach()) {
            successeurs.add(new G2_Successeur(ta.getSommet()));
        }

        for (G2_Tache ta : graphe.getGraph_tach()) {
            for (int contrainte : ta.getContrainte()) {
                int temp = findSuccesseur(successeurs, contrainte);
                if (temp != -1) {
                    successeurs.get(temp).addSuccesseurs(ta.getSommet());
                }
            }
        }

//        for (Successeur su : successeurs) {
//            System.out.print(su);
//        }
        return successeurs;
    }
    private static int findSuccesseur(List<G2_Successeur> suc, int sommet) {
        int index = 0;
        for (G2_Successeur s : suc) {
            if (s.getSommet() == sommet) {
                return index;
            }
            index++;
        }
        return -1;
    }
    private static boolean successeursExiste (List<G2_Successeur> successeurs) {
        for (G2_Successeur s : successeurs) {
            if (!s.getSuccesseurs().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static void cheminCritique (G2_Graphe graphe, int choix, String mem_file, String name_file) {
        List<G2_Successeur> successeurs = createSuccesseur(graphe);
        List<Integer> chemin = new ArrayList<>();
        int[][] Calendrier = calendrier(graphe, choix, mem_file, name_file);
        boolean TestAjout;
        int last_sommet;
        System.out.println("\n\t\t*** Chemin critique ***\n");

        chemin.add(0);
        // While : tant que le dernier point n'est pas Fin, ici la taille de notre graphe
        while (chemin.get(chemin.size()-1) != graphe.getGraph_tach().size()) {
            TestAjout = false;
            // foreach : s parcourt les successeurs du dernier point du chemin critique.
            for (int s : (successeurs.get(findSuccesseur(successeurs, chemin.get(chemin.size()-1))).getSuccesseurs())) {
                // if : si le successeur à sa marge totale = 0 on le choisit
                if (Calendrier[findRangCalendarInTab(Calendrier, s)][3] == 0) {
                    System.out.printf("La marge totale de %d = 0 on ajoute le point %d au chemin critique\n", s, s);
                    chemin.add(s);
                    TestAjout = true;
                }
            }
            if (!TestAjout) {
                System.out.println("on ne peux ajouter aucun point il faut reculer");
                last_sommet = chemin.get(chemin.size()-1);
                chemin.remove(chemin.size()-1);
                successeurs.get(findSuccesseur(successeurs, chemin.get(chemin.size()-1))).getSuccesseurs().remove((Integer) last_sommet);
            }
        }
        System.out.println("\n\n* Un chemin critique possible *\n\t" + chemin);
    }
}

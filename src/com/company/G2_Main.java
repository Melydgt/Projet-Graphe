package com.company;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class G2_Main {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String name_file="";

            System.out.println("Bonjour et bienvenue dans ce programme qui vous aides pour l'ordonnancement d'un graphe");
            System.out.println("Entrer le nom du fichier que vous voulez lire");
            name_file = sc.nextLine();
            name_file = "./fichier_test/" + name_file + ".txt";
            System.out.println("Nous ouvrons le fichier " + name_file);

            // Le fichier d'entrée
            System.out.println("\n\n\tDefault table 1");
            File file = new File("./fichier_test/table 1.txt");

            // Créer l'objet File Reader
            FileReader fr = new FileReader(file);

            // Créer l'objet BufferedReader
            BufferedReader br = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String line;

/*            // On compte le nbr de ligne pour initialiser le graphe
            int nb_ligne=0;
            while((line2 = br.readLine()) != null) {
                nb_ligne++;
            }*/

            // On créer le graphe
            G2_Graphe graphe = new G2_Graphe();
            String[] l;
            int[] i;

            while((line = br.readLine()) != null) {
                // ajoute la ligne au buffer
                sb.append(line);
                sb.append("\n");
                l = line.split(" ");
                i = new int[l.length-2];
                for (int j=2; j<l.length-2; j++) {
                    i[j-2] = Integer.valueOf(l[j]);
                    System.out.println("i[j-2] " + i[j-2]);
                }
                graphe.appendGraph(new G2_Tache(l[0], Integer.valueOf(l[1]), i));
            }
            System.out.println("Contenu du graphe : ");
            System.out.println(graphe);










            fr.close();
            System.out.println("Contenu du fichier: ");
            System.out.println(sb);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

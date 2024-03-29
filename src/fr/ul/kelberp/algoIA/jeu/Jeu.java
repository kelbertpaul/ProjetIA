package fr.ul.kelberp.algoIA.jeu;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Jeu {

    /**
     * 0 = HUMAIN
     * 1 = ALGO
     */
    private int joueurCourant;
    private int profondeur;

    private static final String NOM_JOUEUR = "HUMAIN";
    private static final String NOM_ALGO = "ALGO";

    /**
     * Plateau du jeu
     */
    private final ArrayList<ArrayList<Case>> plateau = new ArrayList<>();

    //ATTENTION, FAIRE DES CHANGEMENTS DANS CES VALEURS IMPLIQUE UNE MODIFICATION DU CODE ENTIER
    private final static int COLONNES_PLATEAU = 7;
    private final static int LIGNES_PLATEAU = 6;

    public Jeu() {
        this.joueurCourant = 0;
        this.profondeur = 4;
        for (int i = 0; i < LIGNES_PLATEAU; i++) {
            ArrayList<Case> tmpList = new ArrayList<>();
            for (int j = 0; j < COLONNES_PLATEAU; j++) {
                tmpList.add(new Case());
            }
            this.plateau.add(tmpList);
        }
    }

    /**
     * @return String : nom du joueurCourant
     */
    public String getNomJoueur() {
        return switch (joueurCourant) {
            case 0 -> NOM_JOUEUR;
            case 1 -> NOM_ALGO;
            default -> null;
        };
    }

    /**
     * Renvoie le plateau du jeu
     *
     * @return Case[][]
     */
    public ArrayList<ArrayList<Case>> getPlateau() {
        return plateau;
    }

    public void afficherPlateau() {
        System.out.println("\n");
        this.ecrireChiffres();
        affPlateauOnly();
        System.out.println("\n");
        this.jouer();
    }

    private void jouer() {
        System.out.println("Au tour de " + this.getNomJoueur());
        System.out.println("Dans quelle colonne voulez vous jouer ?");
        Scanner sc = new Scanner(System.in);
        try {
            int colonne = sc.nextInt();
            if (colonne == 1 || colonne == 2 || colonne == 3 || colonne == 4 || colonne == 5 || colonne == 6 || colonne == 7) {
                try {
                    this.mettreJeton(colonne, 1);
                } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                    System.out.println("Cette colonne est déjà pleine !");
                    this.jouer();
                }
                System.out.println("\n");
                this.afficherPlateau();
            } else {
                System.out.println("Veuillez rentrer un choix valide !");
                this.jouer();
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Veuillez rentrer une valeur valide !");
            this.jouer();
        }
    }
    /**
     * Ajout d'un jeton dans la grille de jeu
     * @param numColonne
     * @param numLigne
     * @param constructionArbre	permet de ne pas afficher les messages de victoire dans le cas de la construction de l'arbre
     * @throws IndexOutOfBoundsException
     */
    public void mettreJeton(int numColonne, int numLigne) throws IndexOutOfBoundsException {
        ArrayList<Case> colonne = this.plateau.get(this.plateau.size() - numLigne);
        if (!colonne.get(numColonne - 1).remplir(this.getJoueurCourant())) {
            mettreJeton(numColonne, numLigne + 1);
        } else {
            String jetonJoueur = "";
            if (this.joueurCourant == 0) {
                jetonJoueur = "x";
            } else {
                jetonJoueur = "o";
            }
            if (this.verfiHorizontale(numColonne - 1, numLigne, jetonJoueur)
                    || this.verifVerticale(numColonne - 1, numLigne, jetonJoueur)
                    || this.verifDiagBasVersHaut(numColonne - 1, numLigne, jetonJoueur)
                    || this.verifDiagHautVersBas(numColonne - 1, numLigne, jetonJoueur)) {
                affPlateauOnly();
            	System.out.println("================== PARTIE FINIE ! BRAVO A " + this.getNomJoueur() + "! ==================");
                System.exit(0);
            } else {
                this.changerJoueurCourant();	
            }
        }
    }

    private void affPlateauOnly() {
        this.tracerHorizontaleHaut();
        for (int i = 0; i < LIGNES_PLATEAU; i++) {
            for (int j = 0; j < COLONNES_PLATEAU; j++) {
                System.out.print(this.getPlateau().get(i).get(j));
            }
            System.out.println();
            this.tracerHorizontale();
        }
    }

    private boolean verfiHorizontale(int numColonne, int numLigne, String jetonJoueur) {
        ArrayList<Case> ligne = this.plateau.get(this.plateau.size() - numLigne);
        // x _ _ _
        try {
            if (ligne.get(numColonne).isRemplie() && Objects.equals(ligne.get(numColonne).getJeton(), jetonJoueur)
                    && ligne.get(numColonne + 1).isRemplie() && Objects.equals(ligne.get(numColonne + 1).getJeton(), jetonJoueur)
                    && ligne.get(numColonne + 2).isRemplie() && Objects.equals(ligne.get(numColonne + 2).getJeton(), jetonJoueur)
                    && ligne.get(numColonne + 3).isRemplie() && Objects.equals(ligne.get(numColonne + 3).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ x _ _
        try {
            if (ligne.get(numColonne - 1).isRemplie() && Objects.equals(ligne.get(numColonne - 1).getJeton(), jetonJoueur)
                    && ligne.get(numColonne).isRemplie() && Objects.equals(ligne.get(numColonne).getJeton(), jetonJoueur)
                    && ligne.get(numColonne + 1).isRemplie() && Objects.equals(ligne.get(numColonne + 1).getJeton(), jetonJoueur)
                    && ligne.get(numColonne + 2).isRemplie() && Objects.equals(ligne.get(numColonne + 2).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ _ x _
        try {
            if (ligne.get(numColonne - 2).isRemplie() && Objects.equals(ligne.get(numColonne - 2).getJeton(), jetonJoueur)
                    && ligne.get(numColonne - 1).isRemplie() && Objects.equals(ligne.get(numColonne - 1).getJeton(), jetonJoueur)
                    && ligne.get(numColonne).isRemplie() && Objects.equals(ligne.get(numColonne).getJeton(), jetonJoueur)
                    && ligne.get(numColonne + 1).isRemplie() && Objects.equals(ligne.get(numColonne + 1).getJeton(), jetonJoueur)
            ) {
                return true;
            }

        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ _ _ x
        try {
            if (ligne.get(numColonne - 3).isRemplie() && Objects.equals(ligne.get(numColonne - 3).getJeton(), jetonJoueur)
                    && ligne.get(numColonne - 2).isRemplie() && Objects.equals(ligne.get(numColonne - 2).getJeton(), jetonJoueur)
                    && ligne.get(numColonne - 1).isRemplie() && Objects.equals(ligne.get(numColonne - 1).getJeton(), jetonJoueur)
                    && ligne.get(numColonne).isRemplie() && Objects.equals(ligne.get(numColonne).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        return false;
    }

    private boolean verifVerticale(int numColonne, int numLigne, String jetonJoueur) {
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 3).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 3).get(numColonne).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return false;
        }
        return false;
    }

    private boolean verifDiagBasVersHaut(int numColonne, int numLigne, String jetonJoueur) {
        // x _ _ _
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne + 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne + 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne + 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne + 2).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 3).get(numColonne + 3).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 3).get(numColonne + 3).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ x _ _
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne - 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne - 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne + 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne + 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne + 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne + 2).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ _ x _
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne - 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne - 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne - 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne - 2).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne + 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne + 1).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ _ _ x
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne - 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne - 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne - 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne - 2).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 3).get(numColonne - 3).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 3).get(numColonne - 3).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        return false;
    }

    private boolean verifDiagHautVersBas(int numColonne, int numLigne, String jetonJoueur) {
        // x _ _ _
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne + 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne + 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne + 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne + 2).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 3).get(numColonne + 3).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 3).get(numColonne + 3).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ x _ _
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne - 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne - 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne + 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne + 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne + 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 2).get(numColonne + 2).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ _ x _
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne - 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne - 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne - 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne - 2).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne + 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne + 1).get(numColonne + 1).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        // _ _ _ x
        try {
            if (this.plateau.get(this.plateau.size() - numLigne).get(numColonne).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne).get(numColonne).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne - 1).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 1).get(numColonne - 1).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne - 2).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 2).get(numColonne - 2).getJeton(), jetonJoueur)
                    && this.plateau.get(this.plateau.size() - numLigne - 3).get(numColonne - 3).isRemplie()
                    && Objects.equals(this.plateau.get(this.plateau.size() - numLigne - 3).get(numColonne - 3).getJeton(), jetonJoueur)
            ) {
                return true;
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
        return false;
    }

    public void debut() {
        this.afficherPlateau();
    }

    private void ecrireChiffres() {
        for (int i = 0; i < COLONNES_PLATEAU; i++) {
            System.out.print("  " + (i + 1) + "  ");
        }
        System.out.println();
    }

    private void tracerHorizontaleHaut() {
        for (int i = 0; i < COLONNES_PLATEAU * 5; i++) {
            System.out.print("_");
        }
        System.out.println();
    }

    private void tracerHorizontale() {
        for (int i = 0; i < COLONNES_PLATEAU * 5; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public void changerProfondeur() {
        Scanner scProf = new Scanner(System.in);
        System.out.print("Veuillez entrer la nouvelle profondeur d'exploration (>0): ");
        try {
            int prof = scProf.nextInt();
            if (prof <= 0) {
                throw new InputMismatchException();
            } else {
                this.profondeur = prof;
                System.out.println("Pouf ! Profondeur changée !\n");
            }
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Veuillez rentrer une valeur valide !");
            this.changerProfondeur();
        }
    }

    public void changerJoueurCourant() {
        this.joueurCourant = Math.abs(this.joueurCourant - 1);
        System.out.println("Pouf ! Joueur changé !\n");
    }

    public int getJoueurCourant() {
        return joueurCourant;
    }

    public int getProfondeur() {
        return profondeur;
    }
}

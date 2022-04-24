import java.util.ArrayList;

/**
 * Le modèle : le coeur de l'application.
 *
 * Le modèle étend la classe [Observable] : il va posséder un certain nombre
 * d'observateurs (ici, un : la partie de la vue responsable de l'affichage)
 * et devra les prévenir avec [notifyObservers] lors des modifications.
 * Voir la méthode [avance()] pour cela.
 */
class CModele extends Observable {
    /** On fixe la taille de la grille. */
    public static final int HAUTEUR=6, LARGEUR=6;
    /** On stocke un tableau de cellules. */
    private Tuile[][] jeu;
    private Aventurier aventurier;

    /** Construction : on initialise un tableau de cellules. */
    public CModele() {
        /**
         * Pour éviter les problèmes aux bords, on ajoute une ligne et une
         * colonne de chaque côté, dont les cellules n'évolueront pas.
         */
        jeu = new Tuile[LARGEUR+2][HAUTEUR+2];
        for(int i=0; i<LARGEUR+2; i++) {
            for(int j=0; j<HAUTEUR+2; j++) {
                jeu[i][j] = new Tuile(this, i, j, 1, false, Artefact.NONE);
            }
        }
        init();
    }

    /**
     * Initialisation aléatoire des cellules, exceptées celle des bords qui
     * ont été ajoutés.
     */
    public void init() {
        // On init les tuiles mer
        jeu[1][1].setMer();
        jeu[1][2].setMer();
        jeu[2][1].setMer();

        jeu[1][5].setMer();
        jeu[1][6].setMer();
        jeu[2][6].setMer();

        jeu[5][6].setMer();
        jeu[6][6].setMer();
        jeu[6][5].setMer();

        jeu[5][1].setMer();
        jeu[6][1].setMer();
        jeu[6][2].setMer();
        // + Les tuiles invisbles
        for (int i = 0; i < 1; i++) {
            jeu[i][0].setMer();
            jeu[0][i].setMer();
            jeu[LARGEUR+1][i].setMer();
            jeu[i][LARGEUR+1].setMer();
        }

        // On init les tuiles avec les artefacts

    }

    /**
     * Mouvement avance du joueur
     */
    public void aventurierMonte() {
        /**
         * On procède en deux étapes.
         *  - D'abord, pour chaque cellule on évalue ce que sera son état à la
         *    prochaine génération.
         *  - Ensuite, on applique les évolutions qui ont été calculées.
         */

        /**
         * Pour finir, le modèle ayant changé, on signale aux observateurs
         * qu'ils doivent se mettre à jour.
         */
        notifyObservers();
    }

    public void aventurierDescend() {

    }

    public void aveturierDroite() {

    }

    public void aventurierGauche() {

    }

    /**
     * Une méthode pour renvoyer la cellule aux coordonnées choisies (sera
     * utilisée par la vue).
     */
    public Tuile getTuile(int x, int y) {
        return jeu[x][y];
    }
    /**
     * Notez qu'à l'intérieur de la classe [CModele], la classe interne est
     * connue sous le nom abrégé [Cellule].
     * Son nom complet est [CModele.Cellule], et cette version complète est
     * la seule à pouvoir être utilisée depuis l'extérieur de [CModele].
     * Dans [CModele], les deux fonctionnent.
     */
}

/** Fin de la classe CModele. */

/**
 * Définition de la classe zones pour les zones du jeu
 */

class Tuile {
    /**
     * L'Etat est défini par un entier
     * 1 = normale
     * 0 = inondee
     * -1 = submergee
     */
    private CModele modele;
    private boolean heliport;
    private int etat;
    private final int x, y;
    private Artefact artefact;
    // Si c'est dans la mer
    // On ne peut pas accéder à cette tuile;
    private boolean mer;

    public Tuile(CModele modele, int x, int y, int etat, boolean heliport, Artefact artefact) {
        this.x = x; this.y = y;
        this.modele = modele;
        this.etat = etat;
        this.heliport = heliport;
        this.artefact = artefact;
        this.mer = false;
    }

    public int getEtat() { return this.etat; }

    public Artefact getArtefact() { return this.artefact; }

    public void changeEtat(int etat) { this.etat = etat; }

    public boolean isNormale() { return this.etat == 1; }

    public boolean isInondee() { return this.etat == 0; }

    public boolean isSubmergee() { return this.etat == -1; }

    public boolean isHeliport() { return this.heliport; }

    public boolean isMer() { return this.mer; }

    public void setMer() { this.mer = true; }

    // Permet de savoir si une tuile est valide
    public boolean isValide() {
        if ((this.etat != -1) && (this.mer == false)) {
            return true;
        } else {
            return false;
        }
    }
}

class Aventurier {

    private Tuile position;
    private Artefact cle;
    private boolean artefact;
    private int numberAction;

    public Aventurier(Tuile position, Artefact cle, int numberAction) {
        this.cle = cle;
        this.numberAction = numberAction;
        this.position = position;
    }

    public void ajouteCle(Artefact cle) {
        this.cle = cle;
    }

    public int getNumberAction() { return this.numberAction; }

    public void decreaseNumberAction() {
        if (this.numberAction > 0) {
            this.numberAction--;
        }
    }

    public void deplaceAventurier(Tuile position) {
        if (position.isValide()) {
            this.position = position;
        }
    }

    public boolean haveArtefact() {
        return this.artefact;
    }

    public void setArtefact(boolean artefact) {
        this.artefact = artefact;
    }

}

class Carte {

}


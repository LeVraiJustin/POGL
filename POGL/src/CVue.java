import javax.swing.*;
import java.awt.*;

/**
 * La vue : l'interface avec l'utilisateur.
 *
 * On définit une classe chapeau [CVue] qui crée la fenêtre principale de 
 * l'application et contient les deux parties principales de notre vue :
 *  - Une zone d'affichage où on voit l'ensemble des cellules.
 *  - Une zone de commande avec un bouton pour passer à la génération suivante.
 */
class CVue {
    /**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueGrille grille;
    private VueCommandes commandes;

    /** Construction d'une vue attachée à un modèle. */
    public CVue(CModele modele) {
        /** Définition de la fenêtre principale. */
        frame = new JFrame();
        frame.setTitle("L'Île interdite.");
        /**
         * On précise un mode pour disposer les différents éléments à
         * l'intérieur de la fenêtre. Quelques possibilités sont :
         *  - BorderLayout (défaut pour la classe JFrame) : chaque élément est
         *    disposé au centre ou le long d'un bord.
         *  - FlowLayout (défaut pour un JPanel) : les éléments sont disposés
         *    l'un à la suite de l'autre, dans l'ordre de leur ajout, les lignes
         *    se formant de gauche à droite et de haut en bas. Un élément peut
         *    passer à la ligne lorsque l'on redimensionne la fenêtre.
         *  - GridLayout : les éléments sont disposés l'un à la suite de
         *    l'autre sur une grille avec un nombre de lignes et un nombre de
         *    colonnes définis par le programmeur, dont toutes les cases ont la
         *    même dimension. Cette dimension est calculée en fonction du
         *    nombre de cases à placer et de la dimension du contenant.
         */
        frame.setLayout(new FlowLayout());

        /** Définition des deux vues et ajout à la fenêtre. */
        grille = new VueGrille(modele);
        frame.add(grille);
        commandes = new VueCommandes(modele);
        frame.add(commandes);
        /**
         * Remarque : on peut passer à la méthode [add] des paramètres
         * supplémentaires indiquant où placer l'élément. Par exemple, si on
         * avait conservé la disposition par défaut [BorderLayout], on aurait
         * pu écrire le code suivant pour placer la grille à gauche et les
         * commandes à droite.
         *     frame.add(grille, BorderLayout.WEST);
         *     frame.add(commandes, BorderLayout.EAST);
         */

        /**
         * Fin de la plomberie :
         *  - Ajustement de la taille de la fenêtre en fonction du contenu.
         *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
         *  - Préciser que la fenêtre doit bien apparaître à l'écran.
         */
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}


/**
 * Une classe pour représenter la zone d'affichage des cellules.
 *
 * JPanel est une classe d'éléments graphiques, pouvant comme JFrame contenir
 * d'autres éléments graphiques.
 *
 * Cette vue va être un observateur du modèle et sera mise à jour à chaque
 * nouvelle génération des cellules.
 */
class VueGrille extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private CModele modele;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 96;

    /** Constructeur. */
    public VueGrille(CModele modele) {
        this.modele = modele;
        /** On enregistre la vue [this] en tant qu'observateur de [modele]. */
        modele.addObserver(this);
        /**
         * Définition et application d'une taille fixe pour cette zone de
         * l'interface, calculée en fonction du nombre de cellules et de la
         * taille d'affichage.
         */
        Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,
                TAILLE*CModele.HAUTEUR);
        this.setPreferredSize(dim);
    }

    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() { repaint(); }

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
        super.repaint();
        /** Pour chaque cellule... */
        for(int i=1; i<=CModele.LARGEUR; i++) {
            for(int j=1; j<=CModele.HAUTEUR; j++) {
                /**
                 * ... Appeler une fonction d'affichage auxiliaire.
                 * On lui fournit les informations de dessin [g] et les
                 * coordonnées du coin en haut à gauche.
                 */
                paint(g, modele.getTuile(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
            }
        }
    }
    /**
     * Fonction pour dessiner, colorer une tuile
     */

    private void paint(Graphics g, Tuile t, int x, int y) {
        if (t.isMer()) {
            g.setColor(Color.BLUE.darker());
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRoundRect(x, y, TAILLE, TAILLE, 36, 36);
    }
}


/**
 * Une classe pour représenter la zone contenant le bouton.
 *
 * Cette zone n'aura pas à être mise à jour et ne sera donc pas un observateur.
 * En revanche, comme la zone précédente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private CModele modele;

    /** Constructeur. */
    public VueCommandes(CModele modele) {
        this.modele = modele;
        /**
         * On crée un nouveau bouton, de classe [JButton], en précisant le
         * texte qui doit l'étiqueter.
         * Puis on ajoute ce bouton au panneau [this].
         */
        JButton boutonAvance = new JButton("Monte");
        boutonAvance.setActionCommand("Monte");
        /**
         * Nouveaux boutons pour le joueur
         */

        this.add(boutonAvance);
        /**
         * Le bouton, lorsqu'il est cliqué par l'utilisateur, produit un
         * événement, de classe [ActionEvent].
         *
         * On a ici une variante du schéma observateur/observé : un objet
         * implémentant une interface [ActionListener] va s'inscrire pour
         * "écouter" les événements produits par le bouton, et recevoir
         * automatiquements des notifications.
         * D'autres variantes d'auditeurs pour des événements particuliers :
         * [MouseListener], [KeyboardListener], [WindowListener].
         *
         * Cet observateur va enrichir notre schéma Modèle-Vue d'une couche
         * intermédiaire Contrôleur, dont l'objectif est de récupérer les
         * événements produits par la vue et de les traduire en instructions
         * pour le modèle.
         * Cette strate intermédiaire est potentiellement riche, et peut
         * notamment traduire les mêmes événements de différentes façons en
         * fonction d'un état de l'application.
         * Ici nous avons un seul bouton réalisant une seule action, notre
         * contrôleur sera donc particulièrement simple. Cela nécessite
         * néanmoins la création d'une classe dédiée.
         */
        Controleur ctrl = new Controleur(modele);
        /** Enregistrement du contrôleur comme auditeur du bouton. */
        boutonAvance.addActionListener(ctrl);

        /**
         * Variante : une lambda-expression qui évite de créer une classe
         * spécifique pour un contrôleur simplissime.
         *
         JButton boutonAvance = new JButton(">");
         this.add(boutonAvance);
         boutonAvance.addActionListener(e -> { modele.avance(); });
         *
         */
        JButton droite = new JButton("Droite");
        JButton gauche = new JButton("Gauche");
        JButton descend = new JButton("Descendre");

        this.add(droite);
        this.add(gauche);
        this.add(descend);

    }
}
/** Fin de la vue. */
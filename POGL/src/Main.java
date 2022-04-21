import java.awt.*;
import java.util.ArrayList;


public class Main {

    // Dans cette classe on va mettre toute les déclarations
    // Ca va être ici qu'on va lancer le jeu, C'est notre classe principale

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            CModele modele = new CModele();
            CVue vue = new CVue(modele);
        });

    }

}


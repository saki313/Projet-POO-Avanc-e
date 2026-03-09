package managerui;

import javafx.scene.Scene;

public class Theme {
    private static Scene scene;
    private static String currentTheme = "light"; // light ou dark

    /**
     * Enregistre la scène principale pour appliquer les feuilles de style.
     */
    public static void setScene(Scene scene) {
        Theme.scene = scene;
    }

    /**
     * Applique le thème spécifié en chargeant le fichier CSS correspondant.
     * @param theme "light" ou "dark"
     */
    public static void apply(String theme) {
        if (scene == null) return;

        // Supprime les feuilles de style précédentes
        scene.getStylesheets().removeIf(s -> s.contains("light") || s.contains("dark"));

        // Ajoute la nouvelle feuille de style
        String cssFile = "/style/" + theme + ".css";
        try {
            scene.getStylesheets().add(Theme.class.getResource(cssFile).toExternalForm());
            currentTheme = theme;
        } catch (Exception e) {
            System.err.println("Impossible de charger le thème : " + cssFile);
            e.printStackTrace();
        }
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }
}
package managerui;

import ui.ChatView;

public class Theme {
    private static String currentTheme = "clair"; // clair ou sombre

    // ========== NOUVEAU : Appliquer le thème ==========
    public static void applyTheme(ChatView chatView) {
        // Récupérer le thème depuis les préférences (à implémenter)
        // Pour l'instant, on utilise le thème clair par défaut
        
        if (currentTheme.equals("sombre")) {
            // Thème sombre
            chatView.setStyle("-fx-background-color: #1e1e1e;");
            // Autres modifications de thème...
        }
    }
}

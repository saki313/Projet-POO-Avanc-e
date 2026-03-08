// ========== Gestion des notifications sonores ==========
package managerui;

import java.net.URL;

import javafx.scene.media.AudioClip;

public class Notification {
    
    public static AudioClip notificationSound;
    public static AudioClip connectSound;
    public static boolean soundEnabled = true;

    // ========== NOUVEAU : Charger les sons ==========
    public void loadSounds() {
        try {
            URL notificationUrl = getClass().getResource("/ressources/sounds/dragon-studio-new-notification-3-398649.mp3");
            URL connectUrl = getClass().getResource("/ressources/sounds/universfield-new-notification-09-352705.mp3");
            
            if (notificationUrl != null) {
                notificationSound = new AudioClip(notificationUrl.toExternalForm());
            }
            if (connectUrl != null) {
                connectSound = new AudioClip(connectUrl.toExternalForm());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Sons non trouvés, mode silencieux");
            soundEnabled = false;
        }
    }

    // ========== NOUVEAU : Jouer un son ==========
    public static void playSound(AudioClip sound) {
        if (soundEnabled && sound != null) {
            sound.play();
        }
    }
}

package square;

import square.window.Window;
import square.window.layers.classes.WelcomeScreen;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("Snake!", 450 / 32 * 32, 450 / 32 * 32);
        window.addLayer(new WelcomeScreen(window));
    }
}

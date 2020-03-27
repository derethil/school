// Reference for Lanterna 3: https://github.com/mabe02/lanterna/blob/master/docs/contents.md
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class ConwaysLife {
    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            TextGraphics graphics = screen.newTextGraphics();

            TerminalSize size = screen.getTerminalSize();
            // LifeSimulator simulation = new LifeSimulator(size.getColumns(), size.getRows());

            screen.startScreen();
            screen.setCursorPosition(null);

            for (int i = 0; i < 50; i++) {
                //render(simulation, screen, graphics);   // Render the current state of the simulation
                sampleRender(screen, graphics, i);
                Thread.yield();                         // Let the JVM have some time to update other things
                Thread.sleep(100);                // Sleep for a bit to make for a nicer paced animation
                //simulation.update();                    // Tell the simulation to update
            }

            screen.stopScreen();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }
    }

    public static void sampleRender(Screen screen, TextGraphics graphics, int xPos) {
        screen.clear();

        // Not very interesting, but showing how to set characters
        graphics.setCharacter(xPos, 10, 'X');

        // This is what causes the console to render the new state, it is required
        try {
            screen.refresh();
        } catch (Exception ex) {
        }
    }
}

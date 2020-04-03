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
            LifeSimulator sim = new LifeSimulator(size.getColumns(), size.getRows());

//            sim.insertPattern(pattern, (sim.getSizeX()- pattern.getSizeX()) / 2,(sim.getSizeY() - pattern.getSizeY()) / 2);
            sim.insertPattern(new patternP144(), 45,3);
            sim.insertPattern(new patternGliderGun(), 0,0);

            screen.startScreen();
            screen.setCursorPosition(null);

            for (int i = 0; i < 400; i++) {
                render(sim, screen, graphics);   // Render the current state of the sim
                Thread.yield();                         // Let the JVM have some time to update other things
                Thread.sleep(25);                // Sleep for a bit to make for a nicer paced animation
                sim.update();                    // Tell the sim to update
            }

            screen.stopScreen();
        } catch (Exception ex) {
            System.out.println("Something bad happened: " + ex.getMessage());
        }
    }

    public static void render(LifeSimulator sim, Screen screen, TextGraphics graphics) {
        screen.clear();

        for (int row = 0; row < sim.getSizeY(); row++) {

            for (int col = 0; col < sim.getSizeX(); col++) {
                if (sim.getCell(col, row)) {
                    graphics.setCharacter(col, row, 'X');
                }
            }
        }

        try {
            screen.refresh();
        } catch (Exception ex) {
        }
    }
}

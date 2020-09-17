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

            screen.startScreen();
            screen.setCursorPosition(null);

            sim.insertPattern(new patternBlock(), 10,10);
            sim.insertPattern(new patternGlider(), 20, 10);
            sim.insertPattern(new patternBlink(), 30, 10);

            for (int i = 0; i < 400; i++) {
                render(sim, screen, graphics);
                Thread.yield();
                Thread.sleep(25);
                sim.update();
            }

            screen.clear();
            sim = new LifeSimulator(size.getColumns(), size.getRows());
            patternAcorn acorn = new patternAcorn();
            sim.insertPattern(acorn, (sim.getSizeX()- acorn.getSizeX()) / 2,(sim.getSizeY() - acorn.getSizeY()) / 2);

            for (int i = 0; i < 400; i++) {
                render(sim, screen, graphics);
                Thread.yield();
                Thread.sleep(25);
                sim.update();
            }

            screen.clear();
            sim = new LifeSimulator(size.getColumns(), size.getRows());
            sim.insertPattern(new patternP144(), 45,3);
            sim.insertPattern(new patternGliderGun(), 0,0);

            // Main Loop
            for (int i = 0; i < 400; i++) {
                render(sim, screen, graphics);
                Thread.yield();
                Thread.sleep(25);
                sim.update();
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

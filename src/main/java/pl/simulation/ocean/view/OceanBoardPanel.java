package pl.simulation.ocean.view;

import pl.simulation.ocean.model.*;
import pl.simulation.ocean.util.Position;

import javax.swing.JPanel;
import java.awt.*;

/**
 * Panel Swing rysujący planszę oceanu 20×20.
 * Każda komórka ma stały rozmiar {@link #CELL_SIZE} pikseli; na planszy
 * wyświetlane są plankton (zielone kółka), rybki (żółte owale z etykietą)
 * i rekiny (czerwone trójkąty). Martwe organizmy rysowane są na szaro.
 */
public class OceanBoardPanel extends JPanel {

    /** Szerokość i wysokość jednej komórki planszy w pikselach. */
    public static final int CELL_SIZE = 28;

    /** Kolory warstw rysowania: tło wody, siatka, organizmy żywe i martwe. */
    private static final Color WATER = new Color(30, 90, 140);
    private static final Color GRID = new Color(20, 60, 100);
    private static final Color PLANKTON = new Color(120, 220, 80);
    private static final Color FISH = new Color(255, 200, 60);
    private static final Color SHARK = new Color(220, 70, 70);
    private static final Color DEAD = new Color(80, 80, 80, 120);

    private Ocean ocean;

    /**
     * Tworzy panel powiązany z danym oceanem i ustawia preferowany rozmiar
     * na całą planszę ({@link Ocean#WIDTH} × {@link Ocean#HEIGHT} komórek).
     *
     * @param ocean model oceanu, którego stan będzie rysowany
     */
    public OceanBoardPanel(Ocean ocean) {
        this.ocean = ocean;
        int size = Ocean.WIDTH * CELL_SIZE;
        setPreferredSize(new Dimension(size, Ocean.HEIGHT * CELL_SIZE));
        setBackground(WATER);
    }

    /**
     * Podmienia referencję do oceanu (np. po resecie symulacji) i wymusza ponowne narysowanie panelu.
     *
     * @param ocean aktualny stan oceanu do wyświetlenia
     */
    public void setOcean(Ocean ocean) {
        this.ocean = ocean;
        repaint();
    }

    /**
     * Główna metoda rysowania Swing: wypełnia tło wodą, rysuje siatkę,
     * następnie warstwami plankton → rybki → rekiny (żywy plankton pomijany po zjedzeniu).
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(WATER);
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawGrid(g2);

        for (Plankton plankton : ocean.getPlanktons()) {
            if (plankton.isAlive()) {
                drawPlankton(g2, plankton.getPosition());
            }
        }

        for (Fish fish : ocean.getFish()) {
            drawFish(g2, fish);
        }

        for (Shark shark : ocean.getSharks()) {
            drawShark(g2, shark);
        }

        g2.dispose();
    }

    /** Rysuje pionowe i poziome linie siatki co {@link #CELL_SIZE} pikseli. */
    private void drawGrid(Graphics2D g2) {
        g2.setColor(GRID);
        for (int x = 0; x <= Ocean.WIDTH; x++) {
            int px = x * CELL_SIZE;
            g2.drawLine(px, 0, px, Ocean.HEIGHT * CELL_SIZE);
        }
        for (int y = 0; y <= Ocean.HEIGHT; y++) {
            int py = y * CELL_SIZE;
            g2.drawLine(0, py, Ocean.WIDTH * CELL_SIZE, py);
        }
    }

    /** Plankton: małe zielone kółko na środku komórki. */
    private void drawPlankton(Graphics2D g2, Position pos) {
        int cx = cellCenterX(pos.getX());
        int cy = cellCenterY(pos.getY());
        g2.setColor(PLANKTON);
        g2.fillOval(cx - 4, cy - 4, 8, 8);
    }

    /** Rybka: żółty owal (szary gdy martwa); przy życiu etykieta skrócona z nazwy (np. „R1”). */
    private void drawFish(Graphics2D g2, Fish fish) {
        Position pos = fish.getPosition();
        int cx = cellCenterX(pos.getX());
        int cy = cellCenterY(pos.getY());
        g2.setColor(fish.isAlive() ? FISH : DEAD);
        g2.fillOval(cx - 9, cy - 7, 18, 14);
        if (fish.isAlive()) {
            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(9f));
            String label = fish.getName().replace("Rybka", "R");
            g2.drawString(label, cx - 8, cy + 3);
        }
    }

    /** Rekin: czerwony trójkąt skierowany w górę (szary gdy martwy); przy życiu etykieta „K” + numer. */
    private void drawShark(Graphics2D g2, Shark shark) {
        Position pos = shark.getPosition();
        int cx = cellCenterX(pos.getX());
        int cy = cellCenterY(pos.getY());
        g2.setColor(shark.isAlive() ? SHARK : DEAD);
        int[] xs = { cx, cx - 11, cx + 11 };
        int[] ys = { cy - 10, cy + 8, cy + 8 };
        g2.fillPolygon(xs, ys, 3);
        if (shark.isAlive()) {
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 9f));
            String label = shark.getName().replace("Rekin", "K");
            g2.drawString(label, cx - 6, cy + 4);
        }
    }

    /** Współrzędna X środka komórki planszy w pikselach. */
    private static int cellCenterX(int x) {
        return x * CELL_SIZE + CELL_SIZE / 2;
    }

    /** Współrzędna Y środka komórki planszy w pikselach. */
    private static int cellCenterY(int y) {
        return y * CELL_SIZE + CELL_SIZE / 2;
    }
}

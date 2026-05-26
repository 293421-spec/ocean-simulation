package pl.simulation.ocean.view;

import pl.simulation.ocean.logic.Simulation;
import pl.simulation.ocean.logic.SimulationSnapshot;
import pl.simulation.ocean.model.Ocean;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.function.LongFunction;

public class SimulationWindow extends JFrame {

    private static final int MIN_DELAY_MS = 50;
    private static final int MAX_DELAY_MS = 1500;
    private static final int DEFAULT_DELAY_MS = 350;
    private static final int MAX_HISTORY = 100;

    private final LongFunction<Simulation> simulationFactory;

    private Simulation simulation;
    private long currentSeed;

    private OceanBoardPanel boardPanel;
    private final JLabel statusLabel;
    private final JButton startPauseButton;
    private final JButton stepButton;
    private final JButton stepBackButton;
    private final JButton resetButton;
    private final Timer turnTimer;
    private final JScrollPane scrollPane;

    private boolean running;

    private final Deque<SimulationSnapshot> history = new ArrayDeque<>();

    public SimulationWindow(LongFunction<Simulation> simulationFactory, long seed) {
        this.simulationFactory = simulationFactory;
        this.currentSeed = seed;
        this.simulation = simulationFactory.apply(seed);

        setTitle("Symulacja oceanu — seed " + seed);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        boardPanel = new OceanBoardPanel(simulation.getOcean());

        statusLabel = new JLabel(buildStatusText(), SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        startPauseButton = new JButton("Start");
        stepBackButton = new JButton("Cofnij");
        stepButton = new JButton("Dalej");
        resetButton = new JButton("Resetuj");

        JSlider speedSlider = new JSlider(MIN_DELAY_MS, MAX_DELAY_MS, DEFAULT_DELAY_MS);
        speedSlider.setInverted(true);
        speedSlider.setMajorTickSpacing(500);
        speedSlider.setPaintLabels(false);
        speedSlider.setPreferredSize(new Dimension(160, 40));

        controls.add(startPauseButton);
        controls.add(stepBackButton);
        controls.add(stepButton);
        controls.add(resetButton);
        controls.add(new JLabel("Szybkość:"));
        controls.add(speedSlider);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        legend.add(legendItem(new Color(120, 220, 80), "plankton"));
        legend.add(legendItem(new Color(255, 200, 60), "rybka"));
        legend.add(legendItem(new Color(220, 70, 70), "rekin"));

        JPanel north = new JPanel(new BorderLayout());
        north.add(statusLabel, BorderLayout.CENTER);
        north.add(legend, BorderLayout.SOUTH);

        scrollPane = new JScrollPane(boardPanel);

        add(north, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        turnTimer = new Timer(DEFAULT_DELAY_MS, e -> advanceTurn());
        turnTimer.setInitialDelay(DEFAULT_DELAY_MS);

        speedSlider.addChangeListener(e -> {
            if (!speedSlider.getValueIsAdjusting()) {
                turnTimer.setDelay(speedSlider.getValue());
            }
        });

        startPauseButton.addActionListener(e -> toggleRunning());
        stepButton.addActionListener(e -> stepOnce());
        stepBackButton.addActionListener(e -> stepBack());
        resetButton.addActionListener(e -> promptReset());

        updateControls();
        pack();
        setLocationRelativeTo(null);
    }

    public SimulationWindow(Simulation simulation, long seed) {
        this(s -> new Simulation(new Random(s), false), seed);
    }

    private static JPanel legendItem(Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel swatch = new JPanel();
        swatch.setBackground(color);
        swatch.setPreferredSize(new Dimension(14, 14));
        swatch.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        item.add(swatch);
        item.add(new JLabel(text));
        return item;
    }

    private void toggleRunning() {
        if (simulation.isFinished()) return;
        running = !running;
        if (running) {
            turnTimer.start();
        } else {
            turnTimer.stop();
        }
        updateControls();
    }

    private void stepOnce() {
        if (simulation.isFinished() || running) return;
        advanceTurn();
    }

    private void advanceTurn() {
        pushHistory();
        boolean hasMore = simulation.executeNextTurn(false);
        boardPanel.repaint();
        statusLabel.setText(buildStatusText());

        if (!hasMore) {
            turnTimer.stop();
            running = false;
            statusLabel.setText(buildStatusText() + " — koniec (wszystkie rybki zginęły)");
        }
        updateControls();
    }

    private void pushHistory() {
        if (history.size() >= MAX_HISTORY) {
            history.removeLast();
        }
        history.push(SimulationSnapshot.capture(simulation));
    }

    private void stepBack() {
        if (history.isEmpty() || running) {
            return;
        }
        history.pop().restore(simulation);
        boardPanel.setOcean(simulation.getOcean());
        statusLabel.setText(buildStatusText());
        updateControls();
    }

    private void promptReset() {
        if (running) {
            turnTimer.stop();
            running = false;
        }

        Object[] options = {"Nowy seed", "Ten sam seed (" + currentSeed + ")", "Anuluj"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Czy zresetować symulację?",
                "Resetuj symulację",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) return;

        long newSeed = (choice == 0)
                ? System.currentTimeMillis()
                : currentSeed;

        applyReset(newSeed);
    }

    private void applyReset(long seed) {
        currentSeed = seed;
        simulation = simulationFactory.apply(seed);
        history.clear();
        boardPanel.setOcean(simulation.getOcean());

        setTitle("Symulacja oceanu — seed " + seed);
        statusLabel.setText(buildStatusText());
        updateControls();
        repaint();
    }

    private String buildStatusText() {
        Ocean ocean = simulation.getOcean();
        return String.format(
                "Tura: %d  |  Rybki: %d  |  Rekiny: %d  |  Plankton: %d",
                simulation.getTurnNumber(),
                ocean.getLiveFish().size(),
                ocean.getLiveSharks().size(),
                ocean.getLivePlankton().size());
    }

    private void updateControls() {
        boolean finished = simulation.isFinished();
        startPauseButton.setEnabled(!finished);
        startPauseButton.setText(running ? "Pauza" : "Start");
        stepButton.setEnabled(!finished && !running);
        stepBackButton.setEnabled(!history.isEmpty() && !running);
        resetButton.setEnabled(!running);
    }

    public static void show(LongFunction<Simulation> factory, long seed) {
        SwingUtilities.invokeLater(() -> {
            SimulationWindow window = new SimulationWindow(factory, seed);
            window.setVisible(true);
        });
    }

    public static void show(Simulation simulation, long seed) {
        show(s -> new Simulation(new Random(s), false), seed);
    }
}
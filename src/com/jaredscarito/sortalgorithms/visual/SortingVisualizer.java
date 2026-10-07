package com.jaredscarito.sortalgorithms.visual;

import com.jaredscarito.sortalgorithms.searchs.binarysearch.BinarySearch;
import com.jaredscarito.sortalgorithms.searchs.seqsearch.SeqSearch;
import com.jaredscarito.sortalgorithms.sorts.bubblesort.BubbleSort;
import com.jaredscarito.sortalgorithms.sorts.heapsort.HeapSort;
import com.jaredscarito.sortalgorithms.sorts.insertionsort.InsertionSort;
import com.jaredscarito.sortalgorithms.sorts.mergesort.MergeSort;
import com.jaredscarito.sortalgorithms.sorts.quicksort.QuickSort;
import com.jaredscarito.sortalgorithms.sorts.selectionsort.SelectionSort;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;

public final class SortingVisualizer extends JFrame {
    private static final int[] DEMO = {8, 3, 7, 1, 9, 2, 6, 4, 10, 5};
    private static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font CODE_FONT = new Font("Consolas", Font.PLAIN, 13);

    private final JTextArea guideArea = textArea(UI_FONT, true);
    private final JTextArea explanation = textArea(new Font("Segoe UI", Font.PLAIN, 16), true);
    private final JLabel stepTitle = new JLabel(" ");
    private final JLabel stepLabel = new JLabel("Step 1 of 1");
    private final JLabel countsLabel = new JLabel("0 comparisons   0 moves");
    private final JLabel originLabel = new JLabel(" ");
    private final JLabel findLabel = new JLabel("Find");
    private final JSlider scrubber = new JSlider(0, 1, 0);
    private final JSlider sizeSlider = new JSlider(1, 20, DEMO.length);
    private final JSlider speedSlider = new JSlider(1, 100, 62);
    private final JTextField customField = new JTextField(18);
    private final JSpinner targetSpinner = new JSpinner(new SpinnerNumberModel(9, -100000, 100000, 1));
    private final FlatButton playButton = new FlatButton("Play");
    private final BarPanel barPanel = new BarPanel();
    private final HeapTreePanel heapPanel = new HeapTreePanel();
    private final Choice[] choices = new Choice[Algorithm.values().length];
    private final Timer timer;

    private int[] current = DEMO.clone();
    private List<Frame> frames = java.util.Collections.emptyList();
    private int index;
    private boolean updating;
    private Algorithm selected = Algorithm.BUBBLE;

    public SortingVisualizer() {
        super("Sorting and Searching Visualizer");
        timer = new Timer(delay(), new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                advance();
            }
        });
        timer.setInitialDelay(delay());
        playButton.setPrimary(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1040, 720));
        setSize(1280, 900);
        setLocationRelativeTo(null);
        setContentPane(buildUi());
        bindKeys();
        wireListeners();
        showNumbers();
        select(Algorithm.BUBBLE, false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                toFront();
                requestFocusInWindow();
                play();
            }
        });
    }

    private JPanel buildUi() {
        JPanel root = new JPanel(new BorderLayout(16, 12));
        root.setBackground(Palette.BG);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
        root.add(header(), BorderLayout.NORTH);
        root.add(sidebar(), BorderLayout.WEST);
        root.add(chart(), BorderLayout.CENTER);
        root.add(controls(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel header() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Palette.BG);
        JLabel title = new JLabel("Sorting and Searching");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Palette.TEXT);
        JLabel subtitle = new JLabel("Watch one decision at a time. Orange is the comparison. Green is finished.");
        subtitle.setFont(UI_FONT);
        subtitle.setForeground(Palette.MUTED);
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        return header;
    }

    private JPanel sidebar() {
        JPanel algorithms = new JPanel();
        algorithms.setLayout(new BoxLayout(algorithms, BoxLayout.Y_AXIS));
        algorithms.setBackground(Palette.BG);
        algorithms.add(groupLabel("Sorts"));
        int slot = 0;
        for (Algorithm algorithm : Algorithm.values()) {
            if (algorithm == Algorithm.SEQUENTIAL) {
                algorithms.add(Box.createVerticalStrut(10));
                algorithms.add(groupLabel("Searches"));
            }
            Choice choice = new Choice(algorithm);
            choices[slot++] = choice;
            algorithms.add(choice);
            algorithms.add(Box.createVerticalStrut(2));
        }

        JPanel guide = new JPanel(new BorderLayout(0, 8));
        guide.setBackground(Palette.BG);
        guide.add(caption("How this one works"), BorderLayout.NORTH);
        guide.add(scroll(guideArea), BorderLayout.CENTER);

        JPanel sidebar = new JPanel(new BorderLayout(0, 12));
        sidebar.setBackground(Palette.BG);
        sidebar.setPreferredSize(new Dimension(248, 100));
        sidebar.add(algorithms, BorderLayout.NORTH);
        sidebar.add(guide, BorderLayout.CENTER);
        return sidebar;
    }

    private JPanel chart() {
        stepTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        stepTitle.setForeground(Palette.TEXT);
        explanation.setRows(3);

        JPanel heading = new JPanel(new BorderLayout(0, 4));
        heading.setBackground(Palette.BG);
        heading.add(stepTitle, BorderLayout.NORTH);
        heading.add(explanation, BorderLayout.CENTER);

        JPanel chart = new JPanel(new BorderLayout(0, 10));
        chart.setBackground(Palette.BG);
        chart.add(heading, BorderLayout.NORTH);
        chart.add(barPanel, BorderLayout.CENTER);
        chart.add(heapPanel, BorderLayout.SOUTH);
        heapPanel.setVisible(false);
        barPanel.setPreferredSize(new Dimension(700, 320));
        return chart;
    }

    private JPanel controls() {
        styleField(customField);
        findLabel.setFont(UI_FONT);
        findLabel.setForeground(Palette.MUTED);
        stepLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        stepLabel.setForeground(Palette.TEXT);
        countsLabel.setFont(UI_FONT);
        countsLabel.setForeground(Palette.MUTED);
        originLabel.setFont(CODE_FONT);
        originLabel.setForeground(Palette.MUTED);
        scrubber.setOpaque(false);
        sizeSlider.setOpaque(false);
        speedSlider.setOpaque(false);
        scrubber.setToolTipText("Drag to any step");
        sizeSlider.setToolTipText("How many random numbers to generate");
        speedSlider.setToolTipText("Move right to play faster");
        customField.setToolTipText("Whole numbers, separated by commas or spaces");

        FlatButton restart = new FlatButton("Restart");
        FlatButton back = new FlatButton("Back");
        FlatButton next = new FlatButton("Next");
        FlatButton finish = new FlatButton("End");
        FlatButton random = new FlatButton("Shuffle");
        FlatButton apply = new FlatButton("Use these");
        restart.onClick(new Runnable() {
            public void run() {
                showIndex(0);
            }
        });
        back.onClick(new Runnable() {
            public void run() {
                showIndex(index - 1);
            }
        });
        next.onClick(new Runnable() {
            public void run() {
                showIndex(index + 1);
            }
        });
        finish.onClick(new Runnable() {
            public void run() {
                showIndex(frames.size() - 1);
            }
        });
        playButton.onClick(new Runnable() {
            public void run() {
                togglePlay();
            }
        });
        random.onClick(new Runnable() {
            public void run() {
                generateRandom();
            }
        });
        apply.onClick(new Runnable() {
            public void run() {
                applyCustom();
            }
        });
        customField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                applyCustom();
            }
        });

        JPanel stats = flow();
        stats.add(stepLabel);
        stats.add(countsLabel);

        JPanel buttons = flow();
        buttons.add(restart);
        buttons.add(back);
        buttons.add(playButton);
        buttons.add(next);
        buttons.add(finish);
        buttons.add(Box.createHorizontalStrut(8));
        buttons.add(random);

        JPanel options = flow();
        options.add(inline("Size", sizeSlider));
        options.add(inline("Speed", speedSlider));
        options.add(findLabel);
        options.add(targetSpinner);

        JPanel numbers = new JPanel(new BorderLayout(8, 0));
        numbers.setOpaque(false);
        numbers.add(customField, BorderLayout.CENTER);
        numbers.add(apply, BorderLayout.EAST);

        JLabel hint = new JLabel("Space plays and pauses. Left and right arrows step. Drag the bar under the chart to any moment.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(Palette.MUTED);

        JPanel south = new JPanel(new GridBagLayout());
        south.setBackground(Palette.BG);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(3, 0, 3, 0);
        constraints.gridy = 0;
        south.add(originLabel, constraints);
        constraints.gridy = 1;
        south.add(stats, constraints);
        constraints.gridy = 2;
        south.add(scrubber, constraints);
        constraints.gridy = 3;
        south.add(buttons, constraints);
        constraints.gridy = 4;
        south.add(options, constraints);
        constraints.gridy = 5;
        south.add(numbers, constraints);
        constraints.gridy = 6;
        south.add(hint, constraints);
        return south;
    }

    private void wireListeners() {
        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (!sizeSlider.getValueIsAdjusting() && !updating) {
                    generateRandom();
                }
            }
        });
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                int nextDelay = delay();
                timer.setDelay(nextDelay);
                timer.setInitialDelay(nextDelay);
            }
        });
        scrubber.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (updating || frames.isEmpty()) {
                    return;
                }
                pause();
                index = scrubber.getValue();
                showFrame();
            }
        });
        targetSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                if (!updating && selected.search) {
                    rebuild(true);
                }
            }
        });
        barPanel.setBarClick(new BarPanel.BarClick() {
            @Override
            public void onBar(int barIndex, int value) {
                if (!selected.search) {
                    return;
                }
                updating = true;
                targetSpinner.setValue(value);
                updating = false;
                rebuild(true);
            }
        });
    }

    private void bindKeys() {
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "toggle", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!typing()) {
                    togglePlay();
                }
            }
        });
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "next", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!typing()) {
                    showIndex(index + 1);
                }
            }
        });
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "back", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!typing()) {
                    showIndex(index - 1);
                }
            }
        });
    }

    private void bindKey(KeyStroke stroke, String name, AbstractAction action) {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, name);
        getRootPane().getActionMap().put(name, action);
    }

    private boolean typing() {
        Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        return owner instanceof JTextField;
    }

    private void select(Algorithm algorithm, boolean autoplay) {
        selected = algorithm;
        for (Choice choice : choices) {
            if (choice != null) {
                choice.setChosen(choice.algorithm == algorithm);
            }
        }
        rebuild(autoplay);
    }

    private void generateRandom() {
        int[] values = new int[sizeSlider.getValue()];
        Random random = new Random();
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextInt(90) + 5;
        }
        setArray(values, true);
    }

    private void applyCustom() {
        try {
            int[] values = parseCustom(customField.getText());
            updating = true;
            if (values.length >= sizeSlider.getMinimum() && values.length <= sizeSlider.getMaximum()) {
                sizeSlider.setValue(values.length);
            }
            updating = false;
            setArray(values, true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Use whole numbers separated by commas or spaces. For example: 8, 3, 7, 1",
                    "Those numbers could not be read",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Too many numbers", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setArray(int[] values, boolean autoplay) {
        current = values.clone();
        updating = true;
        showNumbers();
        targetSpinner.setValue(Integer.valueOf(current[current.length / 2]));
        updating = false;
        rebuild(autoplay);
    }

    private void rebuild(boolean autoplay) {
        pause();
        int[] data = current.clone();
        int target = ((Number) targetSpinner.getValue()).intValue();
        switch (selected) {
            case SELECTION:
                frames = SelectionSort.sort(data);
                break;
            case INSERTION:
                frames = InsertionSort.sort(data);
                break;
            case MERGE:
                frames = MergeSort.sort(data);
                break;
            case QUICK:
                frames = QuickSort.sort(data);
                break;
            case HEAP:
                frames = HeapSort.sort(data);
                break;
            case SEQUENTIAL:
                frames = SeqSearch.search(data, target);
                break;
            case BINARY:
                frames = BinarySearch.search(data, target);
                break;
            case BUBBLE:
            default:
                frames = BubbleSort.sort(data);
                break;
        }
        index = 0;
        guideArea.setText(selected.summary + "\n\n" + selected.cost + "\n\n" + selected.pseudocode);
        guideArea.setCaretPosition(0);
        findLabel.setEnabled(selected.search);
        targetSpinner.setEnabled(selected.search);
        barPanel.setGuide(selected.focusLabel, selected.specialLabel, selected.search);
        barPanel.setBarsClickable(selected.search);
        heapPanel.setVisible(selected == Algorithm.HEAP);
        revalidate();
        showFrame();
        if (autoplay) {
            play();
        }
    }

    private void showIndex(int next) {
        if (frames.isEmpty()) {
            return;
        }
        pause();
        index = Math.max(0, Math.min(frames.size() - 1, next));
        showFrame();
    }

    private void showFrame() {
        if (frames.isEmpty()) {
            return;
        }
        Frame frame = frames.get(index);
        barPanel.setFrame(frame);
        heapPanel.setFrame(frame);
        stepTitle.setText(selected.displayName + "  ·  " + frame.title);
        explanation.setText(frame.detail);
        explanation.setCaretPosition(0);
        originLabel.setText("Started as  " + join(current));
        stepLabel.setText("Step " + (index + 1) + " of " + frames.size());
        countsLabel.setText(frame.comparisons + " comparisons    " + frame.moves + " moves");
        updating = true;
        scrubber.setMaximum(Math.max(0, frames.size() - 1));
        scrubber.setValue(index);
        updating = false;
    }

    private void advance() {
        if (index >= frames.size() - 1) {
            pause();
            return;
        }
        index++;
        showFrame();
    }

    private void togglePlay() {
        if (timer.isRunning()) {
            pause();
        } else {
            play();
        }
    }

    private void play() {
        if (frames.size() <= 1) {
            return;
        }
        if (index >= frames.size() - 1) {
            index = 0;
            showFrame();
        }
        int nextDelay = delay();
        timer.setDelay(nextDelay);
        timer.setInitialDelay(index == 0 ? Math.max(nextDelay, 700) : nextDelay);
        timer.start();
        playButton.setLabel("Pause");
    }

    private void pause() {
        timer.stop();
        playButton.setLabel("Play");
    }

    private int delay() {
        return 30 + (100 - speedSlider.getValue()) * 14;
    }

    private static int[] parseCustom(String text) {
        String trimmed = text == null ? "" : text.trim();
        if (trimmed.length() == 0) {
            throw new NumberFormatException("empty");
        }
        String[] parts = trimmed.split("[,\\s]+");
        if (parts.length > 20) {
            throw new IllegalArgumentException("Use 20 numbers or fewer so each bar stays easy to see.");
        }
        int[] values = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Integer.parseInt(parts[i]);
        }
        return values;
    }

    private void showNumbers() {
        customField.setText(join(current));
        customField.setCaretPosition(0);
    }

    private static String join(int[] values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(values[i]);
        }
        return builder.toString();
    }

    private static JLabel groupLabel(String text) {
        JLabel label = caption(text.toUpperCase());
        label.setBorder(BorderFactory.createEmptyBorder(0, 4, 6, 0));
        return label;
    }

    private static JLabel caption(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(Palette.MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static JTextArea textArea(Font font, boolean wrap) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(wrap);
        area.setWrapStyleWord(true);
        area.setFont(font);
        area.setBackground(Palette.PANEL);
        area.setForeground(Palette.TEXT);
        area.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        area.setOpaque(true);
        return area;
    }

    private static JScrollPane scroll(JComponent view) {
        JScrollPane pane = new JScrollPane(view);
        pane.setBorder(BorderFactory.createLineBorder(Palette.LINE));
        pane.getViewport().setBackground(Palette.PANEL);
        pane.setOpaque(true);
        return pane;
    }

    private static JPanel flow() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        return panel;
    }

    private static JPanel inline(String name, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panel.setOpaque(false);
        JLabel label = new JLabel(name);
        label.setFont(UI_FONT);
        label.setForeground(Palette.MUTED);
        component.setPreferredSize(new Dimension(120, 28));
        panel.add(label);
        panel.add(component);
        return panel;
    }

    private static void styleField(JTextField field) {
        field.setFont(CODE_FONT);
        field.setPreferredSize(new Dimension(200, 32));
        field.setBackground(Palette.PANEL);
        field.setForeground(Palette.TEXT);
        field.setCaretColor(Palette.TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.LINE),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private final class Choice extends JComponent {
        private final Algorithm algorithm;
        private boolean chosen;
        private boolean hover;

        private Choice(Algorithm algorithm) {
            this.algorithm = algorithm;
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            setPreferredSize(new Dimension(220, 32));
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent event) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent event) {
                    hover = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent event) {
                    select(algorithm, true);
                }
            });
        }

        private void setChosen(boolean chosen) {
            this.chosen = chosen;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (chosen) {
                g2.setColor(Palette.SELECTED);
            } else if (hover) {
                g2.setColor(Palette.PANEL);
            } else {
                g2.setColor(Palette.BG);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            if (chosen) {
                g2.setColor(Palette.BAR);
                g2.fillRoundRect(0, 6, 3, getHeight() - 12, 3, 3);
            }
            g2.setFont(UI_FONT);
            g2.setColor(Palette.TEXT);
            g2.drawString(algorithm.displayName, 14, 21);
            String kind = algorithm.search ? "Search" : "Sort";
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(Palette.MUTED);
            int kindWidth = g2.getFontMetrics().stringWidth(kind);
            g2.drawString(kind, getWidth() - kindWidth - 10, 21);
            g2.dispose();
        }
    }
}

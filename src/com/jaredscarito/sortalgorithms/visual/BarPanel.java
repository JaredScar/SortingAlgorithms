package com.jaredscarito.sortalgorithms.visual;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class BarPanel extends JPanel {
    interface BarClick {
        void onBar(int index, int value);
    }

    private Frame frame;
    private Rectangle[] boxes = new Rectangle[0];
    private boolean barsClickable;
    private BarClick barClick;
    private String focusLabel = "Comparing";
    private String specialLabel;
    private boolean search;

    BarPanel() {
        setOpaque(true);
        setBackground(Palette.BG);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (!barsClickable || barClick == null || frame == null) {
                    return;
                }
                int index = indexAt(event.getPoint());
                if (index >= 0) {
                    barClick.onBar(index, frame.values[index]);
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                boolean overBar = barsClickable && indexAt(event.getPoint()) >= 0;
                setCursor(overBar ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });
    }

    void setFrame(Frame frame) {
        this.frame = frame;
        repaint();
    }

    void setGuide(String focusLabel, String specialLabel, boolean search) {
        this.focusLabel = focusLabel;
        this.specialLabel = specialLabel;
        this.search = search;
        repaint();
    }

    void setBarsClickable(boolean barsClickable) {
        this.barsClickable = barsClickable;
        if (!barsClickable) {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    void setBarClick(BarClick barClick) {
        this.barClick = barClick;
    }

    private int indexAt(Point point) {
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] != null && boxes[i].contains(point)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Palette.BG);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Palette.PANEL);
        g2.fillRoundRect(0, 0, Math.max(0, getWidth() - 1), Math.max(0, getHeight() - 1), 16, 16);

        if (frame == null || frame.values.length == 0) {
            g2.setColor(Palette.MUTED);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2.drawString("Add some numbers to begin.", 28, getHeight() / 2);
            g2.dispose();
            return;
        }

        int n = frame.values.length;
        int left = 24;
        int right = 24;
        int top = 46;
        int bottom = n <= 16 ? 58 : 36;
        int base = getHeight() - bottom;
        int maxHeight = Math.max(12, base - top);
        int gap = n > 18 ? 3 : 10;
        int usable = Math.max(n, getWidth() - left - right - gap * Math.max(0, n - 1));
        int barWidth = Math.max(4, usable / n);
        int min = frame.values[0];
        int max = frame.values[0];
        for (int value : frame.values) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        int span = Math.max(1, max - min);
        boxes = new Rectangle[n];

        paintRangeBand(g2, n, left, barWidth, gap, top, base);
        paintRelation(g2);
        paintCompareBridge(g2, n, left, barWidth, gap, base, min, span, maxHeight);

        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Palette.LINE);
        g2.drawLine(left, base, getWidth() - right, base);

        boolean showValues = n <= 20 && barWidth >= 14;
        boolean showTags = n <= 14 && barWidth >= 28;
        for (int i = 0; i < n; i++) {
            int height = 10 + (int) Math.round((frame.values[i] - min) / (double) span * (maxHeight - 10));
            int x = left + i * (barWidth + gap);
            int y = base - height;
            Color color = colorFor(i);
            g2.setColor(color);
            int arc = Math.min(12, barWidth);
            g2.fillRoundRect(x, y, barWidth, height, arc, arc);
            boxes[i] = new Rectangle(x - gap / 2, 8, barWidth + gap, Math.max(1, getHeight() - 16));

            if (showValues) {
                drawValue(g2, i, x, y, barWidth, height, color);
            }
            drawIndex(g2, i, x, barWidth, base);
            if (showTags) {
                drawTag(g2, i, x, barWidth, base + 28);
            }
        }
        g2.dispose();
    }

    private void paintRangeBand(Graphics2D g2, int n, int left, int barWidth, int gap, int top, int base) {
        boolean partial = frame.rangeLow > 0 || frame.rangeHigh < n - 1;
        if (!partial || frame.rangeHigh < frame.rangeLow) {
            return;
        }
        int low = Math.max(0, frame.rangeLow);
        int high = Math.min(n - 1, frame.rangeHigh);
        int x = left + low * (barWidth + gap) - 6;
        int end = left + high * (barWidth + gap) + barWidth + 6;
        g2.setColor(new Color(99, 162, 255, 36));
        g2.fillRoundRect(x, top - 8, Math.max(8, end - x), base - top + 16, 12, 12);
    }

    private void paintRelation(Graphics2D g2) {
        String text = relationText();
        if (text == null) {
            return;
        }
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics metrics = g2.getFontMetrics();
        int width = metrics.stringWidth(text) + 18;
        int x = Math.max(12, getWidth() - width - 16);
        int y = 10;
        g2.setColor(Palette.BG);
        g2.fillRoundRect(x, y, width, 22, 11, 11);
        g2.setColor(Palette.FOCUS);
        g2.drawString(text, x + 9, y + 16);
    }

    private String relationText() {
        if (frame.focus.length == 0 && frame.special < 0 && frame.found < 0 && search) {
            return "Click a bar to search for it";
        }
        if (frame.focus.length >= 2) {
            int first = frame.focus[0];
            int second = frame.focus[1];
            if (first > second) {
                int swap = first;
                first = second;
                second = swap;
            }
            if (first < 0 || second >= frame.values.length) {
                return null;
            }
            String title = frame.title;
            if (title.startsWith("Swap") || title.startsWith("Move") || title.startsWith("Take")) {
                return null;
            }
            int left = frame.values[first];
            int right = frame.values[second];
            String sign = left < right ? " < " : left > right ? " > " : " = ";
            return left + sign + right;
        }
        if (frame.found >= 0 && frame.found < frame.values.length) {
            return "Found " + frame.values[frame.found];
        }
        if (specialLabel != null && frame.special >= 0 && frame.special < frame.values.length) {
            return specialLabel + " " + frame.values[frame.special];
        }
        return null;
    }

    private void paintCompareBridge(Graphics2D g2, int n, int left, int barWidth, int gap, int base, int min, int span, int maxHeight) {
        if (frame.focus.length < 2) {
            return;
        }
        int first = frame.focus[0];
        int second = frame.focus[1];
        if (first < 0 || second < 0 || first >= n || second >= n || first == second) {
            return;
        }
        int x1 = left + first * (barWidth + gap) + barWidth / 2;
        int x2 = left + second * (barWidth + gap) + barWidth / 2;
        int h1 = 10 + (int) Math.round((frame.values[first] - min) / (double) span * (maxHeight - 10));
        int h2 = 10 + (int) Math.round((frame.values[second] - min) / (double) span * (maxHeight - 10));
        int y = Math.min(base - h1, base - h2) - 16;
        g2.setColor(Palette.FOCUS);
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawLine(x1, y, x2, y);
        g2.fillOval(x1 - 3, y - 3, 6, 6);
        g2.fillOval(x2 - 3, y - 3, 6, 6);
    }

    private Color colorFor(int index) {
        boolean inRange = index >= frame.rangeLow && index <= frame.rangeHigh;
        Color color = inRange ? Palette.BAR : Palette.DIM;
        if (frame.isSettled(index)) {
            color = Palette.SETTLED;
        }
        if (frame.isFocus(index)) {
            color = Palette.FOCUS;
        }
        if (index == frame.special) {
            color = Palette.SPECIAL;
        }
        if (index == frame.found) {
            color = Palette.FOUND;
        }
        return color;
    }

    private void drawValue(Graphics2D g2, int index, int x, int y, int barWidth, int height, Color barColor) {
        String text = String.valueOf(frame.values[index]);
        g2.setFont(new Font("Segoe UI", Font.BOLD, barWidth >= 32 ? 13 : 11));
        FontMetrics metrics = g2.getFontMetrics();
        boolean inside = height > metrics.getHeight() + 8 && metrics.stringWidth(text) < barWidth - 4;
        int textX = x + (barWidth - metrics.stringWidth(text)) / 2;
        if (inside) {
            g2.setColor(Palette.INK);
            g2.drawString(text, textX, y + metrics.getAscent() + 4);
        } else {
            boolean quiet = barColor == Palette.DIM;
            g2.setColor(quiet ? Palette.MUTED : Palette.TEXT);
            g2.drawString(text, textX, y - 6);
        }
    }

    private void drawIndex(Graphics2D g2, int index, int x, int barWidth, int base) {
        String text = String.valueOf(index);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        FontMetrics metrics = g2.getFontMetrics();
        g2.setColor(Palette.MUTED);
        g2.drawString(text, x + (barWidth - metrics.stringWidth(text)) / 2, base + 16);
    }

    private void drawTag(Graphics2D g2, int index, int x, int barWidth, int y) {
        String tag = tagFor(index);
        if (tag == null) {
            return;
        }
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        FontMetrics metrics = g2.getFontMetrics();
        int width = metrics.stringWidth(tag);
        int textX = x + (barWidth - width) / 2;
        g2.setColor(colorFor(index));
        g2.drawString(tag, textX, y);
    }

    private String tagFor(int index) {
        if (index == frame.found) {
            return "found";
        }
        if (index == frame.special && specialLabel != null) {
            return specialLabel.toLowerCase();
        }
        if (frame.isFocus(index) && focusLabel != null) {
            return focusLabel.toLowerCase();
        }
        return null;
    }
}

package com.jaredscarito.sortalgorithms.visual;

import javax.swing.JPanel;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

final class HeapTreePanel extends JPanel {
    private Frame frame;

    HeapTreePanel() {
        setOpaque(true);
        setBackground(Palette.BG);
        setPreferredSize(new java.awt.Dimension(100, 168));
    }

    void setFrame(Frame frame) {
        this.frame = frame;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Palette.BG);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Palette.PANEL);
        g2.fillRoundRect(8, 0, Math.max(0, getWidth() - 16), Math.max(0, getHeight() - 8), 18, 18);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2.setColor(Palette.TEXT);
        g2.drawString("Same array, drawn as a heap", 24, 22);

        if (frame == null || frame.values.length == 0) {
            g2.dispose();
            return;
        }

        int heapSize = frame.values.length;
        for (int i = 0; i < frame.settled.length && i < frame.values.length; i++) {
            if (frame.settled[i]) {
                heapSize = i;
                break;
            }
        }
        if (heapSize <= 0) {
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.setColor(Palette.MUTED);
            g2.drawString("The heap is empty. Every value has been moved into its final place.", 24, 78);
            g2.dispose();
            return;
        }

        int levels = 1;
        while ((1 << levels) - 1 < heapSize) {
            levels++;
        }
        int radius = heapSize > 15 ? 12 : 16;
        for (int i = 0; i < heapSize; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < heapSize) {
                drawEdge(g2, i, left, heapSize, levels, radius);
            }
            if (right < heapSize) {
                drawEdge(g2, i, right, heapSize, levels, radius);
            }
        }
        for (int i = 0; i < heapSize; i++) {
            drawNode(g2, i, heapSize, levels, radius);
        }
        g2.dispose();
    }

    private void drawEdge(Graphics2D g2, int parent, int child, int heapSize, int levels, int radius) {
        int x1 = xAt(parent, levels);
        int y1 = yAt(parent, levels);
        int x2 = xAt(child, levels);
        int y2 = yAt(child, levels);
        boolean hot = parent == frame.special || child == frame.special || frame.isFocus(parent) || frame.isFocus(child);
        g2.setColor(hot ? Palette.FOCUS : Palette.LINE);
        g2.drawLine(x1, y1, x2, y2);
    }

    private void drawNode(Graphics2D g2, int index, int heapSize, int levels, int radius) {
        int x = xAt(index, levels);
        int y = yAt(index, levels);
        java.awt.Color color = Palette.BAR;
        if (frame.isFocus(index)) {
            color = Palette.FOCUS;
        }
        if (index == frame.special) {
            color = Palette.SPECIAL;
        }
        g2.setColor(color);
        g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        String text = String.valueOf(frame.values[index]);
        g2.setFont(new Font("Segoe UI", Font.BOLD, radius > 14 ? 12 : 10));
        FontMetrics metrics = g2.getFontMetrics();
        g2.setColor(Palette.BG);
        if (metrics.stringWidth(text) <= radius * 2 - 4) {
            g2.drawString(text, x - metrics.stringWidth(text) / 2, y + metrics.getAscent() / 2 - 2);
        } else {
            g2.setColor(Palette.TEXT);
            g2.drawString(text, x - metrics.stringWidth(text) / 2, y - radius - 4);
        }
        if (heapSize <= 10) {
            String indexText = "i " + index;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            FontMetrics indexMetrics = g2.getFontMetrics();
            g2.setColor(Palette.MUTED);
            g2.drawString(indexText, x - indexMetrics.stringWidth(indexText) / 2, y + radius + 12);
        }
    }

    private int xAt(int index, int levels) {
        int level = 31 - Integer.numberOfLeadingZeros(index + 1);
        int offset = index - ((1 << level) - 1);
        int nodes = 1 << level;
        int usable = Math.max(nodes, getWidth() - 64);
        return 32 + (int) ((offset + 0.5) * (usable / (double) nodes));
    }

    private int yAt(int index, int levels) {
        int level = 31 - Integer.numberOfLeadingZeros(index + 1);
        int top = 48;
        int usable = Math.max(levels, getHeight() - top - 24);
        return top + (int) ((level + 0.5) * (usable / (double) levels));
    }
}

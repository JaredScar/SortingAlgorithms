package com.jaredscarito.sortalgorithms.visual;

import javax.swing.JComponent;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

final class FlatButton extends JComponent {
    private String label;
    private boolean primary;
    private boolean hover;
    private Runnable action;

    FlatButton(String label) {
        this.label = label;
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFocusable(false);
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
                if (action != null && isEnabled()) {
                    action.run();
                }
            }
        });
    }

    void setLabel(String label) {
        this.label = label;
        repaint();
    }

    void setPrimary(boolean primary) {
        this.primary = primary;
        repaint();
    }

    void onClick(Runnable action) {
        this.action = action;
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        FontMetrics metrics = getFontMetrics(getFont());
        int width = Math.max(76, metrics.stringWidth(label) + 28);
        return new java.awt.Dimension(width, 34);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (primary) {
            g2.setColor(hover ? Palette.TEXT : Palette.BAR);
        } else if (hover) {
            g2.setColor(Palette.SELECTED);
        } else {
            g2.setColor(Palette.PANEL);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        if (!primary) {
            g2.setColor(Palette.LINE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        }
        g2.setFont(getFont());
        FontMetrics metrics = g2.getFontMetrics();
        g2.setColor(primary ? Palette.INK : Palette.TEXT);
        int x = (getWidth() - metrics.stringWidth(label)) / 2;
        int y = (getHeight() + metrics.getAscent() - metrics.getDescent()) / 2;
        g2.drawString(label, x, y);
        g2.dispose();
    }
}

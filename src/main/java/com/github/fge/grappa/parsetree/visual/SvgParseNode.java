package com.github.fge.grappa.parsetree.visual;

import com.github.chrisbrenton.grappa.parsetree.nodes.ParseNode;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public final class SvgParseNode
{
    private static final int LEFTMARGIN = 5;
    private static final int TOPMARGIN = 5;
    private static final int RIGHTMARGIN = 5;
    private static final int BOTTOMARGIN = 5;

    private final Graphics2D graphics;
    private final String text;

    private final int rwidth;
    private final int rheight;
    private final int textXOffset;
    private final int textYOffset;

    @SuppressWarnings("ObjectToString")
    public SvgParseNode(final Graphics2D graphics, final ParseNode node)
    {
        this.graphics = Objects.requireNonNull(graphics);
        text = Objects.requireNonNull(node).toString();

        /*
         * First, get the bounds of the text
         */
        final Font font = graphics.getFont();
        final FontMetrics metrics = graphics.getFontMetrics(font);
        final Rectangle2D bounds = metrics.getStringBounds(text, graphics);
        final int boundsHeight = (int) bounds.getHeight();

        /*
         * The width of the target rectangle is that of the bounds width, plus
         * both left and right margin on the x axis.
         */
        //rwidth = (int) bounds.getWidth() + LEFTMARGIN + RIGHTMARGIN;

        /*
         * Well, that was the plan. However, the bounds are completely off, and
         * the larger the text, the more "off" it is. See here:
         *
         * http://stackoverflow.com/q/35975116/1093528
         *
         * So, we use the following instead... Which seems to work OK. Seems.
         */
        rwidth = metrics.stringWidth(text) + LEFTMARGIN + RIGHTMARGIN;

        /*
         * The height is that of the bounds height, plus both up and down
         * margins on the y avis
         */
        rheight = boundsHeight + TOPMARGIN + BOTTOMARGIN;

        /*
         * The x offset of the text is that of the left x margin
         */
        textXOffset = LEFTMARGIN;

        /*
         * The y offset is that of half of half the bounds height plus the y
         * up margin
         */
        textYOffset = rheight / 2 + TOPMARGIN;
    }

    public int getWidth()
    {
        return rwidth;
    }

    public int getHeight()
    {
        return rheight;
    }

    public Point getTopAttachPoint(final int xStart, final int yStart)
    {
        return new Point(xStart + rwidth / 2, yStart);
    }

    public Point getDownAttachPoint(final int xStart, final int yStart)
    {
        return new Point(xStart + rwidth / 2, yStart + rheight);
    }

    public void render(final int xStart, final int yStart)
    {
        final Shape rect = new Rectangle(xStart, yStart, rwidth, rheight);
        graphics.draw(rect);
        graphics.drawString(text, xStart + textXOffset, yStart + textYOffset);
    }
}

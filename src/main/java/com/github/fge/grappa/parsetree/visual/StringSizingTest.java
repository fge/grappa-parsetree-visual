package com.github.fge.grappa.parsetree.visual;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class StringSizingTest
{
    private static final String SAMPLESTRING = "foobarbaz";
    private static final int X_OFFSET = 10;
    private static final int Y_OFFSET = 10;
    private static final int VERTICAL_BOUND = 10;
    private static final int HORIZONTAL_BOUND = 10;
    private static final Path OUTFILE;

    static {
        final String s = System.getProperty("java.io.tmpdir");
        if (s == null)
            throw new ExceptionInInitializerError("java.io.tmpdir not defined");

        OUTFILE = Paths.get(s, "t.svg");
    }

    public void paint(final Graphics2D g2d)
    {
        final Font font = g2d.getFont();
        final FontMetrics metrics = g2d.getFontMetrics(font);
        final Rectangle2D bounds = metrics.getStringBounds(SAMPLESTRING, g2d);
        final int rwidth = (int) bounds.getWidth() + VERTICAL_BOUND;
        final int rheight = (int) bounds.getHeight() + HORIZONTAL_BOUND;
        final Rectangle2D rect = new Rectangle(X_OFFSET, Y_OFFSET, rwidth,
            rheight);
        final int x = X_OFFSET + HORIZONTAL_BOUND / 2;
        final int y = rheight / 2 + Y_OFFSET + VERTICAL_BOUND / 2;
        g2d.draw(rect);
        g2d.drawString(SAMPLESTRING, x, y);
    }

    public static void main(final String[] args)
        throws IOException
    {

        // Get a DOMImplementation.
        final DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        final String svgNS = "http://www.w3.org/2000/svg";
        final Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        final SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation.
        final StringSizingTest test = new StringSizingTest();
        test.paint(svgGenerator);

        try (
            final Writer out = Files.newBufferedWriter(OUTFILE);
        ) {
            svgGenerator.stream(out, false);
        }
    }
}

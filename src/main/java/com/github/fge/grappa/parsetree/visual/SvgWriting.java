package com.github.fge.grappa.parsetree.visual;

import com.github.chrisbrenton.grappa.parsetree.nodes.ParseNode;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public final class SvgWriting
{
    private static final int XSTART = 10;
    private static final int YSTART = 10;

    private static final Path OUTFILE;

    static {
        final String s = System.getProperty("java.io.tmpdir");
        if (s == null)
            throw new ExceptionInInitializerError("java.io.tmpdir not defined");

        OUTFILE = Paths.get(s, "t2.svg");
    }

    private SvgWriting()
    {
        throw new Error("no instantiation is permitted");
    }

    public static void main(final String... args)
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

        final SvgParseNode node = new SvgParseNode(svgGenerator,
            new DummyNode());

        node.render(XSTART, YSTART);

        try (
            final Writer out = Files.newBufferedWriter(OUTFILE);
        ) {
            svgGenerator.stream(out, false);
        }
    }

    private static final class DummyNode
        extends ParseNode
    {

        private DummyNode()
        {
            super("foo", Collections.emptyList());
        }

        @Override
        public String toString()
        {
            return "I suck at graphics, I said!";
        }
    }
}

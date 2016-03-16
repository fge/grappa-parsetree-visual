package com.github.fge.grappa.parsetree.visual;

import com.github.chrisbrenton.grappa.parsetree.nodes.ParseNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public final class DotFileGenerator
    implements AutoCloseable
{
    private static final String DOT_PREFIX = "graph {\n";
    private static final String DOT_SUFFIX = "}\n";
    private static final String NODE_FMT = "node%d";
    private static final String NODE_DECL_FMT
        = "%s[label=\"%s\", shape=box]\n";
    private static final String RELATION_FMT = "%s -- %s\n";

    private final Path dotFile;
    private final BufferedWriter writer;
    private final Path svgFile;
    private final Function<ParseNode, String> nodeToLabel;
    private final AtomicInteger nodeCount = new AtomicInteger();

    public DotFileGenerator(final Path svgFile,
        final Function<ParseNode, String> nodeToLabel)
        throws IOException
    {
        dotFile = Files.createTempFile("parsetree", ".dot");
        writer = Files.newBufferedWriter(dotFile);

        this.svgFile = Objects.requireNonNull(svgFile);
        Files.deleteIfExists(svgFile);

        this.nodeToLabel = Objects.requireNonNull(nodeToLabel);
    }

    public DotFileGenerator(final Path svgFile)
        throws IOException
    {
        this(svgFile, Object::toString);
    }

    public void render(final ParseNode node)
        throws IOException
    {
        Objects.requireNonNull(node);
        writer.write(DOT_PREFIX);
        doRender(null, node);
        writer.write(DOT_SUFFIX);
    }

    private void doRender(final String parentNodeName, final ParseNode node)
        throws IOException
    {
        final int nodeNumber = nodeCount.getAndIncrement();
        final String nodeName = String.format(NODE_FMT, nodeNumber);
        final String decl = String.format(NODE_DECL_FMT, nodeName,
            nodeToLabel.apply(node).replace("\"", "\\\""));
        writer.write(decl);
        if (parentNodeName != null)
            writer.write(String.format(RELATION_FMT, parentNodeName, nodeName));

        for (final ParseNode child: node.getChildren())
            doRender(nodeName, child);
    }

    @Override
    public void close()
        throws IOException, InterruptedException
    {
        writer.close();

        final ProcessBuilder pb = new ProcessBuilder("dot", "-Tsvg",
            dotFile.toString());

        pb.redirectOutput(svgFile.toFile());

        final int retCode = pb.start().waitFor();

        Files.delete(dotFile);

        if (retCode != 0)
            throw new IllegalStateException("invocation of the dot command"
                + " ended up with non zero exit status " + retCode);

        if (!Files.exists(svgFile))
            throw new IllegalStateException("dot invocation succeeded but"
                + " target file " + svgFile + " does not exist??");
    }
}

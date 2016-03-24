package com.github.fge.grappa.parsetree.visual.example;

import com.github.chrisbrenton.grappa.parsetree.build.ParseNodeConstructorProvider;
import com.github.chrisbrenton.grappa.parsetree.build.ParseTreeBuilder;
import com.github.chrisbrenton.grappa.parsetree.node.ParseNode;
import com.github.fge.grappa.Grappa;
import com.github.fge.grappa.parsetree.visual.DotFileGenerator;
import com.github.fge.grappa.run.ParseRunner;
import com.github.fge.grappa.run.ParsingResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Example
{
    /*
     * The path to the SVG file we want to generate
     */
    private static final Path SVGFILE;

    static {
        final String tmpdir = System.getProperty("java.io.tmpdir");
        if (tmpdir == null)
            throw new ExceptionInInitializerError("java.io.tmpdir not defined");
        SVGFILE = Paths.get(tmpdir, "example.svg");
    }
    private Example()
    {
        throw new Error("no instantiation is permitted");
    }

    public static void main(final String... args)
        throws IOException, InterruptedException
    {
        final Class<SimpleExpressionParser> parserClass
            = SimpleExpressionParser.class;

        /*
         * First, create our parser instance and runner instance.
         */
        final SimpleExpressionParser parser = Grappa.createParser(parserClass);

        final ParseRunner<Void> runner = new ParseRunner<>(parser.expression());

        /*
         * Next, our parse tree builder.
         */
        final ParseNodeConstructorProvider provider
            = new ParseNodeConstructorProvider(parserClass);

        final ParseTreeBuilder<Void> listener
            = new ParseTreeBuilder<>(provider);

        /*
         * Not forgetting to register it to the runner, of course...
         */
        runner.registerListener(listener);

        /*
         * Now, parse some input.
         */
        final ParsingResult<Void> result = runner.run("2 * (4 + (5-9))");

        if (!result.isSuccess())
            throw new IllegalStateException();

        /*
         * Get the root node...
         */
        final ParseNode node = listener.getTree();

        /*
         * And render it.
         */
        try (
            final DotFileGenerator generator = new DotFileGenerator(SVGFILE);
        ) {
            generator.render(node);
        }
    }
}

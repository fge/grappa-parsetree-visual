package com.github.fge.grappa.parsetree.visual.example;


import com.github.chrisbrenton.grappa.parsetree.node.MatchTextSupplier;
import com.github.chrisbrenton.grappa.parsetree.node.ParseNode;

import java.util.List;

public final class ExpressionNode
    extends ParseNode
{
    public ExpressionNode(final MatchTextSupplier supplier,
        final List<ParseNode> children)
    {
        super(supplier, children);
    }

    @Override
    public String getValue()
    {
        return "EXPRESSION";
    }
}

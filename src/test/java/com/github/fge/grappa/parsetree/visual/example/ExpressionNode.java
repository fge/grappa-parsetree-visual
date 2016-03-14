package com.github.fge.grappa.parsetree.visual.example;

import com.github.chrisbrenton.grappa.parsetree.nodes.ParseNode;

import java.util.List;

public final class ExpressionNode
    extends ParseNode
{
    public ExpressionNode(final String value, final List<ParseNode> children)
    {
        super(value, children);
    }

    @Override
    public String toString()
    {
        return "EXPRESSION";
    }
}

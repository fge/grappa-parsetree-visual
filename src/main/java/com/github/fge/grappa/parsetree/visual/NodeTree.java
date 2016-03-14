package com.github.fge.grappa.parsetree.visual;

import com.github.chrisbrenton.grappa.parsetree.nodes.ParseNode;

import java.awt.Graphics2D;
import java.util.List;
import java.util.stream.Collectors;

public final class NodeTree
{
    private static final int LEAF_SPACING = 5;
    private static final int SUBTREE_SPACING = 10;

    private final Graphics2D graphics;

    private final SvgParseNode node;
    private final int rootWidth;

    private final List<NodeTree> children;
    private final int childrenWidth;

    public NodeTree(final Graphics2D graphics, final ParseNode node)
    {
        this.graphics = graphics;
        this.node = new SvgParseNode(graphics, node);
        rootWidth = this.node.getWidth();
        children = node.getChildren().stream()
            .map(child -> new NodeTree(graphics, child))
            .collect(Collectors.toList());
        childrenWidth = getChildrenWidth();
    }

    public SvgParseNode getNode()
    {
        return node;
    }

    public int getRootWidth()
    {
        return rootWidth;
    }

    public int getTreeWidth()
    {
        return Math.max(rootWidth, childrenWidth);
    }

    private int getChildrenWidth()
    {
        final int nrChildren = children.size();

        switch (nrChildren) {
            case 0:
                return 0;
            case 1:
                return children.get(0).childrenWidth;
        }

        final int rawChildrenWidth = children.stream()
            .mapToInt(NodeTree::getTreeWidth)
            .sum();

        final boolean allLeaves = children.stream()
            .allMatch(NodeTree::isLeaf);

        final int spacingWidth = (nrChildren - 1) *
            (allLeaves ? LEAF_SPACING : SUBTREE_SPACING);

        return rawChildrenWidth + spacingWidth;
    }

    private boolean isLeaf()
    {
        return children.isEmpty();
    }
}

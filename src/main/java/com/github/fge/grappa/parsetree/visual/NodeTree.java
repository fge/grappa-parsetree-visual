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
    private final SvgParseNode root;
    private final int rootWidth;
    private final List<NodeTree> children;

    public NodeTree(final Graphics2D graphics, final ParseNode node)
    {
        this.graphics = graphics;
        root = new SvgParseNode(graphics, node);
        rootWidth = root.getWidth();
        children = node.getChildren().stream()
            .map(child -> new NodeTree(graphics, child))
            .collect(Collectors.toList());
    }

    public int getRootWidth()
    {
        return rootWidth;
    }

    public int treeWidth()
    {
        final int nrChildren = children.size();

        switch (nrChildren) {
            case 0:
                return rootWidth;
            case 1:
                return Math.max(rootWidth, children.get(0).treeWidth());
        }

        final int rawChildrenWidth = children.stream()
            .mapToInt(NodeTree::treeWidth)
            .sum();

        final boolean allLeaves = children.stream()
            .allMatch(NodeTree::isLeaf);

        final int spacingWidth = (nrChildren - 1) *
            (allLeaves ? LEAF_SPACING : SUBTREE_SPACING);

        return Math.max(rootWidth, rawChildrenWidth + spacingWidth);
    }

    private boolean isLeaf()
    {
        return children.isEmpty();
    }
}

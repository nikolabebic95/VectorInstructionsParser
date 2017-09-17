package rs.ac.bg.etf.par.vectorinstructionparser.graph;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import rs.ac.bg.etf.par.vectorinstructionparser.instructions.Instruction;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Represents a data dependency graph (DDG)
 */
public class DataDependencyGraph {
    private final ArrayList<ArrayList<String>> matrix = new ArrayList<>();

    private static final int SIZE = 40;
    private static final int INTERVAL = 40;
    private static final int SPACE = 10;

    public DataDependencyGraph(ArrayList<Instruction> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < instructions.size(); j++) {
                matrix.get(i).add("");
            }
        }

        HashMap<String, ArrayList<Integer>> in = new HashMap<>();
        HashMap<String, Integer> out = new HashMap<>();

        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            for (String operand : instruction.inOperands()) {
                if (out.containsKey(operand)) {
                    int from = out.get(operand);
                    if (matrix.get(from).get(i).equals(""))  {
                        matrix.get(from).set(i, operand);
                    } else {
                        String newValue = matrix.get(from).get(i) + ", " + operand;
                        matrix.get(from).set(i, newValue);
                    }
                }

                if (!in.containsKey(operand)) {
                    in.put(operand, new ArrayList<>());
                }

                in.get(operand).add(i);
            }

            for (String operand : instruction.outOperands()) {
                if (in.containsKey(operand)) {
                    for (Integer from : in.get(operand)) {
                        if (matrix.get(from).get(i).equals(""))  {
                            matrix.get(from).set(i, operand);
                        } else {
                            String newValue = matrix.get(from).get(i) + ", " + operand;
                            matrix.get(from).set(i, newValue);
                        }
                    }

                    in.remove(operand);
                }

                if (out.containsKey(operand)) {
                    int from = out.get(operand);
                    if (matrix.get(from).get(i).equals(""))  {
                        matrix.get(from).set(i, operand);
                    } else {
                        String newValue = matrix.get(from).get(i) + ", " + operand;
                        matrix.get(from).set(i, newValue);
                    }

                    out.replace(operand, i);
                } else {
                    out.put(operand, i);
                }
            }
        }

        for (int i = 0; i < matrix.size(); i++) {
            matrix.get(i).set(i, "");
        }
    }

    public mxGraphComponent getVisualComponent() {
        mxGraph mxGraph = new mxGraph();

        mxStylesheet stylesheet = mxGraph.getStylesheet();

        Hashtable<String, Object> vertex = new Hashtable<>();
        vertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertex.put(mxConstants.STYLE_OPACITY, 50);
        vertex.put(mxConstants.STYLE_FONTCOLOR, "#A9B7C6");
        vertex.put(mxConstants.STYLE_FILLCOLOR, "#CB772F");
        vertex.put(mxConstants.STYLE_STROKECOLOR, "#A9B7C6");
        vertex.put(mxConstants.STYLE_FONTSIZE, 18);
        stylesheet.putCellStyle("vertex", vertex);

        Hashtable<String, Object> edge = new Hashtable<>();
        edge.put(mxConstants.STYLE_ROUNDED, true);
        edge.put(mxConstants.STYLE_ORTHOGONAL, false);
        edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
        edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        edge.put(mxConstants.STYLE_STROKECOLOR, "#A9B7C6");
        edge.put(mxConstants.STYLE_FONTSIZE, 18);
        edge.put(mxConstants.STYLE_FONTCOLOR, "#9876AA");
        stylesheet.putCellStyle("edge", edge);

        Object parent = mxGraph.getDefaultParent();

        mxGraph.getModel().beginUpdate();
        try {
            ArrayList<Object> vertices = new ArrayList<>();
            int levels[] = new int[matrix.size()];

            for (int i = 0; i < matrix.size(); i++) {
                int level = level(i);
                int y = level * SIZE + SPACE + SIZE / 2 + level * INTERVAL;
                int x = levels[level] * SIZE + SPACE + SIZE / 2 + levels[level] * INTERVAL;

                levels[level]++;

                vertices.add(mxGraph.insertVertex(parent, null, "" + (i + 1), x, y, SIZE, SIZE, "vertex"));
            }

            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.size(); j++) {
                    if (!matrix.get(i).get(j).equals("")) {
                        mxGraph.insertEdge(parent, null, matrix.get(i).get(j), vertices.get(i), vertices.get(j), "edge");
                    }
                }
            }
        }
        finally {
            mxGraph.getModel().endUpdate();
        }

        mxGraph.setCellsEditable(false);
        mxGraph.setAllowDanglingEdges(false);
        mxGraph.setAllowLoops(false);
        mxGraph.setCellsDeletable(false);
        mxGraph.setCellsCloneable(false);
        mxGraph.setCellsDisconnectable(false);
        mxGraph.setDropEnabled(false);
        mxGraph.setSplitEnabled(false);
        mxGraph.setCellsBendable(false);
        mxGraph.setConnectableEdges(false);

        mxGraphComponent ret = new mxGraphComponent(mxGraph);
        ret.setConnectable(false);
        ret.getViewport().setBackground(new Color(0x2b, 0x2b, 0x2b));
        ret.setBorder(new EmptyBorder(0, 0, 0, 0));
        return ret;
    }

    private int level(int node) {
        int ret = 0;

        for (int i = 0; i < matrix.size(); i++) {
            if (!matrix.get(i).get(node).equals("")) {
                int possible = level(i) + 1;
                if (possible > ret) {
                    ret = possible;
                }
            }
        }

        return ret;
    }
}

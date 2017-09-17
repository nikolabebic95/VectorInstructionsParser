package rs.ac.bg.etf.par.vectorinstructionparser.graph;

import rs.ac.bg.etf.par.vectorinstructionparser.instructions.Instruction;
import rs.ac.bg.etf.par.vectorinstructionparser.instructions.InstructionFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a data dependency graph (DDG)
 */
public class DataDependencyGraph {
    private final ArrayList<ArrayList<String>> matrix = new ArrayList<>();
    private final ArrayList<Instruction> instructions;

    public DataDependencyGraph(ArrayList<Instruction> instructions) {
        this.instructions = instructions;

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

        in.clear();
        out.clear();
    }

    public static void main(String[] args) {
        ArrayList<Instruction> i = new ArrayList<>();
        i.add(InstructionFactory.create("lv v1, r1"));
        i.add(InstructionFactory.create("lv v2, r2"));
        i.add(InstructionFactory.create("addv.d v3, v1, v2"));
        i.add(InstructionFactory.create("subv.d v3, v3, v1"));
        new DataDependencyGraph(i);
        i.add(null);
    }
}

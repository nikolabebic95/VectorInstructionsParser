package rs.ac.bg.etf.par.vectorinstructionparser.instructions;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing the unknown instruction
 */
public class UnknownInstruction implements Instruction {
    private final String name;
    private final ArrayList<String> outOperands = new ArrayList<>();
    private final ArrayList<String> inOperands = new ArrayList<>();

    public UnknownInstruction(String name, String[] operands) {
        this.name = name;
        if (operands.length > 0) {
            outOperands.add(operands[0]);
            inOperands.addAll(Arrays.asList(operands).subList(1, operands.length));
        }
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public ArrayList<String> inOperands() {
        return inOperands;
    }

    @Override
    public ArrayList<String> outOperands() {
        return outOperands;
    }
}

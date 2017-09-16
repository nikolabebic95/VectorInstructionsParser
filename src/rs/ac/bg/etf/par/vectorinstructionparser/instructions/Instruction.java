package rs.ac.bg.etf.par.vectorinstructionparser.instructions;

import java.util.ArrayList;

/**
 * Represents the instruction
 */
public interface Instruction {
    String name();
    ArrayList<String> inOperands();
    ArrayList<String> outOperands();
}

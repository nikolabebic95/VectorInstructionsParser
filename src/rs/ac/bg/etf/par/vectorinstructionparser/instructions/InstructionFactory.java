package rs.ac.bg.etf.par.vectorinstructionparser.instructions;

import java.util.HashMap;

/**
 * Utility factory used to create instruction objects
 */
public class InstructionFactory {

    private InstructionFactory() {}


    public static Instruction create(String line) {
        line = line.trim();
        int spaceIndex = line.indexOf(' ');
        String code = spaceIndex != -1 ? line.substring(0, spaceIndex) : line;
        String operands = spaceIndex != -1 ? line.substring(spaceIndex + 1) : "";

        String []operandsArray = operands.split(",");
        for (int i = 0; i < operandsArray.length; i++) {
            operandsArray[i] = operandsArray[i].trim();
        }

        return new UnknownInstruction(code, operandsArray);
    }

    public static void main(String[] args) {
        create("kurac");
    }
}

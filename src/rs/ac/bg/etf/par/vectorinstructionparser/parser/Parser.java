package rs.ac.bg.etf.par.vectorinstructionparser.parser;

import rs.ac.bg.etf.par.vectorinstructionparser.instructions.Instruction;
import rs.ac.bg.etf.par.vectorinstructionparser.instructions.InstructionFactory;

import java.util.ArrayList;

/**
 * Utility class used to parse the source code into the list of instructions
 */
public class Parser {
    private final static String LABEL_CHAR = ":";
    private final static String COMMENT_CHAR = ";";

    private Parser() {}

    public static ArrayList<Instruction> parse(String text) {
        ArrayList<Instruction> ret = new ArrayList<>();

        // Split the string into lines
        text = text.replaceAll("\r\n", "\n");
        String []lines = text.split("\n");

        for (String line : lines) {
            // Strip the comment
            String lineWithoutComment = line.contains(COMMENT_CHAR) ? line.substring(0, line.indexOf(COMMENT_CHAR) - 1) : line;

            String restOfLine = lineWithoutComment;

            // Extract the label
            if (containsLabel(lineWithoutComment)) {
                int index = lineWithoutComment.indexOf(LABEL_CHAR);
                restOfLine = lineWithoutComment.substring(index + 1);
            }

            // Skip empty lines
            if (restOfLine.matches("\\s*")) {
                continue;
            }

            // Parse the instruction
            ret.add(InstructionFactory.create(restOfLine));
        }

        return ret;
    }

    private static boolean containsLabel(String lineWithoutComment) {
        return lineWithoutComment.contains(LABEL_CHAR);
    }
}

package rs.ac.bg.etf.par.vectorinstructionparser.gui;

import rs.ac.bg.etf.par.vectorinstructionparser.helpers.NonWrappingTextPane;
import rs.ac.bg.etf.par.vectorinstructionparser.helpers.TextLineNumber;
import rs.ac.bg.etf.par.vectorinstructionparser.instructions.Instruction;
import rs.ac.bg.etf.par.vectorinstructionparser.parser.Parser;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main form class
 */
public class Form {
    private JPanel mainPanel;
    private JTextPane code;
    private JPanel left;
    private JButton parse;
    private JPanel canvas;
    private JScrollPane scrollPane;

    public Form() {
        parse.addActionListener((event) -> {
            String text = code.getText();
            ArrayList<Instruction> instructions = Parser.parse(text);
            System.out.println(text);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vector instruction parser");
        frame.setContentPane(new Form().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        final StyleContext cont = StyleContext.getDefaultStyleContext();

        final AttributeSet operand = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0x98, 0x76, 0xaa));
        final AttributeSet instruction = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0xcb, 0x77, 0x2f));
        final AttributeSet comment = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0x61, 0x96, 0x47));
        final AttributeSet label = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0xa9, 0xb7, 0xc6));
        final AttributeSet constant = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0x68, 0x97, 0xbb));
        final AttributeSet defaultAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0xa9, 0xb7, 0xc6));

        DefaultStyledDocument doc = new DefaultStyledDocument() {
            private void highlightSyntax() throws BadLocationException {
                String text = getText(0, getLength());
                int first = 0;
                int last;
                boolean newLine = true;
                boolean inComment = false;

                if (text.length() > 0 && text.charAt(0) == ';') {
                    inComment = true;
                }

                while (first < getLength() && (text.charAt(first) == ' ' || text.charAt(first) == '\t' || text.charAt(first) == '\n' || text.charAt(first) == '\n')) {
                    first++;
                }

                setCharacterAttributes(0, first, defaultAttr, false);
                last = first;

                while (first < getLength() && last < getLength()) {
                    if (!inComment && text.charAt(last) == ';') {
                        inComment = true;
                        first = last;
                    } else if (inComment && text.charAt(last) == '\n') {
                        setCharacterAttributes(first, last - first, comment, false);
                        first = last;
                        inComment = false;
                        newLine = true;
                        while (first < getLength() && (text.charAt(first) == ' ' || text.charAt(first) == '\t' || text.charAt(first) == '\n' || text.charAt(first) == ',')) {
                            first++;
                        }

                        setCharacterAttributes(last, first - last, defaultAttr, false);
                        last = first;
                    } else if (inComment) {
                        last++;
                    } else if (text.charAt(last) == ' ' || text.charAt(last) == ',' || text.charAt(first) == '\t' || text.charAt(last) == '\n') {
                        if (text.charAt(last - 1) == ':') {
                            setCharacterAttributes(first, last - first, label, false);
                            first = last;
                            newLine = false;
                            while (first < getLength() && (text.charAt(first) == ' ' || text.charAt(first) == '\t' || text.charAt(first) == '\n'|| text.charAt(first) == ',')) {
                                if (text.charAt(first) == '\n') {
                                    newLine = true;
                                }

                                first++;
                            }

                            setCharacterAttributes(last, first - last, defaultAttr, false);
                            last = first;
                        } else if (newLine) {
                            setCharacterAttributes(first, last - first, instruction, false);
                            first = last;
                            newLine = false;
                            while (first < getLength() && (text.charAt(first) == ' ' || text.charAt(first) == '\t' || text.charAt(first) == '\n' || text.charAt(first) == ',')) {
                                if (text.charAt(first) == '\n') {
                                    newLine = true;
                                }

                                first++;
                            }

                            setCharacterAttributes(last, first - last, defaultAttr, false);
                            last = first;
                        } else {
                            if (text.charAt(first) == '#') {
                                setCharacterAttributes(first, last - first, constant, false);
                            } else {
                                setCharacterAttributes(first, last - first, operand, false);
                            }

                            first = last;
                            while (first < getLength() && (text.charAt(first) == ' ' || text.charAt(first) == '\t' || text.charAt(first) == '\n' || text.charAt(first) == ',')) {
                                if (text.charAt(first) == '\n') {
                                    newLine = true;
                                }

                                first++;
                            }

                            setCharacterAttributes(last, first - last, defaultAttr, false);
                            last = first;
                        }
                    } else {
                        last++;
                    }

                }

                if (first < getLength()) {
                    while (first < getLength() && (text.charAt(first) == ' ' || text.charAt(first) == '\t' || text.charAt(first) == '\n' || text.charAt(first) == ',')) {
                        if (text.charAt(first) == '\n') {
                            newLine = true;
                        }

                        first++;
                    }

                    if (first >= getLength()) {
                        return;
                    }

                    if (inComment) {
                        setCharacterAttributes(first, getLength() - first, comment, false);
                    } else {
                        if (text.charAt(getLength() - 1) == ':') {
                            setCharacterAttributes(first, last - first, label, false);
                        } else if (newLine) {
                            setCharacterAttributes(first, getLength() - first, instruction, false);
                        } else if (text.charAt(first) == '#') {
                            setCharacterAttributes(first, last - first, constant, false);
                        } else {
                            setCharacterAttributes(first, last - first, operand, false);
                        }
                    }
                }
            }

            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                str = str.replaceAll("\t", "    ");
                super.insertString(offset, str, a);

                highlightSyntax();
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                highlightSyntax();
            }
        };

        code = new NonWrappingTextPane(doc);
        TextLineNumber tln = new TextLineNumber(code);
        tln.setBackground(new Color(0x2b, 0x2b, 0x2b));
        tln.setForeground(new Color(0xa9, 0xb7, 0xc6));
        tln.setFont(new Font("Consolas", Font.PLAIN, 18));
        scrollPane = new JScrollPane(code);
        scrollPane.setRowHeaderView(tln);

        TabStop[] tabs = new TabStop[4];
        tabs[0] = new TabStop(60, TabStop.ALIGN_RIGHT, TabStop.LEAD_NONE);
        tabs[1] = new TabStop(100, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
        tabs[2] = new TabStop(200, TabStop.ALIGN_CENTER, TabStop.LEAD_NONE);
        tabs[3] = new TabStop(300, TabStop.ALIGN_DECIMAL, TabStop.LEAD_NONE);
        TabSet tabset = new TabSet(tabs);

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.TabSet, tabset);
        code.setParagraphAttributes(aset, false);
    }
}

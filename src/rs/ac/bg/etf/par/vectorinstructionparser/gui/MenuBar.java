package rs.ac.bg.etf.par.vectorinstructionparser.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.Arrays;
import java.util.List;

/**
 * MenuBar for the form
 */
public class MenuBar extends JMenuBar {
    private String fileName = "";

    public MenuBar(Form form) {
        JMenu file = new JMenu("File");

        JMenuItem add = new JMenuItem();
        JMenuItem load = new JMenuItem();
        JMenuItem save = new JMenuItem();
        JMenuItem saveAs = new JMenuItem();

        Action addAction = new AbstractAction("New") {
            @Override
            public void actionPerformed(ActionEvent e) {
                form.getCanvas().removeAll();
                form.getCanvas().revalidate();
                form.getCode().setText("");
                fileName = "";
            }
        };

        addAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

        add.setAction(addAction);

        Action loadAction = new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File selected = fileChooser.getSelectedFile();

                    try {
                        List<String> lines = Files.readAllLines(Paths.get(selected.getAbsolutePath()));
                        StringBuilder sb = new StringBuilder();
                        lines.forEach(line -> sb.append(line).append("\n"));
                        form.getCode().setText(sb.toString());

                        fileName = selected.getAbsolutePath();
                    } catch (Exception exception) {
                        // Ignore
                    }
                }
            }
        };

        loadAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        load.setAction(loadAction);

        Action saveAction = new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileName.equals("")) {
                    JFileChooser fileChooser = new JFileChooser();
                    int ret = fileChooser.showSaveDialog(null);
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File selected = fileChooser.getSelectedFile();

                        try {
                            List<String> lines = Arrays.asList(form.getCode().getText().split("\r\n"));
                            Files.write(Paths.get(selected.getAbsolutePath()), lines);
                            fileName = selected.getAbsolutePath();
                        } catch (Exception exception) {
                            // Ignore
                        }
                    }
                } else {
                    try {
                        List<String> lines = Arrays.asList(form.getCode().getText().split("\r\n"));
                        Files.write(Paths.get(fileName), lines);
                    } catch (Exception exception) {
                        // Ignore
                    }
                }
            }
        };

        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

        save.setAction(saveAction);

        Action saveAsAction = new AbstractAction("Save as") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showSaveDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File selected = fileChooser.getSelectedFile();

                    try {
                        List<String> lines = Arrays.asList(form.getCode().getText().split("\r\n"));
                        Files.write(Paths.get(selected.getAbsolutePath()), lines);
                        fileName = selected.getAbsolutePath();
                    } catch (Exception exception) {
                        // Ignore
                    }
                }
            }
        };

        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));

        saveAs.setAction(saveAsAction);

        file.add(add);
        file.add(load);
        file.add(save);
        file.add(saveAs);

        add(file);
    }
}

package DIC.component.detailview;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;

//import org.apache.commons.lang3.text.WordUtils;

import static DIC.util.commons.Utility.getButton;

/**
 * Created by Arnab Saha on 10/10/15.
 */

public class SqlEditorView extends JPanel implements ActionListener {
    private JPanel sqlEditorPanel;
    private DefaultDetailView bottomPanel;
    private JTextArea editor;
    private JButton open, save, saveAs, execute, clearAll, cut, copy, paste, caseToggle, history, closeAllTabs;
    private Connection connection;
    private Collection<String> keyWords = Arrays.asList("select", "from");
    private String path;

    public JTextArea getEditor() {
        return editor;
    }

    public SqlEditorView(Connection connection) {
        this.connection = connection;
        path = null;
        JSplitPane splitPane;
        setLayout(new BorderLayout());
        sqlEditorPanel = new JPanel();
        sqlEditorPanel.setLayout(new BorderLayout());
        open = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\open.gif")), "Open", KeyEvent.VK_O);
        open.addActionListener(this);
        save = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\save.gif")), "Save", KeyEvent.VK_S);
        save.addActionListener(this);
        saveAs = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\saveas.gif")), "Save As");
        saveAs.addActionListener(this);
        cut = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\cut_16.gif")), "Cut", KeyEvent.VK_X);
        cut.addActionListener(this);
        copy = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\copy_16.gif")), "Copy", KeyEvent.VK_C);
        copy.addActionListener(this);
        paste = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\paste.gif")), "Paste", KeyEvent.VK_V);
        paste.addActionListener(this);
        caseToggle = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\change_case.gif")), "Case Toggle", KeyEvent.VK_T);
        caseToggle.addActionListener(this);
        execute = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\execute.gif")), "Execute", KeyEvent.VK_E);
        execute.addActionListener(this);
        clearAll = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\edits\\delete_all.gif")), "Clear All", KeyEvent.VK_L);
        clearAll.addActionListener(this);
        history = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\find_columncount_16.gif")), "History", KeyEvent.VK_H);
        history.addActionListener(this);
        closeAllTabs = getButton(new ImageIcon(getClass().getResource("..\\..\\resource\\clear_all_tabs.png")), "Close All Tabs", KeyEvent.VK_TAB);
        closeAllTabs.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(open);
        buttonPanel.add(save);
        buttonPanel.add(saveAs);
        buttonPanel.add(cut);
        buttonPanel.add(copy);
        buttonPanel.add(paste);
        buttonPanel.add(clearAll);
        buttonPanel.add(caseToggle);
        buttonPanel.add(execute);
        buttonPanel.add(history);
        buttonPanel.add(closeAllTabs);
        buttonPanel.setMaximumSize(new Dimension(1200, 16));
        sqlEditorPanel.add(BorderLayout.NORTH, buttonPanel);

        editor = new JTextArea();
        JScrollPane scrollBar = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sqlEditorPanel.add(BorderLayout.CENTER, scrollBar);
        bottomPanel = new DefaultDetailView();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sqlEditorPanel, bottomPanel);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(200);
        add(BorderLayout.CENTER, splitPane);
        addPropertyChangeListener(bottomPanel);
        sqlEditorPanel.setFocusable(true);
        sqlEditorPanel.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == open) {
            if (fileOpen()) return;
        }

        if (e.getSource() == save) {
            if (path == null) {
                file_save_as();
                return;
            }
            try {
                FileWriter fw = new FileWriter(path);
                fw.write(editor.getText());
                fw.close();
            } catch (IOException i) {
                JOptionPane.showMessageDialog(this, "Failed to save the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == saveAs) {
            file_save_as();
        }
        if (e.getSource() == execute)
            firePropertyChange("sql", connection, editor.getText());

        if (e.getSource() == clearAll)
            editor.setText("");

        if (e.getSource() == cut)
            editor.cut();

        if (e.getSource() == copy)
            editor.copy();

        if (e.getSource() == paste)
            editor.paste();

        if (e.getSource() == caseToggle) {
            String editorText = editor.getText();
//            editorText = editorText != null ? WordUtils.swapCase(editorText) : "";
            editor.setText(editorText);
        }

        if (e.getSource() == closeAllTabs)
            firePropertyChange("closeAllTabs", null, null);
        updateUI();
    }

    private boolean fileOpen() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int r = fc.showOpenDialog(this);
        if (r == JFileChooser.CANCEL_OPTION)
            return true;
        File myFile = fc.getSelectedFile();
        if (myFile == null || myFile.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Select a file!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        try {
            BufferedReader input = new BufferedReader(new FileReader(myFile));
            StringBuffer str = new StringBuffer();
            String line;
            while ((line = input.readLine()) != null) {
                str.append(line).append("\n");
            }
            editor.setText(str.toString());
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "File not found: " + fnfe);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "IO ERROR: " + ioe);
        }
        return false;
    }

    private void file_save_as() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("Text File", "txt");
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("Sql File", "sql");
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(filter1);
        fc.addChoosableFileFilter(filter2);
        int r = fc.showSaveDialog(this);
        if (r == JFileChooser.CANCEL_OPTION)
            return;
        File myfile = fc.getSelectedFile();
        if (myfile == null || myfile.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter a file name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (myfile.exists()) {
            r = JOptionPane.showConfirmDialog(this, "A file with same name already exists!\nAre you sure want to overwrite?");
            if (r != 0)
                return;
        }
        try {
            FileFilter extension = fc.getFileFilter();
            String fileName = myfile.getAbsolutePath();
            if (extension.getDescription().equals("Text File")) {
                fileName += ".txt";
            } else {
                fileName += ".sql";
            }
            myfile = new File(fileName);
            FileWriter fw = new FileWriter(myfile);
            fw.write(editor.getText());
            fw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

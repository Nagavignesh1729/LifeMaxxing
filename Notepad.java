import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

class NotepadApp extends JFrame {
    JTextArea textarea;
    JTabbedPane tabbedPane;
    JMenuBar menuBar;
    JToolBar toolBar;

    NotepadApp() {
        // Initialize components
        textarea = new JTextArea();
        tabbedPane = new JTabbedPane();
        menuBar = new JMenuBar();
        toolBar = new JToolBar();

        // Set up the JFrame
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set up menu bar
        setupMenuBar();

        // Set up toolbar
        setupToolBar();

        // Add components to tabbed pane
        tabbedPane.addTab("Untitled", new JScrollPane(textarea));

        // Add components to frame
        setJMenuBar(menuBar);
        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setupMenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Add action listeners
        newItem.addActionListener(e -> createNewTab());
        saveItem.addActionListener(new SaveAction());
        openItem.addActionListener(new OpenAction());
        exitItem.addActionListener(e -> System.exit(0));
    }

    private void setupToolBar() {
        JButton newButton = new JButton("New");
        JButton saveButton = new JButton("Save");
        JButton openButton = new JButton("Open");

        // Add action listeners for toolbar buttons
        newButton.addActionListener(e -> createNewTab());
        saveButton.addActionListener(new SaveAction());
        openButton.addActionListener(new OpenAction());

        // Add buttons to toolbar
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
    }

    private void createNewTab() {
        JTextArea newTextArea = new JTextArea();
        tabbedPane.addTab("Untitled", new JScrollPane(newTextArea));
        tabbedPane.setSelectedComponent(new JScrollPane(newTextArea)); // Switch to new tab
    }

    private class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(NotepadApp.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(file)) {
                    JTextArea currentTextArea = getCurrentTextArea();
                    if (currentTextArea != null) {
                        writer.write(currentTextArea.getText());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class OpenAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(NotepadApp.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    JTextArea newTextArea = new JTextArea();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        newTextArea.append(line + "\n");
                    }
                    tabbedPane.addTab(file.getName(), new JScrollPane(newTextArea));
                    tabbedPane.setSelectedComponent(new JScrollPane(newTextArea)); // Switch to new tab
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private JTextArea getCurrentTextArea() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) selectedComponent;
            Component viewportComponent = scrollPane.getViewport().getView();
            if (viewportComponent instanceof JTextArea) {
                return (JTextArea) viewportComponent;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotepadApp::new);
    }
}

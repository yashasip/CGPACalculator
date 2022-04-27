import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;


class Window {

    int SEM_COUNT = 8;

    JFrame window,helpWindow;

    JLabel usnLabel, nameLabel;
    JTextField usnTextBox, nameTextBox;
    JButton computeSgpaBtn, saveBtn;

    JTabbedPane tabBar;
    JLabel sgpaLabel, sgpaPaneLabels[], cgpaLabel, percentLabel, namePaneLabel, usnPaneLabel;
    JPanel sgpaPanel, finalGradePanel;
    JScrollPane tableScrollPane;
    Table[] sgpaTables = new Table[SEM_COUNT];
    JComboBox<String> semComboBox;
    JSeparator[] inTabSeparators = new JSeparator[3];

    JMenuBar windowMenuBar;
    JMenu fileMenu, tableMenu, aboutMenu;
    JMenuItem openItem, saveItem, exitItem, computeSgpaItem, clearTableItem, clearAllTablesItem, helpItem;

    JFileChooser fileDialog;

    String usn, name; // stores usn and name with label

    private void setupMenu(JFrame parentWindow) {
        windowMenuBar = new JMenuBar();

        windowMenuBar.setBorderPainted(true);

        // menus & menuItems
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open(beta)");
        fileMenu.add(openItem);
        fileMenu.add(new JSeparator());
        saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        fileMenu.add(new JSeparator());
        exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        windowMenuBar.add(fileMenu);

        tableMenu = new JMenu("Table");
        computeSgpaItem = new JMenuItem("Compute SGPA");
        tableMenu.add(computeSgpaItem);
        tableMenu.add(new JSeparator());
        clearTableItem = new JMenuItem("Clear Table");
        tableMenu.add(clearTableItem);
        tableMenu.add(new JSeparator());
        clearAllTablesItem = new JMenuItem("Clear All Tables");
        tableMenu.add(clearAllTablesItem);
        windowMenuBar.add(tableMenu);

        aboutMenu = new JMenu("About");
        helpItem = new JMenuItem("Help");
        aboutMenu.add(helpItem);
        windowMenuBar.add(aboutMenu);

        // set menu bar in window
        parentWindow.setJMenuBar(windowMenuBar);
    }

    private void setupTabs(JFrame parentWindow) {
        // tab component set
        tabBar = new JTabbedPane();
        tabBar.setBounds(50, 130, 400, 360);
        parentWindow.add(tabBar);

        // first tab
        sgpaPanel = new JPanel();
        sgpaPanel.setLayout(null);
        tabBar.add(sgpaPanel, "SGPA");

        // combo box semester choices
        String[] semChoices = new String[SEM_COUNT];
        for (int i = 0; i < SEM_COUNT; i++) {
            semChoices[i] = "SEM " + String.valueOf(i + 1);
        }

        semComboBox = new JComboBox<>(semChoices);
        semComboBox.setBounds(310, 5, 70, 20);
        semComboBox.setToolTipText("Choose Semester");
        sgpaPanel.add(semComboBox);

        // tables
        for (int i = 0; i < SEM_COUNT; i++)
            sgpaTables[i] = new Table(i + 1);

        tableScrollPane = new JScrollPane(sgpaTables[semComboBox.getSelectedIndex()].table);
        tableScrollPane.setBounds(10, 40, 380, 233);
        tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sgpaPanel.add(tableScrollPane);

        sgpaLabel = new JLabel("SGPA = ");
        sgpaLabel.setBounds(260, 270, 150, 25);
        sgpaPanel.add(sgpaLabel);

        inTabSeparators[0] = new JSeparator();
        inTabSeparators[0].setBounds(0, 293, 400, 25);
        sgpaPanel.add(inTabSeparators[0]);

        computeSgpaBtn = new JButton("+Compute SGPA");
        computeSgpaBtn.setBounds(125, 300, 150, 25);
        computeSgpaBtn.setToolTipText("Compute Grade Point Average of Semester");
        sgpaPanel.add(computeSgpaBtn);

        // second tab
        finalGradePanel = new JPanel();
        finalGradePanel.setLayout(null);
        tabBar.add(finalGradePanel, "Final Grade");

        saveBtn = new JButton("Save");
        saveBtn.setBounds(280, 295, 100, 30);
        saveBtn.setToolTipText("Save Student's Grade Card");
        finalGradePanel.add(saveBtn);

        usnPaneLabel = new JLabel("USN: ");
        usnPaneLabel.setBounds(125, 10, 230, 25);
        usnPaneLabel.setToolTipText("Student USN");
        finalGradePanel.add(usnPaneLabel);

        namePaneLabel = new JLabel("Name: ");
        namePaneLabel.setBounds(125, 30, 230, 25);
        namePaneLabel.setToolTipText("Student Name");
        finalGradePanel.add(namePaneLabel);

        // finalGrade tab final grade sheet sgpa labels set
        sgpaPaneLabels = new JLabel[SEM_COUNT];
        for (int i = 0; i < SEM_COUNT; i++) {

            sgpaPaneLabels[i] = new JLabel("SGPA " + String.valueOf(i + 1) + ": ");
            sgpaPaneLabels[i].setBounds(125, 60 + 20 * i, 90, 25);
            sgpaPaneLabels[i].setToolTipText("SGPA of Semester " + String.valueOf(i + 1));
            finalGradePanel.add(sgpaPaneLabels[i]);
        }

        cgpaLabel = new JLabel("CGPA: 0.0 ");
        cgpaLabel.setBounds(100, 260, 70, 25);
        cgpaLabel.setToolTipText("Cumulative Grade Point Average");
        finalGradePanel.add(cgpaLabel);

        percentLabel = new JLabel("Percentage: 0.0 ");
        percentLabel.setBounds(180, 260, 200, 25);
        percentLabel.setToolTipText("Final Percentage");
        finalGradePanel.add(percentLabel);

        inTabSeparators[1] = new JSeparator();
        inTabSeparators[1].setBounds(0, 260, 400, 25);
        finalGradePanel.add(inTabSeparators[1]);

        inTabSeparators[2] = new JSeparator();
        inTabSeparators[2].setBounds(0, 285, 400, 25);
        finalGradePanel.add(inTabSeparators[2]);
    }

    public void setupWindow() {
        window = new JFrame("CGPA Calculator");
        window.setSize(500, 600);
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usnTextBox = new JTextField();
        usnTextBox.setBounds(150, 25, 230, 35);
        usnTextBox.setToolTipText("Enter your USN");
        window.add(usnTextBox);

        usnLabel = new JLabel("USN:");
        usnLabel.setBounds(100, 30, 40, 25);
        usnLabel.setToolTipText("USN");
        window.add(usnLabel);

        nameTextBox = new JTextField();
        nameTextBox.setBounds(150, 70, 230, 35);
        nameTextBox.setToolTipText("Enter your Name");
        window.add(nameTextBox);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 75, 40, 25);
        nameLabel.setToolTipText("Name");
        window.add(nameLabel);

        // setting up tabs
        setupTabs(window);

        // setting up MenuBar
        setupMenu(window);

        // adding button listeners
        addActionListeners();

        window.setVisible(true);
    }

    // sets up Final Grade Data that is to be written into file, returns String array
    private String[] getFinalGradeData() {
        String[] gradeData = new String[12];

        gradeData[0] = usnPaneLabel.getText();
        gradeData[1] = namePaneLabel.getText();

        for (int i = 0; i < SEM_COUNT; i++)
            // starts from 2 because first two index has usn and name data
            gradeData[2 + i] = sgpaPaneLabels[i].getText();

        gradeData[10] = cgpaLabel.getText();
        gradeData[11] = percentLabel.getText();

        return gradeData;
    }

    // creates and writes to file with usn as file name
    private void writeToFile() {
        try{

            FileHandler file = new FileHandler(usn);
            String[] finalGradesData = getFinalGradeData();
            
            // writes Final Grade Tab Data
            file.writeGradeCard(finalGradesData);
            // writes All Tables Data
            for (int i = 0; i < SEM_COUNT; i++) {
                file.writeSemTable(String.valueOf(i + 1), sgpaTables[i].getTableData(), finalGradesData[2 + i]);
            }
    
            // display save message.
            JOptionPane.showMessageDialog(window, "File Saved Successfully in " + file.workingFile.getAbsolutePath(),"Save Successful", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(window, "Check if USN Entered is Valid", "File Not Created", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) {
            // when all fails
            JOptionPane.showMessageDialog(window, "Some Error Occurred", "File Write Error!",JOptionPane.ERROR_MESSAGE);
        }
    }

    // Sets all grade labels after computation
    private void setGradeLabels(String sgpa, int semTable) {

        // set sgpa label in first tab and second tab respectively
        sgpaLabel.setText("SGPA = " + sgpa);
        sgpaPaneLabels[semTable].setText(sgpaPaneLabels[semTable].getText().substring(0, 8) + sgpa);

        // Calculating cgpa and percentage setting respective labels
        cgpaLabel.setText("CGPA: " + sgpaTables[semTable].getCgpa());
        percentLabel.setText("Percentage: " + sgpaTables[semTable].getPercentage());
    }

    // gets computed sgpa value and sets all Labels
    private void setSgpa(int currentSemTable) {
        // getting table that is being viewed currently
        try {
            // gets sgpa of current table
            String currentSgpa = sgpaTables[currentSemTable].getSgpa();
            // sets all labels
            setGradeLabels(currentSgpa, currentSemTable);
        } catch (numberInvalidException e) {
            // user input error
            JOptionPane.showMessageDialog(sgpaPanel, e.getErrorType(), "Try Again!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // saves all data as text file
    public void saveFile() {
        // if usn not entered
        if (usnTextBox.getText().isBlank()) {
            JOptionPane.showMessageDialog(window, "Enter USN!", "USN not Entered", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // writes to a file
        writeToFile();
    }

    public void openFile() {
        ArrayList<String> finalGradeSheetData = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> semestersTableData = new ArrayList<>();
        FileHandler readFile;
        int fieldStatus; // to hold status if file selected or not

        fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Open Grades Data File");
        fileDialog.setFileFilter(new FileNameExtensionFilter(null, "txt")); // only text files
        fieldStatus = fileDialog.showOpenDialog(window);

        // return when file is not selected or error occurs
        if(fieldStatus==JFileChooser.CANCEL_OPTION) 
            return;
        else if(fieldStatus==JFileChooser.ERROR_OPTION){
            JOptionPane.showMessageDialog(window, "Some Error Occurred", "Try Again!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        readFile = new FileHandler(fileDialog.getSelectedFile());
        
        // read file data
        finalGradeSheetData = readFile.readFinalGradeCard();
        semestersTableData = readFile.readSemTable();

        // setup textbox and name
        usn = finalGradeSheetData.get(0);
        usnTextBox.setText(usn);
        usnPaneLabel.setText("USN: " + usn.toUpperCase());
        
        name = finalGradeSheetData.get(1);
        nameTextBox.setText(name);
        namePaneLabel.setText("Name: " + name.toUpperCase());

        clearAllTables();
        // setup semester tables
        for(int i = 0; i < sgpaTables.length; i++){
            if(semestersTableData.get(i).size() <= 0)
                continue;
            sgpaTables[i].setTableData(semestersTableData.get(i));
            setSgpa(i);
        }
    }

    // clears all table data
    private void clearAllTables(){
        for (int i = 0; i < SEM_COUNT; i++) {
            sgpaTables[i].clearTable();
            // sets label values to default after clearing
            setGradeLabels("", i);
        }
    }

    private void addActionListeners() {
        usnTextBox.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                // limiting usn string length to 15
                if (usnTextBox.getText().length() <= 15) {
                    // if string length<15 then chnages are applied
                    // store usn
                    usn = usnTextBox.getText().trim();
                    usnPaneLabel.setText("USN: " + usn.toUpperCase());
                }
                else {
                    // reverts changes if length exceeds limit
                    usnTextBox.setText(usn);
                }
            };
        });

        nameTextBox.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                // name string length limit 40
                if (nameTextBox.getText().length() <= 40) {
                    // if within string limit changes are applied
                    namePaneLabel.setText("Name: " + nameTextBox.getText().toUpperCase());
                    // store name
                    name = namePaneLabel.getText();
                }
                else {
                    // reverts changes if length exceeds limit
                    nameTextBox.setText(namePaneLabel.getText().substring(6));
                }
            };
        });

        semComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // get the combo box value
                int currentSemTable = semComboBox.getSelectedIndex();

                // change table in the tableScrollPane 
                tableScrollPane.setViewportView(sgpaTables[currentSemTable].table);

                // set sgpa label by refering sgpaPaneLabels
                String currentSgpa = "SGPA = " + sgpaPaneLabels[currentSemTable].getText().substring(7);
                sgpaLabel.setText(currentSgpa);
            };
        });

        computeSgpaBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                    setSgpa(semComboBox.getSelectedIndex());
                }
            }
        );
        // for menu item
        computeSgpaItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setSgpa(semComboBox.getSelectedIndex());
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                saveFile();
            }
        });
        // for menu item
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                openFile();
            }
        });

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                saveFile();
            }
        });
        
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // kills all windows
                if (helpWindow!=null && !helpWindow.isActive()) {
                    helpWindow.dispose();
                }
                window.dispose();
            }
        });


        clearTableItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int currentSemTable = semComboBox.getSelectedIndex();
                sgpaTables[currentSemTable].clearTable();
                // sets label values to default after clearing
                setGradeLabels("", currentSemTable);
            }
        });

        clearAllTablesItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                clearAllTables();
            }
        });

        helpItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // help window content
                String helpContent = "USN: 4S019pppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp\nName:\nSGPA 1: 8.8\nSGPA 2:\n SGPA 3:\n SGPA 4:\n SGPA 5:\n SGPA 6:\n SGPA 7:\n SGPA 8:\n CGPA: 8.8\nPercentage: 80.5\nSemester 1:\nSubjectCode	Credits	Score\n18MAT11		4		84\n18PHY12		4		81\n18ELE13		3		68\n18CIV14		3		70\n18EGDL15		3		90\n18PHYL16		1		98\n18ELEL17		1		91\n18EGH18		1		83\nSGPA 1: 8.8\nSemester 2:\nSubjectCode	Credits	Score\n18MAT21		4		89\n18CHE22		4		91\n18CPS23		3		87\n18ELN24		3		92\n18ME25		3		87\n18CHEL26		1		86\n18CPL27		1		90\n18EGH28		1		86\nSGPA 2:\nSemester 3:\nSubjectCode	Credits	Score\n18MAT31		4		59\n18CS32				64\n18CS33				74\n18CS34				73\n18CS35				63\n18CS36		1		87\n18CSL37		1		93\n18CSL38				86\n18CPC39				80\nSGPA 3:\n";

                // helpWindow Components
                helpWindow = new JFrame("Help - CGPA Calculator");
                JTextArea aboutTextArea = new JTextArea(helpContent);
                JScrollPane aboutScrollPane = new JScrollPane(aboutTextArea);

                helpWindow.setSize(400, 500);
                helpWindow.setResizable(false);
                helpWindow.setLayout(null);
                helpWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                aboutScrollPane.setBounds(10, 10, 380, 480);
                aboutScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                helpWindow.add(aboutScrollPane);
                
                aboutTextArea.setEditable(false);
                aboutTextArea.setBounds(10, 10, 240, 200);
                aboutTextArea.setLineWrap(true);

                helpWindow.setVisible(true);
            }
        });
    }
}

public class CGPACalculator {
    public static void main(String args[]) {
        Window Frame = new Window();
        Frame.setupWindow();
    }
}
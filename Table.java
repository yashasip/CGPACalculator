import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import java.awt.*;
import java.awt.event.*;


public class Table {
    int SEM_COUNT = 8;

    JTable table;
    DefaultTableModel tableModel;
    JButton addRowBtn, deleteRowBtn;

    int sem;
    static Calculation calculator = new Calculation();
    ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();

    String[][][] semTableData = {{ { "18MAT11", "4", "84" }, { "18PHY12", "4", "81" }, { "18ELE13", "3", "68" }, { "18CIV14", "3", "70" },{ "18EGDL15", "3", "90" }, { "18PHYL16", "1", "98" }, { "18ELEL17", "1", "91" },{ "18EGH18", "1", "83" } },{ { "18MAT21", "4", "89" }, { "18CHE22", "4", "91" }, { "18CPS23", "3", "87" }, { "18ELN24", "3", "92" },{ "18ME25", "3", "87" }, { "18CHEL26", "1", "86" }, { "18CPL27", "1", "90" },{ "18EGH28", "1", "86" } } ,{ { "18MAT31", "3", "59" }, { "18CS32", "4", "64" }, { "18CS33", "3", "74" }, { "18CS34", "3", "73" },{ "18CS35", "3", "63" }, { "18CS36", "3", "87" }, { "18CSL37", "2", "93" },{ "18CSL38", "2", "86" },{ "18CPC39", "1", "80" }}};
    
    // each instance assigned within semester and sets up table
    public Table(int sem) {
        this.sem = sem;
        setupTable();
    }
    
    public JTable setupTable() {
        // objects used to setup table
        String[] columnNames = { "Subject Code", "Credit", "Score" };
        Object[][] emptyRow = { { "", "", "" } };

        // table model
        tableModel = new DefaultTableModel(emptyRow, columnNames);
        table = new JTable(tableModel);

        // center alignment for all columns
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        tableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(tableCellRenderer);

        // table property sets
        table.setRowHeight(25);
        // add to scrollpane setup
        table.setPreferredScrollableViewportSize(new Dimension(380, 255));
        table.setFillsViewportHeight(true);
        // disable drag and move of columns
        table.getTableHeader().setReorderingAllowed(false);
        // when clicked out of table, cell editing stops
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // add/remove rows/subject buttons
        addRowBtn = new JButton("+");
        addRowBtn.setBounds(315, 165, 41, 36);
        addRowBtn.setToolTipText("Add Subject");
        table.add(addRowBtn);

        deleteRowBtn = new JButton("-");
        deleteRowBtn.setBounds(315, 120, 41, 36);
        deleteRowBtn.setToolTipText("Remove Subject");
        table.add(deleteRowBtn);

        // button actions
        addTableActionListeners();

        return table;
    }

    private void addTableActionListeners() {

        addRowBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // set upper row limit = 25
                if (tableModel.getRowCount() <= 25) {
                    tableModel.setRowCount(tableModel.getRowCount() + 1);
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        // initializing all cells with ""
                        table.setValueAt("", tableModel.getRowCount() - 1, i);
                    }
                }
            }
        });

        deleteRowBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // set lower row limit = 1
                if (tableModel.getRowCount() > 1) {
                    tableModel.setRowCount(tableModel.getRowCount() - 1);
                }
            }
        });

        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent event) {
                // executes only when event is performed on first column
                if (event.getColumn() == 0) {
                    // gets last row selected
                    int rowSelected = event.getLastRow();

                    // get string from cell from first column
                    String subjectCode = String.valueOf(table.getValueAt(rowSelected, 0));
                    // computes credit
                    String value = calculator.computeCredit(subjectCode);
                    // sets credit value of respective row
                    table.setValueAt(value, rowSelected, 1);
                }
            }
        });
    }

    // only extracts table data, has no return value
    private void extractTableData() {
        // clear all previous extracted data to avoid conflict
        tableData.clear();

        for (int i = 0; i < table.getRowCount(); i++) {
            ArrayList<String> rowData = new ArrayList<String>();

            for (int j = 0; j < table.getColumnCount(); j++) {
                String data = String.valueOf(table.getValueAt(i, j)).trim();
                rowData.add(data);
            }
            tableData.add(rowData);
        }
    }

    // sets up the table
    public void setTableData(ArrayList<ArrayList<String>> readTableData){
        tableModel.setRowCount(readTableData.size()); // initialize rows to be filled
        for (int i=0; i < readTableData.size(); i++){ // fill data in the row
            table.setValueAt(readTableData.get(i).get(0), i , 0);
            table.setValueAt(readTableData.get(i).get(1), i , 1);
            table.setValueAt(readTableData.get(i).get(2), i , 2);            
        }
    }

    // clear contents of table
    public void clearTable() {
        // deletes all rows to clear data
        tableModel.setRowCount(0);

        // adds one row 
        Object[] emptyRow = { "", "", "" } ;
        tableModel.addRow(emptyRow);

        // clears existing data related to table
        tableData.clear();
        calculator.creditSumsList[sem - 1] = 0;
        calculator.sgpaList[sem - 1] = 0.0f;
    }

    // returns table data as ArrayList
    public ArrayList<ArrayList<String>> getTableData() {
        extractTableData();

        return tableData;
    }


    // returns sgpa value of extracted table data as string or exception for wrong user input
    public String getSgpa() throws numberInvalidException{

        String result;
        try {
            result = String.valueOf(calculator.computeSgpa(this.sem, getTableData()));
            return result;
        }
        catch (numberInvalidException e) {
            throw e;
        }
    }

    // returns cgpa as string
    public String getCgpa() {
        return String.valueOf(calculator.computeCgpa());
    }

    // returns percentage as string
    public String getPercentage() {
        return String.valueOf(calculator.percentage);
    }
}
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;

// TODO:
// better error message for io exception
// validate read data

public class FileHandler {

    File workingFile;
    String usn;


    // creates file with usn name
    FileHandler(String usn){
        this.usn = usn;
        workingFile = new File(usn + ".txt");
    }
    // file read with this name
    FileHandler(File workingFile){
        this.workingFile = workingFile;
    }

    // writes Final Grade Card Data to final ie contents in second tab
    public void writeGradeCard(String[] gradeData) throws  IOException,Exception {
        try {
            FileWriter writeHandle = new FileWriter(workingFile);
            // writes all sgpa data
            for (int i = 0; i < gradeData.length; i++)
                writeHandle.write(gradeData[i] + "\n");
            writeHandle.close();

        } 
        catch (IOException e) {//if file does not exist or is not created
            throw e;
        }
    }

    // appends contents of a table into the same file includes subject code, marks, credits
    public void writeSemTable(String sem, ArrayList<ArrayList<String>> semTable,String sgpa) throws IOException, Exception {
        try {
            FileWriter writeHandle = new FileWriter(this.usn + ".txt", true);
            // write semester heading and column heading of the table
            writeHandle.write("\nSemester " + sem + ":\n");
            writeHandle.write("SubjectCode\tCredits\tScore\n");
            // writes all table contents
            for (int i = 0; i < semTable.size(); i++) {
                if(semTable.get(i).get(0).isBlank() && semTable.get(i).get(1).isBlank() && semTable.get(i).get(2).isBlank()) // skip empty row (for empty tables)
                    continue;
                else if(semTable.get(i).get(0).isBlank()) // when subject code is not given give default name
                    semTable.get(i).set(0, "Subject" + String.valueOf(i+1));

                writeHandle.write(semTable.get(i).get(0) + "\t\t" + semTable.get(i).get(1) + "\t\t"
                        + semTable.get(i).get(2) + "\n");
            }
            writeHandle.write(sgpa+"\n");

            writeHandle.close();
        }
        catch (IOException e) {// if file does not exist or is not created
            throw e;
        }
    }

    // reads only final grade sheet data from text file
    public ArrayList<String> readFinalGradeCard(){
        ArrayList<String> finalGradeSheetData = new ArrayList<String>();
        Scanner readHandle;
        
        // validate
        try{
            readHandle = new Scanner(this.workingFile);
            for(int i=0; i<12; i++){
                finalGradeSheetData.add(readHandle.nextLine().split(":")[1].strip()); // split on : and read sgpa grade
            }
            readHandle.close();
        }
        catch(FileNotFoundException e){
            // display error message
        }
        
        return finalGradeSheetData;
    }

    public ArrayList<ArrayList<ArrayList<String>>> readSemTable(){
        ArrayList<String> rawReadData = new ArrayList<String>();
        Scanner readHandle;
        // validate
        try{
            readHandle = new Scanner(workingFile);
            String lineData = readHandle.nextLine();
            for(int i = 0; readHandle.hasNextLine(); i++){
                lineData = readHandle.nextLine().strip();
                if(i<12 || lineData.isBlank()) // skip final grades sheet data
                    continue;
                rawReadData.add(lineData); // reads data in raw format
            }
            readHandle.close();
            // System.out.println(rawReadData);
        }
        catch(FileNotFoundException e){
            // display error message
        }
        return structureRawData(rawReadData);
        
    }

    // structures the rawData read from the file
    private ArrayList<ArrayList<ArrayList<String>>> structureRawData(ArrayList<String> rawData){
        ArrayList<ArrayList<ArrayList<String>>> tablesData = new ArrayList<ArrayList<ArrayList<String>>>(); // stores collection of semester table data
        ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>(); // stores data of one semester table
        
        for(int i = 0; i < rawData.size() ; i++){
            if( rawData.get(i).startsWith("Semester") || rawData.get(i).matches("SubjectCode(\\s*)Credits(\\s.*)Score") ) // skip the headers of each semester table
                continue;
            else if(rawData.get(i).startsWith("SGPA")){ // when SGPA is read from the TextFile, indicates end of semester table
                tablesData.add(tableData); // add read semester table to existing semester table.
                tableData = new ArrayList<ArrayList<String>>(); // allocate space and create new ArrayList for reading of next Semester Data
            }
            else{ // adds semester table data
                tableData.add(new ArrayList<String>(Arrays.asList(rawData.get(i).split("\\s+"))));
            }
        }
        return tablesData;
    }

}

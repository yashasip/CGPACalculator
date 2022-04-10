import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    File workingFile;
    String usn;

    // creates file with usn name
    FileHandler(String usn){
        this.usn = usn;
        workingFile = new File(usn + ".txt");
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
        catch (Exception e) {
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
                writeHandle.write(semTable.get(i).get(0) + "\t\t" + semTable.get(i).get(1) + "\t\t"
                        + semTable.get(i).get(2) + "\n");
            }
            writeHandle.write(sgpa+"\n");

            writeHandle.close();
        }
        catch (IOException e) {// if file does not exist or is not created
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}

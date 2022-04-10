import java.util.ArrayList;
import java.util.HashMap;

public class Calculation {

    int SEM_COUNT = 8;

    float cgpa, percentage;

    float[] sgpaList = new float[SEM_COUNT];
    int[] creditSumsList = new int[SEM_COUNT];

    private HashMap<String, String> creditMap = new HashMap<>();

    // credits 
    private String[][] creditData = {
            { "4", "18MAT11", "18PHY12", "18CHE12", "18MAT21", "18PHY22", "18MAT21", "18CHE22", "18CS32", "18CS42",
                    "18CS52", "18CS53", "18CS61", "18CS62", "18CS63", "18CS71", "18CS72" },
            { "3", "18ELE13", "18CIV14", "18EGDL15", "18CPS13", "18ELN14", "18ME15", "18ELE23", "18CIV24", "18EGDL25",
                    "18CPS23", "18ELN24", "18ME25", "18MAT31", "18CS33", "18CS34", "18CS35", "18CS36", "18MAT41",
                    "18CS43", "18CS44", "18CS45", "18CS46", "18CS51", "18CS54", "18CS55", "18CS56", "18CS641",
                    "18CS642", "18CS643", "18CS644", "18CS645", "18CS651", "18CS652", "18CS652", "18CS653", "18CS654",
                    "18CS731", "18CS732", "18CS733", "18CS734", "18CS741", "18CS742", "18CS743", "18CS744", "18CS745",
                    "18CS751", "18CS752", "18CS753", "18CS754", "18CS81", "18CS821", "18CS822", "18CS823", "18CS824",
                    "18CSI85" },
            { "2", "18CSL37", "18CSL38", "18CSL47", "18CSL48", "18CSL57", "18CSL58", "18CSL66", "18CSMP68", "18CSL67",
                    "18CSL76" },
            { "1", "18PHYL16", "18ELEL17", "18EGH18", "18CHEL16", "18CPL17", "18EGH18", "18PHYL26", "18ELEL27", "18EGH28",
                    "18CHEL26", "18CPL27", "18EGH28", "18KVK39", "18KAK39", "18CPC39", "18KVK49", "18KAK49", "18CPC39",
                    "18CIV59", "18CSP77", "18CSS84" },
            { "8", "18CSP83" } };

    // Creates a HashMap of all <subjectCode,credit> using creditData
    Calculation() {
        for (String[] subjectCodes : creditData) {
            for (String subjectCode : subjectCodes) {
                // dont add the credit as hashkey
                if (subjectCodes[0] != subjectCode) {
                    creditMap.put(subjectCode, subjectCodes[0]);
                }
            }
        }
    }

    private int computeGrade(int score) {

        if (score < 40) {
            return 0;
        } else if (score < 45) {
            return 4;
        } else if (score < 50) {
            return 5;
        } else if (score < 60) {
            return 6;
        } else if (score < 70) {
            return 7;
        } else if (score < 80) {
            return 8;
        } else if (score < 90) {
            return 9;
        } else {
            return 10;
        }
    }

    // computes sgpa of given sem value
    public float computeSgpa(int sem, ArrayList<ArrayList<String>> semTableData) throws numberInvalidException {

        int creditSum = 0, credit, score, grade, creditSumxGrade = 0;
        float sgpa = 0;

        for (ArrayList<String> subject : semTableData) {
            // cell address set to credit cell of row.
            int[] cellAddress = {semTableData.indexOf(subject), 1};
            try{
                credit = Integer.valueOf(subject.get(1));
            }
            catch (NumberFormatException e) {
                if (subject.get(1).isBlank()) {
                    throw new numberInvalidException(numberInvalidType.EMPTY_CELL, cellAddress);
                } else {
                    throw new numberInvalidException(numberInvalidType.NOT_INTEGER, cellAddress);
                }
            }
            catch (Exception e) {
                throw new numberInvalidException(numberInvalidType.INVALID_FORMAT, cellAddress);
            }
            if (credit < 0) {
                // if negative
                throw new numberInvalidException(numberInvalidType.NEGATIVE_NUMBER, cellAddress);
            }
            
            // set cell address to score of the row
            cellAddress[1] = 2;
            try {
                score = Integer.valueOf(subject.get(2));
            } catch (NumberFormatException e) {
                if (subject.get(2).isBlank()) {
                    throw new numberInvalidException(numberInvalidType.EMPTY_CELL, cellAddress);
                } else {
                    throw new numberInvalidException(numberInvalidType.NOT_INTEGER, cellAddress);
                }
            }
            catch (Exception e) {
                throw new numberInvalidException(numberInvalidType.INVALID_FORMAT, cellAddress);
            }
            // score should be within range
            if (score < 0 || score > 100) {
                throw new numberInvalidException(numberInvalidType.OUT_OF_RANGE, cellAddress);
            }
            grade = computeGrade(score);

            creditSum = creditSum + credit; // add credit sum of the semester

            creditSumxGrade = creditSumxGrade + grade * credit;// for sgpa calculation this is the denominator
        }

        sgpa = (float) creditSumxGrade / creditSum;

        // storing sgpa & credits sum in a list, for cgpa calculation
        sgpaList[sem - 1] = formatGrade(sgpa);
        creditSumsList[sem - 1] = creditSum;

        return sgpaList[sem - 1];
    }

    // computes cgpa and percentage
    public float computeCgpa() {
        float sumOfcreditSumxSgpa = 0;
        int sumOfAllCredits = 0;

        for (int i = 0; i < SEM_COUNT; i++) {
            sumOfcreditSumxSgpa += creditSumsList[i] * sgpaList[i];
            sumOfAllCredits += creditSumsList[i];
        }

        cgpa = formatGrade((float) sumOfcreditSumxSgpa / sumOfAllCredits);
        // if cgpa is NaN change to 0
        if (Float.isNaN(cgpa))
            cgpa = 0.0f;
        
        percentage = (cgpa - 0.75f) * 10;
        // if percentage is negative or NaN
        if (percentage < 0 || Float.isNaN(percentage))
            percentage = 0.0f;

        return cgpa;
    }

    // is called by Table class, computes credit 
    public String computeCredit(String subjectCode) {
        subjectCode = subjectCode.toUpperCase().strip();

        String value = String.valueOf(creditMap.get(subjectCode));
        // if code/key valid and found in hashTable
        if (value != "null") {
            return value;
        }
        // when key not found
        return "";
    }

    // returns float grade value with one decimal place without rounding off.
    private float formatGrade(float gradeValue) {
        gradeValue = Float.parseFloat(String.valueOf(gradeValue).substring(0, 3));

        return gradeValue;
    }
}



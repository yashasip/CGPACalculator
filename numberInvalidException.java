enum numberInvalidType {
    // All cases of invalid user input, INVALID FORMAT is a general case
    NEGATIVE_NUMBER, OUT_OF_RANGE, NOT_INTEGER, EMPTY_CELL, INVALID_FORMAT;
}

public class numberInvalidException extends Exception {
    
    numberInvalidType type;
    int cellAddress[];

    public numberInvalidException(numberInvalidType type, int[] cellAddress) {
        this.type = type;// type of error
        this.cellAddress = cellAddress;
    }

    // returns appropriate message based exception of the user input
    public String getErrorType() {
        switch (type) {
            case NEGATIVE_NUMBER:
                return "Cell(" + String.valueOf(cellAddress[0] + 1) + "," + String.valueOf(cellAddress[1] + 1)
                        + ") Value Cannot be Negative!";
            case OUT_OF_RANGE:
                return "Subject - " + String.valueOf(cellAddress[0]+1) +" Score Should be in Range[0-100]";
            case NOT_INTEGER:
                return "In Cell("+String.valueOf(cellAddress[0]+1)+","+String.valueOf(cellAddress[1]+1) +"), Value should be an Integer";
            case EMPTY_CELL:
                return "Cell(" + String.valueOf(cellAddress[0] + 1) + "," + String.valueOf(cellAddress[1] + 1)+ ") Empty!";
            case INVALID_FORMAT:
                return "Cell(" + String.valueOf(cellAddress[0] + 1) + "," + String.valueOf(cellAddress[1] + 1)
                        + ") Value is Invalid";
            default:
                return "Error, Try Entering Score and Credits Again";
        }
    }
}
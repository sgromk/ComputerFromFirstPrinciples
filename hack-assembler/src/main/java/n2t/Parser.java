package n2t;

/**
 * Unpacks instruction into underlying fields
 */
public class Parser {
    Code code;                          // Holds the mapping of assembly language code to dest, comp, and jump bits
    SymbolTable symbolTable;            // Manages the symbol table for variable and label look up
    int labelDeclarationLineNumber;     // Used during first-pass read, tracks which line to set initial label declarations to

    Parser() {
        this.code = new Code();
        this.symbolTable = new SymbolTable();
        this.labelDeclarationLineNumber = 0;
    }

    /**
     * Adds label declarations to the symbol table during the first-pass read of an assembly file
     * @param currentLine the line of the assembly file being parsed
     */
    public void checkLabelDeclaration(String currentLine) {
        // Remove any comments, then leading or trailing whitespace
        currentLine = getCodingSegment(currentLine);

        // If the line's coding segment's starting character is '(', then add the label declaration to the SymbolTable,
        // otherwise, if the line is not empty, increment the line counter
        if (!currentLine.isEmpty()) {
            if (currentLine.charAt(0) == '(') {
                symbolTable.addLabelDeclaration(currentLine.substring(1, currentLine.length() - 1), Integer.toString(labelDeclarationLineNumber));
            } else {
                labelDeclarationLineNumber++;
            }
        }
    }

    public String parseLine(String currentLine) {
        // Remove comments, empty lines, and extra whitespace
        String codingLine = getCodingSegment(currentLine);

        // If the line has no coding segments or contains a label declaration, return immediately
        if (codingLine.isEmpty() || codingLine.charAt(0) == '(') {
            return "";
        } else {
            // If it is an A-instruction, return the 16-bit binary address number
            if (codingLine.charAt(0) == '@') {
                return handleAinstruction(codingLine.substring(1));
            } else {
                return handleCinstruction(codingLine);
            }
        }
    }

    /**
     * Given a line, ignores text after and including the two slash characters,
     * then removes any extra whitespace
     * @param currentLine the line remove comments and whitespace from
     * @return a trimmed line, with only coding segments remaining
     */
    private String getCodingSegment(String currentLine) {
        int commentIndex = currentLine.indexOf("//");
        
        // If a comment was found, only preserve text up to the double slash
        if (commentIndex != -1) {
            currentLine = currentLine.substring(0, commentIndex);
        }

        return currentLine.trim();
    } 

    private String handleAinstruction(String address) {
        if (isNumeric(address)) {
            return decimalToBinary(address);
        } else {
            return decimalToBinary(symbolTable.checkSymbolTable(address));
        }
    }

    /**
     * Converts a string holding zero or a positive integer to a string holding the 
     * 15-bit binary representation of the same number, with an additional leading 0
     * @param decimal a {@code String} holding the integer number
     * @return a {@code String} holding the binary representation of the given number
     */
    private String decimalToBinary(String decimal) {
        // Convert given number in String to int
        int num = Integer.parseInt(decimal);
        String binary = "";

        // Decimal to binary algorithm, repeats 15 times to add leading
        // zeroes until the number is 15 bit, or to ignore bits after 15
        // in case an invalid overly large decimal was somehow provided
        for (int i = 0; i < 15; i++) {
            binary = String.valueOf(num % 2) + binary;
            num = num / 2;
        }

        // Add the final leading 0 for the A-instruction code
        return "0" + binary;
    }

    /**
     * Decomposes a C-instruction into component 'comp', 'dest', and 'jump' parts.
     * 
     * Given an assembly line in the form 'X=Y;Z':
     * - X (before the '=') is encoded into the 'dest' bits.
     * - Y (between the '=' and ';') is endcoded into the comp bits.
     * - Z (after the ';') is encodd into the jump bits.
     * 
     * The '=' and ';' characters serve as delimiters for 'X', 'Y', and 'Z'.
     * 
     * Instructions always contain a 'Y' part, but one of the 'X' or 'Z' parts may be null.
     * 
     * @param codingLine an assembly-form C-instruction
     * @return the binary code-form of the C-instruction
     */
    private String handleCinstruction(String codingLine) {
        // Get the indices of the two delimiters
        int XYdelimiter = codingLine.indexOf("=");
        int YZdelimiter = codingLine.indexOf(";");
        String Xcomponent = "";
        String Ycomponent = "";
        String Zcomponent = "";

        // Update the X part if it is not null, and the Y part if it is null
        if (XYdelimiter != -1) {
            Xcomponent = codingLine.substring(0, XYdelimiter);
        } else {
            Ycomponent = codingLine.substring(0, YZdelimiter);
        }

        // Update the Z part if it is not null, and the Y part if it is null
        if (YZdelimiter != -1) {
            Zcomponent = codingLine.substring(YZdelimiter + 1);
        } else {
            Ycomponent = codingLine.substring(XYdelimiter + 1);
        }

        // If the Y part has still not been updated, then update it with both delimiters
        if (Ycomponent.isEmpty()) {
            Ycomponent = codingLine.substring(XYdelimiter + 1, YZdelimiter);
        }

        return "111" + code.encodeComp(Ycomponent)
                     + code.encodeDest(Xcomponent)
                     + code.encodeJump(Zcomponent);
    }

    /**
     * Checks if a given variable is numeric or alphanumeric, to determine if the
     * symbol table should be consulted
     * @param address the address reference being checked
     * @return true if the number is numeric, false otherwise
     */
    private static boolean isNumeric(String address) {
        try {
            Integer.parseInt(address);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
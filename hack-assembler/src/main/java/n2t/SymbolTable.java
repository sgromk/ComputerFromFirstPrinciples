package n2t;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the symbol table, including label declaration and variable setting/value retrieval
 * Label declarations and variables are set from register 16 onwards
 */
public class SymbolTable {
    // Tracks which position to assign the next declared variable
    private int nextAvailableRegister;

    public SymbolTable() {
        // Start variables from register 16 onwards
        nextAvailableRegister = 16;

        // Initialize the predefined symbols
        initializePreDefinedSymbols();
    }

    // Initialize the hashmap
    private static final Map<String, String> SYMBOL_MAP = new HashMap<>();

    public String checkSymbolTable(String symbol) {
        // Only adds the symbol if it does not already exist
        addSymbol(symbol);

        // Return the symbol, whether it was just added or previously available
        return getSymbolValue(symbol);
    }

    private void addSymbol(String symbol) {
        // Check if a symbol is already in the table, so as not to overwrite the symbol
        if (!inTable(symbol)) {
            SYMBOL_MAP.put(symbol, Integer.toString(nextAvailableRegister));
            nextAvailableRegister++;
        }
    }

    /**
     * Adds a label declaration to the symbol table with the given address. Does not use the {@code nextAvailableRegister}
     * @param label the name of the label being declared
     * @param address the address of the line immediately following the label declaration
     * @throws IllegalStateException if the label already exists
     */
    public void addLabelDeclaration(String label, String address) {
        if (inTable(label)) {
            // Check if the label already exists, i.e. is the assembly code attempting to declare the same method twice
            throw new IllegalStateException("Attempting to declare a duplicate label. Labels may only be declared once.");
        } else {
            // Add the label declaration and the address to the symbol table
            SYMBOL_MAP.put(label, address);
        }
    }

    /**
     * Checks if a given symbol is already in the table
     * @param symbol the symbol being check
     * @return true if the symbol is already in the table, false otherwise
     */
    private boolean inTable(String symbol) {
        return SYMBOL_MAP.containsKey(symbol);
    }

    /**
     * Returns a symbol value, expecting that it already exists
     * @param symbol the key to the key-value pair
     * @return the value to the key-value pair
     */
    public String getSymbolValue(String symbol) {
        return SYMBOL_MAP.get(symbol);
    }

    /**
     * Initializes the 23 predefined symbols into the symbol table
     */
    private void initializePreDefinedSymbols() {
        SYMBOL_MAP.put("SCREEN", "16384");
        SYMBOL_MAP.put("KBD", "24576");
        SYMBOL_MAP.put("SP", "0");
        SYMBOL_MAP.put("LCL", "1");
        SYMBOL_MAP.put("ARG", "2");
        SYMBOL_MAP.put("THIS", "3");
        SYMBOL_MAP.put("THAT", "4");
        SYMBOL_MAP.put("R0", "0");
        SYMBOL_MAP.put("R1", "1");
        SYMBOL_MAP.put("R2", "2");
        SYMBOL_MAP.put("R3", "3");
        SYMBOL_MAP.put("R4", "4");
        SYMBOL_MAP.put("R5", "5");
        SYMBOL_MAP.put("R6", "6");
        SYMBOL_MAP.put("R7", "7");
        SYMBOL_MAP.put("R8", "8");
        SYMBOL_MAP.put("R9", "9");
        SYMBOL_MAP.put("R10", "10");
        SYMBOL_MAP.put("R11", "11");
        SYMBOL_MAP.put("R12", "12");
        SYMBOL_MAP.put("R13", "13");
        SYMBOL_MAP.put("R14", "14");
        SYMBOL_MAP.put("R15", "15");
    }
}
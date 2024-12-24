package n2t;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates each field into corresponding binary value
 */
public class Code {

    public String encodeDest(String destString) {
        return DEST_MAP.get(destString);
    } 

    public String encodeComp(String compString) {
        return COMP_MAP.get(compString);
    }

    public String encodeJump(String jumpString) {
        return JUMP_MAP.get(jumpString);
    }

    // Map containing each dest component mapped to the binary representation
    private static final Map<String, String> DEST_MAP = new HashMap<>();
    static {
        DEST_MAP.put("", "000");
        DEST_MAP.put("M", "001");
        DEST_MAP.put("D", "010");
        DEST_MAP.put("MD", "011");
        DEST_MAP.put("DM", "011");
        DEST_MAP.put("A", "100");
        DEST_MAP.put("AM", "101");
        DEST_MAP.put("AD", "110");
        DEST_MAP.put("AMD", "111");
        DEST_MAP.put("ADM", "111");
    }

    // Map containing each comp component mapped to the binary representation
    private static final Map<String, String> COMP_MAP = new HashMap<>();
    static {
        COMP_MAP.put("0", "0101010");
        COMP_MAP.put("1", "0111111");
        COMP_MAP.put("-1", "0111010");
        COMP_MAP.put("D", "0001100");
        COMP_MAP.put("A", "0110000");
        COMP_MAP.put("M", "1110000");
        COMP_MAP.put("!D", "0001101");
        COMP_MAP.put("!A", "0110001");
        COMP_MAP.put("!M", "1110001");
        COMP_MAP.put("-D", "0001111");
        COMP_MAP.put("-A", "0110011");
        COMP_MAP.put("-M", "1110011");
        COMP_MAP.put("D+1", "0011111");   
        COMP_MAP.put("A+1", "0110111");       
        COMP_MAP.put("M+1", "1110111");
        COMP_MAP.put("D-1", "0001110");
        COMP_MAP.put("A-1", "0110010");
        COMP_MAP.put("M-1", "1110010");
        COMP_MAP.put("D+A", "0000010");
        COMP_MAP.put("D+M", "1000010");
        COMP_MAP.put("D-A", "0010011");
        COMP_MAP.put("D-M", "1010011");
        COMP_MAP.put("A-D", "0000111");
        COMP_MAP.put("M-D", "1000111");
        COMP_MAP.put("D&A", "0000000");
        COMP_MAP.put("D&M", "1000000");
        COMP_MAP.put("D|A", "0010101");
        COMP_MAP.put("D|M", "1010101");
    }

    // Map containing each jump component mapped to the binary representation
    private static final Map<String, String> JUMP_MAP = new HashMap<>();
    static {
        JUMP_MAP.put("", "000");
        JUMP_MAP.put("JGT", "001");
        JUMP_MAP.put("JEQ", "010");
        JUMP_MAP.put("JGE", "011");
        JUMP_MAP.put("JLT", "100");
        JUMP_MAP.put("JNE", "101");
        JUMP_MAP.put("JLE", "110");
        JUMP_MAP.put("JMP", "111");
    }
}

package n2t;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import n2t.commands.Arithmetic;
import n2t.commands.CommandCall;
import n2t.commands.CommandFunction;
import n2t.commands.CommandIf;
import n2t.commands.CommandReturn;
import n2t.commands.CommandType;
import n2t.commands.Goto;
import n2t.commands.Label;
import n2t.commands.Pop;
import n2t.commands.Push;

/**
 * Holds the standard VM mapping for the Hack computer.
 */
public class Mapping {
  private Mapping() {}

  /**
   * Given a segment and index, returns the memory address of the segment[index].
   *
   * @param segment the VM segment
   * @param index the index of the segment
   * @return the memory address of the segment[index] as a String
   */
  public static String accessMemory(String segment, String index, String fileBaseName) {
    switch (segment) {
      case "local":
        return "LCL";
      case "argument":
        return "ARG";
      case "this":
        return "THIS";
      case "that":
        return "THAT";
      case "pointer":
        if (index.equals("0")) {
          return "THIS";
        } else {
          return "THAT";
        }
      case "temp":
        return Integer.toString(5 + Integer.parseInt(index));
      case "static":
        return fileBaseName + "." + index;
      default:
        throw new IllegalArgumentException("Unknown segment: " + segment);
    }
  }

  /**
   * Returns a new {@code CommandType} corresponding to the given line.
   *
   * @param commandElements the VM line elements
   * @return the mapped value of the given VM segment
   */
  public static CommandType getCommand(String[] commandElements) {
    Function<String[], CommandType> commandConstructor = COMMAND_MAP.get(commandElements[0]);

    if (commandConstructor == null) {
      throw new IllegalArgumentException("VM Code contains illegal command type: " 
      + commandElements[0]);
    }
    return COMMAND_MAP.get(commandElements[0]).apply(commandElements);
  }

  // A map of each command type to its corresponding constructor
  private static final Map<String, Function<String[], CommandType>> COMMAND_MAP
      = new HashMap<String, Function<String[], CommandType>>() {
        {
          put("add", Arithmetic::new);
          put("sub", Arithmetic::new);
          put("neg", Arithmetic::new);
          put("eq", Arithmetic::new);
          put("gt", Arithmetic::new);
          put("lt", Arithmetic::new);
          put("and", Arithmetic::new);
          put("or", Arithmetic::new);
          put("not", Arithmetic::new);
          put("push", Push::new);
          put("pop", Pop::new);
          put("label", Label::new);
          put("goto", Goto::new);
          put("if-goto", CommandIf::new);
          put("function", CommandFunction::new);
          put("call", CommandCall::new);
          put("return", CommandReturn::new);
        }
      };

  // A set of all the valid label characters
  private static final String FUNCTION_CHARS = "abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_.";

  // A set of all the valid function name characters
  private static final String LABEL_CHARS = FUNCTION_CHARS + ":$";

  // Create a hash set for function and label characters for more efficient lookup
  private static final Set<Character> VALID_FUNCTION_CHARS = new HashSet<>();

  static {
    for (char c : FUNCTION_CHARS.toCharArray()) {
      VALID_FUNCTION_CHARS.add(c);
    }
  }

  private static final Set<Character> VALID_LABEL_CHARS = new HashSet<>();

  static {
    for (char c : LABEL_CHARS.toCharArray()) {
      VALID_LABEL_CHARS.add(c);
    }
  }

  /**
   * Checks that all characters in the label are within
   * the valid label character set.
   *
   * @param functionName the name to check
   * @return true if the label is valid, false otherwise
   */
  public static boolean isValidFunction(String functionName) {
    return isValidName(functionName, VALID_FUNCTION_CHARS);
  }

  /**
   * Checks that all characters in the label are within
   * the valid label character set.
   *
   * @param label the label to check
   * @return true if the label is valid, false otherwise
   */
  public static boolean isValidLabel(String label) {
    return isValidName(label, VALID_LABEL_CHARS);
  }

  // Checks if all of the characters in the given label are within the given character set
  private static boolean isValidName(String label, Set<Character> validSet) {
    for (char c : label.toCharArray()) {
      if (!validSet.contains(c)) {
        return false;
      }
    }
    return true;
  }
}
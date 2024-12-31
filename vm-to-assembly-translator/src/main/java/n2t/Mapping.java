package n2t;

import java.util.HashMap;
import java.util.Map;
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
   * Returns the address within the stack at the given index
   * within the given stack segment.
   *
   * <p>If the segment is "constant", the index itself is returned as the address.
   *
   * @param segment the stack segment
   * @param index the index within the segment
   * @return the address within the stack at the given index
   */
  public static String calcStackAddress(String segment, String index) {
    if (segment.equals("constant")) {
      return index;
    } else {
      return Integer.toString(STACK.get(segment) + Integer.parseInt(index));
    }
  }

  /**
   * A map of each VM segment to its corresponding base memory address.
   */
  private static final Map<String, Integer> STACK
      = new HashMap<String, Integer>() {
        {
          put("local", 1);
          put("argument", 2);
          put("this", 3);
          put("that", 4);
          put("temp", 5);
          put("pointer", 6);
          put("static", 16);
        }
      };

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
          put("if", CommandIf::new);
          put("function", CommandFunction::new);
          put("call", CommandCall::new);
          put("return", CommandReturn::new);
        }
      };
}
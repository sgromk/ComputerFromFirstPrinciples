package n2t;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the standard VM mapping for the Hack computer.
 */
public class Mapping {
  Mapping() {}

  private static final Map<String, Integer> vmMap = new HashMap<String, Integer>() {
    {
      put("SP", 0);
      put("LCL", 1);
      put("ARG", 2);
      put("THIS", 3);
      put("THAT", 4);
    }
  };

  /**
   * Returns the CommandType corresponding to the given line.
   *
   * @param command the VM line elements
   * @return the mapped value of the given VM segment
   */
  public static CommandType getCommand(String[] command) {

    Map<String, CommandType> commandMap = new HashMap<String, CommandType>() {
      {
        put("add", new Arithmetic(command));
        put("sub", new Arithmetic(command));
        put("neg", new Arithmetic(command));
        put("eq", new Arithmetic(command));
        put("gt", new Arithmetic(command));
        put("lt", new Arithmetic(command));
        put("and", new Arithmetic(command));
        put("or", new Arithmetic(command));
        put("not", new Arithmetic(command));
        put("push", new Push(command));
        put("pop", new Pop(command));
        put("label", new Label(command));
        put("goto", new Goto(command));
        put("if", new If(command));
        put("function", new Function(command));
        put("call", new Call(command));
        put("return", new Return(command));
      }
    };

    return commandMap.get(command[0]);
  }


}
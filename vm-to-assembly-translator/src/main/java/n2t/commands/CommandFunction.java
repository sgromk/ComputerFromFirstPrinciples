package n2t.commands;

import n2t.CodeWriter;

/**
 * Represents the function command in the VM language.
 */
public class CommandFunction extends CommonCommand {
  /**
   * Constructor for the Function class.
   *
   * @param args the arguments of the command
   */
  public CommandFunction(String[] args) {
    super(args);
  }
  
  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) {
    codeWriter.writeFunction(this);
  }
}

package n2t.commands;

import n2t.CodeWriter;

/**
 * Represents the return command in the VM language.
 */
public class CommandReturn extends CommonCommand {
  /**
   * Constructor for the Return class.
   *
   * @param args the arguments of the command
   */
  public CommandReturn(String[] args) {
    super(args);
  } 

  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) {
    codeWriter.writeReturn(this);
  }
}

package n2t.commands;

import n2t.CodeWriter;

/**
 * Represents the If command in the VM language.
 */
public class CommandIf extends CommonCommand {
  /**
   * The constructor for the If command.
   *
   * @param args The arguments for the If command.
   */
  public CommandIf(String[] args) {
    super(args);
  } 

  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) {
    codeWriter.writeIf(this);
  }
}

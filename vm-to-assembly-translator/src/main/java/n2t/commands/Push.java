package n2t.commands;

import n2t.CodeWriter;

/**
 * Represents the push command in the VM language.
 */
public class Push extends CommonCommand {
  /**
   * Constructor for the Push class.
   *
   * @param args the arguments of the command
   */
  public Push(String[] args) {
    super(args);
  }

  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) {
    codeWriter.writePushPop(this);
  }
}

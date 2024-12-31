package n2t.commands;

import java.io.IOException;
import n2t.CodeWriter;

/**
 * Represents the call command in the VM language.
 */
public class CommandCall extends CommonCommand {
  /**
   * Constructor for the Call class.
   *
   * @param args the arguments of the command
   */
  public CommandCall(String[] args) {
    super(args);
  } 

  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) throws IOException {
    codeWriter.writeCall(this);
  }
}

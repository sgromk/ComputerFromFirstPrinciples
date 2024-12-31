package n2t.commands;

import java.io.IOException;
import n2t.CodeWriter;

/**
 * Represents the pop command in the VM language.
 */
public class Pop extends CommonCommand {
  /**
   * Constructor for the Pop class.
   *
   * @param args the arguments of the command
   */
  public Pop(String[] args) {
    super(args);
  }

  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) throws IOException {
    codeWriter.writePop(this);
  }
}

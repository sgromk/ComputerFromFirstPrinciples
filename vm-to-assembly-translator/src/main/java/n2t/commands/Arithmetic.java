package n2t.commands;

import java.io.IOException;
import n2t.CodeWriter;

/**
 * Represents the Arithmetic command in the VM language.
 */
public class Arithmetic extends CommonCommand {

  /**
   * Constructor for the Arithmetic class. Only expects a command
   * type in the arguments. Sets arg1 and arg2 to null.
   *
   * @param args the command type for the arithmetic command
   */
  public Arithmetic(String[] args) {
    super(args);
  }

  /**
   * Calls the appropriate write method in the {@code CodeWriter}.
   *
   * @param codeWriter the {@code CodeWriter} to write the command
   */
  public void writeCommand(CodeWriter codeWriter) throws IOException {
    codeWriter.writeArithmetic(this);
  }
}

package n2t.commands;

import java.io.IOException;
import n2t.CodeWriter;

/**
 * Represents the type of command in the VM language.
 */
public interface CommandType {
  /**
   * Retuns the type of command.
   *
   * @return the type of command as a String
   */
  public String typeCommand();

  /**
   * Returns the first argument of the command.
   *
   * @return the first argument as a String
   */ 
  public String arg1();

  /**
   * Returns the second argument of the command.
   *
   * @return the second argument as a String
   */
  public String arg2();

  /**
   * Calls the corresponding CodeWriter method to write the command.
   */
  public void writeCommand(CodeWriter writer) throws IOException;

  /**
   * Returns the original line of the command composed of the 
   * command type, arg1, arg2, if they exist.
   *
   * @return the original line of the command as a String
   */
  public String getOriginalLine();
}
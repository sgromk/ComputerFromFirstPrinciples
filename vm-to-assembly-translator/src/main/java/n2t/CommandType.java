package n2t;

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
}
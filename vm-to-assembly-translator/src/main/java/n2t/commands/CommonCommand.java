package n2t.commands;

/**
 * Consolidates the common fields of all VM commands.
 */
public abstract class CommonCommand implements CommandType {
  protected String commandType;
  protected String arg1;
  protected String arg2;

  /**
   * Constructor for the CommonCommand class.
   *
   * @param args the arguments of the command
   */
  public CommonCommand(String[] args) {
    this.commandType = args[0];
    this.arg1 = args.length > 1 ? args[1] : null; // Set to null if no arg1 exists
    this.arg2 = args.length > 2 ? args[2] : null; // Set to null if no arg2 exists
  }

  @Override
  public String typeCommand() {
    return commandType;
  }

  @Override
  public String arg1() {
    return arg1;
  }

  @Override
  public String arg2() {
    return arg2;
  }
    
  @Override
  public String getOriginalLine() {
    return commandType + (arg1 != null ? " " + arg1 : "") + (arg2 != null ? " " + arg2 : "");
  }
}

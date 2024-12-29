package n2t;

/**
 * Consolidates the common fields of all VM commands.
 */
public class CommonCommand implements CommandType {
  private String commandType;
  private String arg1;
  private String arg2;

  /**
   * Constructor for the CommonCommand class.
   *
   * @param args the arguments of the command
   */
  public CommonCommand(String[] args) {
    this.commandType = args[0];
    this.arg1 = args[1];
    this.arg2 = args[2];
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
    
}

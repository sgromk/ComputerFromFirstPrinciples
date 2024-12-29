package n2t;



/**
 * Parses each VM command, an entire line from the input file,
 * into the separate valid elements, to be given to the
 * {@code CodeWriter} to write to the output file.
 */
public class Parser {

  /**
   * Constructs a {@code Parser} to handle the input file.
   */
  public Parser() {
  }

  /**
   * Parses the current line of VM code into separate elements.
   *
   * @param currentLine the line of VM code to be parsed
   */
  public String[] parse(String currentLine) {
    String[] temp = {" ", " "};
    return temp;
  }
}
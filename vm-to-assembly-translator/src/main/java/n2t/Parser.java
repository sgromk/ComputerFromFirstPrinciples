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
    String codingLine = getCodingSegment(currentLine);

    // If the line is empty, return an empty array
    if (codingLine.isEmpty()) {
      return new String[] {};
    } else {
      return new String[] {" ", ""};
    }
  }

  /**
   * Returns the coding segment of the current line, excluding any comments or whitespace.
   *
   * @param currentLine the current line of VM code
   * @return the coding segment of the current line
   */
  private String getCodingSegment(String currentLine) {
    int commentIndex = currentLine.indexOf("//");

    if (commentIndex != -1) {
      return currentLine.substring(0, commentIndex).trim();
    } else {
      return currentLine.trim();
    }
  }
}
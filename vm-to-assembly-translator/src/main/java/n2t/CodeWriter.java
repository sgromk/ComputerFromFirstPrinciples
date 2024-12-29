package n2t;

/**
 * Writes the VM code into assembly code, and outputs it to the
 * output file.
 * 
 * <p>Assumes that fields are already separated into valid individual
 * elements by the {@code Parser}.
 * 
 * <p>Precedes each block of assembly code by a comment containing
 * the original line of VM code.
 */
public class CodeWriter {
  CodeWriter(String outPath) {
  } 

  /**
   * Writes the assembly code corresponding to each VM command element
   * to the output file.
   *
   * @param elements the individual elements of the VM command
   */
  public void write(String[] elements) {
    // Existing write logic
  }
}

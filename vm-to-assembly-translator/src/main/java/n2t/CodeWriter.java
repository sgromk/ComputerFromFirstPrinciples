package n2t;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import n2t.commands.CommandType;

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
  FileWriter writer;
  String fileBaseName;

  CodeWriter(String outPath, String fileBaseName) throws IOException {
    this.writer = new FileWriter(outPath);
    this.fileBaseName = fileBaseName;
  } 

  /**
   * Writes the assembly code corresponding to each VM command element
   * to the output file.
   *
   * <p>Empty lines are designated by {@code null} and ignored.
   *
   * @param command the VM command object that knows which CodeWriter method to call to write itself
   */
  public void write(CommandType command) throws IOException {
    if (command == null) {
      return;
    } else {
      try {
        writeLine("// " + command.getOriginalLine()); // Write VM code line as a comment
        command.writeCommand(this);
      } catch (Exception e) {
        System.out.println("Error writing command to file: " + e);
      }
    }
  }

  /**
   * Closes the output file.
   *
   * @throws IOException if the file cannot be closed
   */
  public void close() throws IOException {
    writer.close();
  }

  /**
   * Helper function to reduce visual clutter in each write method
   * by moving the call to {@code FileWriter} and the newline character
   * to a separate method.
   *
   * @param line the line to write to the output file
   * @throws IOException if the line cannot be written
   */
  private void writeLine(String line) throws IOException {
    writer.write(line + "\n");
  }

  /**
   * Writes the assembly code to incremenent the stack pointer.
   *
   * @throws IOException if the assembly code cannot be written
   */
  private void incrementStack() throws IOException {
    writeLine("@SP");
    writeLine("M=M+1");
  }

  /**
   * Writes the assembly code to decrement the stack pointer.
   *
   * @throws IOException if the assembly code cannot be written
   */
  private void decrementStack() throws IOException {
    writeLine("@SP");
    writeLine("M=M-1");
  }

  /**
   * Writes the assembly code to load the address of the given command to D.
   *
   * @param command the command to get the stack address for
   * @return the stack address as a string
   */
  private void loadAddressToD(CommandType command) throws IOException {
    // Set of segments that require dynamic memory allocation
    Set<String> dynamicSegments = Set.of("local", "argument", "this", "that");

    // Set the value of a constant to D directly
    if (command.arg1().equals("constant")) {
      writeLine("@" + command.arg2());
      writeLine("D=A");
    } else if (dynamicSegments.contains(command.arg1())) {
      // Save the base address to R13
      writeLine("@" + Mapping.accessMemory(command.arg1(), command.arg2(), fileBaseName));
      writeLine("D=M");
      writeLine("@R13");
      writeLine("M=D");
      // Save the index to D
      writeLine("@" + command.arg2());
      writeLine("D=A");
      // Add the index to the base address saved in R13
      writeLine("@R13");
      writeLine("D=D+M");
    } else {
      // Otherwise simply load the value of the segment[base + index] to D
      // This applies to static, temp, and pointer segments
      writeLine("@" + Mapping.accessMemory(command.arg1(), command.arg2(), fileBaseName));
      writeLine("D=M");
    }
  }

  /**
   * Writes the assembly code for the given arithmetic command.
   *
   * @param command the arithmetic command
   */
  public void writeArithmetic(CommandType command) throws IOException {
    String arithmeticType = command.typeCommand();

    // Only pop off a single item for unary functions
    decrementStack();
    if (arithmeticType.equals("neg")) {
      writeNeg();
    } else if (arithmeticType.equals("not")) {
      writeNot();
    } else {
      // Save y to D
      writeLine("D=M");
      decrementStack();

      switch (arithmeticType) {
        case "add":
          return;
        default:
          return;
      }
    }
  }

  // TODO: Implement the following methods
  /**
   * Writes the assembly code for the negation command.
   */
  private void writeNeg() {
    return;
  }

  /**
   * Writes the assembly code for the not command.
   */
  private void writeNot() {
    return;
  }

  /**
   * Writes the assembly code for the given push or pop command.
   *
   * @param command the push or pop command
   */
  public void writePush(CommandType command) throws IOException {
    loadAddressToD(command);
    writeLine("@SP");                      // Go to *SP
    writeLine("A=M");
    writeLine("M=D");                      // Push D to stack
    incrementStack();
  }

  /**
   * Writes the assembly code for the given pop command.
   *
   * @param command the pop command
   */
  public void writePop(CommandType command) throws IOException {
    loadAddressToD(command);                        // Load the target address to D
    writeLine("@R13");                       // Save the address temporarily
    writeLine("M=D");
    decrementStack();                               // A now points to popped value
    writeLine("D=M");                      // Load the value to D
    writeLine("@R13");                     // Set the value at the saved address to D
    writeLine("M=D");
  }

  /**
   * Writes the assembly code for the given label command.
   *
   * @param command the label command
   */
  public void writeLabel(CommandType command) throws IOException {
    // Existing writeLabel logic
  }

  /**
   * Writes the assembly code for the given goto command.
   *
   * @param command the goto command
   */
  public void writeGoto(CommandType command) throws IOException {
    // Existing writeGoto logic
  }

  /**
   * Writes the assembly code for the given if-goto command.
   *
   * @param command the if command
   */
  public void writeIf(CommandType command) throws IOException {
    // Existing writeIf logic
  }

  /**
   * Writes the assembly code for the given function command.
   *
   * @param command the function command
   */
  public void writeFunction(CommandType command) throws IOException{
    // Existing writeFunction logic
  }

  /**
   * Writes the assembly code for the given call command.
   *
   * @param command the call command
   */
  public void writeReturn(CommandType command) throws IOException {
    // Existing writeReturn logic
  }

  /**
   * Writes the assembly code for the given return command.
   *
   * @param command the return command
   */
  public void writeCall(CommandType command) throws IOException {
    // Existing writeCall logic
  }
}

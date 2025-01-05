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
  int logicalCounter;

  CodeWriter(String outPath, String fileBaseName) throws IOException {
    this.writer = new FileWriter(outPath);
    this.fileBaseName = fileBaseName;
    this.logicalCounter = 0;
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
    Set<String> staticSegments = Set.of("pointer", "temp", "static");

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
    } else if (staticSegments.contains(command.arg1())) {
      // Otherwise simply load the value of the segment[base + index] to D
      writeLine("@" + Mapping.accessMemory(command.arg1(), command.arg2(), fileBaseName));
      writeLine("D=A");
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
    // Go to the item at the top of the stack
    decrementStack();
    writeLine("A=M");

    if (arithmeticType.equals("neg")) {
      writeLine("M=-M");
    } else if (arithmeticType.equals("not")) {
      writeLine("M=!M");
    } else {

      // Save y to D then go to x
      writeLine("D=M");
      decrementStack();
      writeLine("A=M");

      // Perform binary functions
      switch (arithmeticType) {
        case "add":
          writeLine("M=D+M");   // Add y to x
          break;
        case "sub":
          writeLine("M=D-M");   // Subtract x from y
          writeLine("M=-M");          // Negate the result
          break;
        case "eq":
          // Check if x == y and push the result to the top of the stack
          writeBoolean("JEQ", "JNE");
          break;          
        case "gt":
          // Check if x > y and push the result to the top of the stack
          writeBoolean("JGT", "JLE");
          break;
        case "lt":
          // Check if x < y and push the result to the top of the stack
          writeBoolean("JLT", "JGE");
          break;
        case "and":
          writeLine("M=D&M");   // Bitwise AND x and y
          break;
        case "or":
          writeLine("M=D|M");   // Bitwise OR x and y
          break;
        default:
          break;
      }
      // Set the stack pointer back to the top
    }
    incrementStack();
    writeLine("");  // Empty line following command
  }

  /**
   * Writes the assembly code for the given boolean comparison.
   *
   * @param comparisonTrue the jump condition for true
   * @param comparisonFalse the jump condition for false
   * @throws IOException if the assembly code cannot be written
   */
  private void writeBoolean(String comparisonTrue, String comparisonFalse) throws IOException {
    writeLine("D=M-D");  // Subtract y from x
    checkTrueFalse(comparisonTrue, comparisonFalse);   // Pushes the result to the stack
    logicalCounter++;         // Increment the logical function counter
  }

  /**
   * Sets and then jumps to true or false labels based on the result of the comparison,
   * then pushes the result to the stack.
   *
   * <p>Each true and false label is followed by the current logical counter value.
   *
   * @param comparisonTrue the jump condition for true
   * @param comparisonFalse the jump condition for false
   * @throws IOException if the assembly code cannot be written
   */
  private void checkTrueFalse(String comparisonTrue, String comparisonFalse) throws IOException {
    writeLine("@TRUE_" + logicalCounter); 
    writeLine("D;" + comparisonTrue);             // Jump to true if the comparison is true
    writeLine("@FALSE_" + logicalCounter);
    writeLine("D;" + comparisonFalse);            // Jump to false if the comparison is false
    writeLine("\n(TRUE_" + logicalCounter + ")");
    writeLine("D=-1");                       // Set D to true
    writeLine("@LOGICAL_END_" + logicalCounter);  // Jump to the end of the logical function
    writeLine("0;JMP");
    writeLine("\n(FALSE_" + logicalCounter + ")");
    writeLine("D=0");                        // Set D to false
    writeLine("@LOGICAL_END_" + logicalCounter);  // Jump to the end of the logical function
    writeLine("\n(LOGICAL_END_" + logicalCounter + ")");
    writeLine("@SP");
    writeLine("A=M");
    writeLine("M=D");                        // Push the result to the stack
  }

  /**
   * Writes the assembly code for the given push or pop command.
   *
   * @param command the push or pop command
   */
  public void writePush(CommandType command) throws IOException {
    loadAddressToD(command);
    if (!command.arg1().equals("constant")) {
      writeLine("A=D");  // Go to the target address
      writeLine("D=M");  // Save the value to D
    }
    writeLine("@SP");  // Go to *SP
    writeLine("A=M");
    writeLine("M=D");  // Push D to stack
    incrementStack();
    writeLine("");     // Empty line following command
  }

  /**
   * Writes the assembly code for the given pop command.
   *
   * @param command the pop command
   */
  public void writePop(CommandType command) throws IOException {
    loadAddressToD(command);  // Load the target address to D
    writeLine("@R14");   
    writeLine("M=D");    // Save the target address to R14
    decrementStack();         // Decrement the stack pointer
    writeLine("A=M");    // Go to the top of the stack
    writeLine("D=M");    // Save the value to D
    writeLine("@R14");
    writeLine("A=M");    // Go to the target address
    writeLine("M=D");    // Save the value to the target address
    writeLine("");       // Empty line following command
  }

  /**
   * Writes the assembly code for the given label command.
   *
   * @param command the label command
   */
  public void writeLabel(CommandType command) throws IOException {
    if (Mapping.isValidLabel(command.arg1())) {
      writeLine("(" + command.arg1() + ")");
    } else {
      throw new IllegalArgumentException("Label contains illegal characters: " + command.arg1());
    }
  }

  /**
   * Writes the assembly code for the given goto command.
   *
   * <p>Does not check if the label exists in the file.
   *
   * @param command the goto command
   */
  public void writeGoto(CommandType command) throws IOException {
    writeLine("@" + command.arg1());
    writeLine("0;JMP");
  }

  // TODO: Check that label exists within same function, uses
  // method bar in function Foo to jump to Foo$bar
  // Needs some way to see what the current function is
  /**
   * Writes the assembly code for the given if-goto command.
   *
   * @param command the if command
   */
  public void writeIf(CommandType command) throws IOException {
    if (Mapping.isValidLabel(command.arg1())) {
      decrementStack();         // Decrement the stack pointer
      writeLine("A=M");    // Go to value at the top of the stack
      writeLine("D=M");    // Save the value to D
      writeLine("@" + command.arg1());
      writeLine("D;JNE");  // Jump if the value is not equal to zero
    }
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

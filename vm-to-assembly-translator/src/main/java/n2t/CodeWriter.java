package n2t;

import java.io.FileWriter;
import java.io.IOException;
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

  CodeWriter(String outPath) throws IOException {
    this.writer = new FileWriter(outPath);
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
        writer.write("// " + command.getOriginalLine() + "\n"); // Write VM code line as a comment
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
   * Writes the assembly code to incremenent the stack pointer.
   *
   * @throws IOException if the assembly code cannot be written
   */
  private void incrementStack() throws IOException {
    writer.write("@0\n");
    writer.write("M=M+1\n");
  }

  /**
   * Writes the assembly code to decrement the stack pointer.
   *
   * @throws IOException if the assembly code cannot be written
   */
  private void decrementStack() throws IOException {
    writer.write("@0\n");
    writer.write("M=M-1\n");
  }

  // TODO: Fix logic, needs to go to address first, then add index to segment
  /**
   * Convenience method to get the stack address for the given command.
   *
   * @param command the command to get the stack address for
   * @return the stack address as a string
   */
  private String getStackAddress(CommandType command) {
    return "@" + Mapping.calcStackAddress(command.arg1(), command.arg2());
  }

  /**
   * Writes the assembly code for the given arithmetic command.
   *
   * @param command the arithmetic command
   */
  public void writeArithmetic(CommandType command) throws IOException {
    // Existing writeArithmetic logic
  }

  /**
   * Writes the assembly code for the given push or pop command.
   *
   * @param command the push or pop command
   */
  public void writePush(CommandType command) throws IOException {
    writer.write(getStackAddress(command) + "\n");  // @address
    writer.write("D=M\n");                      // D = *address
    writer.write("@0\n");                       // A = 0
    writer.write("A=M\n");                      // A = *0
    incrementStack();                               // *0++
  }

  /**
   * Writes the assembly code for the given pop command.
   *
   * @param command the pop command
   */
  public void writePop(CommandType command) throws IOException {
    decrementStack();
    writer.write("A=M\n");                      // A = *0
    writer.write("D=M\n");                      // D = *A
    writer.write(getStackAddress(command) + "\n");  // @address
    writer.write("M=D\n");                      // *address = D
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

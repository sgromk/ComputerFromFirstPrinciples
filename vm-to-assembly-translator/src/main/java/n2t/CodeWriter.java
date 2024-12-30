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
  public void write(CommandType command) {
    if (command == null) {
      return;
    } else {
      command.writeCommand(this);
    }
  }

  /**
   * Writes the assembly code for the given arithmetic command.
   *
   * @param command the arithmetic command
   */
  public void writeArithmetic(CommandType command) {
    // Existing writeArithmetic logic
  }

  /**
   * Writes the assembly code for the given push or pop command.
   *
   * @param command the push or pop command
   */
  public void writePushPop(CommandType command) {
    // Existing writePushPop logic
  }

  /**
   * Writes the assembly code for the given label command.
   *
   * @param command the label command
   */
  public void writeLabel(CommandType command) {
    // Existing writeLabel logic
  }

  /**
   * Writes the assembly code for the given goto command.
   *
   * @param command the goto command
   */
  public void writeGoto(CommandType command) {
    // Existing writeGoto logic
  }

  /**
   * Writes the assembly code for the given if-goto command.
   *
   * @param command the if command
   */
  public void writeIf(CommandType command) {
    // Existing writeIf logic
  }

  /**
   * Writes the assembly code for the given function command.
   *
   * @param command the function command
   */
  public void writeFunction(CommandType command) {
    // Existing writeFunction logic
  }

  /**
   * Writes the assembly code for the given call command.
   *
   * @param command the call command
   */
  public void writeReturn(CommandType command) {
    // Existing writeReturn logic
  }

  /**
   * Writes the assembly code for the given return command.
   *
   * @param command the return command
   */
  public void writeCall(CommandType command) {
    // Existing writeCall logic
  }
}

package n2t;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * Drives the translation process from virtual machine code to assembly language and
 * serves as the backbone for the Jack language compiler.
 * 
 * <p>Constructs a {@code Parser} to handle the input file.
 * Constructs a {@code CodeWriter} to handle the output file.
 * 
 * <p>Reads through each line of the input file, writing the corresponding assembly
 * code to the output file underneath a comment of the original VM code line.
 * 
 * <p>Comments are denoted by the "//" characters, and are ignored by the assembler.
 */
public class Main {
  /**
  * Takes the name of an input file containing VM code and translates it to 
  * assembly language in an output file containing the same base name with 
  * a new .asm extension.
  *
  * @param args the name of the input file
  */
  public static void main(String[] args) {
    // Check that the .vm file path is provided
    if (args.length != 1 || !args[0].endsWith(".vm")) {
      System.out.println("Usage: java n2t.Main 'file.vm'");
      // return;
    }

    String inPath = args[0];

    Path filePath = Paths.get(inPath);
    String fileName = filePath.getFileName().toString();
    String fileBaseName = fileName.substring(0, fileName.lastIndexOf('.'));

    // Create the .asm output file path
    String outPath = inPath.replaceAll("\\.\\w+$", ".asm");

    try {
      // Construct the Parser and CodeWriter
      Parser parser = new Parser();
      CodeWriter codeWriter = new CodeWriter(outPath, fileBaseName);

      InputStream is = new FileInputStream(inPath);
      Scanner scan = new Scanner(is);

      // Iterate over each line of the input file and write the corresponding
      // assembly code to the output file
      while (scan.hasNextLine()) {
        String currentLine = scan.nextLine();
        codeWriter.write(parser.parse(currentLine));
      }
      scan.close();
      codeWriter.close();
      
    } catch (Exception e) {
      System.out.println("Error translating file: " + e);
    }
  }
}

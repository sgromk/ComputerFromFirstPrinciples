package n2t;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Drives the translation process from virtual machine code to assembly language and
 * serves as the backbone for the Jack language compiler.
 * 
 * <p>Constructs a {@code Parser} to handle the input file or directory.
 * Constructs a {@code CodeWriter} to handle the output file.
 * 
 * <p>Reads through each line of the input file or directory, writing the corresponding assembly
 * code to the output file underneath a comment of the original VM code line.
 * 
 * <p>Comments are denoted by the "//" characters, and are ignored by the assembler.
 */
public class Main {
  /**
  * Takes the name of an input file or directory of files containing VM code and translates it to 
  * assembly language in an output file containing the same base name with a new .asm extension.
  *
  * @param args the name of the input file or directory
  */
  public static void main(String[] args) {
    // Initialize the input file path, base name, output file path, and list of VM files
    String inPathString = "";
    String outPath = "";
    List<String> vmFiles = new ArrayList<String>();
    String fileBaseName = "";

    // If no file or directory is provided, use the current directory by default
    if (args.length == 0) {
      inPathString = System.getProperty("user.dir");
    } else {
      // Otherwise use the given string
      inPathString = args[0];
    }
    Path inPath = Paths.get(inPathString);

    if (Files.isDirectory(inPath)) {
      // Creating the .asm output file path and base name for label declarations
      outPath = inPathString + ".asm";
      vmFiles = getFileList(inPath);
    } else if (args[0].endsWith(".vm")) {
      // Check that the provided file path ends in .vm then runs translater

      vmFiles.add(inPathString);
      // Create the .asm output file path
      outPath = inPathString.replaceAll("\\.\\w+$", ".asm");
    } else {
      System.out.println("Usage: java n2t.Main 'file.vm or directory'");
    }

    try {
      // Construct the Parser and CodeWriter
      Parser parser = new Parser();
      CodeWriter codeWriter = new CodeWriter(outPath);

      Path currentVmFilePath = null;
      String currentVmFileString = "";

      // Run the translator on each .vm file in the directory,
      // writing the assembly code to the same .asm file
      for (String vmFile : vmFiles) {
        try {
          InputStream is = new FileInputStream(vmFile);

          // Set the CodeWriter's current file name to the current .vm file
          currentVmFilePath = Paths.get(vmFile);
          currentVmFileString = currentVmFilePath.getFileName().toString();
          fileBaseName = currentVmFileString.substring(0, currentVmFileString.lastIndexOf('.'));
          codeWriter.setCurrentFileName(fileBaseName);

          // Iterate over each line of the input file and write the corresponding
          // assembly code to the output file
          try (Scanner scan = new Scanner(is)) {
            while (scan.hasNextLine()) {
              String currentLine = scan.nextLine();
              codeWriter.write(parser.parse(currentLine));
            }
          }
        } catch (Exception e) {
          System.out.println("Error translating file " + vmFile + ": " + e);
        }
      }
      // Close the output file after all files are translated
      codeWriter.close();
      System.out.println("Translation complete.");
    } catch (Exception e) {
      System.out.println("Error constructing Parser or CodeWriter using given name: " + e);
    }
  }

  /**
   * Returns a list of all the .vm files in the given directory.
   *
   * @param inPath the file or directory to search for .vm files
   * @return a list of all the .vm files in the given directory
   */
  public static List<String> getFileList(Path inPath) {
    File directory = inPath.toFile();
    File[] vmFiles = directory.listFiles();
    List<String> outputFileList = new ArrayList<>();

    for (File file : vmFiles) {
      if (file.getName().endsWith(".vm")) {
        outputFileList.add(file.getAbsolutePath());
      }
    }
    return outputFileList;
  }
}
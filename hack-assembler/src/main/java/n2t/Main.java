package n2t;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Initializes the I/O files and manages file reading/writing
 */
public class Main {
   
   // Program entry point
   public static void main(String[] args)
        throws FileNotFoundException 
    {
        Main assembler = new Main();
        assembler.run(args);
    }

    // Runs the assembler
    private void run(String[] args) throws FileNotFoundException {
        // Check if the assembly file has been provided
        if (args.length < 1) {
            System.out.println("Usage: java n2t.Main 'file.path'");
            return;
        }

        // Get the file path from the input args and use it to set an output
        // path with the .hack extension
        String path = args[0];
        String outPath = path.replaceAll("\\.\\w+$", ".hack");

        try {
            // Initializing Parser, SymbolTable, and Code modules
            Parser parser = new Parser();

            InputStream is = new FileInputStream(path);

            try (Scanner scan = new Scanner(is)) {

                // Perform the first-pass scan through, looking for label declarations
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();
                    parser.checkLabelDeclaration(currentLine);
                }


            } catch (Exception e) {
                System.out.println("Error during first-pass read for label declarations: " + e);
            }

            // Close and re-open the file to start back at the top
            is.close();
            is = new FileInputStream(path);

            // Create a new scanner to go through the file
            try (Scanner scan = new Scanner(is)) {
                
                FileWriter outputFileWriter = new FileWriter(outPath);
                
                // Perform the second-pass, parsing each line until the end of line
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();
                    currentLine = parser.parseLine(currentLine);
                    
                    // If the line is not empty, write the binary translation to the output file
                    if (!currentLine.isEmpty()) {
                        outputFileWriter.write(currentLine + "\n");
                    }
                }

                // Close the FileWriter
                outputFileWriter.close();

            } catch (Exception e) {
                System.out.println("Error during second-pass read and translation: " + e);
            }
            System.out.println("Assembler finished.");
        } catch (IOException e) {
            System.out.println("Error occured creating the output file.");
            e.printStackTrace();
        }
    }
}
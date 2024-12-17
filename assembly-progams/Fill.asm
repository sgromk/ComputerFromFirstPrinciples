// Fill.asm
// When a key is pressed, the screen turns black, when no key is
// pressed, the screen turns white

@CHECK_KEY_PRESS_LOOP
0;JMP

(CHECK_KEY_PRESS_LOOP)

@SCREEN
D=A
@address
M=D         // Save the location of the screen

@KBD
D=M         // Get value from keyboard

@CHECK_ALREADY_WHITE
D;JEQ       // If a key is unpressed, go to the key unpressed loop

@CHECK_ALREADY_BLACK
0;JMP       // Otherwise go to the key pressed loop

(CHECK_ALREADY_BLACK)

@address
A=M         // Go to the address for first register of screen
	    // Could use 16384 directly
D=M         // Save the value at the address
@CHECK_KEY_PRESS_LOOP
D;JLT       // If first register is already black (-1), exit early

@PAINT_BLACK_LOOP
0;JMP       // Otherwise start painting the screen black

(PAINT_BLACK_LOOP)

@24576
D=A         // 16384 + 8192 16-bit registers of the screen
@address
D=D-M       // Check if the address >= 24576

@CHECK_KEY_PRESS_LOOP
D;JLE       // If the end of the screen has been reached, exit the paint loop

@address
A=M         // Otherwise, go to the address
M=-1        // Set entire register to black

@address
M=M+1       // Increment the address

@PAINT_BLACK_LOOP
0;JMP       // Go to next iteration of paint black loop


// ************************************************************* //

(CHECK_ALREADY_WHITE)

@address
A=M         // Go to the address for first register of screen
D=M         // Save the value at the address
@CHECK_KEY_PRESS_LOOP
D;JEQ       // If first register is already white (0), exit early

@PAINT_WHITE_LOOP
0;JMP       // Otherwise start painting the screen white

(PAINT_WHITE_LOOP)

@24576
D=A         // 16384 + 8192 16-bit registers of the screen
@address
D=D-M       // Check if the address >= 24576

@CHECK_KEY_PRESS_LOOP
D;JLE       // If the end of the screen has been reached, exit the paint loop

@address
A=M         // Otherwise go to the address
M=0         // Set entire register to white

@address 
M=M+1       // Increment the address

@PAINT_WHITE_LOOP
0;JMP       // Go to next iteration of paint white loop
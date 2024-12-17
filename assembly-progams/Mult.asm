// mult.asm
// Multiplies the number stored in R[0] by the number
// stored in R[1]. Assumes both numbers are greater than 0,
// and that the output number is less than 32768
// Result is stored in R2

@1         
D=M         // Grab the number in R1 

@count      // Store it in variable 'count' To allow decrement every loop
M=D         // R1 could be used directly, but this avoids mutation
            // Alternatively, could start count at 0 and increment until count = R1

@LOOP
0;JMP       // Begin loop

(LOOP)
@count
D=M         // Get the loop iteration counter

@END
D;JEQ       // If the count has reached zero, end the program

@0
D=M         // Get the number in R0

@2
M=D+M       // Add the R0 number to the running total

@count
M=M-1       // Decrement the counter

@LOOP
0;JMP       // Go to next loop iteration

(END)
@END        
0;JMP       // Infinite loop at end
# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# code for fib
fib:
# Entering a new scope.
# Symbols in symbol table:
#  println 
#  i 
#  return 
# Update the stack pointer.
addi $sp $sp 0


# Calling function fib
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 -12($sp)
# Evaluate parameters and save to stack
lw $t1 -4($sp)
li $t2 1
sub $t1 $t1 $t2
sw $t1 -16($sp)
# Update the stack pointer
addi $sp $sp -12
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 12
# Restore $t0-9 registers
lw $t0 -12($sp)
# Restore $ra
move $ra $t0
# Load the value returned by the function
lw $t1 -20($sp)
# Calling function fib
# Save $ra to a register
move $t2 $ra
# Save $t0-9 registers
sw $t0 -12($sp)
sw $t1 -16($sp)
sw $t2 -20($sp)
# Evaluate parameters and save to stack
lw $t3 -4($sp)
li $t4 2
sub $t3 $t3 $t4
sw $t3 -24($sp)
# Update the stack pointer
addi $sp $sp -20
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 20
# Restore $t0-9 registers
lw $t0 -12($sp)
lw $t1 -16($sp)
lw $t2 -20($sp)
# Restore $ra
move $ra $t2
# Load the value returned by the function
lw $t3 -28($sp)
add $t1 $t1 $t3
sw $t1 -8($sp)

# Exiting scope.
addi $sp $sp 0
jr $ra

# code for main
main:
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp 0
# Calling function println
la $a0 datalabel0
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t1 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
# Evaluate parameters and save to stack
li $t3 0
sw $t3 -16($sp)
# Update the stack pointer
addi $sp $sp -12
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 12
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
# Restore $ra
move $ra $t1
# Load the value returned by the function
lw $t3 -20($sp)
move $a0 $t3
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t3 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
# Evaluate parameters and save to stack
li $t4 1
sw $t4 -20($sp)
# Update the stack pointer
addi $sp $sp -16
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 16
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
# Restore $ra
move $ra $t3
# Load the value returned by the function
lw $t4 -24($sp)
move $a0 $t4
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t4 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
# Evaluate parameters and save to stack
li $t5 2
sw $t5 -24($sp)
# Update the stack pointer
addi $sp $sp -20
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 20
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
# Restore $ra
move $ra $t4
# Load the value returned by the function
lw $t5 -28($sp)
move $a0 $t5
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t5 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
# Evaluate parameters and save to stack
li $t6 3
sw $t6 -28($sp)
# Update the stack pointer
addi $sp $sp -24
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 24
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
# Restore $ra
move $ra $t5
# Load the value returned by the function
lw $t6 -32($sp)
move $a0 $t6
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t6 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
# Evaluate parameters and save to stack
li $t7 4
sw $t7 -32($sp)
# Update the stack pointer
addi $sp $sp -28
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 28
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
lw $t6 -28($sp)
# Restore $ra
move $ra $t6
# Load the value returned by the function
lw $t7 -36($sp)
move $a0 $t7
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t7 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
sw $t7 -32($sp)
# Evaluate parameters and save to stack
li $t8 5
sw $t8 -36($sp)
# Update the stack pointer
addi $sp $sp -32
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 32
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
lw $t6 -28($sp)
lw $t7 -32($sp)
# Restore $ra
move $ra $t7
# Load the value returned by the function
lw $t8 -40($sp)
move $a0 $t8
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t8 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
sw $t7 -32($sp)
sw $t8 -36($sp)
# Evaluate parameters and save to stack
li $t9 6
sw $t9 -40($sp)
# Update the stack pointer
addi $sp $sp -36
# Call the function
jal fib
# Restore the stack pointer
addi $sp $sp 36
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
lw $t3 -16($sp)
lw $t4 -20($sp)
lw $t5 -24($sp)
lw $t6 -28($sp)
lw $t7 -32($sp)
lw $t8 -36($sp)
# Restore $ra
move $ra $t8
# Load the value returned by the function
lw $t9 -44($sp)
move $a0 $t9
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function fib
# Save $ra to a register
move $t9 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
sw $t3 -16($sp)
sw $t4 -20($sp)
sw $t5 -24($sp)
sw $t6 -28($sp)
sw $t7 -32($sp)
sw $t8 -36($sp)
sw $t9 -40($sp)
# Evaluate parameters and save to stack
li null 7
sw null -44($sp)

# All memory structures are placed after the
# .data assembler directive
.data

newline:      .asciiz "\n"
datalabel0:   .asciiz "This program prints the first 11 numbers of the Fibonacci sequence"

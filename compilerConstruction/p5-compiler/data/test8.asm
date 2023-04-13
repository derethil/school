# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# code for add
add:
# Entering a new scope.
# Symbols in symbol table:
#  println 
#  x 
#  y 
#  return 
# Update the stack pointer.
addi $sp $sp 0
lw $t0 -4($sp)
lw $t1 -8($sp)
add $t0 $t0 $t1
sw $t0 -12($sp)

# Exiting scope.
addi $sp $sp 0
jr $ra

# code for add2
add2:
# Entering a new scope.
# Symbols in symbol table:
#  println 
#  x 
#  y 
#  return 
# Update the stack pointer.
addi $sp $sp 0
# Calling function add
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 -16($sp)
# Evaluate parameters and save to stack
# Calling function add
# Save $ra to a register
move $t1 $ra
# Save $t0-9 registers
sw $t0 -16($sp)
sw $t1 -20($sp)
# Evaluate parameters and save to stack
lw $t2 -4($sp)
sw $t2 -24($sp)
lw $t2 -8($sp)
sw $t2 -28($sp)
# Update the stack pointer
addi $sp $sp -20
# Call the function
jal add
# Restore the stack pointer
addi $sp $sp 20
# Restore $t0-9 registers
lw $t0 -16($sp)
lw $t1 -20($sp)
# Restore $ra
move $ra $t1
# Load the value returned by the function
lw $t2 -32($sp)
sw $t2 -20($sp)
li $t2 1
sw $t2 -24($sp)
# Update the stack pointer
addi $sp $sp -16
# Call the function
jal add
# Restore the stack pointer
addi $sp $sp 16
# Restore $t0-9 registers
lw $t0 -16($sp)
lw $t1 -20($sp)
# Restore $ra
move $ra $t0
# Load the value returned by the function
lw $t2 -28($sp)
sw $t2 -12($sp)

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
# Calling function add2
# Save $ra to a register
move $t2 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
sw $t1 -8($sp)
sw $t2 -12($sp)
# Evaluate parameters and save to stack
li $t3 2
sw $t3 -16($sp)
li $t3 4
sw $t3 -20($sp)
# Update the stack pointer
addi $sp $sp -12
# Call the function
jal add2
# Restore the stack pointer
addi $sp $sp 12
# Restore $t0-9 registers
lw $t0 -4($sp)
lw $t1 -8($sp)
lw $t2 -12($sp)
# Restore $ra
move $ra $t2
# Load the value returned by the function
lw $t3 -24($sp)
move $a0 $t3
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 0
li $v0 10
syscall

# All memory structures are placed after the
# .data assembler directive
.data

newline:      .asciiz "\n"
datalabel0:   .asciiz "This program prints 7"

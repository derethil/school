# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# code for identity
identity:
# Entering a new scope.
# Symbols in symbol table:
#  println 
#  x 
#  return 
# Update the stack pointer.
addi $sp $sp 0
lw $t0 -4($sp)
sw $t0 -8($sp)

# Exiting scope.
addi $sp $sp 0
jr $ra

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

# code for main
main:
# Entering a new scope.
# Symbols in symbol table:
#  println 
#  return 
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
# Calling function identity
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 -8($sp)
# Evaluate parameters and save to stack
li $t1 7
sw $t1 -12($sp)
# Update the stack pointer
addi $sp $sp -12
# Call the function
jal identity
# Restore the stack pointer
addi $sp $sp 12
# Restore $t0-9 registers
lw $t0 -8($sp)
# Restore $ra
move $ra $t0
# Load the value returned by the function
lw $t1 -16($sp)
move $a0 $t1
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
# Calling function add
# Save $ra to a register
move $t1 $ra
# Save $t0-9 registers
sw $t0 -8($sp)
sw $t1 -12($sp)
# Evaluate parameters and save to stack
li $t2 3
sw $t2 -16($sp)
li $t2 4
sw $t2 -20($sp)
# Update the stack pointer
addi $sp $sp -16
# Call the function
jal add
# Restore the stack pointer
addi $sp $sp 16
# Restore $t0-9 registers
lw $t0 -8($sp)
lw $t1 -12($sp)
# Restore $ra
move $ra $t1
# Load the value returned by the function
lw $t2 -24($sp)
move $a0 $t2
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
datalabel0:   .asciiz "This program prints 7 7"

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
#  i 
#  y 
# Update the stack pointer.
addi $sp $sp 0

# Calling function println
lw $t0 -8($sp)
lw $t1 -12($sp)
add $t0 $t0 $t1
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 0
jr $ra

# code for main
main:
# Entering a new scope.
# Symbols in symbol table:
#  a 
#  println 
#  b 
# Update the stack pointer.
addi $sp $sp 0


# Calling function println
la $a0 datalabel0
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Calling function add
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 -12($sp)
# Evaluate parameters and save to stack
li $t1 3
sw $t1 -16($sp)
li $t1 4
sw $t1 -20($sp)
# Update the stack pointer
addi $sp $sp -12
# Call the function
jal add
# Restore the stack pointer
addi $sp $sp 12
# Restore $t0-9 registers
lw $t0 -12($sp)
# Restore $ra
move $ra $t0

li $t1 5
sw $t1 -4($sp)

li $t1 2
sw $t1 -8($sp)

# Calling function add
# Save $ra to a register
move $t1 $ra
# Save $t0-9 registers
sw $t0 -12($sp)
sw $t1 -16($sp)
# Evaluate parameters and save to stack
lw $t2 -4($sp)
sw $t2 -20($sp)
lw $t2 -8($sp)
sw $t2 -24($sp)
# Update the stack pointer
addi $sp $sp -16
# Call the function
jal add
# Restore the stack pointer
addi $sp $sp 16
# Restore $t0-9 registers
lw $t0 -12($sp)
lw $t1 -16($sp)
# Restore $ra
move $ra $t1

# Exiting scope.
addi $sp $sp 0
li $v0 10
syscall

# All memory structures are placed after the
# .data assembler directive
.data

newline:      .asciiz "\n"
datalabel0:   .asciiz "This program prints 7 7"

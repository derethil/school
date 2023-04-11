# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# code for foo
foo:
# Entering a new scope.
# Symbols in symbol table:
#  println 
#  return 
# Update the stack pointer.
addi $sp $sp 0
# Calling function println
li $t0 7
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 0
jr $ra
# code for fum
fum:
# Entering a new scope.
# Symbols in symbol table:
#  a 
#  println 
#  b 
# Update the stack pointer.
addi $sp $sp 0


li $t0 9
sw $t0 -4($sp)

li $t0 12
sw $t0 -8($sp)

# Calling function println
lw $t0 -8($sp)
lw $t1 -4($sp)
sub $t0 $t0 $t1
li $t1 4
add $t0 $t0 $t1
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function foo
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 4($sp)
# Evaluate parameters and save to stack
# Update the stack pointer
addi $sp $sp -4
# Call the function
jal foo
# Restore the stack pointer
addi $sp $sp 4
# Restore $t0-9 registers
lw $t0 4($sp)
# Restore $ra
move $ra $t0

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

# Calling function foo
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
# Evaluate parameters and save to stack
# Update the stack pointer
addi $sp $sp -4
# Call the function
jal foo
# Restore the stack pointer
addi $sp $sp 4
# Restore $t0-9 registers
lw $t0 -4($sp)
# Restore $ra
move $ra $t0

# Calling function fum
# Save $ra to a register
move $t0 $ra
# Save $t0-9 registers
sw $t0 -4($sp)
# Evaluate parameters and save to stack
# Update the stack pointer
addi $sp $sp -4
# Call the function
jal fum
# Restore the stack pointer
addi $sp $sp 4
# Restore $t0-9 registers
lw $t0 -4($sp)
# Restore $ra
move $ra $t0

# Exiting scope.
addi $sp $sp 0
jr $rali $v0 10
syscall

# All memory structures are placed after the
# .data assembler directive
.data

newline:      .asciiz "\n"
datalabel0:   .asciiz "This program prints 7 7 7"

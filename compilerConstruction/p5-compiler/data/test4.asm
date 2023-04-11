# All program code is placed after the
# .text assembler directive
.text

# Declare main as a global function
.globl	main

j main

# code for main
main:
# Entering a new scope.
# Symbols in symbol table:
# a 
# println 
# b 
# return 
# Update the stack pointer.
addi $sp $sp 0


# println
la $a0 datalabel0
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

li $t0 3
sw $t0 -4($sp)

li $t0 2
sw $t0 -8($sp)

# Entering a new scope.
# Symbols in symbol table:
# a 
# println 
# Update the stack pointer.
addi $sp $sp -8

li $t0 5
sw $t0 -4($sp)

# println
lw $t0 -4($sp)
lw $t1 0($sp)
add $t0 $t0 $t1
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Entering a new scope.
# Symbols in symbol table:
# println 
# b 
# Update the stack pointer.
addi $sp $sp -4

li $t0 9
sw $t0 -4($sp)

li $t0 2
sub $t0 $zero $t0
sw $t0 0($sp)

# println
lw $t0 0($sp)
lw $t1 -4($sp)
add $t0 $t0 $t1
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4

li $t0 4
sw $t0 0($sp)

# Exiting scope.
addi $sp $sp 8

# println
lw $t0 -4($sp)
lw $t1 -8($sp)
add $t0 $t0 $t1
move $a0 $t0
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
datalabel0:   .asciiz "This program prints 7 7 7"

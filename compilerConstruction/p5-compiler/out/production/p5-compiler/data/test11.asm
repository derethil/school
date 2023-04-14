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
#  println 
#  i 
# Update the stack pointer.
addi $sp $sp 0

li $t0 0
sw $t0 -4($sp)

# Calling function println
la $a0 datalabel0
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

loopLabel1:
lw $t0 -4($sp)
li $t1 10
slt $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero endLabel2
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
lw $t0 0($sp)
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

lw $t0 0($sp)
li $t1 1
add $t0 $t0 $t1
sw $t0 0($sp)

# Exiting scope.
addi $sp $sp 4

# All memory structures are placed after the
# .data assembler directive
.data

newline:      .asciiz "\n"
datalabel0:   .asciiz "This program prints 0 through 9."

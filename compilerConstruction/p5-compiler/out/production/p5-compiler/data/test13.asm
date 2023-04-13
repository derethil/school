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
#  a 
#  println 
#  i 
# Update the stack pointer.
addi $sp $sp 0


# Calling function println
la $a0 datalabel0
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

li $t0 0
sw $t0 -4($sp)

# Calling function println
lw $t0 -4($sp)
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

li $t0 2
sw $t0 -4($sp)

# Calling function println
lw $t0 -4($sp)
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

li $t0 2
sw $t0 -8($sp)

lw $t0 -8($sp)
sw $t0 -4($sp)

# Calling function println
lw $t0 -4($sp)
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

li $t0 0
sw $t0 -8($sp)


# Calling function println
lw $t0 -4($sp)
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
lw $t0 -4($sp)
move $a0 $t0
li $v0 1
syscall
la $a0 newline
li $v0 4
syscall

# Calling function println
lw $t0 -4($sp)
li $t1 6
mult $t0 $t1
mflo $t0
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
datalabel0:   .asciiz "This should print 0, 2, 2, 3, 6 and 36"
# t: [false, false, false, false, false, false, false, false, false, false]

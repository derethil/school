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
# Update the stack pointer.
addi $sp $sp 0

li $t0 3
sw $t0 -4($sp)

# Calling function println
la $a0 datalabel0
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

lw $t0 -4($sp)
li $t1 4
slt $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero datalabel1
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel3
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel2
datalabel1:
datalabel2:

lw $t0 -4($sp)
li $t1 4
sgt $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero datalabel4
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel6
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel5
datalabel4:
datalabel5:

lw $t0 -4($sp)
li $t1 4
sgt $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero datalabel7
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel9
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel8
datalabel7:
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel10
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
datalabel8:

lw $t0 -4($sp)
li $t1 3
sle $t0 $t1 $t0
xori $t0 $t0 1
bne $t0 $zero datalabel11
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel13
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel12
datalabel11:
datalabel12:

lw $t0 -4($sp)
li $t1 3
seq $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero datalabel14
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel16
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel15
datalabel14:
datalabel15:

lw $t0 -4($sp)
li $t1 4
sge $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero datalabel17
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel19
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel18
datalabel17:
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel20
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
datalabel18:

lw $t0 -4($sp)
li $t1 4
seq $t0 $t0 $t1
xori $t0 $t0 1
lw $t1 -4($sp)
li $t2 3
seq $t1 $t1 $t2
xori $t1 $t1 1
xori $t0 $t0 1
xori $t1 $t1 1
or $t0 $t0 $t1
xori $t0 $t0 1
bne $t0 $zero datalabel21
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel23
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
j datalabel22
datalabel21:
# Entering a new scope.
# Symbols in symbol table:
#  println 
# Update the stack pointer.
addi $sp $sp -4
# Calling function println
la $a0 datalabel24
li $v0 4
syscall
la $a0 newline
li $v0 4
syscall

# Exiting scope.
addi $sp $sp 4
datalabel22:

# Exiting scope.
addi $sp $sp 0
li $v0 10
syscall

# All memory structures are placed after the
# .data assembler directive
.data

newline:      .asciiz "\n"
datalabel0:   .asciiz "This program prints [1..5] correct."
datalabel3:   .asciiz "1 correct"
datalabel6:   .asciiz "2 not correct"
datalabel9:   .asciiz "2 not correct"
datalabel10:  .asciiz "2 correct"
datalabel13:  .asciiz "3 correct"
datalabel16:  .asciiz "4 correct"
datalabel19:  .asciiz "5 not correct"
datalabel20:  .asciiz "5 correct"
datalabel23:  .asciiz "6 correct"
datalabel24:  .asciiz "6 not correct"
# t: [false, false, false, false, false, false, false, false, false, false]

# Simple routine to demo functions
# USING a stack in this example to preserve
# values of calling function

# ------------------------------------------------------------------
	
	.text

	.globl	main
main:
	# Register assignments
	# $s0 = x
	# $s1 = y

	# Initialize registers
	#lw	$s0, x		# Reg $s0 = x
	#lw	$s1, y		# Reg $s1 = y
	#li $s0 5
	
	# int x, y;
	# fun(x, y)
	
	# Allocate space on the stack for x and y
	#  --> activation record is of size 8 bytes
	
	# assign x to be 3
	li $t0 3
	# Store 3 to x on the stack
	sw $t0 -4($sp)
	# assign y to be 7
	li $t0 7
	# Store 7 to y on the stack
	sw $t0 -8($sp)
	
	# fun(y, 8)
	# Put y and 8 on the stack where fun expects it
	# Get y from the stack
	lw $t0 -8($sp)
	# Save y where fun expects it
	sw $t0 -12($sp)
	# Save 8 where fun expects it
	li $t0 8
	sw $t0 -16($sp)
	
	move $t0 $ra
	
	# Save all $t0-9 registers to the stack
	# and compute sizeThatTemporaryRegistersTookUp
	
	# Update $sp by 8 (the size of the current activation record)
	
	# // Compute sizeOfActivationRecord using SymbolTable
	# StringBuilder data;
	# data.append(String.format("# Update stack pointer before calling function %s\n", functionName));
	# data.append(String.format("addi $sp $sp %d", sizeOfActivationRecord + sizeThatTemporaryRegistersTookUp));
	addi $sp $sp -8
	
	#li $a0 5
	#li $a1 4
	#li $t0 5
	#li $t1 4
	#sw $t0 -4($sp)
	#sw $t1 -8($sp)
	jal fun
	
	# Restore $sp
	# Restore $t0-9
	# Restore $ra
	
	# Print out variable x
	#li $v0 4
	#la $a0 xeq
	#syscall
	#li $v0 1
	#move $a0 $s0
	#syscall

	# Exit
	li	$v0,10		# exit
	syscall

# ------------------------------------------------------------------
	
	# FUNCTION: int fun(int a)
	# Arguments are stored in $a0
	# Return value is stored in $v0
	# Return address is stored in $ra (put there by jal instruction)
	# Typical function operation is:
	
fun:	# This function overwrites $s0 and $s1.
	# So save $s0 and $s1 on the stack.
	# This is PUSH'ing onto the stack.
	
	# We want to use s0 and s1 in this function. So, by convention,
	# we need to save s0 and s1 to the stack
	#sw $s0 -4($sp)
	#sw $s1 -8($sp)
	
	#move $t0 $a0
	#move $t1 $a1
	lw $t0 -4($sp)
	lw $t1 -8($sp)
	#add $t0 $t0 $t1
	#addi $t0 $t0 3
	#mul $t0 $t0 2
	
	li $v0 1
	move $a0 $t0
	syscall
	li $v0 4
	la $a0 lf
	syscall
	
	# restore values of s0 and s1
	#lw $s0 -4($sp)
	#lw $s1 -8($sp)
	
	jr $ra
	
# ------------------------------------------------------------------
	
	# Start .data segment (data!)
	.data
x:	.word 5
y:	.word 0
xeq:	.asciiz	"s0="
hello:	.asciiz	"Hello world!\n"
goodbye:	.asciiz	"Goodbye for now.\n"
lf:     .asciiz	"\n"

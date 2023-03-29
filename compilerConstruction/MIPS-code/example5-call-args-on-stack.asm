# Simple routine to demo functions
# USING a stack in this example to preserve
# values of calling function

# ------------------------------------------------------------------
	
	.text

	.globl	main
main:
	# Add a local variable "a"
	li $t0 42
	sw $t0 -4($sp)
	
	# Get ready to call "fun"
	# Step 1: update stack pointer - main's AR size is 4 (because of a)
	addi $sp $sp -4


	li $t0, 18
	li $t1, 14
	
	# Put parameters on the stack
	#add $s1 $s0 $sp
	#sw $t0 0($s1)
	sw $t0 -4($sp)
	sw $t1 -8($sp)
	
	# Call function
	jal	fun		# Save current PC in $ra, and jump to fun
	
	# Put the return value into $t4
	lw $t4 -12($sp)
	
	# Restore the stack pointer
	addi $sp $sp 4
	
	move $a0 $t4
	li $v0 1
	syscall

	# Exit
	li	$v0,10		# exit
	syscall

# ------------------------------------------------------------------
	
	# FUNCTION: int fun(int a)
	# Arguments are stored in $a0
	# Return value is stored in $v0
	# Return address is stored in $ra (put there by jal instruction)
	# Typical function operation is:
	
fun:
	# Put first formal argument into $t0
	lw $t0 -4($sp)
	# Put second formal argument into $t1
	lw $t1 -8($sp)
	
	# Add the values
	add $t2 $t0 $t1
	#move $a0 $t2
	#li $v0 1
	#syscall
	
	# Put the return value on the stack
	sw $t2 -12($sp)
	
	# Return from function
	jr $ra			# Jump to addr stored in $ra
	
# ------------------------------------------------------------------
	
	# Start .data segment (data!)
	.data
x:	.word 5
y:	.word 0
msg1:	.asciiz	"y="
lf:     .asciiz	"\n"

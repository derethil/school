	.text

	.globl	main
main:
	# Initialize array values
	li $t1 1
	li $t2 3
	li $t3 5
	
	# Store current addresss of stack pointer (to reference array)
	addi $t0 $sp -4
	
	# Allocate array
	sw $t1 -4($sp)
	sw $t2 -8($sp)
	sw $t3 -12($sp)
	
	# Update sp
	addi $sp $sp -12
	
	# Allocate dope vector argument
	li $t4 3
	sw $t0 -4($sp) # Array address
	sw $t4 -8($sp) # # of elements

	# Call fee
	jal fee
	
	# Get return value
	lw $t0 -12($sp)
	
	# Update sp
	addi $sp $sp 12
	
	# Print result
	move $a0 $t0
	li $v0 1
	syscall

	# Exit
	li $v0 10
	syscall
	
	
fee:
	# Initialize sum = 0
	li $t0 0
	
	# Load dope vector from stack
	lw $t0 -4($sp) # array address
	lw $t1 -8($sp) # # of elements
	
	li $t2 0 # i = 0
	
	# Print address of array
	move $a0 $t0
	li $v0 1
	syscall
	
	la $a0 lf
	li $v0 4
	syscall
	
	j loop
	
loop:	
	# i < arr length
	beq $t2 $t1 efee
	
	# Array element address
	
	li $t3 -4 # array element width
	mul $t3 $t3 $t2 # array element offset
	add $t4 $t0 $t3 # array element address
	
	# arr[i]
	lw $t5 0($t4) 
	
	# Print address
	move $a0 $t5
	li $v0 1
	syscall
	
	# Print newline
	la $a0 lf
	li $v0 4
	syscall

	# sum = sum + arr[i]
	add $t0 $t0 $t5 
	
	# ++i
	addi $t2 $t2 1
	
	
	j loop
	
efee:  	
	# Store return value
	sw $t0 -12($sp)
	
	# Exit
	jr $ra
	


# Start .data segment (data!)
	.data
lf:     .asciiz	"\n"
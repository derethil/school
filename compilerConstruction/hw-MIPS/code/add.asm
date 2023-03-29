	.data
	
	.text
main:	
	# Load 3 and 4 into registers
	li $t0 3
	li $t1 4
	
	# Store the sum in $a0
	add $t2 $t0 $t1
	move $a0 $t2
	
	# Print
	li $v0 1
	syscall
	
	# Exit
	li $v0 10
	syscall

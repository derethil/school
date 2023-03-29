# Simple routine to demo a loop
# Compute the sum of N integers: 1 + 2 + 3 + ... + N
# From: http://labs.cs.upt.ro/labs/so2/html/resources/nachos-doc/mipsf.html

	.text

	.globl	main
main:
	
	# prompt the user for N
	li $v0 4 # print string
	la $a0 msg1
	syscall
	
	#li $t0 3
	li $v0 5             # syscall code = 5 is for read_int
	syscall
	move $t0 $v0

	li $t1 0 # t0 is our counter, t1 is our sum
				
	j loop
		
loop:
	# Loop n times
	
	# Add N to t1 (our sum)
	add $t1 $t1 $t0
	
	# print N
	move $a0 $t0
	li $v0 1
	syscall
	
	# print newline
	li $v0 4
	la $a0 lf
	syscall
	
	subi $t0 $t0 1
	li $t2 0
#	beqz $t0 exit
	beq $t0 $t2 exit
	j loop

exit:
	# print the sum
	la $a0 msg2
	li $v0 4
	syscall
	
	move $a0 $t1
	li $v0 1
	syscall

	li	$v0,10		# exit
	syscall

	# Start .data segment (data!)
	.data
msg1:	.asciiz	"Number of integers (N)?  "
msg2:	.asciiz	"Sum = "
lf:     .asciiz	"\n"

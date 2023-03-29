# Simple input/output in MIPS assembly
# From: http://labs.cs.upt.ro/labs/so2/html/resources/nachos-doc/mipsf.html

	# Start .text segment (program code)
	.text
	
	.globl	main
main:
	# Get A - put in $t0
	
	# prompt the user to enter A
	la $a0 msg1
	li $v0 4
	syscall
	
	li $v0 5     # syscall code = 5 is for read_int
	syscall
	move $t0 $v0
	
	# Get B - put in $t1
	
	# prompt the user to enter B
	la $a0 msg2
	li $v0 4
	syscall
	
	li $v0 5     # syscall code = 5 is for read_int
	syscall
	move $t1 $v0

	# Add A and B
	
	add $t0 $t0 $t1
	
	# Make a nice output message
	la $a0 msg3
	li $v0 4
	syscall
	
	move $a0 $t0
	li $v0 1
	syscall

	# Make a nice output message
	la $a0 newline
	li $v0 4
	syscall
	
	li	$v0,10		# exit
	syscall

	# Start .data segment (data!)
	.data
msg1:	.asciiz	"Enter A:   "
msg2:	.asciiz	"Enter B:   "
msg3:	.asciiz	"A + B = "
newline:   .asciiz	"\n"

# "Hello World" in MIPS assembly
# From: http://labs.cs.upt.ro/labs/so2/html/resources/nachos-doc/mipsf.html
	
	# All program code is placed after the
	# .text assembler directive
	.text

	# Declare main as a global function
	.globl	main
	
# The label 'main' represents the starting point
main:
	la    , $a0 msg2
	li $v0,4      # 4 is to print a string
	syscall

	li	$v0,10		# Code for syscall: exit
	syscall

	# All memory structures are placed after the
	# .data assembler directive
	.data

	# The .asciiz assembler directive creates
	# an ASCII string in memory terminated by
	# the null character. Note that strings are
	# surrounded by double-quotes
msg:	.asciiz	"Hello world!\n"
msg2:	.ascii "Goodbye yellow brick road\n\0"
msg3:	.ascii "Thick as a brick\n"

# Simple routine to demo a loop
# Compute the sum of N integers: 1 + 2 + 3 + ... + N
#
# int addv(int a, int b) {
#   return a + b;
# }
# 
# void main() {
#   int sum;
#   int i;
#   i = 1;
#   sum = 0;
#   while (i < 10) {
#     // sum += i;
#     sum = addv(sum, i);
#   }
#   println(sum)
# }

# ------------------------------------------------------------------
	
	.text

	.globl	main
main:
	# sum stored in $t0
	li $t0 0
	# i stored in $t1
	li $t1 1
	# stopping point stored in $t2
	li $t2 4
	
loop:	
	beq $t1 $t2 exit
	
	# Print the counter
	move $a0 $t1
	li $v0 1
	syscall
	
	# Print newline
	li $v0 4
	la $a0 lf
	syscall
	
	#------------------------
	# Make call to addv
	#------------------------
	# Update sp
	addi $sp $sp -0
	
	# Put formal arguments on the stack
	sw $t0 -4($sp)
	sw $t1 -8($sp)
	
	jal addv
	
	# Get return value from the stack; put in $t0
	# (which is the sum variable)
	lw $t0 -12($sp)
	
	# Update sp
	addi $sp $sp 0
	
	# increment i (counter)
	addi $t1 $t1 1
	j loop
	
	j exit
	
addv:
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



exit:	

	# Print the sum
	move $a0 $t0
	li $v0 1
	syscall

	# exit
	li	$v0,10
	syscall

	
# ------------------------------------------------------------------
	
	# Start .data segment (data!)
	.data
msg1:	.asciiz	"Number of integers (N)?  "
msg2:	.asciiz	"Sum = "
lf:     .asciiz	"\n"

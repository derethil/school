	.text

	.globl	main
main:
	# Update sp
	addi $sp $sp -0
	
        # put 3 and 4 on the stack using the register $sp
        li $t0 3
        li $t1 4
        
        sw $t0 -4($sp)
        sw $t1 -8($sp)

        # call fee. You'll probably want to use the jal instruction to
        # store the current address in $ra
        
        jal fee

	# load results from stack to registers
	lw $t2 -12($sp)
	
	# update sp
        addi $sp $sp 0

	# print result
	move $a0 $t2
	li $v0 1
	syscall
	
        # exit
        li $v0 10
        syscall

fee:
        # copy a and b from stack to local registers
        lw $t0 -4($sp)
        lw $t1 -8($sp)

	# add a and b
	add $t2 $t0 $t1

        # place result on stack
        sw $t2 -12($sp)

        # return using jr instruction
        jr $ra
	
# Start .data segment (data!)
	.data

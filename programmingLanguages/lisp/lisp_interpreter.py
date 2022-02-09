import string
import os

# importing the sys module
import sys

# the setrecursionlimit function is
# used to modify the default recursion
# limit set by python. Using this,
# we can increase the recursion limit
# to satisfy our needs

sys.setrecursionlimit(10**5)

PATH = "/home/jaren/development/school/programmingLanguages/lisp/"

LispAtoms = ["True", "False", "nil"]
LispFunctions = [
    "+",
    "-",
    "*",
    "/",
    "if",
    "not",
    "and",
    "or",
    "eq",
    "<",
    "atom",
    "cons",
    "first",
    "rest",
    "quote",
    "eval",
]


def runProgram(fileName="code.lsp", trace=False):
    # reads the code in file name, parses it, executes each statement
    global Trace
    Trace = trace
    program = readInFile(fileName)
    # parse the string into a list of lists
    codeList = parseProgram(program)
    # print(codeList)
    evalProgram(codeList)


def readInFile(fileName):
    # reads in a lisp program, returns as one big string
    print("Reading %s" % (PATH + fileName,))
    with open(PATH + fileName, "r") as file:  # ignore comment lines
        lines = [l for l in file.readlines() if not l[0] == "#"]
    whole = ""
    for line in lines:
        whole = whole + line
    return whole


########################################################################################################
### SYNTAX
########################################################################################################
def parseProgram(programString):
    # takes a string that represents a program, multiple statements
    # returns a list of list of tokens, each a statement
    tokens = spaceOut(programString).split()
    # print(tokens)
    codeList = []
    while not tokens == []:
        code = createCode(tokens)
        # print(code)
        codeList.append(code)
    return codeList


def spaceOut(string):
    return string.replace("(", " ( ").replace(")", " ) ")


def createCode(tokenList):
    # takes a token list and returns a parse tree
    token = tokenList.pop(0)  # pop off ( or atom
    if token.isdigit() or token[0].isdigit():  # integer
        return int(token)
    if isinstance(token, str) and not token == "(":  # atom
        return token
    args = []
    while not tokenList[0] == ")":
        args.append(createCode(tokenList))
    tokenList.pop(0)  # pop the ')'
    return args


#######################################################################################################################
###### semantics
#######################################################################################################################
def evalProgram(codeList):
    # top call, evaluates a list of parsed statements
    evalLispBlock(codeList, {})


def evalLispBlock(block, oneBinding):
    # walks down the block (a list of expressions) evaluating each statement
    # and accumulating bindings of function names to definitions
    # into oneBinding
    if len(block) == 0:  # empty block, return None
        return
    code = block[0]
    if (
        isinstance(code, list) and code[0] == "defun"
    ):  # put mapping from name to arguments, body
        oneBinding[code[1] + "_defun"] = (code[2], code[3])
        if Trace:
            print(
                "Create function->definition binding: %s = [%s, %s]"
                % (code[1], str(code[2]), str(code[3]))
            )
        # keep working through block
        evalLispBlock(block[1:], oneBinding)
    else:  # found an expression, evaluate it with current bindings
        evalLisp(block[0], [oneBinding])
        # print("\n")
        evalLispBlock(block[1:], oneBinding)


def evalLisp(code, bindings=[], depth=0):
    # evaluates the code with the bindings so far
    # bindings is a list of dictionaries, each with a mapping from a symbol
    # to its value. These are generated from def statements, and when
    # a user-defined function is called
    if Trace or depth == 0:
        print("%sEval %s" % ("|  " * depth, toString(code)))
    answer = evalLispHelp(code, bindings, depth)
    if Trace or depth == 0:
        print("%sAns  %s " % ("|  " * depth, toString(answer)))
    return answer


def evalLispHelp(code, bindings, depth):
    # print(code)
    # main code that implements semantics of lisp
    # do all atoms
    if isinstance(code, int):
        return code
    if "True" == code:
        return True
    if "False" == code:
        return False
    if "nil" == code:  # nil is the empty list
        return []
    if isinstance(code, str):  # variable
        return findValue(code, bindings, depth)
    # do all composite code
    fun = code[0]  # get the function name
    # numerical functions
    if fun == "+":
        return evalLisp(code[1], bindings, depth + 1) + evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "-":
        return evalLisp(code[1], bindings, depth + 1) - evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "*":
        return evalLisp(code[1], bindings, depth + 1) * evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "/":  # integer division
        return evalLisp(code[1], bindings, depth + 1) // evalLisp(
            code[2], bindings, depth + 1
        )
    # control functions
    if fun == "if":
        if evalLisp(code[1], bindings, depth + 1):
            return evalLisp(code[2], bindings, depth + 1)
        else:
            return evalLisp(code[3], bindings, depth + 1)
    # boolean functions
    if fun == "not":
        return not evalLisp(code[1], bindings, depth + 1)
    if fun == "and":
        return evalLisp(code[1], bindings, depth + 1) and evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "or":
        return evalLisp(code[1], bindings, depth + 1) or evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "eq":  # only works between atoms
        return simpleEqual(
            evalLisp(code[1], bindings, depth + 1),
            evalLisp(code[2], bindings, depth + 1),
        )
    if fun == "<":
        return evalLisp(code[1], bindings, depth + 1) < evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "atom":
        ans = evalLisp(code[1], bindings, depth + 1)
        return (
            isinstance(ans, int)
            or isinstance(ans, str)
            or ans == True
            or ans == False
            or ans == []
        )
    # data structures
    if fun == "cons":  # add the first to a list
        return [evalLisp(code[1], bindings, depth + 1)] + evalLisp(
            code[2], bindings, depth + 1
        )
    if fun == "first":  # eval then return the first
        return evalLisp(code[1], bindings, depth + 1)[0]
    if fun == "rest":  # eval then return the rest
        return evalLisp(code[1], bindings, depth + 1)[1:]
    # evaluation
    if fun == "quote":  # stop evaluation
        return code[1]
    if fun == "eval":  # evaluate to get the code, then evaluate the code
        return evalLisp(evalLisp(code[1], bindings, depth + 1))
    # must be a user function call
    return evalUserLispFun(code[0], code[1:], bindings, depth + 1)


def evalUserLispFun(funName, args, bindings, depth):
    # Execute a user defined function
    # print(funName)
    # print(args)
    # print(bindings)
    if Trace:
        print("|  " * depth + "Calling function %s" % (funName,))
    # lookup the function definition in bindings
    # parameter list and then the body of the function
    (params, body) = findValue(funName + "_defun", bindings, depth)
    # print(params)
    # print(body)
    # create a new binding to hold local variables and their values
    # oneBinding is a single dictionary
    oneBinding = {}
    # add the mapping from parameter names to values
    for (oneParam, oneExpression) in zip(params, args):
        # evaluate the arguments and create the bindings
        value = evalLisp(oneExpression, bindings, depth + 1)
        oneBinding[oneParam] = value
        if Trace:
            print(
                "|  " * depth
                + "Create parameter->value binding: %s = %s" % (oneParam, value)
            )
    # work through each statement in block
    return evalLisp(body, [oneBinding] + bindings, depth=depth + 1)


def simpleEqual(a, b):
    # only works on primitives
    if isinstance(a, int) and isinstance(b, int):
        return a == b
    if (a is True and b is True) or (a is False and b is False):
        return True
    if (a == []) and (b == []):
        return True
    return False


def findValue(name, bindings, depth):
    # print(len(bindings))
    # name is a variable, bindings is a list of dictionaries ordered most
    # recent first, search up the bindings seeing if this variable is in
    # one of the dictionaries
    for oneBinding in bindings:
        if name in oneBinding:  # found its value
            if Trace:
                print(
                    "|  " * depth + "Found Value of %s as %s" % (name, oneBinding[name])
                )
            return oneBinding[name]

    print("did not find value?" + str(name))


def toString(code):
    # prints the parse tree in a more readable form
    if isinstance(code, int) or isinstance(code, str) or len(code) == 0:
        return str(code) + " "
    string = "("
    for item in code:
        string = string + toString(item)
    return string + ")"


########################################################################################################################
fileName = "code.lsp"
# print(readInProgram(fileName))
# runProgram(fileName, True)
runProgram(fileName, False)
# print(parseProgram("(+ 1 2) (+ 4 5)"))

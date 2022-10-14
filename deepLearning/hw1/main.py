import numpy as np
import pandas


def is_pos_def(A):
    if np.array_equal(A, A.T):
        try:
            np.linalg.cholesky(A)
            return True
        except np.linalg.LinAlgError:
            return False
    else:
        return False


A = np.array([[1, -2], [3, 4], [-5, 6]])
B = np.identity(3)
C = np.array([[2, 2, 1], [2, 3, 2], [1, 2, 2]])

result = np.matmul((C - 0.01), np.matmul(A, A.T))

print("\nResult is\n", result)

print("\nEigenvalues are\n", np.linalg.eigvals(result))

print("\nPositive definite?", is_pos_def(result))

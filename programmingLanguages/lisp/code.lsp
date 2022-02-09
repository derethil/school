# --- APPEND ----

(defun append (list0 list1)
    (if (eq list0 nil)
        list1
        (cons (first list0) (append (rest list0) list1))))

(append nil nil)
(append nil (quote (5 8 10 1)))
(append (cons 1 (cons 2 nil )) nil)
(append (quote (1 2 3)) (quote (4 5 6)))

# --- REVERSEA ---

(defun reverseA (list)
    (if (eq list nil)
        nil
        (append (reverseA (rest list)) (cons (first list) nil)))
)

(reverseA nil)
(reverseA (quote (1 3 7 10 15 3 2 7 )))
(reverseA (cons 8 (cons 5 (cons 2 nil ))))

# --- LENGTH ---

(defun length (list)
    (if (eq list nil)
        0
        (+ 1 (length (rest list)))))

(length nil )
(length (cons True (cons False (cons False (cons False nil )))))
(length (quote (1 8 6 4 4 3 2 1 )))

# --- FLATTEN ---

(defun flatten (expr)
    (if (atom expr)
        expr
        (if (eq expr nil)
            nil
            (if (atom (first expr))
                (cons (first expr) (flatten (rest expr)))
                (append (flatten (first expr)) (flatten (rest expr)))))))

(flatten nil)
(flatten 1)
(flatten (quote (1 2 3)))
(flatten (quote ((1 )(2 )(3))))
(flatten (quote (1 (7 8 (3 )(((4 ))(5 6 ))(9 10 0 (11 ))))))

# --- EQUAL ---

(defun equal (expr0 expr1)
    (or (eq expr0 expr1)
        (if (or (atom expr0) (atom expr1))
            False
            (and (equal (first expr0) (first expr1))
                 (equal (rest expr0) (rest expr1))))))

(equal nil nil)
(equal nil 1)
(equal (quote (1 ))(quote (2 3 4 5 )))
(equal (quote (1 (7 8 (3 )(((4 ))(5 6 ))(9 10 0 (11 )))))(quote (1 (7 8 (3 )(((4 ))(5 6 ))(9 10 0 (11 ))))))

# --- FIND ---

(defun find (item expr)
    (if (eq expr nil)
        False
        (if (eq item (first expr))
            True
            (find item (rest expr)))))

(find 1 nil )
(find True (cons False (cons False nil )))
(find 111111 (quote (1 2 3 4 6 )))
(find 555 (quote (6 555 67 545 )))

# --- GET ---

(defun get (list index)
    (if (eq list nil)
        nil
        (if (eq index 0)
            (first list)
            (get (rest list) (- index 1)))))

(get nil 1 )
(get (cons 4 nil )0 )
(get (quote (1 b c d e ))3 )

# --- SELECT ---

(defun select (list start end)
    (if (eq list nil)
        list
        (selectA list nil start (- end 1))))


(defun selectA (list selected start end)
    (if (eq start end)
        (append selected (cons (get list start) nil))
        (if (not (eq start 0))
            (selectA (rest list) selected (- start 1) (- end 1))
            (selectA (rest list) (append selected (cons (first list) nil)) start (- end 1)))))

(select (quote (0 1 2 3 4 5 6 7 8 )) 0 (/ (length (quote (0 1 2 3 4 5 6 7 8 ))) 2 ))
(select (quote (0 1 2 3 4 5 6 7 8 )) (/ (length (quote (0 1 2 3 4 5 6 7 8 ))) 2 )(length (quote (0 1 2 3 4 5 6 7 8 ))))

# --- MERGE SORT ---

(defun mergeSort (list)
    (if (or (< (length list) 1) (eq (length list) 1))
        list
        (merge (mergeSort (select list 0 (/ (length list) 2))) (mergeSort (select list (/ (length list) 2) (length list))) nil)))

(defun merge (list1 list2 merged)
    (if (and (eq list1 nil) (eq list2 nil))
        merged

        (if (eq list1 nil)
            (merge list1 (rest list2) (append merged (cons (first list2) nil)))

            (if (eq list2 nil)
                (merge (rest list1) list2 (append merged (cons (first list1) nil)))

                (if (< (first list1) (first list2))
                    (merge (rest list1) list2 (append merged (cons (first list1) nil)))
                    (merge list1 (rest list2) (append merged (cons (first list2) nil))))))))

(mergeSort nil)
(mergeSort (quote (1 )))
(mergeSort (quote (4 3 1 )))
(mergeSort (quote (6 3 2 1 5 7 10 )))

# --- COUNT THROWS ---

(defun countThrows (countDice target)
    (if (eq countDice 0)
        (if (eq target 0)
            1
            0)
        (countOne countDice target)))

(defun countOne (countDice target)
    (+ (countThrows (- countDice 1) (- target 1))
    (+ (countThrows (- countDice 1) (- target 2))
    (+ (countThrows (- countDice 1) (- target 3))
    (+ (countThrows (- countDice 1) (- target 4))
    (+ (countThrows (- countDice 1) (- target 5))
    (countThrows (- countDice 1) (- target 6))))))))

(countThrows 0 0)
(countThrows 0 1)
(countThrows 1 3)
(countThrows 2 7)
(countThrows 2 12)
(countThrows 4 10)
(countThrows 3 16)
(countThrows 3 1)
(countThrows 3 10)
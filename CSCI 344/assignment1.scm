#lang scheme
;CSCI Assignment 1
;author Egor Kozitski

;1 @function 2nd returns the second element of the list
;  @param xs -> List
(define 2nd
  (lambda (xs)
    (cadr xs)))
;2 @function return #t if the list has exactly one element
;  @param xs -> List
(define one?
  (lambda (xs)
   (cond
     [(null? xs) #f]
     [(null? (cdr xs)) #t]
         [else #f])))


;4 @function index finds the index of the element in the list
;  @param sym -> Symbol syms-> List of Symbols
(define index
  (lambda (sym syms)
    (cond
      [(null? syms) #f]
      [(eq? sym (car syms)) 0]
      [(eq? (index sym (cdr syms)) #f) #f]
      [else (+ 1 (index sym (cdr syms)))])))

     
;3 helper
; @function helper for insertion sort
; @param xs-> Number ys->List of numbers
(define insert
  (lambda (xs ys)
    (cond
     [(null? ys) (cons xs ys)]
     [(<= xs (car ys))(cons xs ys)]
     [else (cons (car ys)(insert xs (cdr ys)))])))

;3 @function insertion-sort will sort elements in
; ascnding order
;  @param ys -> List of numbers
(define insertion-sort
  (lambda (ys)
    (cond
      [(null? ys) '()]
      [else (insert (car ys)(insertion-sort (cdr ys)))])))
;5  @function filter-by will return list based on the predicate
;   @params p -> Predicate L-> List
(define filter-by
  (lambda (p L)
    (cond
      [(null? L) '()]
      [(p (car L)) (cons  (car L) (filter-by p (cdr L)))]
      [else (filter-by p (cdr L))])))


;5 @function find-less
(define find-less
  (lambda (xs ys)
    (filter-by (lambda (x) (< x xs)) ys)))

;5 @function find-same
(define find-same
  (lambda (xs ys)
    (filter-by (lambda (x) (= x xs)) ys)))

;5 @find-more
(define find-more
  (lambda (xs ys)
    (filter-by (lambda (x) (> x xs)) ys)))


(display "#1 Testing 2nd\n")

(equal? (2nd '(x y)) 'y)
(equal? (2nd '(a a a a a)) 'a)

(display "#2 Testing one?\n")

(equal? (one? '(s)) #t)
(equal? (one? '()) #f)

(display "#3 Testing insertion-sort\n")

(equal? (insertion-sort '()) '())
(equal? (insertion-sort '(3 1 2 5 4)) '(1 2 3 4 5))

(display "#4 Testing index\n")

(equal? (index '2 '(1 2 4 5)) 1)
(equal? (index 'x '(q r s x y z)) 3)

(display "#5 Testing filter-by\n")

(equal? (filter-by symbol? '(() a 5 (1 2 3) (x y z) b 10)) '(a b))
(equal? (filter-by number? '(() a 5 (1 2 3) (x y z) b 10)) '(5 10))

(display "Testing find-less\n")

(equal? (find-less '3 '(1 2 4 5)) '(1 2))
(equal? (find-less '4 '(5 5 5 5)) '())

(display "Testing find-same\n")

(equal? (find-same '3 '(1 3 4 5)) '(3))
(equal? (find-same '2 '(1 4 5 6 )) '())

(display "Testing find-more\n")

(equal? (find-more '3 '(1 233 4 5 )) '(233 4 5))
(equal? (find-more '2 '(4 5 6 6 7)) '(4 5 6 6 7))


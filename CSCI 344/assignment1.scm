#lang scheme
(define 2nd
  (lambda (xs)
    (cadr xs)))

    
(define one?
  (lambda (xs)
    (cond [(= (length xs) 1) #t]
          [else #f])))


(define insert
  (lambda (xs ys)
    (cons xs ys)))
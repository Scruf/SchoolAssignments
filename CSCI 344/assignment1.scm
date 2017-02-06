#lang scheme
(define 2nd
  (lambda (xs)
    (cadr xs)))

(define one?
  (lambda (xs)
   (cond
     [(null? xs) #f]
     [(null? (cdr xs)) #t]
         [else #f])))

#lang scheme
#1
(define 2nd
  (lambda (xs)
    (cadr xs)))
#2
(define one?
  (lambda (xs)
   (cond
     [(null? xs) #f]
     [(null? (cdr xs)) #t]
         [else #f])))


#4
(define index
  (lambda (sym syms)
    (cond
      [(null? syms) #f]
      [(eq? sym (car syms)) 0]
      [(eq? (index sym (cdr syms)) #f) #f]
      [else (+ 1 (index sym (cdr syms)))])))

     
#3.1
(define insert
  (lambda (xs ys)
    (cons xs ys)))

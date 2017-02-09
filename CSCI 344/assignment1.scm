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

     
;3
(define insert
  (lambda (xs ys)
    (cond
     [(null? ys) (cons xs ys)]
     [(<= xs (car ys))(cons xs ys)]
     [else (cons (car ys)(insert xs (cdr ys)))])))


(define insertion-sort
  (lambda (ys)
    (cond
      [(null? ys) '()]
      [else (insert (car ys)(insertion-sort (cdr ys)))])))

(define filter-by
  (lambda (p L)
    (cond
      [(null? L) '()]
      [(p (car L)) (cons  (car L) (filter-by p (cdr L)))]
      [else (filter-by p (cdr L))])))

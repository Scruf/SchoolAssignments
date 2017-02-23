#lang scheme
;merge will merge two list
(define merge
  (lambda (order1 order2)
    (cond
      [(null? order1) order2]
      [(null? order2) order1]
      [(>= (car order1)(car order2))(cons (car order2)(merge order1 (cdr order2))) ]
      [else (cons (car order1)(merge (cdr order1) order2))])))

;will sort two lists using merge sort

;will get even index numbers
(define even
  (lambda (xs)
    (cond
      [(null? xs) '()]
      [(null? (cdr xs)) (cons (car xs) '())]
      [else (cons (car xs)(even (cddr xs)))])))

(even'(1 2 3 4 5 6 7 8 9))
;will get odd index numbers
(define odd
  (lambda (xs)
    (cond
      [(null? xs) '()]
      [(null? (cdr xs))'()]
      [else (cons (cadr xs) (odd (cddr xs)))])))
(odd '(1 2 3 4 5 6 7 8 9))


(define merge-sort
  (lambda (nums)
    (cond
      [(null? nums) '()]
      [(null? (cdr nums)) nums]
      [else (merge (merge-sort(even nums)) (merge-sort(odd nums)))])))



  
(odd '(1 2 3 4 5 6 7 8 9))


;(equal? (make-expt '(+ x 2) 2) '(^ (+ x 2) 2))
;(equal? (make-expt 'x 2) '(^ x 2))



;(define (make-expt e n) (list'^ e n))

(define make-expt
  (lambda (e n)
    (cond
      [(and (number? e) (number? n)) (expt e n)]
      [(or (eq? e 0) (eq? n 0)) 1]
      [(eq? n 1) e]
      [else (list '^ e n)])))
(display "Testing make-expt\n")
(equal? (make-expt '(+ x 2) 2) '(^ (+ x 2) 2))
(equal? (make-expt 'x 2) '(^ x 2))
(eq? (make-expt 2 4) 16)
(eq? (make-expt 0 0) 1)
(eq? (make-expt 'u 1) 'u)
(display "End of make-expt testing\n")


(define (arg1 e) (car (cdr e)))

(define (arg2 e) (car (cdr (cdr e))))

(define expt?
  (lambda (e)
    (cond
      [(pair? e) #t]
      [(eq? (car e) '^)#t]
      [else #f])))

(equal? (expt? '(^ x 2)) #t)
(equal? (expt? '(^ x 2)) #t)
(equal? (expt? '(^ '(+ x 2) 2)) #t)

(define variable? symbol?)

; (eq? (variable? 'x) #t)
; (eq? (variable? 3) #f)

; variable=?: VarExp VarExp -> Bool
(define variable=? eq?)

; (variable=? 'x 'x)
; (not (variable=? 'x 'y))

;; a sum is represented as a list with three elements: tag, e1, e2.
;; the tag is the symbol +

; sum?: Any -> Bool
(define (sum? a) (and (pair? a) (eq? (car a) '+)))

; (eq? (sum? '(+ 2 3)) #t)
; (eq? (sum? '3) #f)

; make-sum: ArithExp ArithExp -> SumExp
(define (make-sum e1 e2)
  (cond
    [(and (number? e1) (number? e2)) (+ e1 e2)]
    [(eq? e1 0) e2]
    [(eq? e2 0) e1]
    [else (list '+ e1 e2)]))
(display "Testing make-sum\n") 
(equal? (make-sum 'x1 'x2) '(+ x1 x2))
(equal? (make-sum 0 'x1) 'x1)
(equal? (make-sum 'x1 0) 'x1)
(eq? (make-sum 2 3) 5)
(display "End of make-sum\n")
; (equal? (make-sum 2 3) '(+ 2 3))

;; a product is represented as a list with three elements: tag, e1, e2.
;; the tag is the symbol *

; product?: Any -> Bool
(define (product? a) (and (pair? a) (eq? (car a) '*)))

; (eq? (product? '(* 2 3)) #t)
; (eq? (product? '3) #f)

; make-sum: ArithExp ArithExp -> ProductExp
(define (make-product e1 e2)
  (cond
    [(and (number? e1) (number? e2)(* e1 e2))]
    [(eq? e1 1) e2]
    [(eq? e2 1 ) e1]
    [(or (eq? e1 0) (eq? e2 0)) 0]
    [else (list '* e1 e2)]))
(display "Testing make-product\n")
(eq? (make-product 2 3) 6)
(eq? (make-product 1 'x) 'x)
(eq? (make-product 'x 1) 'x)
(eq? (make-product 0 'x) 0)
(eq? (make-product 'x 0) 0)
(equal? (make-product 'x 'y) '(* x y))
(display "End of make-product test\n")


;; sums and products will use the same selectors





(define (deriv exp var)
  (cond ((number? exp) 0)
        ((variable? exp)
         (if (variable=? exp var) 1 0))
        ((sum? exp) 
         (make-sum (deriv (arg1 exp) var) (deriv (arg2 exp) var)))
        ((product? exp)
         (make-sum (make-product (arg1 exp) (deriv (arg2 exp) var))
                   (make-product (arg2 exp) (deriv (arg1 exp) var))))
        ((expt? exp)
         (make-product (arg2 exp) (make-expt (arg1 exp) (- (arg2 exp) 1))))
        (else (error 'deriv "Unexpected Input, not an ArithExp"))))



 (= (deriv 1 'x) 0)
 (= (deriv 'y 'x) 0)
 (= (deriv 'x 'x) 1)
 



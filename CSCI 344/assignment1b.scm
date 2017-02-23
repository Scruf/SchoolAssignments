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
    (cons '^ (list e n))))

;(equal? (make-expt '(+ x 2) 2) '(^ (+ x 2) 2))
;(equal? (make-expt 'x 2) '(^ x 2))

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
(define (make-product e1 e2) (list '* e1 e2))

; (equal? (make-product 2 3) '(* 2 3))

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
 (equal? (deriv (make-sum (make-product 'x 'x) (make-product (make-expt 'x 3) 5)) 'x) '(+ (+ (* x 1) (* x 1)) (+ (* (^ x 3) 0) (* 5 (* 3 (^ x 2))))))



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

(define (deriv exp var)
  (cond ((number? exp) 0)
        ((variable? exp)
         (if (variable=? exp var) 1 0))
        ((sum? exp) 
         (make-sum (deriv (arg1 exp) var)
                   (deriv (arg2 exp) var)))
        ((product? exp)
         (make-sum (make-product (arg1 exp) (deriv (arg2 exp) var))
                   (make-product (arg2 exp) (deriv (arg1 exp) var))))
        (else (error 'deriv "Unexpected Input, not an ArithExp"))))

  
(odd '(1 2 3 4 5 6 7 8 9))

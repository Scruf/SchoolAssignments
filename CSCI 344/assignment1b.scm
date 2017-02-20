;merge will merge two list
(define merge
  (lambda (order1 order2)
    (cond
      [(null? order1) order2]
      [(null? order2) order1]
      [(>= (car order1)(car order2))(cons (car order2)(merge order1 (cdr order2))) ]
      [else (cons (car order1)(merge (cdr order1) order2))])))

;will sort two lists using merge sort
(define merge
  (lambda (order1 order2)
    (cond
      [(null? order1) order2]
      [(null? order2) order1]
      [(>= (car order1)(car order2))(cons (car order2)(merge order1 (cdr order2))) ]
      [else (cons (car order1)(merge (cdr order1) order2))])))
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
    (cond
      [(null? xs) '()]
      [(null? (cdr xs))'()]
      [else (cons (cadr xs) (odd (cddr xs)))])))
(odd '(1 2 3 4 5 6 7 8 9))

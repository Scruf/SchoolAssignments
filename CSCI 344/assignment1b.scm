(define merge
  (lambda (order1 order2)
    (cond
      [(null? order1) order2]
      [(null? order2) order1]
      [(>= (car order1)(car order2))(cons (car order2)(merge order1 (cdr order2))) ]
      [else (cons (car order1)(merge (cdr order1) order2))])))


(define even
  (lambda (xs)
    (cond
      [(null? xs) '()]
      [(null? (cdr xs)) (cons (car xs) '())]
      [else (cons (car xs)(even (cdr (cdr xs))))])))



(define odd
  (lambda (xs)
    (cond
      [(null? xs) '()]
      [(null? (cdr xs))'()]
      [else (cons (cadr xs) (odd (cddr xs)))])))
(odd '(1 2 3 4 5 6 7 8 9))

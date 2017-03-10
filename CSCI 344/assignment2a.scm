#lang scheme
(define union
  (lambda (set1 set2)
    (cond
      [(null? set1) set2]
      [(null? set2) set1]
      [(not (equal? (member (car set1) set2) #f)) (union (cdr set1) set2)]
      [else (cons (car set1) (union (cdr set1) set2))])))
      
(display "Testing Union\n")
(equal? (union '() '()) '())
(equal? (union '(2) '()) '(2))
(equal? (union '() '(1)) '(1))
(equal? (union '(1 2 3 4) '(5 6 7 8 9)) '(9 8 7 6 5 1 2 3 4))
(equal? (union '(1 2 3 4) '(5 2 6 3)) '(6 5 1 2 3 4))
; Terminals are quoted.
; A rule A -> X1 ... Xn is written (A (X1 ... Xn))
; A grammar is a list of rules.

(define *grammar*
  '((S (E 'eof))
    (E (T E2))
    (E2 ('+ T E2))
    (E2 ('- T E2))
    (E2 ())
    (T (F T2))
    (T2 ('* F T2))
    (T2 ('/ F T2))
    (T2 ())
    (F ('n))
    (F ('id))
    (F ('- F))
    (F ('OP E 'CP))))

; rule-lhs : Rule -> Variable
(define rule-lhs car)

; (eq? (rule-lhs '(E (T E2))) 'E)

; rule-rhs : Rule -> List(Variables or Terminals)
(define rule-rhs cadr)

; (equal? (rule-rhs '(E (T E2))) '(T E2))

; variable? : Any -> Boolean
(define variable? symbol?)

; (variable? 'E)
; (not (variable? ''+))

; terminal? : Any -> Boolean
(define (terminal? a) (and (pair? a) (eq? (car a) 'quote)))

; (terminal? ''+)
; (not (terminal? 'E))
(define first3
  (lambda (grammar alpha seen)
    (cond
      [(null? alpha) '()]
      [else (cond
               [(pair? (car alpha))
                  (cond
                    [(equal? (caar alpha) 'quote) (cdar alpha)]
                    [else '()])]
             [else (first-var3 grammar (filter (lambda (x) (equal? (car x) (car alpha))) grammar) seen)])])))
                    







    
(define first-var3
  (lambda (grammar rules seen)
    (if (null? rules)
        '()
        (if (equal? (member (car rules) seen) #f)(union (first3 grammar (cadar rules) (cons seen (car rules))) (first-var3 grammar (cdr rules) (cons seen (car rules))))
            (first-var3 grammar (cdr rules) seen)))))

(define first-alpha
  (lambda (grammar alpha)
    (first3 grammar alpha '())))
    

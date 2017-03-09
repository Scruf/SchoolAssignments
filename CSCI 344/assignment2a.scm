#lang scheme
(define union
  (lambda (set1 set2)
    (cond
      [(null? set2) set1]
      [(member (car set2) set1) (union set1 (cdr set2))]
      [#t (union (cons (car set2) set1) (cdr set2))])))
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

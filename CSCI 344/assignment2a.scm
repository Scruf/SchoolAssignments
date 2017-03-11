;author Egor Kozitski
;3 does not work
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
(equal? (union '(1 2 3 4) '(5 6 7 8 9)) '(1 2 3 4 5 6 7 8 9))
(equal? (union '(1 2 3 4) '(5 2 6 3)) '(1 4 5 2 6 3))
(display "End of Union Testing\n")
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



(display "Testing first3\n")
(equal? (first3 *grammar* '(E2) '()) '(+ -))
(equal? (first3 *grammar* '(T2) '()) '(* /))
(equal? (first3 *grammar* '('+ E) '()) '(+))
(display "End of first3 Testing\n")
(display "Testing first-var3 \n")
(equal? (first-var3 *grammar* '()'()) '())
(equal? (first-var3 *grammar* '((E2 ())) '()) '())
(equal? (first-var3 *grammar* '((T2 ())) '()) '())
(display "End of first-var3 Testing \n")


(define first-alpha
  (lambda (grammar alpha)
    (first3 grammar alpha '())))

(display "Testing first-alpha\n")
(equal? (first-alpha *grammar* '(T E2)) '(n id - OP))
(equal? (first-alpha *grammar* '('+ T E2)) '(+))
(equal? (first-alpha *grammar* '()) '())
(display "End of first-alpa testing\n")




     
                       
             
            
                                                          
    

;; Author: Arthur Nunes-Harwitt

;; Import the parser and lexer generators.


(require (lib "yacc.ss" "parser-tools")
         (lib "lex.ss" "parser-tools")
         (prefix : (lib "lex-sre.ss" "parser-tools")))

(require (lib "pretty.ss"))

(define-tokens value-tokens (NUM ID))

(define-empty-tokens op-tokens
  (OP 
   CP
   COMMA
   EQ1
   LET 
   IN 
   + 
   - 
   * 
   /
   EOF))

(define-lex-abbrevs
 (lower-letter (:/ "a" "z"))
 (upper-letter (:/ "A" "Z"))
 (letter (:or lower-letter upper-letter))
 (digit (:/ "0" "9"))
 (ident (:+ letter))
 (number (:+ digit)))



;get-token: inputPort -> token
(define get-token
  (lexer
   ((eof) 'EOF)
   ("let" 'LET)
   ("in" 'IN)
   ("(" 'OP)
   (")" 'CP)
   ("," 'COMMA)
   ("=" 'EQ1)
   ("+" '+)
   ("-" '-)
   ("*" '*)
   ("/" '/)
   (number (token-NUM (string->number lexeme)))
   (ident (token-ID (string->symbol lexeme)))
   (whitespace (get-token input-port))))



;;; data definitions

;; A small language expression (SmallLangExp) is one of the following.
;; a number n
;; an identifier x
;; a sum with parts e1 and e2, 
;;   where e1 and e2 are small language expressions
;; a difference with parts e1 and e2, 
;;   where e1 and e2 are small language expressions
;; a product with parts e1 and e2,
;;   where e1 and e2 are small language expressions
;; a quotient with parts e1 and e2, 
;;   where e1 and e2 are small language expressions
;; a negation with part e,
;;   where e is an small language expression
;; a bindings with parts defs and e, 
;;   where defs is a list of identifiers * SmallLangExp
;;   and e is an small language expression

;; functions for associated with each part: predicate, constructor, selectors.

;; Number is a Scheme number

;; Identifier is a Scheme symbol

; make-sum: SmallLangExp * SmallLangExp -> SumExp
(define (make-sum exp1 exp2)
  (list 'sum exp1 exp2))

; (equal? (make-sum 2 3) '(sum 2 3))


; make-diff: SmallLangExp * SmallLangExp -> DiffExp
(define (make-diff exp1 exp2)
  (list 'diff exp1 exp2))

; (equal? (make-diff 2 3) '(diff 2 3))


; make-prod: SmallLangExp * SmallLangExp -> ProdExp
(define (make-prod exp1 exp2)
  (list 'prod exp1 exp2))

; (equal? (make-prod 2 3) '(prod 2 3))

; make-quo: SmallLangExp * SmallLangExp -> QuoExp
(define (make-quo exp1 exp2)
  (list 'quo exp1 exp2))

; (equal? (make-quo 2 3) '(quo 2 3))

; make-neg: SmallLangExp -> NegExp
(define (make-neg exp)
  (list 'neg exp))

; (equal? (make-neg 2) '(neg 2))

; make-let: Listof(Identifier*SmallLangExp) * SmallLangExp -> BindingExp
; Identifier*SmallLangExp is represented as a two element list
(define (make-let defs exp)
  (list 'with-bindings defs exp))

; (equal? (make-let (list (list 'x 1) (list 'y 2)) 3) '(with-bindings ((x 1) (y 2)) 3))

; parse-small-lang: (() -> token) -> SmallLangExp
(define parse-small-lang
  (parser
   (start exp)
   (end EOF)
   (tokens value-tokens op-tokens)
   (error (lambda (a b c) (error 'parse-small-lang "error occurred, ~v ~v ~v" a b c)))
   (grammar
    (exp ((LET let-defs IN exp) (make-let $2 $4))
         ((math-exp) $1))
    (let-def ((ID EQ1 exp) (list $1 $3)))
    (let-defs ((let-def) (list $1))
              ((let-def COMMA let-defs) (cons $1 $3)))
    (math-exp ((math-exp + term) (make-sum $1 $3))
              ((math-exp - term) (make-diff $1 $3))
              ((term) $1))
    (term ((term * factor) (make-prod $1 $3))
          ((term / factor) (make-quo $1 $3))
          ((factor) $1))
    (factor ((ID) $1)
            ((NUM) $1)
            ((- factor) (make-neg $2))
            ((OP exp CP) $2)))))


; lexer/parser test
(let* ((example "let x = -2 + 3 * 4, y = 0 in -2+5*x+y")
       (i (open-input-string example))) ; convert string to inputPort
  (equal? (parse-small-lang (lambda () (get-token i)))
          '(with-bindings ((x (sum (neg 2) (prod 3 4)))
                           (y 0))
             (sum (sum (neg 2) (prod 5 x)) y))))


(define sum?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '+))))
(display "Testing Part 1 \n")
(display "Testing sum?\n")
(eq? (sum? '(- 2 3)) #f)
(eq? (sum? '(+ 2 4)) #t)
(eq? (sum? '((()))) #f)
(display "End of sum testing\n")
(define difference?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '-))))
(display "\nTesting difference\n")
(eq? (difference? '(- 2 3)) #t)
(eq? (difference? '(()) ) #f)
(eq? (difference? '(+ 2 4)) #f)
(display "End of difference testing\n")
(define product?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '*))))
(display "\nTesting product\n")
(eq? (product? '(())) #f)
(eq? (product? '(* 2 3)) #t)
(eq? (product? '(+ 2 3)) #f)
(display "End of product testing\n")
(define quotient?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '/))))
(display "\nTesting quotient\n")
(eq? (quotient? '(/ 2 3)) #t)
(eq? (quotient? '(())) #f)
(eq? (quotient? '(+ 2 3)) #f)
(display "End of quotient testing\n")
(define negate?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '-))))
(display "\nTesting Negate\n")
(eq? (negate? '(- 2)) #t)
(eq? (negate? '(+ 2)) #f)
(eq? (negate? '(())) #f)
(display "End of negate test\n")
(define let?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '-))))
(display "\nTesting let?\n")
(eq? (let? '(- 2)) #t)
(eq? (let? '(())) #f)
(eq? (let? '(+ 2 3)) #f)
(display "End of let testing\n")
(define arg1
  (lambda (ex)
    (cadr ex)))
(display "\nTesting arg1\n")
(= (arg1 (make-sum 4 5)) 4)
(= (arg1 (make-sum 3 5)) 3)
(= (arg1 (make-sum 5 6)) 5)
(display "\nEnd of arg1 testing")
(define arg2
  (lambda (xs)
    (caddr xs)))
(display "\nTestig arg2\n")
(= (arg2 (make-sum 3 4)) 4)
(= (arg2 (make-sum 5 6)) 6)
(= (arg2 (make-sum 7 8)) 8)
(display "\nEnd of arg2 testing\n")
(define neg-exp
  (lambda (xs)
    (cadr xs)))
(display "\nTesting neg-exp\n")
(= (neg-exp (make-neg 4)) 4)
(= (neg-exp (make-neg 3 )) 3)
(= (neg-exp (make-neg 5)) 5)
(display "End of neg-exp testing\n")
(define let-exp
  (lambda (xs)
    (caddr xs)))
(display "\nTesting let-exp\n")
(= (let-exp (make-sum 3 4)) 4)
(= (let-exp (make-sum 5 6)) 6)
(= (let-exp (make-sum 7 8)) 8)
(display "End of let-exp testing\n")
(define let-dfs
  (lambda (xs)
    (cadr xs)))
(display "\nTesting let-dfs\n")
(= (neg-exp (make-neg 4)) 4)
(= (neg-exp (make-neg 3 )) 3)
(= (neg-exp (make-neg 5)) 5)
(display "\nEnd of let-dfs\n")
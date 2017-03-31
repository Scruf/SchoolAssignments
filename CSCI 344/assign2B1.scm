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
    (and (pair? xs) (eq? (car xs) 'sum))))
(display "Testing Part 1 \n")
(display "Testing sum?\n")
(eq? (sum? '(sum 2 3)) #t)
(eq? (sum? '(+ 2 4)) #f)
(eq? (sum? '((()))) #f)
(display "End of sum testing\n")
(define difference?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'diff))))
(display "\nTesting difference\n")
(eq? (difference? '(diff 2 3)) #t)
(eq? (difference? '(()) ) #f)
(eq? (difference? '(+ 2 4)) #f)
(display "End of difference testing\n")
(define product?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'prod))))
(display "\nTesting product\n")
(eq? (product? '(())) #f)
(eq? (product? '(prod 2 3)) #t)
(eq? (product? '(+ 2 3)) #f)
(display "End of product testing\n")
(define quotient?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'quo))))
(display "\nTesting quotient\n")
(eq? (quotient? '(quo 2 3)) #t)
(eq? (quotient? '(())) #f)
(eq? (quotient? '(+ 2 3)) #f)
(display "End of quotient testing\n")
(define negate?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'neg))))
(display "\nTesting Negate\n")
(eq? (negate? '(neg 2)) #t)
(eq? (negate? '(+ 2)) #f)
(eq? (negate? '(())) #f)
(display "End of negate test\n")
(define let?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'with-bindings))))
(display "\nTesting let?\n")
(eq? (let? '(with-bindings 2 3)) #t)
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
;; A large language expression (LargeLangExp) is one of the following.
;; a number n
;; an identifier x
;; a sum with parts e1 and e2 are small language expression
;;   where e1 and e2 are small language expressions
;; a difference with parts e1 and e2,
;;   where e1 and e2 are small language expressions
;; a product with parts e1 and e2,;
;;   where e1 and e2 are small language expressions
;; a quotient with parts e1 and e2,
;;   where e1 and e2 are small language expressions
;; a negation with part e,
;;   where e is an small language expression
;; a bindings with parts defs and e,
;;   where defs is a list of identifiers * SmallLangExp
;; and e is an small language expression
;; functions are  predicate, constructor, selectors.
;; Identifier is Scheme symbol is a Scheme number
;; Number is  Scheme number
(define program?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'program))))
(display "\nTesting program\n")
(display "\nEnd of program testing\n")
(define class-decl?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'class))))
(display "\nTesting class-decl\n")
(display "\nEnd of class-decl testing\n")
(define method?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'method))))
(display "\nTesting method\n")
(display "\nEnd of method testing\n")
(define new?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'new))))
(display "\nTesting new \n")
(display "\nEnd of new testig\n")
(define supercall?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'super))))
(display "\nTesting supercall\n")
(display "\nEnd of supercall\n")
(define seq?
  (lambda (xs)
    (and (pari? xs) (equal? (arg1 xs) 'sequence))))
(display "\nTesting seq\n")
(display "\nEnd of seq testing\n")
(define procs?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'procedures))))
(display "\nTesting procs\n")
(display "\nEnd of procs testing\n")
(define if?
  (lambda (xs)
    (and (pair? xs) (equal (arg1 xs) 'if))))
(display "\nTesting if? \n")
(display "\nEnd of if testing\n")
(define assign?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'assign!))))
(display "\nTesting assign\n")
(display "\nEnd of assign\n")
(define equality?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'equality?))))
(display "\nTesting equal\n")
(display "\nEnd of equal\n")
(define proc?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'proc))))
(display "\nTesting proc?\n")
(display "\nEnd of proc testing\n")
(define access?
  (lambda (xs)
    (and (pair? xs) (equal? (arg1 xs) 'send))))
(display "\nTesting access\n")
(display "\nEnd of access\n")
(define funcall?
  (lambda (xs)
    (and (pair? xs) (equal? (carg1 xs) 'funcall))))
(display "\nTesting funcall\n")
(display "\nEnd of funcall testing\n")
(define make-program
  (lambda (ex1 ex2)
    (list 'program ex1 ex2)))
(display "\nTesting make-program\n")
(display "\nEnd of make-program\n")
(define make-class
  (lambda (ex1 ex2 ex3 ex4)
    (list 'class ex1 ex2 ex3 ex4)))
(display "\nTesting make-class\n")
(display "\nEnd of make-class testing\n")
(define make-method
  (lambda (ex1 ex2)
    (list 'method ex1 ex2)))
(display "\nTesting make-method\n")
(display "\nEnd of make-method\n")
(define make-new
  (lambda (ex1 ex2)
     (list 'new ex1 ex2)))
(display "\nTesting make-new\n")
(display "\nEnd of make-new testing\n")
(define make-supercall
  (lambda (ex1 ex2)
    (list 'super ex1 ex2)))
(display "\nTesting make-supercall\n")
(display "\nEnd of make-supercall\n")
(define make-seq
  (lambda (ex1)
    (list 'sequence ex1)))
(display "\nTesting make-seq\n")
(display "\nEnd of make-seq\n")
(define make-procs
  (lambda (ex1 ex2)
    (list 'procedures ex1 ex2)))
(display "\nTesting make-procs\n")
(display "\nEnd of make-procs\n")
(define make-if
  (lambda (ex1 ex2 ex3)
    (list 'if ex1 ex2 ex3)))
(display "\nTesting make-if\n")
(display "End if make-if testing\n")
(define make-assign
  (lambda (ex1 ex2)
    (list 'assign! ex1 ex2)))
(display "\nTesting make-assign\n")
(display "\nEnd of make-assing testing\n")
(define make-equal
  (lambda (ex1 ex2)
    (list 'equality? ex1 ex2)))
(display "\nTesting make-equal\n")
(display "\nEnd of make-equal\n")
(define make-proc
  (lambda (ex1 ex2)
    (list 'proc ex1 ex2)))
(display "\nTesting make-proc\n")
(display "\nEnd of make-proc\n")
(define make-access
  (lambda (ex1 ex2)
    (list 'send ex1 ex2)))
(display "\nTesting make-access\n")
(display "\nEnd of make-access\n")
(define make-funcall
  (lambda (ex1 ex2)
    (list 'funcall ex1 ex2)))
(display "\nTesting make-funcall\n")
(display "\nEnd of make-funcall testing\n")
(define program-decls
  (lambda (xs)
    (cadr xs)))
(display "\nTesting program-decls\n")
(display "\nEnd of program-decls\n")
(define program-exp
  (lambda (xs)
    (caddr xs)))
(display "\nTesting program-exp\n")
(display "\nEnd of program-exp\n")
(define class-name
  (lambda (xs)
    (cadr xs)))
(display "\nTesting class-name\n")
(display "\nEnd of class-name testing\n")
(define class-parent
  (lambda (xs)
    (caddr xs)))
(display "\nTesting class-parent\n")
(display "\nEnd of class-parent testing\n")
(define class-fields
  (lambda (xs)
    (cadddr xs)))
(display "\nTesting class fields\n")
(display "\nEnd of class fields\n")
(define class-methods
  (lambda (xs)
    (caddddr xs)))
(display "\nTesting class-methods\n")
(display "\nEnd of class methods testing\n")
(define method-name
  (lambda (xs)
    (cadr xs)))
(display "\nTesting method-name\n")
(display "\nEnd of method-name testing\n")
(define method-formals
  (lambda (xs)
    (caddr xs)))
(display "\nTesting method-formals\n")
(display "\nEnd of method-formals testing\n")
(define method-exp
  (lambda (xs)
    (cadddr xs)))
(display "\nTesting method-exp\n")
(display "\nEnd of method-exp\n")
(define new-name
  (lambda (xs)
    (cadr xs)))
(display "\nTesting new-name\n")
(display "\nEnd of new-name\n")
(define new-rands
  (lambda (xs)
    (cadr xs)))
(display "\nTesting new-rands\n")
(display "\nEnd of new-rands\n")
(define supercall-name
  (lambda (xs)
    (caddr xs)))
(display "\nTesting supercall-name\n")
(display "\nEnd of supercall-name\n")
(define supercall-rands
  (lambda (xs)
    (cadr xs)))
(display "\nTesting supercall-rands\n")
(display "\nEnd of supercall-rands\n")
(define seq-exps
  (lambda (xs)
    (cadr xs)))
(display "\nTesting seq-exps\n")
(display "\nEnd of seq-exps\n")
(define procs-def
  (lambda (xs)
    (cadr xs)))
(display "\nTesting procs-def\n")
(display "\nEnd of prcs-def testing\n")
(define procs-exp
  (lambda (xs)
    (caddr xs)))
(display "\nTesting procs-expp\n")
(display "\nEnd of procs-exp testing\n")
(define if-exp1
  (lambda (xs)
    (cadr xs)))
(display "\nTesting if-exp1\n")
(display "\nEnd of if-exp1 testing\n")
(define if-exp2
  (lambda (xs)
    (caddr (xs))))
(display "\nTesting if-exp2\n")
(display "\nEnd of if-exp2 testing\n")
(define if-exp3
  (lambda (xs)
    (cadddr xs)))
(display "\nTesting if-exp3\n")
(display "\nEnd of if-exp3 testing\n")
(define assign-var
  (lambda (xs)
    (cadr xs)))
(display "\nTesting assign-var\n")
(display "\nEnd of assign-var testing\n")
(define assign-exp
  (lambda (xs)
    (caddr xs)))
(display "\nTesting assign-exp\n")
(display "\nEnd of assign-exp testing\n")
(define proc-formals
  (lambda (xs)
    (cadr xs)))
(display "\nTesting proc-formals\n")
(display "\nEnd of proc-formals\n")
(define proc-exp
  (lambda (xs)
    (cadr xs)))
(display "\nTesting proc-exp\n")
(display "\nEnd of proc-exp\n")
(define access-exp
  (lambda (xs)
    (caddr xs)))
(display "\nTesting access-exp\n")
(display "\nEnd of access-exp\n")
(define access-message
  (lambda (xs)
    (cadr xs)))
(display "\nTesting access-message\n")
(display "\nEnd of access-message\n")
(define funcall-rator
  (lambda (xs)
    (cadr xs)))
(display "\nTesting funcall-rator\n")
(display "\nEnd of funcall-rator testing\n")
(define funcall-rands
  (lambda (xs)
    (caddr xs)))
(display "\nTesting funcall-rands\n")
(display "\nEnd of funcall-rands\n")

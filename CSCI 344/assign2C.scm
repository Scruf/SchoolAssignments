;; Author: Arthur Nunes-Harwitt
;; @author Egor Kozitski
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
   EOF
   NUMBER
   SC
   PERIOD
   CLASS
   ELSE
   BACKSLASH
   ARROW
   NEW
   SUPER
   EXTENDS
   OBC
   CBC
   METHOD
   PROCEDURE
   IF
   THEN
   COMP
   FIELD))


(define-lex-abbrevs
  (lower-letter (:/ "a" "z"))
  (upper-letter (:/ "A" "Z"))
  (letter (:or lower-letter upper-letter))
  (digit (:/ "0" "9"))
  (idfirst (:or (:or letter "_") "$"))
  (idrest (:or idfirst digit))
  (ident (:: idfirst (:* idrest)))
  (digits (:+ digit))
  (number (:: (:: digits(:?(:: "." digits)))
              (:?(::(::(:or "E" "e")(:? (:or "+" "-"))
                       digits))))))

  

;get-token: inputPort -> token
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
   ("procedures" 'PROCEDURE)
   ("if" 'IF)
   ("then" 'THEN)
   ("else" 'ELSE)
   ("class" 'CLASS)
   ("extends" 'EXTENDS)
   ("{" 'OBC)
   ("}" 'CBC)
   ("method" 'METHOD)
   ("\\" 'BACKSLASH)
   ("->" 'ARROW)
   ("number" 'NUMBER)
   (";" 'SC)
   ("." 'PERIOD)
   ("new" 'NEW)
   ("super" 'SUPER)
   ("==" 'COMP)
   ("field" 'FIELD)
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
(define (make-sum xs ys)
  (list 'sum xs ys))

(define (make-diff xs ys)
  (list 'diff xs ys))

(define (make-prod xs ys)
  (list 'prod xs ys))

(define (make-quo xs ys)
  (list 'quo xs ys))

(define (make-neg xs)
  (list 'neg xs))

(define (make-let xs ys)
  (list 'with-bindings xs ys))

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
  (lambda (xs)
    (car (cdr xs))))
(display "\nTesting arg1\n")
(= (arg1 (make-sum 4 5)) 4)
(= (arg1 (make-sum 3 5)) 3)
(= (arg1 (make-sum 5 6)) 5)
(display "\nEnd of arg1 testing")
(define arg2
  (lambda (xs)
    (car (cdr (cdr xs)))))
(display "\nTestig arg2\n")
(= (arg2 (make-sum 3 4)) 4)
(= (arg2 (make-sum 5 6)) 6)
(= (arg2 (make-sum 7 8)) 8)
(display "\nEnd of arg2 testing\n")
(define neg-exp
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting neg-exp\n")
(= (neg-exp (make-neg 4)) 4)
(= (neg-exp (make-neg 3 )) 3)
(= (neg-exp (make-neg 5)) 5)
(display "End of neg-exp testing\n")
(define let-exp
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting let-exp\n")
(= (let-exp (make-sum 3 4)) 4)
(= (let-exp (make-sum 5 6)) 6)
(= (let-exp (make-sum 7 8)) 8)
(display "End of let-exp testing\n")
(define let-dfs
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting let-dfs\n")
(= (neg-exp (make-neg 4)) 4)
(= (neg-exp (make-neg 3 )) 3)
(= (neg-exp (make-neg 5)) 5)
(display "\nEnd of let-dfs\n")
(define make-program
  (lambda (ex1 ex2)
    (list 'program ex1 ex2)))
(display "\nTesting make-program\n")
(equal? (make-program 1 2) '(program 1 2))
(equal? (make-program 'a 'b) '(program a b))
(equal? (make-program '() '()) '(program () ()))
(display "\nEnd of make-program\n")
(define make-class
  (lambda (ex1 ex2 ex3 ex4)
    (list 'class ex1 ex2 ex3 ex4)))
(display "\nTesting make-class\n")
(equal? (make-class 1 2 3 4) '(class 1 2 3 4))
(equal? (make-class 'a 'b 'c 'd) '(class a b c d))
(equal? (make-class '() '() '() '()) '(class () () () ()))
(display "\nEnd of make-class testing\n")
(define make-method
  (lambda (ex1 ex2 ex3)
    (list 'method ex1 ex2 ex3)))
(display "\nTesting make-method\n")
(equal? (make-method 1 2 3) '(method 1 2 3))
(equal? (make-method 'a 'b 'c) '(method a b c))
(equal? (make-method '() '() '()) '(method () () ()))
(display "\nEnd of make-access\n")

(display "\nEnd of make-method\n")
(define make-new
  (lambda (ex1 ex2)
     (cons 'new (cons ex1 ex2))))
(display "\nTesting make-new\n")
(equal? (make-new 1 2) '(new 1 2))
(equal? (make-new 'a 'b) '(new a b))
(equal? (make-new '() '()) '(new () ()))
(display "\nEnd of make-new testing\n")
(define make-supercall
  (lambda (ex1 ex2)
    (list 'super ex1 ex2)))
(display "\nTesting make-supercall\n")
(equal? (make-supercall 1 2) '(super 1 2))
(equal? (make-supercall 'a 'b) '(super a b))
(equal? (make-supercall '() '()) '(super () ()))
(display "\nEnd of make-supercall\n")
(define make-seq
  (lambda (ex1)
    (cons 'sequence ex1)))
(display "\nTesting make-seq\n")
(equal? (make-seq 1) '(sequence 1))
(equal? (make-seq 'a) '(sequence a))
(equal? (make-seq '()) '(sequence ()))
(display "\nEnd of make-seq\n")
(define make-procs
  (lambda (ex1 ex2)
   (list 'procedures ex1 ex2)))
(display "\nTesting make-procs\n")
(equal? (make-procs 1 2) '(procedures 1 2))
(equal? (make-procs 'a 'b) '(procedures a b))
(equal? (make-procs '() '()) '(procedures () ()))
(display "\nEnd of make-procs\n")
(define make-if
  (lambda (ex1 ex2 ex3)
    (list 'if ex1 ex2 ex3)))
(display "\nTesting make-if\n")
(equal? (make-if 1 2 3) '(if 1 2 3))
(equal? (make-if 'a 'b 'c) '(if a b c))
(equal? (make-if '() '() '()) '(if () () ()))
(display "End if make-if testing\n")
(define make-assign
  (lambda (ex1 ex2)
    (list 'assign! ex1 ex2)))
(display "\nTesting make-assign\n")
(equal? (make-assign 1 2) '(assign! 1 2))
(equal? (make-assign 'a 'b) '(assign! a b))
(equal? (make-assign '() '()) '(assign! () ()))
(display "\nEnd of make-assing testing\n")
(define make-equal
  (lambda (ex1 ex2)
    (list 'equality? ex1 ex2)))
(display "\nTesting make-equal\n")
(equal? (make-equal 1 2) '(equality? 1 2))
(equal? (make-equal 'a 'b) '(equality? a b))
(equal? (make-equal '() '()) '(equality? () ()))
(display "\nEnd of make-equal\n")
(define make-proc
  (lambda (ex1 ex2)
    (list 'proc ex1 ex2)))
(display "\nTesting make-proc\n")
(equal? (make-proc 1 2) '(proc 1 2))
(equal? (make-proc 'a 'b) '(proc a b))
(equal? (make-proc '() '()) '(proc () ()))
(display "\nEnd of make-proc\n")
(define make-access
  (lambda (ex1 ex2)
    (list 'send ex1 ex2)))
(display "\nTesting make-access\n")
(equal? (make-access 1 2) '(send 1 2))
(equal? (make-access 'a 'b) '(send a b))
(equal? (make-access '() '()) '(send () ()))
(display "\nEnd of make-access\n")
(define (make-funcall rator rands)
  (cons 'funcall (cons rator rands)))
(display "\nTesting make-funcall\n")
(equal? (make-funcall 1 2) '(funcall 1 2))
(equal? (make-funcall 'a 'b) '(funcall a b))

(display "\nEnd of make-funcall testing\n")
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
    (and (pair? xs) (eq? (car xs) 'program))))
(display "\nTesting program\n")
(equal? (program? (make-program 3 4)) #t)
(equal? (program? (make-program '() '())) #t)
(equal? (program? (make-program 'a 'b)) #t)
(display "\nEnd of program testing\n")
(define class-decl?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'class))))
(display "\nTesting class-decl\n")
(equal? (class-decl? (make-class 1 2 4 6)) #t)
(equal? (class-decl? (make-class 'a 'b 'c 'd)) #t)
(equal? (class-decl? (make-class 1 3 5 7)) #t)
(display "\nEnd of class-decl testing\n")
(define method?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'method))))
(display "\nTesting method\n")
(equal? (method? (make-method 1 5 7)) #t)
(equal? (method? (make-method 'q 'w 'e)) #t)
(equal? (method? (make-method 2 4 6)) #t)
(display "\nEnd of method testing\n")
(define new?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'new))))
(display "\nTesting new \n")
(equal? (new? (make-new 1 3)) #t)
(equal? (new? (make-new '() '())) #t)
(equal? (new? (make-new 'a 'b)) #t)
(display "\nEnd of new testig\n")
(define supercall?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'super))))
(display "\nTesting supercall\n")
(equal? (supercall? (make-supercall 1 2)) #t)
(equal? (supercall? (make-supercall '() '())) #t)
(equal? (supercall? (make-supercall 'a 'b)) #t)
(display "\nEnd of supercall\n")
(define seq?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'sequence))))
(display "\nTesting seq\n")
(equal? (seq? (make-seq 1)) #t)
(equal? (seq? (make-seq 'a)) #t)
(equal? (seq? (make-seq '())) #t)
(display "\nEnd of seq testing\n")
(define procs?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'procedures))))
(display "\nTesting procs\n")
(equal? (procs? (make-procs 1 2)) #t)
(equal? (procs? (make-procs 'a 'b)) #t)
(equal? (procs? (make-procs '() '())) #t)
(display "\nEnd of procs testing\n")
(define if?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'if))))
(display "\nTesting if? \n")
(equal? (if? (make-if 1 2 3)) #t)
(equal? (if? (make-if 'a 'b 'c)) #t)
(equal? (if? (make-if '() '() '())) #t)
(display "\nEnd of if testing\n")
(define assign?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'assign!))))
(display "\nTesting assign\n")
(equal? (assign? (make-assign 1 2)) #t)
(equal? (assign? (make-assign 'a 'b)) #t)
(equal? (assign? (make-assign '() '())) #t)
(display "\nEnd of assign\n")
(define equality?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'equality?))))
(display "\nTesting equal\n")
(equal? (equality? (make-equal 'a 'b)) #t)
(equal? (equality? (make-equal 1 2)) #t)
(equal? (equality? (make-equal '() '())) #t)
(display "\nEnd of equal\n")
(define proc?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'proc))))
(display "\nTesting proc?\n")
(equal? (proc? (make-proc 'a 'b)) #t)
(equal? (proc? (make-proc 1 2)) #t)
(equal? (proc? (make-proc '() '())) #t)
(display "\nEnd of proc testing\n")
(define access?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'send))))
(display "\nTesting access\n")
(equal? (access? (make-access 'a 'b)) #t)
(equal? (access? (make-access 1 2)) #t)
(equal? (access? (make-access '() '())) #t)
(display "\nEnd of access\n")
(define funcall?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'funcall))))
(display "\nTesting funcall\n")
(equal? (funcall? (make-funcall 'a 'b)) #t)
(equal? (funcall? (make-funcall 1 2)) #t)
(equal? (funcall? (make-funcall '() '())) #t)
(display "\nEnd of funcall testing\n")


(define program-decls
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting program-decls\n")
(equal? (program-decls (make-program 1 2)) 1)
(equal? (program-decls (make-program 'a 'b)) 'a)
(equal? (program-decls (make-program '() '()))'())
(display "\nEnd of program-decls\n")
(define program-exp
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting program-exp\n")
(equal? (program-exp (make-program 1 2)) 2)
(equal? (program-exp (make-program 'a 'b)) 'b)
(equal? (program-exp (make-program '() '()))'())
(display "\nEnd of program-exp\n")
(define class-name
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting class-name\n")
(equal? (class-name (make-class 1 2 3 4)) 1)
(equal? (class-name (make-class 'a 'b 'c 'd)) 'a)
(equal? (class-name (make-class '() '() '() '()))'())
(display "\nEnd of class-name testing\n")
(define class-parent
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting class-parent\n")
(equal? (class-parent (make-class 1 2 3 4)) 2)
(equal? (class-parent (make-class 'a 'b 'c 'd)) 'b)
(equal? (class-parent (make-class '() '() '() '()))'())
(display "\nEnd of class-parent testing\n")
(define class-fields
  (lambda (xs)
  (car (cdr (cdr (cdr xs))))))
(display "\nTesting class fields\n")
(equal? (class-fields (make-class 1 2 3 4)) 3)
(equal? (class-fields (make-class 'a 'b 'c 'd)) 'c)
(equal? (class-fields (make-class '() '() '() '()))'())
(display "\nEnd of class fields\n")
(define class-methods
  (lambda (xs)
   (car (cdr (cdr (cdr (cdr xs)))))))
(display "\nTesting class-methods\n")
(equal? (class-methods (make-class 1 2 3 4)) 4)
(equal? (class-methods (make-class 'a 'b 'c 'd)) 'd)
(equal? (class-methods (make-class '() '() '() '()))'())
(display "\nEnd of class methods testing\n")
(define method-name
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting method-name\n")
(equal? (method-name (make-class 1 2 3 4)) 1)
(equal? (method-name (make-class 'a 'b 'c 'd)) 'a)
(equal? (method-name (make-class '() '() '() '())) '())
(display "\nEnd of method-name testing\n")
(define method-formals
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting method-formals\n")
(equal? (method-formals (make-class 1 2 3 4)) 2)
(equal? (method-formals (make-class 'a 'b 'c 'd)) 'b)
(equal? (method-formals (make-class '() '() '() '())) '())
(display "\nEnd of method-formals testing\n")
(define method-exp
  (lambda (xs)
     (car (cdr (cdr (cdr xs))))))
(display "\nTesting method-exp\n")
(equal? (method-exp (make-class 1 2 3 4)) 3)
(equal? (method-exp (make-class 'a 'b 'c 'd)) 'c)
(equal? (method-exp (make-class '() '() '() '())) '())
(display "\nEnd of method-exp\n")
(define new-name
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting new-name\n")
(equal? (new-name (make-new 1 2)) 1)
(equal? (new-name (make-new 'a 'b)) 'a)
(equal? (new-name (make-new '() '())) '())
(display "\nEnd of new-name\n")
(define new-rands
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting new-rands\n")

(display "\nEnd of new-rands\n")
(define supercall-name
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting supercall-name\n")
(equal? (supercall-name (make-supercall 1 2)) 1)
(equal? (supercall-name (make-supercall 'a 'b)) 'a)
(equal? (supercall-name (make-supercall '() '())) '())
(display "\nEnd of supercall-name\n")
(define supercall-rands
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting supercall-rands\n")
(equal? (supercall-rands (make-supercall 1 2)) 2)
(equal? (supercall-rands (make-supercall 'a 'b)) 'b)
(equal? (supercall-rands (make-supercall '() '())) '())
(display "\nEnd of supercall-rands\n")
(define seq-exps
  (lambda (xs)
    (cdr xs)))
(display "\nTesting seq-exps\n")
(equal? (seq-exps (make-seq 1)) 1)
(equal? (seq-exps (make-seq 'a)) 'a)
(equal? (seq-exps (make-seq '())) '())
(display "\nEnd of seq-exps\n")
(define procs-defs
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting procs-def\n")
(equal? (procs-defs (make-procs 1 2)) 1)
(equal? (procs-defs (make-procs 'a 'b)) 'a)
(equal? (procs-defs (make-procs '() '())) '())
(display "\nEnd of prcs-def testing\n")
(define procs-exp
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting procs-expp\n")
(equal? (procs-exp (make-procs 1 2)) 2)
(equal? (procs-exp (make-procs 'a 'b)) 'b)
(equal? (procs-exp (make-procs '() '())) '())
(display "\nEnd of procs-exp testing\n")
(define if-exp1
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting if-exp1\n")
(equal? (if-exp1 (make-if 1 2 3)) 1)
(equal? (if-exp1 (make-if 'a 'b 'c)) 'a)
(equal? (if-exp1 (make-if '() '() '())) '())
(display "\nEnd of if-exp1 testing\n")
(define if-exp2
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting if-exp2\n")
(equal? (if-exp2 (make-if 1 2 3)) 2)
(equal? (if-exp2 (make-if 'a 'b 'c)) 'b)
(equal? (if-exp2 (make-if '() '() '())) '())
(display "\nEnd of if-exp2 testing\n")
(define if-exp3
  (lambda (xs)
   (car (cdr (cdr (cdr xs))))))
(display "\nTesting if-exp3\n")
(equal? (if-exp3 (make-if 1 2 3)) 3)
(equal? (if-exp3 (make-if 'a 'b 'c)) 'c)
(equal? (if-exp3 (make-if '() '() '())) '())
(display "\nEnd of if-exp3 testing\n")
(define assign-var
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting assign-var\n")
(equal? (assign-var (make-assign 1 2)) 1)
(equal? (assign-var (make-assign 'a 'b)) 'a)
(equal? (assign-var (make-assign '() '())) '())
(display "\nEnd of assign-var testing\n")
(define assign-exp
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting assign-exp\n")
(equal? (assign-exp (make-assign 1 2)) 2)
(equal? (assign-exp (make-assign 'a 'b)) 'b)
(equal? (assign-exp (make-assign '() '())) '())
(display "\nEnd of assign-exp testing\n")
(define proc-formals
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting proc-formals\n")
(equal? (proc-formals (make-proc 1 2)) 1)
(equal? (proc-formals (make-proc 'a 'b)) 'a)
(equal? (proc-formals (make-proc '() '() )) '())
(display "\nEnd of proc-formals\n")
(define proc-exp
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting proc-exp\n")
(equal? (proc-exp (make-proc 1 2)) 2)
(equal? (proc-exp (make-proc 'a 'b)) 'b)
(equal? (proc-exp (make-proc '() '())) '())
(display "\nEnd of proc-exp\n")
(define access-exp
  (lambda (xs)
    (arg1 xs)))
(display "\nTesting access-exp\n")
(equal? (access-exp (make-access 1 2)) 1)
(equal? (access-exp (make-access 'a 'b)) 'a)
(equal? (access-exp (make-access '() '())) '())
(display "\nEnd of access-exp\n")
(define access-message
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting access-message\n")
(equal? (access-message (make-access 1 2)) 2)
(equal? (access-message (make-access 'a 'b)) 'b)
(equal? (access-message (make-access '() '())) '())
(display "\nEnd of access-message\n")
(define funcall-rator
  (lambda (xs)
    (arg2 xs)))
(display "\nTesting funcall-rator\n")

(display "\nEnd of funcall-rator testing\n")
(define funcall-rands
  (lambda (xs)
    (cddr xs)))


(define parse-lang
  (parser
   (start program)
   (end EOF)
   (tokens value-tokens op-tokens)
   (error (lambda (a b c) (error 'parse-small-lang "error occurred, ~v ~v ~v" a b c)))
   (grammar
        
    (let-def ((ID EQ1 expression) (list $1 $3)))
    (let-defs ((let-def) (list $1))
             ((let-def COMMA let-defs) (cons $1 $3)))
    (proc-def (( ID OP formals CP EQ1 expression) (list $1 (make-proc $3 $6))))
    (proc-defs ((proc-def) (list $1))
              ((proc-def COMMA proc-defs) (cons $1 $3)))
   
    (exprsions ((expression) (list $1))
           ((expression SC exprsions) (cons $1 $3)))
    (expression ((LET let-defs IN expression) (make-let $2 $4))
          ((PROCEDURE proc-defs IN expression) (make-procs $2 $4))
          (( OBC exprsions CBC ) (make-seq $2))
          (( IF expression THEN expression ELSE expression) (make-if $2 $4 $6))
          ((BACKSLASH formals BACKSLASH ARROW expression) (make-proc $2 $5))
          (( NEW ID OP actuals CP) (make-new $2 $4))
          (( SUPER ID OP actuals CP) (make-supercall $2 $4))
          (( ID EQ1 expression) (make-assign $1 $3))
          (( comp-expression ) $1))
    (comp-expression ((math-expression COMP math-expression) (make-equal $1 $3))
              ((math-expression) $1))
    (math-expression ((math-expression + term) (make-sum $1 $3))
               ((math-expression - term) (make-diff $1 $3))
               ((term) $1))
   
    (term (( term * factor) (make-prod $1 $3))
          (( term / factor) (make-quo $1 $3))
          (( factor) $1))
    (factor ((simple) $1)
            ((NUM) $1)
            ((- factor) (make-neg $2)))
    (simple ((ID) $1)
            ((simple PERIOD ID) (make-access $1 $3))
            ((simple OP actuals CP) (make-funcall $1 $3))
            ((OP expression CP) $2))
    (actuals (() null)
             ((nonemptyactuals) $1))
    (nonemptyactuals ((expression) (list $1))
                     ((expression COMMA nonemptyactuals) (cons $1 $3)))
    (formals (() null)
             ((nonemptyformals) $1))
    (nonemptyformals (( ID) (list $1))
                     (( ID COMMA nonemptyformals) (cons $1 $3)))
    (program ((class-declarationss expression) (make-program $1 $2)))
    (class-declaration ((CLASS ID EXTENDS ID OBC fielddecls methdecls CBC) (make-class $2 $4 $6 $7)))
    (class-declarationss (() null)
                ((class-declaration class-declarationss) (cons $1 $2)))
   
    (fielddecls (() null)
                ((FIELD ID fielddecls) (cons $2 $3)))
    (methdecls (() null)
               ((METHOD ID OP formals CP expression  methdecls) (cons (make-method $2 $4 $6) $7)))
    )))
(let* ((example "let x = 2 + 2 * 2, y = 0 in -2+3/x+y")
       (i (open-input-string example))) ; convert string to inputPort
  (parse-lang (lambda () (get-token i))))

(let* ((example "let x = -(1+1) + 3 * 4, y = 0 in {y = 14; x == y}")
       (i (open-input-string example)))
  (equal? (parse-lang (lambda () (get-token i)))
          '(program
            ()
            (with-bindings
             ((x (sum (neg (sum 1 1)) (prod 3 4))) (y 0))
             (sequence (assign! y 14) (equality? x y))))))

(let* ((example 
"let pred = \\k\\->k-1 
  in procedures f(n) = if n == 0
                       then 1 
                       else n * f(pred(n)) 
      in f(4+1)
")
       (i (open-input-string example)))
  (equal? (parse-lang (lambda () (get-token i)))
          '(program
            ()
            (with-bindings
             ((pred (proc (k) (diff k 1))))
             (procedures
              ((f
                (proc
                 (n)
                 (if (equality? n 0) 1 (prod n (funcall f (funcall pred n)))))))
              (funcall f (sum 4 1)))))))


(let* ((example 
"class point extends object{
  field x
  field y
  method init(initx, inity){
   x = initx;
   y = inity
  }
  method move(dx, dy){
   x = x + dx;
   y = y + dy
  }
}
let ob = new point(2+3, 1+4*7) in
  ob.move(0.1,3)
")
       (i (open-input-string example)))
  (equal? (parse-lang (lambda () (get-token i)))
          '(program
            ((class point object
                    (x y)
                    ((method
                      init
                      (initx inity)
                      (sequence (assign! x initx) (assign! y inity)))
                     (method
                      move
                      (dx dy)
                      (sequence (assign! x (sum x dx)) (assign! y (sum y dy)))))))
            (with-bindings
             ((ob (new point (sum 2 3) (sum 1 (prod 4 7)))))
             (funcall (send ob move) 0.1 3)))))

;;; Author: Arthur Nunes-Harwitt

(require (lib "yacc.ss" "parser-tools")
         (lib "lex.ss" "parser-tools")
         (prefix : (lib "lex-sre.ss" "parser-tools")))

(require (lib "pretty.ss"))

(define-tokens value-tokens (NUM ID))

(define-empty-tokens op-tokens
  (BEGIN
   END
   SEMI
   EQ1
   EQ2
   IF
   THEN
   ELSE
   OP
   CP
   COMMA
   LET
   IN
   BSLASH
   ARROW
   PROCS
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
   ("if" 'IF)
   ("then" 'THEN)
   ("else" 'ELSE)
   ("let" 'LET)
   ("in" 'IN)
   ("\\" 'BSLASH) ;just one, it looks like two because \ is an escape character
   ("->" 'ARROW)
   ("procedures" 'PROCS)
   ("{" 'BEGIN)
   ("}" 'END)
   ("(" 'OP)
   (")" 'CP)
   (";" 'SEMI)
   ("," 'COMMA)
   ("=" 'EQ1)
   ("==" 'EQ2)
   ("+" '+)
   ("-" '-)
   ("*" '*)
   ("/" '/)
   (number (token-NUM (string->number lexeme)))
   (ident (token-ID (string->symbol lexeme)))
   (whitespace (get-token input-port))))


;; An expression (Exp) is one of the following.

;; a Number n
;; an Identifier x

;; a sum with parts e1 and e2,

;;   where e1 and e2 are expressions
;; a let with parts x, e1, and e2,

;;   where x is an identifier
;;   and e1 and e2 are expressions
;; an if with parts e1, e2, and e3,
;;   where e1, e2, and e3 are expressions
;; a proc with parts x and e
;;   where x is an identifier and e is an expression
;; a funcall with parts e1 and e2
;;   where e1 and e2 are expressions
;; an assignment with parts x and e
;;   where x is an identifier and e is an expression

;; functions for associated with each part: predicate, constructor, selectors.

;; Number is a Scheme number


;; Identifier is a Scheme symbol


; ident?: Any -> Bool

(define ident? symbol?)

; (ident? 'x)


; sum?: Any -> Bool

(define (sum? a) (and (pair? a) (eq? (car a) '+)))

; (eq? (sum? '(+ 2 3)) #t)

; (eq? (sum? '3) #f)


; make-sum: Exp * Exp -> SumExp

(define (make-sum exp1 exp2)
  (list '+ exp1 exp2))

; (equal? (make-sum 2 3) '(+ 2 3))

; arg1: SumExp -> Exp

(define (arg1 e) (car (cdr e)))

; (= (arg1 (make-sum 2 3)) 2)



; arg2: SumExp -> Exp

(define (arg2 e) (car (cdr (cdr e))))

; (= (arg2 (make-sum 2 3)) 3)


; let?: Any -> Bool

(define (let? a) (and (pair? a) (eq? (car a) 'let)))

; (eq? (let? '(let x 1 3)) #t)

; (eq? (let? '3) #f)


; make-let: Identifier*Exp * Exp -> LetExp

; Identifier*Exp is represented as a two element list

(define (make-let var exp1 exp2)
  (list 'let var exp1 exp2))

; (equal? (make-let 'x 1 3) '(let x 1 3))


; let-var: LetExp -> Identifier

(define let-var cadr)

; (eq? (let-var (make-let 'x 1 3)) 'x)

; let-exp: LetExp -> Exp

(define let-exp caddr)

; (= (let-exp (make-let 'x 1 3)) 1)

; let-body: LetExp -> Exp

(define let-body cadddr)

; (= (let-body (make-let 'x 1 3)) 3)

; if?: Any -> Bool
(define (if? a) (and (pair? a) (eq? (car a) 'if)))

; (if? (make-if 1 2 3))
; (not (if? 5))

; make-if: Exp * Exp * Exp -> IfExp
(define (make-if exp1 exp2 exp3)
  (list 'if exp1 exp2 exp3))

; (equal? (make-if 1 2 3) '(if 1 2 3))

; if-exp1: IfExp -> Exp
(define if-exp1 cadr)

; (= (if-exp1 (make-if 1 2 3)) 1)

; if-exp2: IfExp -> Exp
(define if-exp2 caddr)

; (= (if-exp2 (make-if 1 2 3)) 2)

; if-exp3: IfExp -> Exp
(define if-exp3 cadddr)

; (= (if-exp3 (make-if 1 2 3)) 3)

; proc?: Any -> Bool
(define (proc? a) (and (pair? a) (eq? (car a) 'proc)))

; (proc? '(proc x 1))
; (not (proc? 3))

; make-proc: Identifier * Exp -> ProcExp
(define (make-proc var exp)
  (list 'proc var exp))

; (equal? (make-proc 'x 1) '(proc x 1))

; proc-var: ProcExp -> Identifier
(define proc-var cadr)

; (eq? (proc-var (make-proc 'x 1)) 'x)

; proc-exp: ProcExp -> Exp
(define proc-exp caddr)

; (= (proc-exp (make-proc 'x 1)) 1)


; funcall? Any -> Bool
(define (funcall? a) (and (pair? a) (eq? (car a) 'funcall)))

; (funcall? '(funcall f 1))
; (not (funcall? 3))

; make-funcall: Exp * Exp -> FuncallExp
(define (make-funcall exp1 exp2)
  (list 'funcall exp1 exp2))

; (equal? (make-funcall 'f 1) '(funcall f 1))

; funcall-rator: FuncallExp -> Exp
(define funcall-rator cadr)

; (eq? (funcall-rator (make-funcall 'f 1)) 'f)

; funcall-rand: FuncallExp -> Exp
(define funcall-rand caddr)

; (= (funcall-rand (make-funcall 'f 1)) 1)

; assign?: Any -> Bool
(define (assign? a) (and (pair? a) (eq? (car a) 'assign!)))

; (eq? (assign? '(assign! x 3)) #t)
; (eq? (assign? '3) #f)


; make-assign: Identifier * LangExp -> AssignExp
(define (make-assign var exp)
  (list 'assign! var exp))

; (equal? (make-assign 'x 3) (assign! x 3))

; assign-var: AssignExp -> Identifier
(define assign-var cadr)

; (eq? (assign-var (make-assign 'x 3)) 'x)

; assign-exp: AssignExp -> LangExp
(define assign-exp caddr)

; (= (assign-exp (make-assign 'x 3)) 3)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define diff?
  (lambda (xs)
    (and (pair? xs)(eq? (car xs) '-))))
(display "Testing diff?\n")
(eq? (diff? '(- 1 2)) #t)
(eq? (diff? '(+ 1 2)) #f)
(eq? (diff? '(2 2 2)) #f)
(display "End of diff? testing\n")

(define prod?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '*))))
(display "Testing prod?\n")
(eq? (prod? '(* 1 2)) #t)
(eq? (prod? '(+ 1 2)) #f)
(eq? (prod? '(2 2 2)) #f)
(display "End of prod? testing\n")
(define quo?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) '/))))
(display "Testing quo?\n")
(eq? (quo? '(/ 1 2)) #t)
(eq? (quo? '(+ 1 2)) #f)
(eq? (quo? '(2 2 2)) #f)
(display "End of quo? testing\n")
(define neg?
  (lambda (xs)
    (and (pair? xs) (eq? (car xs) 'neg))))
(display "Testing neg? \n")
(eq? (neg? '(- 2)) #t)
(eq? (neg? '(+ 1 2)) #f)
(eq? (neg? '(2 2 2)) #f)
(display "End of neg? testing\n")
(define equality?
  (lambda (xs)
    (and (pair? xs)(eq? (car xs) '=))))
(display "Testing equality?\n")
(eq? (equality? '(= 1 2)) #t)
(eq? (equality? '(+ 1 2)) #f)
(eq? (equality? '(2 2 2)) #f)
(display "End of equality tetsting\n")

(define make-diff
  (lambda (xs ys)
    (list '- xs ys)))
(display "Testing make-diff\n")
(equal? (make-diff 2 3) '(- 2 3))
(display "End of make-diff testing\n")
(define make-prod
  (lambda (xs ys)
    (list '* xs ys)))
(display "Testing make-prod\n")
(equal? (make-prod 2 3) '(* 2 3))
(display "End of make-prod\n")
(define make-quo
  (lambda (xs ys)
    (list '/ xs ys)))
(display "Testing make-quo\n")
(equal? (make-quo 2 3) '(/ 2 3))
(display " End of make-quo\n")
(define make-sum
  (lambda (xs ys)
    (list '+ xs ys)))
(display "Testing make-sum testing\n")
(equal? (make-sum 2 3) '(+ 2 3))
(display "End of make-sum\n")
(define make-neg
  (lambda (xs)
    (list 'neg xs)))
(display "Testing make-neg\n")
(equal? (make-neg 2) '(neg 2))
(display "End of make-neg testing\n")
(define make-equality
  (lambda (xs ys)
    (list '= xs ys)))
(display "Tesing make-equality\n")
(equal? (make-equality 2 3) '(= 2 3))
(display "End of make-equality testing\n")
(define neg-exp cadr)



(define make-letstar
  (lambda (xs ys)
    (cond
      [(null? xs) ys]
      [(make-let (caar xs) (cadr (car xs))(make-letstar (cdr xs) ys))])))

(define make-seq
  (lambda (xs)
    (cond
      [(null? xs) '()]
      [(cond
               [(null? (cdr xs)) (car xs)]
               [(make-let '*temp* (car xs) (make-seq (cdr xs)))])])))
(display "Testing make-seq\n")
(equal? (make-seq '(+)) '+)
(equal? (make-seq '(a b)) '(let *temp* a b))

(display "End of make-seq testing\n")
(display "Tesing make-letstar\n")
(equal? (make-letstar '() '(+ x 1)) '(+ x 1))
(equal? (make-letstar '((x 1) (y 2)) '(+ 1 2)) '(let x 1 (let y 2 (+ 1 2))))
(display "End of make-letstr testing\n")
(define make-curried-proc
  (lambda (xs ys)
    (cond
      [(null? xs) (make-proc '*temp* ys)]
      [(cond
         [(null? (cdr xs)) (make-proc (car xs) ys)]
         [(make-proc (car xs) (make-curried-proc (cdr xs) ys))])])))
(display "Testing make-curried-proc\n")
(equal? (make-curried-proc '() '(a b)) (make-proc '*temp* '(a b)))
(equal? (make-curried-proc '(a) '(b c)) (make-proc 'a '(b c)))
(display "End of make-curried-proc\n")
(define make-curried-funcall
  (lambda (xs ys)
    (cond
      [(null? ys) (make-funcall xs 0)]
      [(null? (cdr ys)) (make-funcall xs (car ys))]
      [(make-curried-funcall (make-funcall xs (car ys)) (cdr ys))]))) 

(display "Testing make-curried-funcall\n")
(equal? (make-curried-funcall 'x '()) (make-funcall 'x 0))

(equal? (make-curried-funcall 'x '(a b)) (make-curried-funcall (make-funcall 'x 'a) '(b)))
(display "End of make-curried-funcall\n")
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; parse-lang: (() -> token) -> SmallLangExp
(define parse-lang
  (parser
   (start exp)
   (end EOF)
   (tokens value-tokens op-tokens)
   (error (lambda (a b c) (error 'parse-lang "error occurred, ~v ~v ~v" a b c)))
   (grammar
    (exp ((BEGIN exps END) (make-seq $2))
         ((LET let-defs IN exp) (make-letstar $2 $4))
         ((PROCS rec-defs IN exp) (make-procs $2 $4))
         ((IF exp THEN exp ELSE exp) (make-if $2 $4 $6))
         ((BSLASH formals BSLASH ARROW  exp ) (make-curried-proc $2 $5))
         ((ID EQ1 exp) (make-assign $1 $3))
         ((comp-exp) $1))
    (exps ((exp) (list $1))
          ((exp SEMI exps) (cons $1 $3)))
    (let-def ((ID EQ1 exp) (list $1 $3)))
    (let-defs ((let-def) (list $1))
              ((let-def COMMA let-defs) (cons $1 $3)))
    (rec-def ((ID OP formals CP EQ1 exp) (list $1 (make-curried-proc $3 $6))))
    (rec-defs ((rec-def) (list $1))
              ((rec-def COMMA rec-defs) (cons $1 $3)))
    (comp-exp ((math-exp EQ2 math-exp) (make-equality $1 $3))
              ((math-exp) $1))
    (math-exp ((math-exp + term) (make-sum $1 $3))
              ((math-exp - term) (make-diff $1 $3))
              ((term) $1))
    (term ((term * factor) (make-prod $1 $3))
          ((term / factor) (make-quo $1 $3))
          ((factor) $1))
    (factor ((simple) $1)
            ((NUM) $1)
            ((- factor) (make-neg $2))
            ((simple OP actuals CP) (make-curried-funcall $1 $3)))
    (simple ((ID) $1)
            ((OP exp CP) $2))
    (actuals (() null)
             ((actualsNE) (reverse $1)))
    (actualsNE ((exp) (list $1))
               ((actualsNE COMMA exp) (cons $3 $1)))
    (formals (() null)
             ((formalsNE) (reverse $1)))
    (formalsNE ((ID) (list $1))
               ((formalsNE COMMA ID) (cons $3 $1))))))


; lexer/parser test
(let* ((example "2+3+4")
       (i (open-input-string example)))
  (equal? (parse-lang (lambda () (get-token i)))
          '(+ (+ 2 3) 4)))


;; An environment (Env) is a Scheme function from Identifier to Loc.
;; where a Loc is a NatNum

(define empty-env
  (lambda (var) (error 'empty-env "variable undefined")))

; apply-env: Env * Identifier -> Loc
(define (apply-env env var) (env var))

; extend-env: Env * Identifier * Loc -> Env
(define (extend-env env var val)
  (lambda (var2)
    (if (eq? var var2)
        val
        (env var2))))

;; A store (Store) is a list of Loc * Val
;; where a Loc is a NatNum

(define empty-store '())

; extend-store: Store * NatNum * Val -> Store
(define (extend-store store loc v) (cons (cons loc v) store))

; apply-store: Store * NatNum -> Val
(define (apply-store store loc)
  (if (null? store)
      (error 'apply-store "location undefined")
      (if (= (car (car store)) loc)
          (cdr (car store))
          (apply-store (cdr store) loc))))

;; continuations

(define init-k (lambda (v) v))

;; denotation

(define (boolify num) (not (eqv? num 0)))


; meaning: Exp * Env * (Val * Store -> A) * Store -> A
(define (meaning exp env k)
  (cond ((number? exp) (k exp))
        ((ident? exp)((apply-env env exp) k))
        ((sum? exp)
         (meaning (arg1 exp)
                  env
                  (lambda (v1)
                    (meaning (arg2 exp)
                             env
                             (lambda (v2)
                               (k (+ v1 v2)))))))
        ((diff? exp)
           (meaning (arg1 exp)
                  env
                  (lambda (v1)
                    (meaning (arg2 exp)
                             env
                             (lambda (v2)
                               (k (- v1 v2)))))))
                            
        ((prod? exp)
           (meaning (arg1 exp)
                  env
                  (lambda (v1)
                    (meaning (arg2 exp)
                             env
                             (lambda (v2)
                               (k (* v1 v2) ))
                             ))
                  ))
       ((quo? exp)
         (meaning (arg1 exp)
                  env
                  (lambda (v1 )
                    (meaning (arg2 exp)
                             env
                             (lambda (v2)
                               (k (/ v1 v2)))
                             ))
                  ))
        ((neg? exp)
          (meaning (arg1 exp)
                  env
                  (lambda (v1 )
                    (k (* -1 v1) ))
                  ))
        ((equality? exp)
         (meaning (arg1 exp)
                  env
                  (lambda (v1 )
                    (meaning (arg2 exp)
                             env
                             (lambda (v2)
                               (k (eq? v1 v2) ))
                             ))
                  ))
        ((let? exp)
         (meaning (let-body exp)
                  (extend-env env (let-var exp)
                              (lambda (k2)
                                (meaning (let-exp exp)
                                         env k2))) k))
        ((if? exp)
         (meaning (if-exp1 exp)
                  env
                  (lambda (v)
                    (if (boolify v)
                        (meaning (if-exp2 exp) env k)
                        (meaning (if-exp3 exp) env k)))
                  ))
        ((proc? exp)
         (k (lambda (v k2)
              (meaning (proc-exp exp)
                       (extend-env env (proc-var exp) v) k2))))
        ((funcall? exp)
         (meaning (funcall-rator exp)
                  env
                  (lambda (f)
                    (f (lambda (k2)
                         (meaning (funcall-rand exp) env k2)) k))))
       ; ((assign? exp)
        ; (meaning (assign-exp exp)
         ;         env
          ;        (lambda (v store2)
           ;         (k v (extend-store store2 (apply-env env (assign-var exp)) v)))
            ;      store))
      (else (error 'meaning "Unknown expression"))))

; lexer/parser/meaning test
(let* ((example "let x = 1/0 in 3")
       (i (open-input-string example)))
    (= (meaning (parse-lang (lambda () (get-token i))) empty-env init-k) 3))



(let* ((example "let x = z in 3")
       (i (open-input-string example)))
    (= (meaning (parse-lang (lambda () (get-token i))) empty-env init-k) 3))



(let* ((example "(\\x,y\\->x)(1,1/0)")
       (i (open-input-string example)))
    (= (meaning (parse-lang (lambda () (get-token i))) empty-env init-k) 1))



(let* ((example "(\\x,y\\->x)(1,z)")
       (i (open-input-string example)))
    (= (meaning (parse-lang (lambda () (get-token i))) empty-env init-k) 1))

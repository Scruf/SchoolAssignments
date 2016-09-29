;; Name: 
;; Time spent on assignment: 
;; Collaborators: 


;; (list? x)
;; returns #t if x is a proper list 
;; and returns #f otherwise.
(define list? (x)
  (if (null? x)
      #t
      (if (pair? x)
          (list? (cdr x))
          #f)))

;; (prefix? xs ys)
;; returns #t if xs is a prefix of ys (using equal? to compare elements)
;; and returns #f otherwise. 
(define prefix? (xs ys)
  (if (null? xs)
      #t
      (if (null? ys)
          #f
          (and (equal? (car xs) (car ys))
               (prefix? (cdr xs) (cdr ys))))))


(define even? (n) (= 0 (mod n 2)))
(val odd? (o not even?))
(val positive? ((curry <) 0))
(val zero? ((curry =) 0))
(val negative? ((curry >) 0))
(define flip (f) (lambda (b a) (f a b)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part A (Exercise 2)

;; A.a (Exercise 2a)
(define count (x xs)
  (if (null? xs)
    0
    (if (= x (car xs))
            (+ 1 (count x (cdr xs)))
            (+ 0 (count x (cdr xs)))
      )
    )
)

;; A.b (Exercise 2b)
(define countall (x xs)
  (if (null? xs)
    0
    (if (pair? (car xs))
      (+ (countall x (car xs)) (countall x (cdr xs)))
      (if (equal? x (car xs))
        (+ 1 (countall x (cdr xs)))
        (countall x (cdr xs)))
    )
  )
)


;; A.c (Exercise 2c)
(define mirror(xs)
  (if (null? xs)
    xs
    (if (pair? xs)
      (append (mirror (cdr xs))(list1 (mirror(car xs))))
      xs
    )
  )
)

;; A.d (Exercise 2d)
(define flatten(xs)
  (if (null? xs)
    xs
    (if (atom? xs)
      (list1 xs)
      (append(flatten (car xs))(flatten (cdr xs))))))

;; A.e (Exercise 2e)
(define sublist? (xs ys)
  (if (null? xs)
    #t
    (if (null? ys)
      #f
      (if (equal? (car xs)(car ys))
        (sublist? (cdr xs)(cdr ys))
        (sublist? xs (cdr ys))
      )
    )
  )
)

;; A.f (Exercise 2f)
(define subseq? (xs ys)
  (if (null? xs)
    #t
    (if (null? ys)
      #f
      (if (equal? (car xs)(car ys))
        (subseq? (cdr xs) (cdr ys))
        (subseq? xs (cdr ys))
      )
    )
  )
)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part B

;; B.1 (take)
(define take (n xs)
  (if (null? xs)
    xs
    (if (equal? n 0)
      '()
      (cons (car xs)(take(- n 1)(cdr xs))
    )
)))

;; B.2 (drop)
(define drop (n xs)
  (if (null? xs)
    xs
    (if (equal? n 0)
      xs
    (drop(- n 1)(cdr xs))))
)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part C (interleave)

(define interleave( xs ys)
  (if (null? xs)
    (if (null? ys)
      ys
      (interleave ys xs))
    (cons(car xs)(interleave ys(cdr xs)))
  )

)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part D (permutation?) !bonus!

;; DEFINE permutation? HERE


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part E (Exercise 10)

;; E.a (Exercise 10a)
(define takewhile (p? xs)
  (if (null? xs)
     '()
      (if (p? (car xs))
           (cons (car xs) (takewhile p? (cdr xs)))
          '()
      )
   )
)

;; E.b (Exercise 10b)
(define dropwhile(p? xs)
  (if (null? xs)
    '()
      (if (p? (car xs))
        (dropwhile p? (cdr xs))
        xs
      )
  )
)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part F (arg-max)

;; DEFINE arg-max HERE


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part G (Exercise 14)

;; G.b (Exercise 14b)
(define max*(xs)
  (foldr max
    (car xs)
    xs
  )
)

;; G.c (Exercise 14c)
(define gcd* (xs)
  (foldr gcd
         0
         xs)
)

;; G.g (Exercise 14h)
(define append-via-fold(xs ys)
  (foldr cons ys xs)
)

;; G.i (Exercise 14j)
(define reverse-via-fold (xs)
  (foldl cons '() xs))

;; G.j (Exercise 14k)

(define insert (x xs)
  (if (null? xs)
      (list1 x)
      (if (< x (car xs))
          (cons x xs)
          (cons (car xs) (insert x (cdr xs))))))

(define insertion-sort (xs)
  (foldr (letrec 
             ((insert (lambda (next rest)
                        (if (null? rest)
                            (list1 next)
                            (if (< next (car rest))
                                (cons next rest)
                                (cons (car rest) (insert next (cdr rest))))))))
           insert)     
         '()
         xs))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part H (Exercise 19)

(val emptyset (lambda (x) #f))
(define member? (x s) (s x))

;; H.1 (Exercise 19c)
(define add-element(x s)
  (lambda (y) (or (s y)(= y x)))
)

;; H.2 (Exercise 19c)
(define union (s1 s2)
  (lambda (x)(or (s1 x)(s2 x)))
)

;; H.3 (Exercise 19c)
(define inter(s1 s2)
  (lambda(y) (and (s1 y)(s2 y)))
)

;; H.4 (Exercise 19c)
(define diff(s1 s2)
  (lambda (y)(and (s1 y) (not s2 y)))
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Part I

;; DEFINE clamp HERE

#lang scheme
(define union
  (lambda (set1 set2)
    (cond
      [(null? set1) set2]
      [(null? set2) set1]
      [else append set1 set2])))




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

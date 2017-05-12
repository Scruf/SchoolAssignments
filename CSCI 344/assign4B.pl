%Egor Kozitski
judgeType(Gamma, N, int) :- number(N).

judgeType(Gamma, X, T) :- atom(X), lookUp(X, Gamma, T).

judgeType(Gamma, if(M, N1, N2), T) :-
        judgeType(Gamma, M, bool),
        judgeType(Gamma, N1, T),
        judgeType(Gamma, N2, T).

judgeType(Gamma, let(X, M, N), T) :-
        judgeType(Gamma, M, T1),
        extend(Gamma, X, T1, Gamma2),
        judgeType(Gamma2, N, T).

judgeType(Gamma, letRec(Y, X, TX, TM, M, N), T) :-
        extend(Gamma, X, TX, Gamma2),
        extend(Gamma2, Y, arrow(TX, TM), Gamma3),
        judgeType(Gamma3, M, TM),
        extend(Gamma, Y, arrow(TX, TM), Gamma4),
        judgeType(Gamma4, N, T).

judgeType(Gamma, proc(X, T, M), arrow(T, TM)) :-
        extend(Gamma, X, T, Gamma2),
        judgeType(Gamma2, M, TM).

judgeType(Gamma, funcall(M, N), T) :-
        judgeType(Gamma, N, TN),
        judgeType(Gamma, M, arrow(TN, T)).

judgeType(Gamma, nil(T), list(T)).

judgeType(Gamma, cons(M, N), list(T)) :-
    judgeType(Gamma, N, T),
    judgeType(Gamma, M, list(T)).
                               
judgeType(Gamma, isNil(M), bool) :-
    judgeType(Gamma, M, list(T)). 
                      
judgeType(Gamma, head(M), list(T)) :-
    judgeType(Gamma, M, T).
    
judgeType(Gamma, tail(M), list(T)) :-
    judgeType(Gamma, M, list(T)).


lookUp(X, [[X, Type] | Tail], Type) :- !.
lookUp(X, [[Y, T] | Tail], Type) :- 
    lookUp(X, Tail, Type).

extend(Gamma, X, T, [[X, T] | Gamma]).

initGamma([
    [eq, arrow(int, arrow(int, bool))],
    [prod, arrow(int, arrow(int, int))],
    [sum, arrow(int, arrow(int, int))],
    [diff, arrow(int, arrow(int, int))]
          ]).

/*
 * Derivative
 *
 */
d(X, X, 1).
d(C, X, 0) :- atomic(C), C \= X.
d(U + V, X, DU + DV) :- d(U, X, DU), d(V, X, DV).
d(U * V, X, U * DV + V * DU) :- d(U, X, DU), d(V, X, DV).
partition([],P,Less,Same,Greater):- Less = [], Same = [], Greater = [].
partition([H|T],P,Less1,Same1,Greater1):-
	H=P,
	partition(T,P,Less2,Same2,Greater2),
	Less1 = Less2,
	Same1 = [H |Same2],
	Greater1 = Greater2.
partition([H|T], P, Less1, Same1, Greater1):-
	H<P,
	partition(T,P,Less2,Same2,Greater2),
	Less1 = [H |Less2],
	Same1 = Same2,
	Greater1 = Greater2.
partition([H|T],P,Less1,Same1,Greater1):-
	H>P,
	partition(T,P,Less2,Same2,Greater2),
	Less1 = Less2,
	Same1 = Same2,
	Greater1 = [H | Greater2].


%quicksort.pl quicksort
quicksort([], []).
quicksort([H | T], S) :-
   partition(T, H, Less, Same, Greater),
   quicksort(Less, SLess),
   quicksort(Greater, SGreater),
   append(SLess, [H | Same], SGreater, S).

append([], L, L).
append([H | T], L, [H | A]) :- append(T, L, A).

append(L1, L2, L3, A) :- append(L2, L3, L23), append(L1, L23, A).                   


merge([], XS, XS).
merge(XS, [], XS).
merge([H1|T1], [H2|T2], [H1|T3]) :- H1 =< H2, merge(T1, [H2|T2], T3).
merge([H1|T1], [H2|T2], [H2|T3]) :- H1 > H2, merge([H1|T1],T2,T3).


split_list([],[],[]).
split_list([A],[A],[]).
split_list([A,B|R],[A|Ra],[B|Rb]) :- split_list(R,Ra,Rb).




merge_sort([],[]).
merge_sort([A],[A]).
merge_sort([A,B|R],S) :-
    split_list([A,B|R],L1,L2),
    merge_sort(L1,S1),
    merge_sort(L2,S2),
    merge(S1,S2,S).


simp(X,X) :- atomic(X), !.
simp(X+0, Y) :- simp(X,Y).
simp(0+X,Y) :- simp(X,Y).

simp(A+B,D):-
	number(A),
	number(B),
	number(A,B,D).

simp(X*0,0).
simp(0*X,0).
simp(X*1,X).
simp(1*X,X).
simp(A*B,D):-
	number(A),
	number(B),
	sump_prod(A,B,D).

simp(A*X+B*X,D):-
	number(A),
	number(B),
	simp_sum(A,B,Z),
	simp(Z*X, D).

simp(X,X).

simp_sum(E1,E2,E3):-
	E3 is E1+E2.

simp_prod(E1,E2,E3):-
	E3 is E1*E2.

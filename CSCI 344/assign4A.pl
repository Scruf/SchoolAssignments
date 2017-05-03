%Egor Kozitski

%1
male(tony).
male(ethan).
male(chris).
male(ben).
male(nathan).

female(sim).
female(helen).
female(sophie).
female(olivia).

parents(helen, sim, tony).
parents(chris, sim, tony).
parents(ben, sim, tony).
parents(sophie, helen, ethan).
parents(olivia, helen, ethan).
parents(nathan, helen, ethan).

syster_of(X,Y) :- X\=Y,
	female(X), parents(X,XS,YS), parents(Y,XS,YS).

%2
second([H|[E|T]], E).

%3
one([H]).

%4

insert(H,[],[H]).
insert(H,[HA|TA],[HA|S]) :- H > HA, insert(H, TA,S).
insert(H,[HA|TA],[H| [HA|TA]]) :-H =< HA.

insertion_sort([],[]).
insertion_sort([H],[H]).
insertion_sort([H|T],E):-insertion_sort(T,A),insert(H,A,E).

%5
index(X,[X|T],0).
index(X,[H|T],N):-index(X,T,XS),N is XS+1.

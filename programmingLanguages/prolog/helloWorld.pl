father(john, nick).
father(lloyd, john).
mother(jean, nick).
mother(mary, john).

parent(X,Y) :- mother(X,Y) ; father(X, Y).

ancestor(X,Y) :- parent(X,Y).
ancestor(X,Y) :- parent(X0, Y), ancestor(X, Y0).

member(X,[X|_]).
member(X,[_|R]) :- member(X,R).

len([],0).
len([_|Rest], C) :-
    len(Rest, C0),
    C is C0 + 1.

fact(0,1).
fact(N, F) :-
    N0 is N - 1,
    fact(N0, F0),
    F is N * F0.


connected(X, Y) :-
    edge(X, Y); edge(Y, X).

path(X, Y, Path) :-
    path(X, Y, [X, Y], P1),
    Path = [X|P1].

path(X, Y, _, Path) :-
    connected(X, Y),
    Path = [Y].

path(X, Y, Visited, Path) :-
    connected(X, Z),
    not(member(Z, Visited)),
    path(Z, Y, [Z|Visited], P1),
    Path = [Z|P1].
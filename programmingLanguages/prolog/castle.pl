% Helper rules
member(X,[X|_]).
member(X,[_|R]) :- member(X,R).

subset([], _).
subset([X | Rest], List) :- member(X, List), subset(Rest, List).

% Find paths from any two rooms
connected(Castle, X, Y) :-
    room(Castle, X, Y, _); room(Castle, Y, X, _).

path(Castle, X, Y, Path) :-
    path(Castle, X, Y, [X, Y], P1),
    Path = [X | P1].

path(Castle, X, Y, _, Path) :-
    connected(Castle, X, Y),
    Path = [Y].

path(Castle, X, Y, Visited, Path) :-
    connected(Castle, X, Z),
    not(member(Z, Visited)),
    path(Castle, Z, Y, [Z | Visited], P1),
    Path = [Z | P1].


% Find total cost of a given path
pathCost(_, [_], 0).
pathCost(Castle, [First, Second | Rest], TotalCost) :-
    room(Castle, First, Second, Cost),
    pathCost(Castle, [Second | Rest], SubCost),
    TotalCost is SubCost + Cost.

% Print all elements in a list
printList([]).
printList([H]) :- write(H), write('\n').
printList([H | T]) :- write(H), write('\n'), printList(T).

% Check if all rooms in RoomList are on any paths from enter to exit
solveRooms(Castle, RoomList) :-
  path(Castle, enter, exit, Path),
  subset(RoomList, Path),
  printList(Path).

% Check if any paths from enter to exit have a cost less than Limit
solveRoomsWithinCost(Castle, Limit) :-
  path(Castle, enter, exit, Path),
  pathCost(Castle, Path, Cost),
  Cost =< Limit,
  write('Cost is '), write(Cost), write(' within limit of '), write(Limit), write('\n'),
  printList(Path).

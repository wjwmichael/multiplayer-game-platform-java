


## Intro
This is an online game server in Java where multiple users can join a game room and play game simultaneously. Each player’s state is synchronized by RPC Service. It also supports group chat and an example game is developed to showcase. Multiple design patterns are implemented to achieve better abstraction and cleaner code.


## Showcase:

Start the game server:

![alt text][screenshot1]

Open a default game room:

![alt text][screenshot3]

Gameplaying Showcase:

![alt text][screenshot2]


## Game Description

This game is called Minesweeper. It could be played by multiple players. The users in the
same chatroom is in one team. Mines are placed ramdonly on Rice Campus. The target of a
team is to reveal as many mines as possible. Users can choose a location and then a certain
distance closer to this point which forms up a round region will be revealed. The users hope
that this region contains as many mines as possible. Then, this team will also receive a piece
of information which indicates how many mines there are in neighborhood region which
haven’t been revealed, which provides a hint for next step. The game will last for 2 rounds
or when the mines are all revealed.

Teamwork is important for this game. You should use the information provided by the
action of your teammates to help you choose a location which more possibly contains many
mines.

## Design Principle

In our design, the game server can be split into three MVC model: chatApp module,
gameTeam module and gamePlay module. The app module is mainly responsible for
connecting players,join a player team,etc. The gameTeam module is used for team-wise
communication. The game module will contains the neccessary part to start a game.

## Work Flow

First, when you start the client, you are going to find someone as your team members, you
may accomplish this through invite other people to your chatroom or through join an
existed chatroom

When you have gathered enough team members, You may send a invitation message to the
server . The server will send back a start game message to all users in the chatrroom. This
message is an unknown type to clients.

When a client receives this unknown data type, it will ask the server to send it the correct
command and add it to its local visitor.

The server receives CommandRequest,then it will send back a command, which contains a
factory class which may start a game locally.

Clients will receive the command from server, get the game factory and able to start game.


[screenshot1]: https://github.com/wjwmichael/multiplayer-game-platform-java/blob/master/JavaCommunicationPlatform/screenshots/connectingserver.png?raw=true
[screenshot2]:https://github.com/wjwmichael/multiplayer-game-platform-java/blob/master/JavaCommunicationPlatform/screenshots/gameplaying.png?raw=true
[screenshot3]:https://github.com/wjwmichael/multiplayer-game-platform-java/blob/master/JavaCommunicationPlatform/screenshots/gameroom.png?raw=true








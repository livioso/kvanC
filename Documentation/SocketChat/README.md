### SocketChat Documentation

##### Author: Livio Bieri, March 2015

#### User Interface
![UI](assets/Screenshot-I.png)

#### Overview

![Overview](assets/Communication.pdf)

**Description:**

The overview shows the situation after the client connected with the server. The server created an instance of **ConnectionHandler** after the client connected to the port `1235` which got detected by **ConnectionListener** (more accurately `accept()`). Then the ConnectionHandler thread started listening on a `InputStream` on this particular client socket. Waiting for client messages and dispatching when received (by `readline()` using `dispatchMessage()`) the thread lives as long as the client remains connected. `dispatchMessage()` then tries to parse the received String as JSON and looks up the key `action` which indicates what kind of action is expected to happen (example: `remove_participant`). From there the actual dispatch can be started and depending on the `action` the server performs the requested task. As soon as the request has been processed (e.g. updated the **ChatRoom** singleton held by the server) all clients get a notification about what changed. The format of this message is JSON as well. Each client is then expected to dispatch this information and update its user interface (this is done by **ClientMessageReceiver** (see `dispatchMessage()`).

#### Client
The client can send the following request to the server:

-  Add New User: `{"action":"new_user","name":"mary"}`
-  Remove User: `{"action":"remove_user","name":"mary"}`
-  Add Topic: `{"action":"add_topic","topic":"whatever"}`
-  Remove Topic: `{"action":"remove_topic","topic":"whatever"}`
-  Add Message: `{"action":"add_message","message":"hi","topic":"whatever"}`
-  Get Latest Messages: `{"action":"get_latest_messages"}`
-  Get All Topics: `{"action":"get_all_topics"}`
-  Get All Participants: `{"action":"get_all_participants"}`

#### Server Messages
The server can send the following messages to the client (this are replies to client request calls).

##### Notification to all Clients (Broadcast):
-  New User Added: `{"action":"new_user","name":"mary"}`
-  Removed User: `{"action":"remove_user","name":"mary"}`
-  Added Topic: `{"action":"add_topic","topic":"whatever"}`
-  Removed Topic: `{"action":"remove_topic","topic":"whatever"}`
-  Added Message: `{"action":"add_message","message":"hi","topic":"whatever"}`

##### Notification to single Client:
-  Response Latest Messages: `{"action":"response_latest_messages","messages":"message2;message1"}`
-  Response All Topics: `{"action":"response_all_topics","topics":"topic1;topic2"}`
-  Response All Participants: `{"action":"response_all_participants","participants":"alice;bob"}`

# Personal Notes kvanC
**Author** Livio Bieri **Date** April 2015

## Threading

### Process

A process is an executing program that:

- is memory allocated by OS
- usually does not share memory with other processes

### Thread

- runs in the address space of a process
- has its own program counter
- has its own stack frame
- shares code and data with other threads

### Scheduling

- Per processor, only one thread is running at any given time
- **Scheduling:** *allocation of CPU time to threads*
- JVM does not mandate a threading models

#### Cooperative Scheduling
- *pseudo parallelism*
- thread decide when they ‘can’ give up time to other threads
- **Example:** `yield(); sleep(1000);`

**Note:** `yield()` *is essentially used to notify the system that the current thread is willing to give up the CPU for a while. The [thread scheduler](http://www.javamex.com/tutorials/threads/thread_scheduling.shtml) will select a different thread to run instead of the current one.*

#### Pre-emptive Scheduling
- *quasi parallelism*
- OS interrupts threads at any time (time sliced)
- no thread can unfairly hog the processor
- **Approaches**
	- First In First Out (FIFO)
	- [Earliest deadline first scheduling (EDF)](https://en.wikipedia.org/wiki/Earliest_deadline_first_scheduling)
	- [Shortest remaining time (sim. Shortest Job First, SJF)](https://en.wikipedia.org/wiki/Shortest_remaining_time)
  	- [Fixed priority pre-emptive scheduling (FPPS)](https://en.wikipedia.org/wiki/Fixed_priority_pre-emptive_scheduling)
  	- [Round-robin scheduling (RR)](https://en.wikipedia.org/wiki/Round-robin_scheduling)

#### Java `Thread` Class

##### `join()`

- It makes sure that as soon as a thread calls `join()` the **current thread** will not execute till the one on which `join()` has been called will finish


``` java

// ...

threadOne.join(); // waits until terminated
threadTwo.join(); // same here

// here both threads are finished 
```

##### `setDaemon(bool)`

- daemon theads run in background
- if only deamon threads are active, the process stops

### Thread Safety

- A class is thread safe when it behaves always in the same manner (i.e. correctly) when accessed from multiple threads, regardless of the scheduling or interleaving of the execution of these threads by the runtime environment and with no additional synchronisation on the part of the calling code.

- Stateless objects (immutable classes) do not have fields and they do not reference fields from other classes therefore they are *always* thread safe.

#### Compound Operations

We always have thread unsafe applications when we use compounds operations  
*(because they are not [atomic operations](https://en.wikipedia.org/wiki/Linearizability))* such as:

- Check-Then-Act (CTA)
- [Read-Modify-Write (RMW)](https://en.wikipedia.org/wiki/Read-modify-write)


### Memory Model

#### `volatile`
- Essentially, volatile is used to indicate that a variable's value will be modified by different threads
- The value of this variable will never be cached thread-locally: all reads and writes will go straight to main memory
- Access to the variable acts as though it is enclosed in a synchronized block, synchronized on itself
- Equivalent to wrapping all operations as synchronized blocks sharing the same lock object.
- Reads and writes which *happen before* *(partial ordering)* a volatile access are visible by other threads accessing the same volatile field.
- See also [double check locking](https://en.wikipedia.org/wiki/Double-checked_locking).

## Networking

### Sockets

#### Datagram Sockets
- Use **User Datagram Protocol (UDP)** as protocol.
- Not connection oriented, not reliable.

#### Stream Sockets

- Use **Transmission Control Protocol (TCP)** as end to end protocol.
- Provide a [reliable](https://en.wikipedia.org/wiki/Transmission_Control_Protocol#Reliable_transmission) **byte-stream**.
- **Connection oriented:** Socket represents one end of a TCP connection.

##### TCP

- Packages are delivered in right order.
- Lost packages are retransmitted.
- Connection is **full duplex**.
- **3-Way-Handshake:** *SYN, SYN_ACK, ACK*

###### DDoS TCP

- Keep sending SYN without acknowledging them (ACK).
- Server gets unresponsive due to many half open connections.

*This attack can be prevented by:*

- Filtering
- Reducing the SYN_RECIEVED timer
- Firewall, Proxies

##### `java.net.Socket`

- `Socket(String host, int port, InetAdress localAddr, int localport)`
- `TCP_NODELAY`: `setTcpNoDelay()` Sends the packets as quickly as possible, normally small packets are combined with larger packets before sent.

##### `java.net.ServerSocket`

- `ServerSocket(int port)`
- `.accept()` blocks until a connection is requested.
- `￼￼.setSoTimeout(int ms)` timeout for accept.

### Hypertext Transfer Protocol (HTTP)

- **Stateless** protocol.
- Request: `GET /web/index.html HTTP/1.1` `<header>`
- Response: `HTTP/1.0 200 OK` `<message>`
- `HEAD` same as `GET` without body (getting metadata). 

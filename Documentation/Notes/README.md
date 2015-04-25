## Personal notes for kvanC
**Author** Livio Bieri **Date** April 2015


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
	- `yield()` *is essentially used to notify the system that the current thread is willing to give up the CPU for a while. The [thread scheduler](http://www.javamex.com/tutorials/threads/thread_scheduling.shtml) will select a different thread to run instead of the current one.*

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

>A class is thread safe it it behaves always in the same manner (i.e. correctly) when accessed from multiple threads, regardless of the scheduling or interleaving of the execution of these threads by the runtime environment and with no additional synchronisation on the part of the calling code.

- Stateless objects (immutable classes) do not have fields and they do not reference fields from other classes therefore they are *always* thread safe.
- We always have thread unsafe applications when we use compounds operations like (they are not [atomic operations](https://en.wikipedia.org/wiki/Linearizability)):
	- **Read-Modify-Write (RMW)**
	- **Check-Then-Act (CTA)**

### Memory Model

##### `volatile`
- Essentially, volatile is used to indicate that a variable's value will be modified by different threads
- The value of this variable will never be cached thread-locally: all reads and writes will go straight to main memory
- Access to the variable acts as though it is enclosed in a synchronized block, synchronized on itself

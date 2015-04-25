### Notes for kvanC

#### Scheduling

- Per processor, only one thread is running at any given time
- **Scheduling =** *allocation of CPU time to threads*


#### Threading Models

*__Note: JVM does not mandate a threading model.__*

##### Cooperative Threading

- *pseudo parallelism*
- thread decide when they ‘can’ give up time to other threads
- **Example:** `yield(); sleep(1000)`
- `yield()`: This static method is essentially used to notify the system that the current thread is willing to "give up the CPU" for a while. The [thread scheduler](http://www.javamex.com/tutorials/threads/thread_scheduling.shtml) will select a different thread to run instead of the current one.


##### Pre-emptive Threading

- *quasi parallelism*
- OS interrupts threads at any time (time sliced)
- no thread can unfairly hog the processor
- - [First In First Out (FIFO)](https://en.wikipedia.org/wiki/FIFO_(computing_and_electronics)




- [Earliest deadline first scheduling (EDF)](https://en.wikipedia.org/wiki/Earliest_deadline_first_scheduling)
- [Shortest remaining time (sim. Shortest Job First, SJF)](https://en.wikipedia.org/wiki/Shortest_remaining_time)
- [Fixed priority pre-emptive scheduling (FPPS)](https://en.wikipedia.org/wiki/Fixed_priority_pre-emptive_scheduling)
- [Round-robin scheduling (RR)](https://en.wikipedia.org/wiki/Round-robin_scheduling)




#### Process

A process is an executing program that:

- is memory allocated by OS
- usually does not share memory with other processes


#### Thread

- runs in the address space of a process
- has its own program counter
- has its own stack frame
- shares code and data with other threads


#### Thread class: `join()`

- It makes sure that as soon as a thread calls `join()` the **current thread** will not execute till the one on which `join()` has been called will finish


``` java

threadOne.join(); // waits until terminated
threadTwo.join(); // same here

// here both threads are finished 
```

#### 
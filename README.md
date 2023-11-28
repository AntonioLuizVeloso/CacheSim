## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

TEST CASE 1: SEQUENTIAL SEQUENCE ANALYSIS

Input: As n=number of cache blocks=32, the sequence contains 0-63, in
order, 4 times. That is to say, after each 63, the sequence goes back
to 0 to start over.

Cache Memory Trace: For the first 32 elements of the sequence, 0-31, 
the simulation will miss because each element is unique and the cache
blocks haven't been filled yet. These elements will fill the cache
blocks in order because it uses the Free Associative method of mapping.
Afterwards, since there are no more empty cache blocks, the next 32 
elements (32-63) will replace the last block because that is the most 
recently used cache block and the simulation uses the Most Recently
Used replacement algorithm. 

After the first 63, the following numbers 0-30 will be hits as they
are already within the cache blocks and as such will not require the 
replacement algorithm. However, once it gets to number 31, the cache
simulation will miss, and since the most recently use cache block is
30, 31 will replace 30, then 32 will replace 31, and so on and so
forth until 62. At the second 63, the simulation will hit since there
is already a 63 in the last cache block, thus leaving the last 2 cache
blocks with 62 and 63. 

As the sequence repeats 4 times, the process above will repeat another
2 times, leaving the last 4 cache blocks as 60, 61, 62, and 63, and
keeping the rest as 0-27. Presumably, if the sequence were to repeat
32 times, the cache blocks would have been populated by 32-63.

Outputs: 
Memory Access Count is 256 because that is the number of memory blocks
to access (2n x 4 = 64 x 4 = 256). 

Cache Hit Count is 96 because the first 0-63 had no hits, then the 
second had 31 consecutive hits + 1 hit for the last element, then the 
third had 30 consecutive hits + 2 hits for the last two elements, then
the fourth had 29 consecutive hits + 3 hits for the last three 
elements. 31 + 30 + 29 + 1 + 2 + 3 = 96. 

Cache Miss Count is the memory access count - cache miss count (256 - 
96 = 160).

Cache Hit Rate is cache hit count over memory access count (96 / 256 =
0.38).

Cache Miss Rate is cache miss count over memory access count (160 /
256 = 0.625, rounded to two decimal places, 0.63)

Average Memory Access Time uses the formula from the slides (hitRate*
cacheAccTime) + ((missRate)*missPenalty)

Total Memory Access Time uses the formula for load-through (# of hits 
* # of words per block * cache access time) + misses * (1ns for 
probing + (# of words per block * memory access time))

TEST CASE 2: RANDOM SEQUENCE

Input: The sequence was generated using a random number generator run
4n = 4(32) = 128 times, generating 128 random numbers.

Cache Memory Trace: As the inputs for test case 2 are randomly
generated every time the test case is run, the final snapshots will
almost always be different. However, generally, the simulation will
iterate through the cache blocks until there is a hit, at which point
the most recently used block will go to the block of the hit. After,
if there are still empty blocks, the simulation will seek to fill 
those up before running the replacement algorithm, at which point the
most recently used block becomes relevant, as after the final block is
filled, further misses will replace that block until a hit, at which
point further misses will replace the block that was hit and so on and
so forth.

Outputs: The outputs are different every time, though the cache hit
count and the cache miss count will always add up to the memory access
count, which is 128. 

TEST CASE 3: MID-REPEAT BLOCKS

Input: Given n=32, the sequence would repeat the middle sequence twice, 
which is 1-30, and continue until 2n. The sequence would then be repeated
4 times.

Cache Memory Trace: Due to the fact that MRU replacement algorithm is 
being used, after the first mid-sequence repeat (0-30, 1-31) the rest
of the elements would be assigned at the last cache block as that is 
the most recently used. This would go on for each repetition with 2 key
difference, the 1st is that the final assignments would go to the 
previous cache block each time. The 2nd difference is that from the 3rd
iteration onwards, the previous x cache blocks before "final" block 
where all the other values would be assigned, would have their values 
replaced due to the nature of MRU, with x being the number of iterations
above 3. 
ex:
at the 1st iteration the cache would look like this: 
[0 1 2 ..  25 26 27 28 29 30 63]

at the 2nd iteration it will be like this: 
[0 1 2 ... 25 26 27 28 29 62 63] 

at the 3rd iteration it will be:
[0 1 2 ... 25 26 27 29 61 62 63]
it can be seen that the element before the 
n-3 element has been replaced due to the fact that it can no longer fit 
the entire 30 elements from the mid-sequence repeat, hence it started 
replacing the using MRU.

at the 4th iteration it will be:
[0 1 2 ... 25 27 29 60 61 62 63]
2 elements before n-4 has been replaced.

Based on this pattern it can be assumed that the previous elements would
continue to be replaced until eventually the entire cache would be replaced
and filled with the upper half values of 2n

Outputs: The memory access count can be obtained by adding the number of 
mid-sequence block to 2n and multiplying it 4 times: 4*(64+30)=376. 
Counting the hit rate per iteration: 30, 62, 61, 60 and the miss rate per
iteration: 64, 32, 33, 34, it can be seen that aside from the first 
iteration, which is a different case since it sets the first assignments
as misses, the hit rate decreases while the miss rate increases as the 
iterations increase. This is in line with the pattern observed that as
the number of iterations increase the cache would eventually run out of
blocks that would be a hit. Adding these values would would generate 
a hit and miss rate of 0.57 (213/376) and 0.43 (163/376). With the generated
values, the Average and Total Memory Access Time can be computed by simply
implementing the formulas. 

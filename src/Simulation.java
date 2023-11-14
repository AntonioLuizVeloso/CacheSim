/* S12 Group 3: Full Associative Mapping + Most Recently Used
 * IBAOC, Christian Gabriel P.
 * MAGDAMO, Bien Rafael O.
 * VALERO, Nigel Kristoffer C.
 * VELOSO, Antonio Luiz G.
 */

 import java.util.Arrays;
 import java.util.Random;

public class Simulation {
    public static int cacheBlocks = 32;
    public static int cacheLine = 64;

    public static void main(String[] args) throws Exception {
        int[] memSeqSample = {1, 7, 5, 0, 2, 1, 5, 6, 5, 2, 2, 0};
        mapping(memSeqSample);

        testCases();
    }

    public static void testCases() {
        //Sequential Sequence
        System.out.println("\nTEST CASE 1");
        int[] seqSeq = new int[8*cacheBlocks];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2*cacheBlocks; j++) {
                seqSeq[(2*i*cacheBlocks) + j] = j;
            }
        }

        mapping(seqSeq);

        System.out.println("\nTEST CASE 2");
        //Random Sequence
        int[] ranSeq = new int[4*cacheBlocks];
        for (int i = 0; i < 4*cacheBlocks; i++) {
            Random rand = new Random();
            ranSeq[i] = rand.nextInt(4*cacheBlocks + 1);
        }

        mapping(ranSeq);

        System.out.println("\nTEST CASE 3");
        //Mid-Repeat Blocks
        int[] midSeq = new int[4*(2*cacheBlocks+(cacheBlocks-2))];
        int midSeqIndex = 0;
        for (int i = 0; i < 4; i++) {
            // First half of the sequence
            for (int j = 0; j < cacheBlocks-1; j++) {
                midSeq[midSeqIndex++] = j;
            }
            // Mid-repeat sequence
            for (int j = 1; j < cacheBlocks-1; j++) {
                midSeq[midSeqIndex++] = j;
            }
            // Second half of the sequence
            for (int j = cacheBlocks-1; j < 2 * cacheBlocks; j++) {
                midSeq[midSeqIndex++] = j;
            }
        }

        mapping(midSeq);
    }

    public static void mapping(int[] memSeq) {
        int[] cacheBlockArray = new int[cacheBlocks];
        Arrays.fill(cacheBlockArray, -1);

        int hit = 0;
        int miss = 0;
        int[][] hitMissTable = new int[memSeq.length][4];

        for (int i = 0; i < memSeq.length; i++)
            Arrays.fill(hitMissTable[i], -1);

        for (int i = 0; i < memSeq.length; i++) {
            hitMissTable[i][0] = memSeq[i];
            int blockIndex = -1;
            boolean inCache = false;

            for (int j = 0; j < cacheBlocks; j++) {
                if (cacheBlockArray[j] == memSeq[i]) {
                    inCache = true;
                    blockIndex = j;
                    hit += 1;
                    break;
                }
            }

            if (cacheBlockArray[i % cacheBlocks] == -1 && !inCache) {
                blockIndex = i % cacheBlocks;
                miss += 1;
            } else if (!inCache) { 
                blockIndex = hitMissTable[i-1][3];
                miss += 1;
            }

            hitMissTable[i][3] = blockIndex;

            if (inCache) hitMissTable[i][1] = memSeq[i];
            else {
                hitMissTable[i][2] = memSeq[i];
                cacheBlockArray[blockIndex] = memSeq[i];
            }

            System.out.println(Arrays.toString(hitMissTable[i]));
        }

        System.out.println("Output: ");
        System.out.println("1. Memory Access Count: ");
        System.out.println("2. Cache Hit Count: " + hit);
        System.out.println("3. Cache Miss Count: " + miss);
        System.out.println("4. Cache Hit Rate: " + (double)hit / memSeq.length);
        System.out.println("5. Cache Miss Rate: " + (double)miss / memSeq.length);
        System.out.println("6. Average Memory Access Time: ");
        System.out.println("7. Total Memory Access Time: ");
    }
}

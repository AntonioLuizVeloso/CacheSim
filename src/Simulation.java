/* S12 Group 3: Full Associative Mapping + Most Recently Used
 * IBAOC, Christian Gabriel P.
 * MAGDAMO, Bien Rafael O.
 * VALERO, Nigel Kristoffer C.
 * VELOSO, Antonio Luiz G.
 */

 import java.util.Arrays;
 import java.util.Random;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;

public class Simulation {
    public int cacheBlocks = 32;
    public int cacheLine = 64;
    public int[] cacheBlockArray = new int[cacheBlocks];

    private int mac;
    private int chc;
    private int cmc;
    private double chr;
    private double cmr;
    private double amat;
    private double tmat;
    private int blockIndex;

    public static void main(String[] args) throws Exception {      
        /*Simulation s = new Simulation();
        s.testCases();*/
    }

    public void testCases() {
        //Sequential Sequence
        System.out.println("\nTEST CASE 1");
        Integer[] seqSeq = new Integer[8*cacheBlocks];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2*cacheBlocks; j++) {
                seqSeq[(2*i*cacheBlocks) + j] = j;
            }
        }

        mapping(seqSeq,1);

        System.out.println("\nTEST CASE 2");
        //Random Sequence
        Integer[] ranSeq = new Integer[4*cacheBlocks];
        for (int i = 0; i < 4*cacheBlocks; i++) {
            Random rand = new Random();
            ranSeq[i] = rand.nextInt(4*cacheBlocks + 1);
        }

        mapping(ranSeq,1);

        System.out.println("\nTEST CASE 3");
        //Mid-Repeat Blocks
        Integer[] midSeq = new Integer[4*(2*cacheBlocks+(cacheBlocks-2))];
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

        mapping(midSeq,1);
    }

    public void mapping(Integer[] memSeq, int fileCheck) {
        //int[] cacheBlockArray = new int[cacheBlocks];
        Arrays.fill(cacheBlockArray, -1);
        int hit = 0;
        int miss = 0;
        int memAccCount = 0;
        int cacheAccTime = 1;
        int memAccTime = 10;
        int missPenalty = cacheAccTime+memAccTime; //load through miss penality
        double hitRate, missRate, avgMemAccTime, totalMemAccTime;

        int storeLastEmptyIndex = 0;

        int[][] hitMissTable = new int[memSeq.length][4];

        for (int i = 0; i < memSeq.length; i++)
            Arrays.fill(hitMissTable[i], -1);

        for (int i = 0; i < memSeq.length; i++) {
            hitMissTable[i][0] = memSeq[i];
            blockIndex = -1; //most recently used block index
            boolean inCache = false;

            for (int j = 0; j < cacheBlocks; j++) {
                if (cacheBlockArray[j] == memSeq[i]) {
                    inCache = true;
                    blockIndex = j;
                    hit += 1;
                    break;
                }
            }

            if (!inCache) {
                //if iterations haven't been completed yet, iterate through the blocks
                if (storeLastEmptyIndex < cacheBlocks) {
                    blockIndex = storeLastEmptyIndex;
                    miss += 1;
                } 
                
                //if all blocks are filled up take the index of most recently used
                else if (storeLastEmptyIndex >= cacheBlocks) {
                    blockIndex = hitMissTable[i-1][3];
                    miss += 1;
                }

                storeLastEmptyIndex += 1;
            }

            hitMissTable[i][3] = blockIndex;

            if (inCache) hitMissTable[i][1] = memSeq[i];
            else {
                hitMissTable[i][2] = memSeq[i];
                cacheBlockArray[blockIndex] = memSeq[i];
            }
        }
        /*
        //FINAL MEMORY SNAPSHOT
        System.out.println("[Block, Data]");
        for (int i = 0; i < cacheBlocks; i++) {
            System.out.print("[" + i + ", ");
            if (cacheBlockArray[i] == -1) System.out.print("N/A]\n");
            else System.out.print(cacheBlockArray[i] + "]\n");
        }
        */
        memAccCount = hit + miss;
        hitRate = (double)hit / memSeq.length;
        missRate = (double)miss / memSeq.length;
        avgMemAccTime = (hitRate*cacheAccTime) + ((missRate)*missPenalty);
        totalMemAccTime = (hit*cacheLine*cacheAccTime) + miss*(1+(cacheLine*memAccTime));

        /*

        System.out.println("Output: ");
        System.out.println("1. Memory Access Count: " + memAccCount);
        System.out.println("2. Cache Hit Count: " + hit);
        System.out.println("3. Cache Miss Count: " + miss);
        System.out.println("4. Cache Hit Rate: " + String.format("%.3f", hitRate));
        System.out.println("5. Cache Miss Rate: " + String.format("%.3f", missRate));
        System.out.println("6. Average Memory Access Time: " + String.format("%.3f", avgMemAccTime) + " T");
        System.out.println("7. Total Memory Access Time: " + totalMemAccTime + " T");
        */
        
        if(fileCheck==1)
        {
            createFile(memAccCount, hitMissTable);
        }
        setOutputs(memAccTime, hit, miss, hitRate, missRate, avgMemAccTime, totalMemAccTime);
    }

    private void setOutputs(int mac,int chc,int cmc, double chr, double cmr, double amat, double tmat)
    {
        this.mac=mac;
        this.chc=chc;
        this.cmc=cmc;
        this.chr=chr;
        this.cmr=cmr;
        this.amat=amat;
        this.tmat=tmat;
    }

    public int[] getIntOutputs()
    {
        int[] arr = {mac,chc,cmc,blockIndex};
        return arr;
    }

    public double[] getDoubOutputs()
    {
        double[] arr = {chr,cmr,amat,tmat};
        return arr;
    }

    public void createFile(int memAccCount, int[][] hitMissTable)
    {
        //TEXT LOG OF THE CACHE MEMORY TRACE
        int[] tempArray = new int[cacheBlocks];
        Arrays.fill(tempArray, -1);

        LocalDateTime currTime = LocalDateTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SS");
        String formattedTime = currTime.format(timeFormat);
        File fileName = new File("text-log-" + formattedTime + ".txt");

        try {
            if (fileName.createNewFile())
                System.out.println("File created: " + fileName.getName());
            else System.out.println("File already exists.");
            
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(fileName.getName());
            for (int i = 0; i < memAccCount; i++) {
                int index = hitMissTable[i][3];
                tempArray[index] = hitMissTable[i][0];

                myWriter.write("Step " + i + " (Sequence: " + tempArray[index] + " | Block: " + index + ")");
                myWriter.write(String.format("\r\n%10s ", "Blocks"));
                for (int j = 0; j < cacheBlocks; j++) {
                    myWriter.write(String.format("%6s ", j));
                }

                myWriter.write(String.format("\r\n%10s ", "Data"));

                for (int j = 0; j < cacheBlocks; j++) {
                    if (tempArray[j] == -1) myWriter.write(String.format("%6s ", " "));
                    else myWriter.write(String.format("%6s ", tempArray[j]));
                }

                myWriter.write("\r\n\n");
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

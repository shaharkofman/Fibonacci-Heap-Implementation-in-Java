import java.util.*;
import java.time.Instant;
import java.time.Duration;

class ExperimentResult {
    long executionTime;
    int heapSize;
    int numLinks;
    int numCuts;
    int numTrees;

    public ExperimentResult(long executionTime, int heapSize, int numLinks, int numCuts, int numTrees) {
        this.executionTime = executionTime;
        this.heapSize = heapSize;
        this.numLinks = numLinks;
        this.numCuts = numCuts;
        this.numTrees = numTrees;
    }
}

public class FibonacciHeapExperiment {
    private static int calculateN(int i) {
        return (int) Math.pow(3, i + 7) - 1;
    }

    // ניסוי ראשון: הכנסת n איברים ומחיקת מינימום
    private static ExperimentResult experiment1(int n) {
        Instant start = Instant.now();
        FibonacciHeap heap = new FibonacciHeap();

        // יצירת מערך של המספרים 1 עד n
        Integer[] numbers = new Integer[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = i + 1;
        }

        // ערבוב המערך
        List<Integer> shuffled = Arrays.asList(numbers);
        Collections.shuffle(shuffled);

        // הכנסת האיברים לערימה
        for (int num : shuffled) {
            heap.insert(num, "");
        }

        // מחיקת המינימום
        heap.deleteMin();

        Instant end = Instant.now();
        return new ExperimentResult(
                Duration.between(start, end).toMillis(),
                heap.size(),
                heap.totalLinks(),
                heap.totalCuts(),
                heap.numTrees()
        );
    }

    // ניסוי שני: הכנסת n איברים ומחיקת n/2 מינימומים
    private static ExperimentResult experiment2(int n) {
        Instant start = Instant.now();
        FibonacciHeap heap = new FibonacciHeap();

        Integer[] numbers = new Integer[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = i + 1;
        }

        List<Integer> shuffled = Arrays.asList(numbers);
        Collections.shuffle(shuffled);

        for (int num : shuffled) {
            heap.insert(num, "");
        }

        // מחיקת n/2 מינימומים
        for (int i = 0; i < n/2; i++) {
            heap.deleteMin();
        }

        Instant end = Instant.now();
        return new ExperimentResult(
                Duration.between(start, end).toMillis(),
                heap.size(),
                heap.totalLinks(),
                heap.totalCuts(),
                heap.numTrees()
        );
    }

    // ניסוי שלישי: הכנסת n איברים, מחיקת מינימום, ואז מחיקת מקסימום עד שנשארים 2^5-1
    private static ExperimentResult experiment3(int n) {
        Instant start = Instant.now();
        FibonacciHeap heap = new FibonacciHeap();
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[n + 1];  // מערך לשמירת המצביעים לצמתים

        // הכנסת המספרים בסדר אקראי
        Integer[] numbers = new Integer[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = i + 1;
        }
        List<Integer> shuffled = Arrays.asList(numbers);
        Collections.shuffle(shuffled);

        // הכנסת האיברים לערימה ושמירת המצביעים במערך
        for (int num : shuffled) {
            nodes[num] = heap.insert(num, "");
        }

        // מחיקת המינימום הראשון
        heap.deleteMin();

        // מחיקת המקסימום בכל פעם עד שנשארים 2^5-1 איברים
        while (heap.size() > Math.pow(2, 5) - 1) {
            // מציאת המקסימום הנוכחי
            int currentMax = n;
            while (currentMax > 0 && nodes[currentMax] == null) {
                currentMax--;
            }

            if (currentMax > 0) {
                heap.delete(nodes[currentMax]);
                nodes[currentMax] = null;
            }
        }

        Instant end = Instant.now();
        return new ExperimentResult(
                Duration.between(start, end).toMillis(),
                heap.size(),
                heap.totalLinks(),
                heap.totalCuts(),
                heap.numTrees()
        );
    }

    public static void main(String[] args) {
        // טבלה לאחסון הממוצעים
        double[][] averages = new double[5][5]; // [i-1][metric]

        // עבור כל i מ-1 עד 5
        for (int i = 1; i <= 5; i++) {
            int n = calculateN(i);

            // מערכים לאחסון תוצאות 20 הריצות
            ExperimentResult[] results1 = new ExperimentResult[20];
            ExperimentResult[] results2 = new ExperimentResult[20];
            ExperimentResult[] results3 = new ExperimentResult[20];

            // ביצוע 20 ריצות של כל ניסוי
            for (int run = 0; run < 20; run++) {
                results1[run] = experiment1(n);
                results2[run] = experiment2(n);
                results3[run] = experiment3(n);
            }

            // חישוב וכתיבת הממוצעים לטבלה
            System.out.printf("\nResults for i=%d (n=%d):\n", i, n);
            System.out.println("Experiment 1:");
            printAverageResults(results1);
            System.out.println("Experiment 2:");
            printAverageResults(results2);
            System.out.println("Experiment 3:");
            printAverageResults(results3);
        }
    }

    private static void printAverageResults(ExperimentResult[] results) {
        // חישוב ממוצעים
        double avgTime = Arrays.stream(results)
                .mapToLong(r -> r.executionTime)
                .average()
                .orElse(0);

        double avgSize = Arrays.stream(results)
                .mapToInt(r -> r.heapSize)
                .average()
                .orElse(0);

        double avgLinks = Arrays.stream(results)
                .mapToInt(r -> r.numLinks)
                .average()
                .orElse(0);

        double avgCuts = Arrays.stream(results)
                .mapToInt(r -> r.numCuts)
                .average()
                .orElse(0);

        double avgTrees = Arrays.stream(results)
                .mapToInt(r -> r.numTrees)
                .average()
                .orElse(0);

        System.out.printf("Time: %.2fms, Size: %.2f, Links: %.2f, Cuts: %.2f, Trees: %.2f\n",
                avgTime, avgSize, avgLinks, avgCuts, avgTrees);
    }
}

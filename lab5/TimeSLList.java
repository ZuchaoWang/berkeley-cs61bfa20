import java.util.ArrayList;
import java.util.List;

/**
 * Class that collects timing information about SLList getLast method.
 */
public class TimeSLList {
    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        int M = 10000;
        int N = 1000;
        int rounds = 8;
        List<Integer> Ns = new ArrayList<Integer>();
        List<Double> times = new ArrayList<Double>();
        List<Integer> opCounts = new ArrayList<Integer>();
        SLList<Integer> sll = null;
        for(int i=0; i<rounds; i++) {
            sll = new SLList<Integer>();
            for(int j=0; j<N; j++) sll.addLast(0);
            Stopwatch sw = new Stopwatch();
            for(int j=0; j<M; j++) sll.getLast();
            double timeInSeconds = sw.elapsedTime();
            Ns.add(N);
            times.add(timeInSeconds);
            opCounts.add(M);
            N *= 2;
        }
        printTimingTable(Ns, times, opCounts);
    }

}

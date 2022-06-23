package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
  private SolverOutcome outcome;
  private double solutionWeight;
  private List<Vertex> solution;
  private double timeSpent;
  private int numStates;

  public AStarSolver(AStarGraph<Vertex> G, Vertex start, Vertex end, double timeout) {
    Stopwatch sw = new Stopwatch();

    // empty state
    outcome = null;
    solutionWeight = 0;
    solution = new ArrayList<Vertex>();
    timeSpent = 0;
    numStates = 0;

    // set up AStar with start vertex
    HashMap<Vertex,Vertex> edgeTo = new HashMap<Vertex,Vertex>();
    HashMap<Vertex,Double> distTo = new HashMap<Vertex,Double>();
    ArrayHeapMinPQ<Vertex> fringe = new ArrayHeapMinPQ<Vertex>();
    edgeTo.put(start, null);
    distTo.put(start, 0.0);
    fringe.add(start, distTo.get(start)+G.estimatedDistanceToGoal(start,end));

    // run AStar
    while(fringe.size() > 0) {
      Vertex cur = fringe.removeSmallest();
      numStates++;
      timeSpent = sw.elapsedTime();
      if (timeSpent > timeout) {
        outcome = SolverOutcome.TIMEOUT;
        timeSpent = sw.elapsedTime();
        return;
      } else if (cur.equals(end)) {
        outcome = SolverOutcome.SOLVED;
        solutionWeight = distTo.get(cur);
        while(!cur.equals(start)) {
          solution.add(cur);
          cur = edgeTo.get(cur); // trace back
        }
        solution.add(start);
        Collections.reverse(solution);
        timeSpent = sw.elapsedTime();
        return;
      } else {
        List<WeightedEdge<Vertex>> neighborEdges = G.neighbors(cur);
        for (WeightedEdge<Vertex> e: neighborEdges) { // relax every neighbour
          Vertex to = e.to();
          double attemptedPathWeight = distTo.get(cur) + e.weight();
          if (!distTo.containsKey(to) || distTo.get(to) > attemptedPathWeight) {
            distTo.put(to, attemptedPathWeight);
            edgeTo.put(to, cur);
            double priority = attemptedPathWeight + G.estimatedDistanceToGoal(to,end);
            if (fringe.contains(to)) fringe.changePriority(to, priority);
            else fringe.add(to, priority);
          }
        }
      }
    }
    outcome = SolverOutcome.UNSOLVABLE;
    timeSpent = sw.elapsedTime();
  }

  public SolverOutcome outcome() {
    return outcome;
  }

  public List<Vertex> solution() {
    return solution;
  }

  public double solutionWeight() {
    return solutionWeight;
  }

  public int numStatesExplored() {
    return numStates;
  } 

  public double explorationTime() {
    return timeSpent;
  }
}

package com.acc.graph;

import com.acc.structure.BasicBlock;

import java.util.*;

/**
 * Created by Rumpy on 14-02-2015.
 */
public class GraphHelper {
    private Worker worker;
    private Map edgeList;
    private int blockLabel;

    public GraphHelper(Worker worker)
    {
        this.worker = worker;
        edgeList=new HashMap<Integer,List<Integer>>();
        blockLabel=0;
    }
    public void begin(BasicBlock root)
    {
        worker.printRootStatement(root);
        DFS(root);
    }

    public void DFS(BasicBlock node)
    {
        if (node == null) {
            return;
        }

        worker.setVisitationStatus(node);
        Integer destinationHashCode;
        Integer sourceHashCode = node.hashCode();
        node.setLabel(blockLabel++);
        for(BasicBlock n: node.getChildren())
        {
            destinationHashCode=n.hashCode();
            //if childs state is not visited then recurse
            if(!(edgeList.containsKey(sourceHashCode) && ((List<Integer>)(edgeList.get(sourceHashCode))).contains(destinationHashCode)))
            {

                List<Integer> destinationList;
                if(edgeList.containsKey(sourceHashCode))
                    destinationList = (List<Integer>)edgeList.get(sourceHashCode);
                else
                    destinationList = new ArrayList<Integer>();
                destinationList.add(destinationHashCode);
                edgeList.put(sourceHashCode,destinationList);
                if(n.visitationCounter()==0) {
                    DFS(n);
                }
                //SET UP S-D connection
                worker.visit(node,n);
            }
        }
    }
}

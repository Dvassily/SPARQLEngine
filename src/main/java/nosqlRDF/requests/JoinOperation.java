package nosqlRDF.requests;

import nosqlRDF.InvalidQueryArgumentException;
import nosqlRDF.SPARQLEngine;
import nosqlRDF.datas.RDFTriple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.aksw.commons.collections.Pair;

public class JoinOperation implements JoinableSet {
    private JoinableSet setFrom;
    private JoinableSet setTo;

    public JoinOperation(JoinableSet setFrom, JoinableSet setTo) {
        this.setFrom = setFrom;
        this.setTo = setTo;
    }

	@Override
	public Set<JoinableTriple> elements(SPARQLEngine engine) throws InvalidQueryArgumentException {
        return perform(engine);
    }

    public Set<JoinableTriple> perform(SPARQLEngine engine) throws InvalidQueryArgumentException {
        Set<JoinableTriple> elementsSetFrom = setFrom.elements(engine);
        Set<JoinableTriple> elementsSetTo = setTo.elements(engine);
        Set<JoinableTriple> resultingSet = new HashSet<>();
        System.out.println("join " + setFrom + " with " + setTo);

        for (JoinableTriple tripleFrom : elementsSetFrom) {
            for (JoinableTriple tripleTo : elementsSetTo) {
                if (!resultingSet.contains(tripleTo)
                    && tripleFrom.hasCommonVariablesWith(tripleTo)
                    && tripleFrom.allCommonVariablesEquals(tripleTo)) {
                    Set<JoinableTriple> propagation = propagate(tripleFrom, elementsSetFrom);
                    resultingSet.addAll(propagation);
                    resultingSet.add(tripleTo);
                }
            }
        }

        return resultingSet;
    }

    private Set<JoinableTriple> propagate(JoinableTriple base, Set<JoinableTriple> elements) {
        Set<JoinableTriple> result = new HashSet<>();
        Set<JoinableTriple> visited = new HashSet<>();
        Queue<JoinableTriple> queue = new LinkedList<>();
        queue.add(base);

        while (!queue.isEmpty()) {
            JoinableTriple current = queue.remove();
            result.add(current);

            for (JoinableTriple triple : elements) {
                if (triple.hasCommonVariablesWith(current)
                    && triple.allCommonVariablesEquals(current)
                    && ! triple.hasSameStructureAs(current)) {
                    if (! visited.contains(triple)) {
                        queue.add(triple);
                        visited.add(current);
                    }
                }
            }
        }

        return result;
    }
}

package nosqlRDF.requests;

import nosqlRDF.InvalidQueryArgumentException;
import nosqlRDF.SPARQLEngine;
import nosqlRDF.datas.RDFTriple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aksw.commons.collections.Pair;

public class JoinOperation implements JoinableSet {
    private JoinableSet setFrom;
    private JoinableSet setTo;

    public JoinOperation(JoinableSet setFrom, JoinableSet setTo) {
        this.setFrom = setFrom;
        this.setTo = setTo;
    }

    public Set<JoinableTriple> perform(SPARQLEngine engine) throws InvalidQueryArgumentException {
        Set<JoinableTriple> elementsSetFrom = setFrom.elements(engine);
        Set<JoinableTriple> elementsSetTo = setTo.elements(engine);
        Set<JoinableTriple> resultingSet = new HashSet<>();

        System.out.println("join " + setFrom + " with " + setTo);

        for (JoinableTriple tripleFrom : elementsSetFrom) {
            for (JoinableTriple tripleTo : elementsSetTo) {
                Map<String, String> variablesFrom = tripleFrom.variables();
                Map<String, String> variablesTo = tripleTo.variables();
                Set<String> commonVariables = new HashSet<>(variablesFrom.keySet());
                commonVariables.retainAll(variablesTo.keySet());

                boolean equality = true;
                boolean hasCommonVariables = ! commonVariables.isEmpty();

                for (String variable : commonVariables) {
                    if (! variablesFrom.get(variable).equals(variablesTo.get(variable))) {
                        equality &= false;
                        continue;
                    }

                }

                if (hasCommonVariables && equality || ! hasCommonVariables) {
                    System.out.println("Join " + tripleFrom + " and " + tripleTo + ", cvar:" + hasCommonVariables);
                    resultingSet.add(tripleTo);
                    resultingSet.add(tripleFrom);
                }
            }
        }

        return resultingSet;
    }

    @Override
	public Set<JoinableTriple> elements(SPARQLEngine engine) throws InvalidQueryArgumentException {
        return perform(engine);
    }
}

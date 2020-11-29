package nosqlRDF.requests;

import nosqlRDF.InvalidQueryArgumentException;
import nosqlRDF.SPARQLEngine;
import nosqlRDF.datas.RDFTriple;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JoinOperation implements JoinableSet {
    private JoinableSet setFrom;
    private JoinableSet setTo;
    private Set<String> resultSet = new HashSet<>();

    public JoinOperation(JoinableSet setFrom, JoinableSet setTo) {
        this.setFrom = setFrom;
        this.setTo = setTo;
    }

    public Set<JoinableTriple> perform(SPARQLEngine engine) throws InvalidQueryArgumentException {
        System.out.println("join " + setFrom + " with " + setTo);
        Set<JoinableTriple> elementsSetFrom = setFrom.elements(engine);
        Set<JoinableTriple> elementsSetTo = setTo.elements(engine);
        Set<JoinableTriple> result = new HashSet<>();

        for (JoinableTriple tripleFrom : elementsSetFrom) {
            for (JoinableTriple tripleTo : elementsSetTo) {
                Map<String, String> variablesFrom = tripleFrom.variables();
                Map<String, String> variablesTo = tripleTo.variables();
                Set<String> commonVariables = new HashSet<>(variablesFrom.keySet());
                commonVariables.retainAll(variablesTo.keySet());

                // All variables are common
                if (commonVariables.size() == variablesFrom.size()) {
                    boolean equality = true;

                    for (String variable : commonVariables) {
                        if (! variablesFrom.get(variable).equals(variablesTo.get(variable))) {
                            equality = false;
                        }
                    }

                    if (equality) {
                        System.out.println("+" + tripleFrom.getSubject());
                        System.out.println("+" + tripleFrom.getObject());
                        result.add(tripleFrom);
                        result.add(tripleTo);
                    }
                } else {
                    result.add(tripleFrom);
                    result.add(tripleTo);
                }
            }
        }

        return result;
    }

    private void project(Set<RDFTriple> tripleSet, String projection) {
        Condition condition = (Condition) tripleSet;

        for (RDFTriple triple : tripleSet) {
            if (condition.getSubject().equals(projection)) {
                resultSet.add(triple.getSubject());
            } else if (condition.getPredicate().equals(projection)) {
                resultSet.add(triple.getPredicate());
            } else if (condition.getObject().equals(projection)) {
                resultSet.add(triple.getObject());
            }
        }
    }

	@Override
	public Set<JoinableTriple> elements(SPARQLEngine engine) throws InvalidQueryArgumentException {
        return perform(engine);
    }

	@Override
	public Set<String> variables() {
		// TODO Auto-generated method stub
		return null;
	}
}

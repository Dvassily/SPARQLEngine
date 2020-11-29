package nosqlRDF.requests;

import nosqlRDF.InvalidQueryArgumentException;
import nosqlRDF.SPARQLEngine;

import java.util.Set;

public class JoinableTripleSet implements JoinableSet
{
    private Set<JoinableTriple> triples;
	private Set<String> variables;

	public JoinableTripleSet(Set<JoinableTriple> triples) {
        this.triples = triples;
    }

    @Override
	public Set<JoinableTriple> elements(SPARQLEngine engine) throws InvalidQueryArgumentException {
        return triples;
    }

    @Override
	public Set<String> variables() {
        return variables;
    }

    public Set<JoinableTriple> getTriples() throws InvalidQueryArgumentException {
        return elements(null);
    }
}

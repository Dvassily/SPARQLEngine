package nosqlRDF.requests;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.SPARQLEngine;
import nosqlRDF.InvalidQueryArgumentException;

import java.util.Set;


public interface JoinableSet {
    public Set<JoinableTriple> elements(SPARQLEngine engine) throws InvalidQueryArgumentException;
    // TODO: Remove if unused
    public Set<String> variables();
}

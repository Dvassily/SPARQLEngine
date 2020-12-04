package nosqlRDF.requests;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.SPARQLEngine;
import nosqlRDF.InvalidQueryArgumentException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Condition implements JoinableSet {
    private String subject;
    private String predicate;
    private String object;
    private boolean subjectIsVariable;
    private boolean predicateIsVariable;
    private boolean objectIsVariable;

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public boolean subjectIsVariable() {
        return subjectIsVariable;
    }

    public boolean predicateIsVariable() {
        return predicateIsVariable;
    }

    public boolean objectIsVariable() {
        return objectIsVariable;
    }

    public void setSubject(String value, boolean variable) {
        subject = value;
        subjectIsVariable = variable;
    }

    public void setPredicate(String value, boolean variable) {
        predicate = value;
        predicateIsVariable = variable;
    }

    public void setObject(String value, boolean variable) {
        object = value;
        objectIsVariable = variable;
    }

    public Set<JoinableTriple> elements(SPARQLEngine engine) throws InvalidQueryArgumentException {
        Set<JoinableTriple> result = new HashSet<>();

        for (RDFTriple triple : findMatchingTriple(engine)) {
            result.add(makeJoinableTriple(triple));
        }

        return result;
    }

    public Set<RDFTriple> findMatchingTriple(SPARQLEngine engine) throws InvalidQueryArgumentException {
       if (! subjectIsVariable() && ! predicateIsVariable() && ! objectIsVariable()) {
            return new HashSet<>(Arrays.asList(new RDFTriple(subject, predicate, object)));
        } else if (! subjectIsVariable() && ! predicateIsVariable() && objectIsVariable()) {
            return engine.findObject(subject, predicate);
        } else if (! subjectIsVariable() && predicateIsVariable() && ! objectIsVariable()) {
            return engine.findPredicate(subject, object);
        } else if (! subjectIsVariable() && predicateIsVariable() && objectIsVariable() ) {
            return engine.findPredicateObject(subject);
        } else if (subjectIsVariable() && ! predicateIsVariable() && ! objectIsVariable()) {
            return engine.findSubject(predicate, object);
        } else if (subjectIsVariable() && ! predicateIsVariable() && objectIsVariable()) {
            return engine.findSubjectObject(predicate);
        } else if (subjectIsVariable() && predicateIsVariable() && ! objectIsVariable()) {
            return engine.findSubjectPredicate(object);
        } else {
           return null;
       }
    }

    public JoinableTriple makeJoinableTriple(RDFTriple triple) {
        JoinableTriple joinableTriple = new JoinableTriple(triple);

        if (!subjectIsVariable() && !predicateIsVariable() && !objectIsVariable()) {
            // Does nothing
        } else if (!subjectIsVariable() && !predicateIsVariable() && objectIsVariable()) {
            joinableTriple.setObjectVariable(object);
        } else if (!subjectIsVariable() && predicateIsVariable() && !objectIsVariable()) {
            joinableTriple.setPredicateVariable(predicate);
        } else if (!subjectIsVariable() && predicateIsVariable() && objectIsVariable()) {
            joinableTriple.setPredicateVariable(predicate);
            joinableTriple.setObjectVariable(object);
        } else if (subjectIsVariable() && !predicateIsVariable() && !objectIsVariable()) {
            joinableTriple.setSubjectVariable(subject);
        } else if (subjectIsVariable() && !predicateIsVariable() && objectIsVariable()) {
            joinableTriple.setSubjectVariable(subject);
            joinableTriple.setObjectVariable(object);
        } else if (subjectIsVariable() && predicateIsVariable() && !objectIsVariable()) {
            joinableTriple.setSubjectVariable(subject);
            joinableTriple.setPredicateVariable(predicate);
        } else if (subjectIsVariable() && predicateIsVariable() && objectIsVariable()) {
            joinableTriple.setSubjectVariable(subject);
            joinableTriple.setPredicateVariable(predicate);
            joinableTriple.setObjectVariable(object);
        } else {
            return null;
        }

        return joinableTriple;
    }

    public Set<String> variables() {
        return new HashSet<>();
    }
}

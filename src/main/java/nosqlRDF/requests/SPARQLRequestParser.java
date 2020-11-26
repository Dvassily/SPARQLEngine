package nosqlRDF.requests;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.Projection;
import org.openrdf.query.algebra.QueryModelNode;
import org.openrdf.query.algebra.QueryRoot;
import org.openrdf.query.algebra.StatementPattern;
import org.openrdf.query.algebra.Var;
import org.openrdf.query.algebra.ProjectionElem;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;
import org.openrdf.query.algebra.helpers.StatementPatternCollector;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.parser.sparql.SPARQLParser;

public class SPARQLRequestParser {
	private String requestPath;
	private SPARQLParser sparqlParser;

	public SPARQLRequestParser(String requestPath) {
    	this.requestPath = requestPath;
		sparqlParser = new SPARQLParser();
	}

    public List<Request> loadQueries() throws IOException {
    	List<Request> requests = new LinkedList<>();

        // Load Queries from file
        File file = new File(requestPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String rawRequests = new String(data, "UTF-8").replace("\n", "").replace("\r", "");
        Pattern r = Pattern.compile("[^\\{]+\\{[^\\}]+\\}");
        Matcher queries = r.matcher(rawRequests);

        boolean foo = false;
        while (queries.find()) {
        	String query = queries.group(0);
			requests.add(parseQuery(query));
        }

        return requests;
    }

    public Request parseQuery(String query) {
        ParsedQuery pq = sparqlParser.parseQuery(query, null);

        List<String> projection = new ArrayList<>();

        pq.getTupleExpr().visit(new QueryModelVisitorBase<RuntimeException>() {
                public void meet(Projection proj) {
                    for (ProjectionElem elem : proj.getProjectionElemList().getElements()) {
                        projection.add(elem.getSourceName());
                    }
                }
            });


        List<StatementPattern> patterns = StatementPatternCollector.process(pq.getTupleExpr());
        List<Condition> conditions = new LinkedList<>();

        for (StatementPattern pattern: patterns) {
            Condition condition = new Condition();
            Var var = pattern.getSubjectVar();
            boolean isVariable = ! var.hasValue();
            String value = isVariable? var.getName() : var.getValue().stringValue();
            condition.setSubject(value, isVariable);

            var = pattern.getPredicateVar();
            isVariable = ! var.hasValue();
            value = isVariable? var.getName() : var.getValue().stringValue();
            condition.setPredicate(value, isVariable);

            var = pattern.getObjectVar();
            isVariable = ! var.hasValue();
            value = isVariable? var.getName() : var.getValue().stringValue();
            condition.setObject(value, isVariable);

            conditions.add(condition);
        }

        return new Request(projection, conditions, query);
	}
}

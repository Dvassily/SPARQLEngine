package nosqlRDF.requests;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.algebra.Projection;
import org.openrdf.query.algebra.QueryModelNode;
import org.openrdf.query.algebra.QueryRoot;
import org.openrdf.query.algebra.StatementPattern;
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
        // Create a Pattern object
        Pattern r = Pattern.compile("[^\\{]+\\{[^\\}]+\\}");

        // Now create matcher object.
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

	final String[] projection = {null};

	pq.getTupleExpr().visit(new QueryModelVisitorBase<RuntimeException>() {
		public void meet(Projection proj) {
		    projection[0] = proj.getProjectionElemList().getElements().get(0).getSourceName();
		}
	    });


	List<StatementPattern> patterns = StatementPatternCollector.process(pq.getTupleExpr());
	List<Condition> conditions = new LinkedList<>();
	
	for (StatementPattern pattern: patterns) {
	    String subject = pattern.getSubjectVar().getName();
	    String predicat = pattern.getPredicateVar().getValue().stringValue();
	    String object = pattern.getObjectVar().getValue().stringValue();
	    Condition cond = new Condition(subject, predicat, object);

	    conditions.add(cond);
	}


	return new Request(projection[0], conditions);
	}
}

package nosqlRDF;

public class BenchmarkEngine {
    private String feature;
    private long startTime;
    private long duration;

    public BenchmarkEngine(String feature) {
	this.feature = feature;

	reset();
    }
    
    public void begin() {
	startTime = System.currentTimeMillis();
    }

    public void end() {
	long endTime = System.currentTimeMillis();
	duration = endTime - startTime;
    }

    public void reset() {
	startTime = -1;
	duration = -1;
    }

    @Override
    public String toString() {
	return feature + " = " + duration + "ms";
    }
}

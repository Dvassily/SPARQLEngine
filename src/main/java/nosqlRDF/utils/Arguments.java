package nosqlRDF.utils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Arguments implements IArguments {
    @Parameter(names = {"-data" , "-d"},
               description = "Provides path containing data",
               required = true)
               private String dataPath = null;

    @Parameter(names = {"-queries" , "-q"},
               description = "Provides path towards the folder containing queries",
               required = true,
               validateWith = ArgumentValidator.class)
               private String requestPath = null;

    @Parameter(names = {"-output" , "-o"},
               description = "Provides path towards the output directory")
               private String outputPath = null;

	@Parameter(names = {"-verbose" , "-v"},
               description = "verbose option to show execution log")
               private boolean verbose = false;

    @Parameter(names = {"-export_query_stats" },
            description = "export query execution statistics")
    private boolean exportQueryStats = false;

    @Parameter(names = {"-export_query_results"},
            description = "export query result to files")
    private boolean exportQueryResults = false;

    @Parameter(names = {"-workload_time"},
            description = "verbose option to show execution log")
    private boolean workLoadTime = false;

    @Parameter(names = {"-jena"},
            description = "verbose option to show execution log")
    private boolean jena = false;

    @Parameter(names = {"-logMemory"},
            description = "verbose option to show execution log")
    public boolean logMemoryUsage = false;

    public double getWarm() {
        return warm;
    }

    public void setWarm(double warm) {
        this.warm = warm;
    }

    @Parameter(names = {"-warm"},
            description = "verbose option to show execution log")
    private double warm = -1;

    @Parameter(names = {"-shuffle"},
            description = "verbose option to show execution log")
    private boolean shuffle = false;


    @Parameter(names = {"-optim_none"},
            description = "verbose option to show execution log")
    private boolean optimNone = false;

    @Parameter(names = {"-star_queries"},
            description = "verbose option to show execution log")
    private boolean starQueries = false;

    @Override
    public void parse(String[] args) {
	JCommander.newBuilder()
	    .addObject(this)
	    .build()
	    .parse(args);
    }

    @Override
    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    @Override
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public boolean isExportQueryStats() {
        return exportQueryStats;
    }

    @Override
    public void setExportQueryStats(boolean exportQueryStats) {
        this.exportQueryStats = exportQueryStats;
    }

    @Override
    public boolean isExportQueryResults() {
        return exportQueryResults;
    }

    @Override
    public void setExportQueryResults(boolean exportQueryResults) {
        this.exportQueryResults = exportQueryResults;
    }

    @Override
    public boolean isWorkLoadTime() {
        return workLoadTime;
    }

    @Override
    public void setWorkLoadTime(boolean workLoadTime) {
        this.workLoadTime = workLoadTime;
    }

    @Override
    public boolean isJena() {
        return jena;
    }

    @Override
    public void setJena(boolean jena) {
        this.jena = jena;
    }

    @Override
    public boolean isShuffle() {
        return shuffle;
    }

    @Override
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    @Override
    public boolean isOptimNone() {
        return optimNone;
    }

    @Override
    public void setOptimNone(boolean optimNone) {
        this.optimNone = optimNone;
    }

    @Override
    public boolean isStarQueries() {
        return starQueries;
    }

    @Override
    public void setStarQueries(boolean starQueries) {
        this.starQueries = starQueries;
    }

    @Override
    public String getDataPath() {
        return dataPath;
    }

    @Override
    public String getRequestPath() {
        return requestPath;
    }

    @Override
    public String getOutputPath() {
        return outputPath;
    }

    @Override
    public boolean isVerbose() {
        return verbose;
    }
}

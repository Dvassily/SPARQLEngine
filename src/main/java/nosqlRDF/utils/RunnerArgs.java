package nosqlRDF.utils;

import com.beust.jcommander.Parameter;

public class RunnerArgs {
    private String dataPath;
    private String queryFile;
    private String outputPath;
    private boolean verbose;
    private boolean check;
    private boolean export_query_stats;
    private boolean workLoadTime;
    private boolean exportQueryResults;
    private  boolean shuffle;
    private boolean jena;
    private boolean warm = false;
    private boolean optimNone = false;
    private boolean starQueries = false;


    public RunnerArgs() {

    }

    public RunnerArgs(Arguments args) {

    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getQueryFile() {
        return queryFile;
    }

    public void setQueryFile(String queryFile) {
        this.queryFile = queryFile;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isExport_query_stats() {
        return export_query_stats;
    }

    public void setExport_query_stats(boolean export_query_stats) {
        this.export_query_stats = export_query_stats;
    }

    public boolean isWorkLoadTime() {
        return workLoadTime;
    }

    public void setWorkLoadTime(boolean workLoadTime) {
        this.workLoadTime = workLoadTime;
    }

    public boolean isExportQueryResults() {
        return exportQueryResults;
    }

    public void setExportQueryResults(boolean exportQueryResults) {
        this.exportQueryResults = exportQueryResults;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isJena() {
        return jena;
    }

    public void setJena(boolean jena) {
        this.jena = jena;
    }

    public boolean isWarm() {
        return warm;
    }

    public void setWarm(boolean warm) {
        this.warm = warm;
    }

    public boolean isOptimNone() {
        return optimNone;
    }

    public void setOptimNone(boolean optimNone) {
        this.optimNone = optimNone;
    }

    public boolean isStarQueries() {
        return starQueries;
    }

    public void setStarQueries(boolean starQueries) {
        this.starQueries = starQueries;
    }
}

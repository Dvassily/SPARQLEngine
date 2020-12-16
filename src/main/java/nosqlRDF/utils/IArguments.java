package nosqlRDF.utils;

public interface IArguments {
    void parse(String[] args);

    void setDataPath(String dataPath);

    void setRequestPath(String requestPath);

    void setOutputPath(String outputPath);

    void setVerbose(boolean verbose);

    boolean isExportQueryStats();

    void setExportQueryStats(boolean exportQueryStats);

    boolean isExportQueryResults();

    void setExportQueryResults(boolean exportQueryResults);

    boolean isWorkLoadTime();

    void setWorkLoadTime(boolean workLoadTime);

    boolean isJena();

    void setJena(boolean jena);

    boolean isShuffle();

    void setShuffle(boolean shuffle);


    boolean isOptimNone();

    void setOptimNone(boolean optimNone);

    boolean isStarQueries();

    void setStarQueries(boolean starQueries);

    String getDataPath();

    String getRequestPath();

    String getOutputPath();

    boolean isVerbose();

    double getWarm();
    public void setWarm(double warm);

}

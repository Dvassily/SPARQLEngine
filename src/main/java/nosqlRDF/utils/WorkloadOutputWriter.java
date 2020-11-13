// package nosqlRDF.utils;

// public class WorkloadOutputWriter extends OutputWriter {
//     public WorkloadOutputWriter(String path) {
//         super(path);

//         this.queryFile = queryFile;
//     }

//     @Override
//     public void insertHeader() {
//         insertRow(pwWorkLoad, new String[] { "nom du fichier de données",
//                                              "nom du fichier de requête",
//                                              "nombre de triplets RDF",
//                                              "nombre de requêtes",
//                                              "temps de lecture des requêtes (ms)",
//                                              "tempscréation dico (ms)",
//                                              "nombre d’index",
//                                              "temps total création des index",
//                                              "temps d’évaluation du workload",
//                                              "temps pris par l’optimisation",
//                                              "temps total du programme" });
//         }

//         public void insert(String dataFile,
//                         String requestFolder,
//                         int numberOfRDFTriples,
//                         int numberOfRequests,
//                         LONG requestReadingDuration,
//                         long dictionaryConstructionDuration,
//                         int numberOfIndex,
//                         int indexesConstructionDuration,
//                         int workloadEvaluationDuration,
//                         int optimizationsDuration,
//                         int total) {

//         }

//         public void close() {
//             printWriter.close();
//         }

//         public void insertRow(PrintWriter writer, String[] args) {
//             String rowStr = Stream.of(args)
//                 .collect(Collectors.joining(","));

//             writer.write(rowStr + "\n");
//         }
//     }

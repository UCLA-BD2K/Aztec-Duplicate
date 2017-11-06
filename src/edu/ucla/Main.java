package edu.ucla;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(
                    "src/data.json"));

            JSONObject jsonObject = (JSONObject) obj;

            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONArray docs = (JSONArray) response.get("docs");

            BaseCases bc = new BaseCases(docs);
            HashSet<List<Integer>> doi = bc.findDOISimilarities();
            HashSet<List<Integer>> name = bc.findNameSimilarities();
            HashSet<List<Integer>> author = bc.findAuthorSimilarities();
            HashSet<List<Integer>> inst = bc.findInstitutionSimilarities();
            HashSet<List<Integer>> link = bc.findLinkSimilarities();
            System.out.println("base cases done");

            printSet(doi, "similar_doi.csv");
            printSet(name, "similar_name.csv");
            printSet(author, "similar_author.csv");
            printSet(inst, "similar_institution.csv");
            printSet(link, "similar_link.csv");
            bc.convertToFiles();
            System.out.println("converting done");

            // Currently matching on names; update if different criteria wanted.
            HashSet<List<Integer>> matches = bc.checkNameSimilarities("similar_name.csv", "similar_doi.csv", "similar_author.csv", "similar_institution.csv");
            matches = bc.clusterMatches(matches);
            printSet(matches, "matched.csv");
            printSet(bc.clusterMatches(bc.readFile("similar_name.csv")), "all_name_matched.csv");
            bc.combine("all_name_matched.csv", "all_name_mapping.csv", "all_name_combined_tools.json");
            System.out.println("combining done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printSet(HashSet<List<Integer>> set, String name) throws IOException{
        FileWriter fw = new FileWriter(name);
        BufferedWriter bw = new BufferedWriter(fw);

        for(List<Integer> s : set){
            StringBuilder buff = new StringBuilder();
            String sep = "";
            for (int str : s) {
                buff.append(sep);
                buff.append(str);
                sep = ",";
            }
            bw.write(buff.toString() + '\n');
        }

        bw.close();
        fw.close();
    }
}

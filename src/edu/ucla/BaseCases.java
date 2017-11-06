package edu.ucla;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by patricktan on 5/15/17.
 */
public class BaseCases {
    private JSONArray documents;
    public BaseCases(JSONArray documents){
        this.documents = documents;
    }

    // Find similarities based on authors (if all authors are equivalent)
    public HashSet<List<Integer>> findAuthorSimilarities() {
        HashSet<List<Integer>> similar = new HashSet<>();

        for(int i = 0; i < documents.size(); i++){
            for(int j = i + 1; j < documents.size(); j++){
                JSONObject first = (JSONObject)documents.get(i);
                JSONObject second = (JSONObject)documents.get(j);

                int first_id = ((Long) first.get("id")).intValue();
                int second_id = ((Long) second.get("id")).intValue();

                JSONArray first_authors = (JSONArray) first.get("authors");
                JSONArray second_authors = (JSONArray) second.get("authors");

                boolean same_authors = true;
                if(first_authors == null || second_authors == null){
                    same_authors = false;
                }
                else if(first_authors.size() != second_authors.size()){
                    same_authors = false;
                }
                else{
                    for(int cur_auth_one = 0; cur_auth_one < first_authors.size(); cur_auth_one++){
                        boolean author_found = false;
                        for(int cur_auth_two = 0; cur_auth_two < second_authors.size(); cur_auth_two++){
                            if(first_authors.get(cur_auth_one).equals(second_authors.get(cur_auth_two))){
                                author_found = true;
                                break;
                            }
                        }
                        if(author_found == false){
                            same_authors = false;
                            break;
                        }
                    }
                }

                if(same_authors){
                    if (first_id > second_id) {
                        int temp = first_id;
                        first_id = second_id;
                        second_id = temp;
                    }
                    similar.add(Arrays.asList(first_id, second_id));
                }
            }
        }
        return similar;
    }

    // Find similarities based on institutions (if any institution is equivalent)
    public HashSet<List<Integer>> findInstitutionSimilarities() {
        HashSet<List<Integer>> similar = new HashSet<>();

        for(int i = 0; i < documents.size(); i++){
            for(int j = i + 1; j < documents.size(); j++){
                JSONObject first = (JSONObject)documents.get(i);
                JSONObject second = (JSONObject)documents.get(j);

                int first_id = ((Long) first.get("id")).intValue();
                int second_id = ((Long) second.get("id")).intValue();

                JSONArray first_institutions = (JSONArray) first.get("institutions");
                JSONArray second_institutions = (JSONArray) second.get("institutions");

                boolean same_institutions = false;
                if(first_institutions == null || second_institutions == null){
                    same_institutions = false;
                }
                else{
                    for(int cur_inst_one = 0; cur_inst_one < first_institutions.size(); cur_inst_one++){
                        boolean institutions_found = false;
                        for(int cur_inst_two = 0; cur_inst_two < second_institutions.size(); cur_inst_two++){
                            if(first_institutions.get(cur_inst_one).equals(second_institutions.get(cur_inst_two))){
                                institutions_found = true;
                                break;
                            }
                        }
                        if(institutions_found == true){
                            same_institutions = true;
                            break;
                        }
                    }
                }

                if(same_institutions){
                    if (first_id > second_id) {
                        int temp = first_id;
                        first_id = second_id;
                        second_id = temp;
                    }
                    similar.add(Arrays.asList(first_id, second_id));
                }
            }
        }
        return similar;
    }

    // Find similarities based on links (if any link is equivalent)
    public HashSet<List<Integer>> findLinkSimilarities() {
        HashSet<List<Integer>> similar = new HashSet<>();

        for(int i = 0; i < documents.size(); i++){
            for(int j = i + 1; j < documents.size(); j++){
                JSONObject first = (JSONObject)documents.get(i);
                JSONObject second = (JSONObject)documents.get(j);

                int first_id = ((Long) first.get("id")).intValue();
                int second_id = ((Long) second.get("id")).intValue();

                JSONArray first_link = (JSONArray) first.get("sourceCodeURL");
                JSONArray second_link = (JSONArray) second.get("sourceCodeURL");

                boolean same_link = false;
                if(first_link == null || second_link == null){
                    same_link = false;
                }
                else{
                    for(int cur_link_one = 0; cur_link_one < first_link.size(); cur_link_one++){
                        boolean link_found = false;
                        for(int cur_link_two = 0; cur_link_two < second_link.size(); cur_link_two++){
                            if(first_link.get(cur_link_one).equals(second_link.get(cur_link_two))){
                                link_found = true;
                                break;
                            }
                        }
                        if(link_found == true){
                            same_link = true;
                            break;
                        }
                    }
                }

                if(same_link){
                    if (first_id > second_id) {
                        int temp = first_id;
                        first_id = second_id;
                        second_id = temp;
                    }
                    similar.add(Arrays.asList(first_id, second_id));
                }
            }
        }
        return similar;
    }

    // Find similarities based on name (if the name is equivalent)
    public HashSet<List<Integer>> findNameSimilarities(){
        HashSet<List<Integer>>  similar = new HashSet<>();

        for(int i = 0; i < documents.size(); i++){
            for(int j = i + 1; j < documents.size(); j++){
                JSONObject first = (JSONObject)documents.get(i);
                JSONObject second = (JSONObject)documents.get(j);

                int first_id = ((Long) first.get("id")).intValue();
                int second_id = ((Long) second.get("id")).intValue();

                if(first.get("name").equals(second.get("name"))){
                    if (first_id > second_id) {
                        int temp = first_id;
                        first_id = second_id;
                        second_id = temp;
                    }
                    similar.add(Arrays.asList(first_id, second_id));
                }
            }
        }
        return similar;
    }

    // Find similarities based on DOI (if the DOI is equivalent)
    public HashSet<List<Integer>> findDOISimilarities(){
        HashSet<List<Integer>>  similar = new HashSet<>();

        for(int i = 0; i < documents.size(); i++){
            for(int j = i + 1; j < documents.size(); j++){
                JSONObject first = (JSONObject)documents.get(i);
                JSONObject second = (JSONObject)documents.get(j);

                int first_id = ((Long) first.get("id")).intValue();
                int second_id = ((Long) second.get("id")).intValue();

                JSONArray doiA = (JSONArray) first.get("publicationDOI");
                JSONArray doiB = (JSONArray) second.get("publicationDOI");

                String a = "";
                String b = "";
                if (doiA != null && doiB != null && doiA.size() > 0 && doiB.size() > 0) {
                    a = (String) doiA.get(0);
                    b = (String) doiB.get(0);
                }
                else {
                    continue;
                }

                if(a == null || b == null || a.equals("") || b.equals("")){
                    continue;
                }

                if(a.equals(b)){
                    if (first_id > second_id) {
                        int temp = first_id;
                        first_id = second_id;
                        second_id = temp;
                    }
                    similar.add(Arrays.asList(first_id, second_id));
                }
            }
        }
        return similar;
    }

    // Converts documents to single files in the "train" folder
    public void convertToFiles(){
        try {
            for(int i = 0; i < documents.size(); i++){
                JSONObject first = (JSONObject)documents.get(i);
                int first_id = ((Long) first.get("id")).intValue();
                String description = (String) first.get("description");
                if(description != null) {
                    String filename = "train/" + first_id + ".txt";

                    PrintWriter out = new PrintWriter(filename);
                    out.print(description);
                    out.close();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // For all name match pairs, check if the pair exists in similar dois, authors, or institutions.
    public HashSet<List<Integer>> checkNameSimilarities(String name_file, String doi_file, String author_file, String institution_file){
        HashSet<List<Integer>> similar_names = readFile(name_file);
        HashSet<List<Integer>> similar_dois = readFile(doi_file);
        HashSet<List<Integer>> similar_authors = readFile(author_file);
        HashSet<List<Integer>> similar_insts = readFile(institution_file);

        HashSet<List<Integer>> matches = new HashSet<>();

        for (List<Integer> names : similar_names){
            if(similar_dois.contains(names) || similar_authors.contains(names) || similar_insts.contains(names)){
                matches.add(names);
            }
        }
        return matches;
    }

    // Cluster the pairs into groups
    public HashSet<List<Integer>> clusterMatches(HashSet<List<Integer>> matches){
        ArrayList<ArrayList<Integer>> clusters = new ArrayList<>();
        int previous_clusters = -1;

        while(previous_clusters != clusters.size()) {
            previous_clusters = clusters.size();
            clusters.clear();
            for (List<Integer> match : matches) {
                int first = match.get(0);
                int second = match.get(1);
                boolean matched = false;
                for (int i = 0; i < clusters.size(); i++) {
                    ArrayList<Integer> cluster = clusters.get(i);
                    boolean first_match = cluster.contains(first);
                    boolean second_match = cluster.contains(second);
                    if (first_match && second_match) {
                        matched = true;
                        break;
                    } else if (first_match) {
                        matched = true;
                        cluster.add(second);
                        clusters.set(i, cluster);
                        break;
                    } else if (second_match) {
                        matched = true;
                        cluster.add(first);
                        clusters.set(i, cluster);
                        break;
                    }
                }
                if (!matched) {
                    clusters.add(new ArrayList<Integer>(match));
                }
            }
        }

        HashSet<List<Integer>> clusters_set = new HashSet<>();
        for(List<Integer> cluster : clusters)
            clusters_set.add(cluster);

        return clusters_set;
    }

    // Reads in csv files
    public HashSet<List<Integer>> readFile(String file_name){
        HashSet<List<Integer>> set = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] line_arr = line.split(",");
                Integer[] int_arr = new Integer[line_arr.length];
                int cur = 0;
                for (String num : line_arr){
                    int_arr[cur] = Integer.parseInt(num);
                    cur++;
                }
                set.add(Arrays.asList(int_arr));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return set;
    }

    // Clusters matches, then combines the metadata
    public void combine(String match_file, String mapping_file, String results_file){
        //id?
        try{
            FileWriter map_file = new FileWriter(mapping_file);
            BufferedWriter buffered_mapping = new BufferedWriter(map_file);
            FileWriter r_file = new FileWriter(results_file);
            BufferedWriter buffered_results = new BufferedWriter(r_file);
            String urlString = "http://dev.aztec.io:8983/solr/BD2K";
            SolrClient solr = new HttpSolrClient.Builder(urlString).build();
            SolrQuery query = new SolrQuery();

            // SET ID TO NEXT AVAILABLE ID IN DATABASE
            int set_id = 10967;
            SolrDocumentList finalList = new SolrDocumentList();

            HashSet<List<Integer>> clusters = readFile(match_file);
            for(List<Integer> cluster : clusters) {
                buffered_mapping.write("[" + cluster.get(0));
                int first = cluster.get(0);
                query.set("q", "id:" + cluster.get(0));
                QueryResponse response = solr.query(query);
                SolrDocumentList list = response.getResults();
                SolrDocument update = list.get(0);


                for(int i = 1; i < cluster.size(); i++) {
                    query = new SolrQuery();
                    query.set("q", "id:" + cluster.get(i));
                    buffered_mapping.write("," + cluster.get(i));
                    response = solr.query(query);
                    list = response.getResults();
                    SolrDocument cur_doc = list.get(0);
//                    System.out.println(doc1.toString());
//                    System.out.println(doc2.toString());

                    combineStringArrays(cur_doc, update, "domains");
                    combineStringArrays(cur_doc, update, "dependencies");
                    combineStringArrays(cur_doc, update, "downstreams");
                    combineStringArrays(cur_doc, update, "upstreams");
                    combineStringArrays(cur_doc, update, "funding");
                    combineStringArrays(cur_doc, update, "inputFiles");
                    combineStringArrays(cur_doc, update, "outputFiles");
                    combineStringArrays(cur_doc, update, "institutions");
                    combineStringArrays(cur_doc, update, "language");
                    combineStringArrays(cur_doc, update, "owners");
                    combineStringArrays(cur_doc, update, "platforms");
                    combineStringArrays(cur_doc, update, "publicationDOI");
                    combineStringArrays(cur_doc, update, "prev_version");
                    combineStringArrays(cur_doc, update, "tags");
                    combineStringArrays(cur_doc, update, "types");

                    chooseLargerInt(cur_doc, update, "forks");
                    chooseLargerInt(cur_doc, update, "subscribers");

                    combineByDescription(cur_doc, update);

                    //combine authors and authorEmails
                    combineTwoStringArrays(cur_doc, update,"authors", "authorEmails");
                    combineTwoStringArrays(cur_doc, update,"licenses", "licenseUrls");
                    combineTwoStringArrays(cur_doc, update,"linkUrls", "linkDescriptions");
                    combineTwoStringArrays(cur_doc, update,"maintainers", "maintainerEmails");

                    //updateDate
                    Date upd_date = (Date)update.get("dateUpdated");
                    Date cur_date = (Date)cur_doc.get("dateUpdated");
                    if(upd_date == null && cur_date == null) {}
                    else if(upd_date == null){
                        update.setField("dateUpdated", cur_date);
                    }
                    else if(cur_date == null){}
                    else{
                        if(upd_date.before(cur_date)){
                            update.setField("dateUpdated", cur_date);
                        }
                    }
                }
                update.setField("id", set_id);
                buffered_mapping.write("]->" + set_id + "\n");
                set_id++;
                finalList.add(update);
//                System.out.println(update.toString());

//                SolrInputDocument update = new SolrInputDocument();
            }

            JSONArray jArray = new JSONArray();
            for (int i = 0; i < finalList.size(); i++) {
                JSONObject json = new JSONObject(finalList.get(i));
                jArray.add(json);
            }

            buffered_results.write(JSONArray.toJSONString(jArray));
            buffered_mapping.close();
            buffered_results.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Helper function for combining string arrays
    public void combineStringArrays(SolrDocument cur_doc, SolrDocument update, String fieldName){
        ArrayList<String> upd_val = ((ArrayList<String>)update.get(fieldName));
        ArrayList<String> cur_val = ((ArrayList<String>)cur_doc.get(fieldName));
        if(upd_val == null && cur_val == null){}
        else if(upd_val == null){
            update.setField(fieldName, cur_val);
        }
        else if(cur_val == null){}
        else {
            for (int cur_index = 0; cur_index < cur_val.size(); cur_index++) {
                String cur_value = cur_val.get(cur_index);
                boolean match = false;
                for (int upd_index = 0; upd_index < upd_val.size(); upd_index++) {
                    if (cur_value.equals(upd_val.get(upd_index))) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    upd_val.add(cur_value);
//                    System.out.println("Adding " + fieldName + ": " + cur_value);
                }
            }
            update.setField(fieldName, upd_val);
        }
    }

    // Helper function for combining two string arrays
    public void combineTwoStringArrays(SolrDocument cur_doc, SolrDocument update, String main_field_name, String secondary_field_name){
        ArrayList<String> update_main = (ArrayList<String>)update.get(main_field_name);
        ArrayList<String> cur_main = (ArrayList<String>)cur_doc.get(main_field_name);
        ArrayList<String> update_secondary = (ArrayList<String>)update.get(secondary_field_name);
        ArrayList<String> cur_secondary = (ArrayList<String>)cur_doc.get(secondary_field_name);
        if(update_main == null && cur_main == null){}
        else if(update_main == null){
            update.setField(main_field_name, cur_main);
            update.setField(secondary_field_name, cur_secondary);
        }
        else if(cur_main == null){}
        else {
            for (int cur_index = 0; cur_index < cur_main.size(); cur_index++) {
                String cur_value = cur_main.get(cur_index);
                boolean match = false;
                for (int upd_index = 0; upd_index < update_main.size(); upd_index++) {
                    if (cur_value.equals(update_main.get(upd_index))) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    update_main.add(cur_value);
//                    System.out.println("Adding " + main_field_name + ": " + cur_value);
                    if(cur_secondary != null && update_secondary != null && cur_secondary.size() == update_secondary.size()) {
                        update_secondary.add(cur_secondary.get(cur_index));
//                        System.out.println("Adding " + secondary_field_name + ": " + cur_secondary.get(cur_index));
                    }
                }
            }
            update.setField(main_field_name, update_main);
            update.setField(secondary_field_name, update_secondary);
        }
    }

    // Helper function to choose the larger integer for fields
    public void chooseLargerInt(SolrDocument cur_doc, SolrDocument update, String fieldName){
        boolean update_exist = update.containsKey(fieldName);
        boolean current_exist = cur_doc.containsKey(fieldName);
        int upd_value = -1;
        int cur_value = -1;
        if(update_exist)
            upd_value = (int)update.get(fieldName);
        if(current_exist)
            cur_value = (int)cur_doc.get(fieldName);
        if(!update_exist && !current_exist) {}
        else if(!update_exist){
            update.setField(fieldName, cur_value);
        }
        else if(!current_exist){}
        else{
            if(upd_value < cur_value){
                update.setField(fieldName, cur_value);
//                System.out.println("Changing " + fieldName + ": from " + upd_value + " to " + cur_value);
            }
        }
    }

    // Helper function to choose the smaller integer for fields
    public void chooseSmallerInt(SolrDocument cur_doc, SolrDocument update, String fieldName){
        boolean update_exist = update.containsKey(fieldName);
        boolean current_exist = cur_doc.containsKey(fieldName);
        int upd_value = -1;
        int cur_value = -1;
        if(update_exist)
            upd_value = (int)update.get(fieldName);
        if(current_exist)
            cur_value = (int)cur_doc.get(fieldName);
        if(!update_exist && !current_exist) {}
        else if(!update_exist){
            update.setField(fieldName, cur_value);
        }
        else if(!current_exist){}
        else{
            if(upd_value > cur_value){
                update.setField(fieldName, cur_value);
                System.out.println("Changing " + fieldName + ": from " + upd_value + " to " + cur_value);
            }
        }
    }

    // Helper function to choose the longer description as the new description
    public void combineByDescription(SolrDocument cur_doc, SolrDocument update) {
        String update_desc = ((String)update.get("description"));
        String cur_desc = ((String)cur_doc.get("description"));

        boolean change = false;

        if(update_desc == null && cur_desc == null){}
        else if(update_desc == null){
            change = true;
        }
        else if(cur_desc == null){}
        else{
            if(update_desc.length() < cur_desc.length()){
                change = true;
            }
        }

        String[] toChange = new String[]{"description", "dateCreated", "latest_version", "logo", "name", "nextVersion", "prevVersion",
            "repo", "source", "sourceCodeURL", "suggestName", "suggestNamePrefix", "suggestTag", "suggestTagPrefix", "summary", "toolDOI", "versionDate", "versionNum"};
        if(change){
            for(String field : toChange){
                if(cur_doc.get(field) != null)
                    update.setField(field, cur_doc.get(field));
            }
        }
    }
}

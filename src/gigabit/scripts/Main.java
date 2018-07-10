package gigabit.scripts;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws IOException {

        Core core = new Core();
//        System.out.println("test");
        String fileName = "errors.txt";

        ArrayList iptv = core.createDataCollection(fileName);

        String fileName2 = "completeSource.txt";

        core.createSourceForIptv(iptv,fileName2);


//        ArrayList<InputDataList> addresses;
//        ArrayList<InputDataList> oltSn;
//        ArrayList<InputDataList> complete;
//        ArrayList<InputDataList> errors;
//        ArrayList<InputDataList> duplicates = new ArrayList<>();
//
//        String fileName = args[0];  //utm
//        String fileName2 = args[1]; //olt
//
//        addresses = core.createDataCollection(fileName);
//        oltSn = core.createDataCollection(fileName2);
//
//        complete = core.createCompleteCollection(oltSn, addresses);
//
//        core.createDuplicateCollection(complete, duplicates);
//
//        core.createErrorsCollection(complete, oltSn);
//        core.createErrorsCollection(duplicates, oltSn);
//
//        errors = oltSn;
//
//        String completeOut = fileName2 + " - complete.txt";
//        String errorsOut = fileName2 + " - errors.txt";
//        String duplicatesOut = fileName2 + " - duplicates.txt";
//
//
//        core.createOutLists(complete, completeOut);
//        core.createOutLists(duplicates, duplicatesOut);
//        core.createOutLists(errors, errorsOut);
//
//
//        int z = complete.size() + errors.size() + duplicates.size();
//        System.out.println("COMPLETE CONTAINS: " + complete.size());
//        System.out.println("ERRORS CONTAINS: " + errors.size());
//        System.out.println("DUPLICATES CONTAINS: " + duplicates.size());
//        System.out.println("SUMM: " + z);

    }

}

class Core {

    public int getStringCount(String fileName) throws IOException {
        //Charset UTF = Charset.forName("UTF-8");
        int i = 0;
        BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        while (bufferedReader.readLine() != null)
            i++;

        bufferedReader.close();
        return i;
    }

    public ArrayList createDataCollection(String fileName) throws IOException {
        ArrayList <InputDataList> arrayList = new ArrayList<InputDataList>();
        Charset UTF = Charset.forName("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        for (int i = 0; i < getStringCount(fileName); i++) {
            String readLine = reader.readLine();

            String[] s = readLine.split(";");
            InputDataList list = new InputDataList(s[0],s[1]);
            arrayList.add(list);
            //System.out.println(s[0] + " and " + s[1] + " added");
        }

        reader.close();
        return arrayList;
    }

    public ArrayList<InputDataList> createCompleteCollection(ArrayList<InputDataList> oltSn, ArrayList<InputDataList> addresses){

        ArrayList<InputDataList> complete = new ArrayList<>();

        for (int i = 0; i < oltSn.size(); i++) {
            String desc = oltSn.get(i).getId();

            for (int j = 0; j < addresses.size(); j++) {
                if (desc.equals(addresses.get(j).getId())){
                    InputDataList inputDataList = new InputDataList(oltSn.get(i).getId(),addresses.get(j).getText(),oltSn.get(i).getText());
                    complete.add(inputDataList);
                }

            }
        }
        return complete;
    }

    public void createDuplicateCollection(ArrayList<InputDataList> complete, ArrayList<InputDataList> duplicates) {

        for (int k = 0; k < complete.size(); k++) {
            InputDataList inputDataList = complete.get(k);
            String id = inputDataList.getId();

            for (int i = 0; i < complete.size(); i++) {
                if (inputDataList.equals(complete.get(i))){
                    continue;
                }

                if (id.equals(complete.get(i).getId())){
                    duplicates.add(complete.get(i));
                    duplicates.add(complete.get(k));
                    complete.remove(i);
                    complete.remove(k);
                    i--;
                    k--;
                }
            }
        }
    }

    public void createErrorsCollection(ArrayList<InputDataList> collection, ArrayList<InputDataList> oltSn ) {

        for (int j = 0; j < collection.size(); j++) {

            String login = collection.get(j).getId();

            for (int i = 0; i < oltSn.size(); i++) {

                String id = oltSn.get(i).getId();

                if (id.equals(login)) {
                    oltSn.remove(i);
                    if (i > 0)
                        i--;

                }
            }

        }
    }

    public void createOutLists(ArrayList<InputDataList> collection, String fileName) throws IOException {

        FileWriter fileWriter = new FileWriter(fileName, false);

        for (InputDataList aCollection : collection) {

            String writeStr = (aCollection.getId()+ ","+ aCollection.getText() +","+aCollection.getText2());
            fileWriter.write(writeStr);
            fileWriter.write(System.lineSeparator());

        }
        fileWriter.close();
    }

    public void createSourceForIptv(ArrayList<InputDataList> collection, String fileName) throws IOException {

        FileWriter fileWriter = new FileWriter(fileName, false);


        //<li>
        // <div class="overflow-hidden"><img class="channel fr-fic fr-dii" src="{THEME}/images/channel/zik.png" data-placement="bottom" title="ZIK" height="75px" width="75px"></div>
        //</li>
        String li = "<li>";
        String liClose = "</li>";
        for (int i=0; i < collection.size(); i++) {

            String writeString = ("<div class=\"overflow-hidden\"><img class=\"channel fr-fic fr-dii\" src=\"{THEME}/images/iptv/" + collection.get(i).getId() + ".png\" data-placement=\"bottom\" title=\"" + collection.get(i).getText() + "\" height=\"75px\" width=\"75px\"></div>");

            fileWriter.write(li);
            fileWriter.write(System.lineSeparator());
            fileWriter.write(writeString);
            fileWriter.write(System.lineSeparator());
            fileWriter.write(liClose);
            fileWriter.write(System.lineSeparator());
        }
        fileWriter.close();
    }

}

class InputDataList {

    private String id;
    private String text;
    private String text2;

    public InputDataList(){}

    public InputDataList(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public InputDataList(String id, String text, String text2){
        this.id = id;
        this.text = text;
        this.text2 = text2;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText2() {
        return text2;
    }
}


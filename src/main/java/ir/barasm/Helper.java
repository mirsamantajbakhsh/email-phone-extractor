package ir.barasm;

import java.io.*;
import java.util.ArrayList;

import org.json.*;

public class Helper {
    public static ArrayList<String> readFile(String filePath) {
        String line;
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lines;
    }

    public static void writeToFile(String filePath, String text) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<String> getLanguageStopwords(String language) {
        String json = readFile(Variables.stopwordsFile).get(0);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(language);
        ArrayList<String> stopwords = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stopwords.add(jsonArray.get(i).toString());
        }
        return stopwords;
    }

    public static int getDirectoryFileCount(String directoryPath) {
        File directory = new File(directoryPath);
        int count = 0;
        for (int i = 0; i < directory.listFiles().length; i++) {
            if (directory.listFiles()[i].isFile())
                count++;
        }
        return count;
    }

    public static String[] getDirectoryFileNames(String directoryPath) {
        File directory = new File(directoryPath);
        String[] files = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        return files;
    }

    public static String removeStopwords(String text, String textLanguage) {
        String newText = text;
        for (String stopword : getLanguageStopwords(textLanguage)) {
            newText = text.replaceAll(stopword, "");
        }
        return newText;
    }

    public static String[] stemAllWords(String text) {
        Stemmer stemmer = new Stemmer();
        String[] textWords = text.split("[ \n\t]+");
        for (int i = 0; i < textWords.length; i++) {
            stemmer.add(textWords[i].toCharArray(), textWords[i].length());
            stemmer.stem();
            textWords[i] = stemmer.toString();
        }
        return textWords;
    }
}

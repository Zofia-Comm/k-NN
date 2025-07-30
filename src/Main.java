import java.io.*;
import java.util.*;

public class Main {

    BufferedReader bufferedReaderData;
    BufferedReader bufferedReaderTest;

    public static void main(String[] args) {
        Main main = new Main();
    }
    double distance (List<Double> data, List<Double> test) {
        double sum = 0;
        for (int k = 0; k < data.size(); k++) {
            double diff = data.get(k) - test.get(k);
            sum+=Math.pow(diff,2);
        }
        sum=Math.sqrt(sum);
        return sum;
    }
    int[] predict (List<List<Double>> data, List<Double> test, int klasyfikator) {

        int [] arrayOfDiff = new int[klasyfikator];
        for (int k = 0; k < klasyfikator; k++) {
            arrayOfDiff[k] = k;
        }
        for (int i = klasyfikator; i < data.size(); i++) {//zaczynamy od k+1
            double dis = distance(data.get(i), test);	  //dystans dla obecnego i od test
            for (int k = 0; k < klasyfikator; k++) {	  //przechodzimy po arrayOfDiff i podmieniamy indeksy
                double diff = distance(data.get(arrayOfDiff[k]), test);
                boolean inDiff = false;
                for(int j = 0; j<klasyfikator;j++){
                    if (i==arrayOfDiff[j]) {
                        inDiff = true;
                    }
                }
                if(dis<diff&& !inDiff){
                    arrayOfDiff[k] = i;
                }
            }
        }
        return arrayOfDiff;
    }

    {

        try {
            bufferedReaderData = new BufferedReader(new FileReader("Z:\\NAI\\miniProjekt01\\src\\iris.data"));
            bufferedReaderTest = new BufferedReader(new FileReader("Z:\\NAI\\miniProjekt01\\src\\iris.test.data"));
            Map<String,Integer> valToSpecies = new HashMap<>();
            valToSpecies.put("Iris-versicolor", 0);
            valToSpecies.put("Iris-virginica", 0);
            valToSpecies.put("Iris-setosa", 0);

            List<List<Double>> textToData = new ArrayList<>();
            List<List<Double>> testToValues = new ArrayList<>();
            List<String > typeData = new ArrayList<>();
            List<String> testType = new ArrayList<>();
            while (bufferedReaderData.ready()){
                String [] text = bufferedReaderData.readLine().split(",");
                List<Double> tmp = new ArrayList<>();
                for (int i = 0; i < 4; i++){
                    tmp.add(Double.parseDouble(text[i]));
                }
                textToData.add(tmp);
                typeData.add(text[4]);
            }
            while (bufferedReaderTest.ready()){
                String [] text = bufferedReaderTest.readLine().split(",");
                List<Double> tmp = new ArrayList<>();
                for (int i = 0; i < 4; i++){
                    tmp.add(Double.parseDouble(text[i]));
                }
                testToValues.add(tmp);
                testType.add(text[4]);
            }

            int total;
            int accuracy = 0;
            for (int j = 0; j < testToValues.size(); j++) {
                int[] arrayOfDiff = predict(textToData, testToValues.get(j), 8);

                for (int i = 0; i < arrayOfDiff.length; i++) {
                    switch (typeData.get(arrayOfDiff[i])) {
                        case "Iris-setosa":
                            valToSpecies.put("Iris-setosa", valToSpecies.get("Iris-setosa") + 1);
                            break;
                        case "Iris-versicolor":
                            valToSpecies.put("Iris-versicolor", valToSpecies.get("Iris-versicolor") + 1);
                            break;
                        case "Iris-virginica":
                            valToSpecies.put("Iris-virginica", valToSpecies.get("Iris-virginica") + 1);
                            break;
                    }
                    System.out.println(typeData.get(arrayOfDiff[i]) + " " + arrayOfDiff[i]);

                }
                String max = "Iris-setosa";
                if (valToSpecies.get("Iris-setosa") < valToSpecies.get("Iris-versicolor")) {
                    max = "Iris-versicolor";
                    if (valToSpecies.get("Iris-versicolor") < valToSpecies.get("Iris-virginica")) {
                        max = "Iris-virginica";
                    }
                } else if (valToSpecies.get("Iris-setosa") < valToSpecies.get("Iris-virginica")) {
                    max = "Iris-virginica";
                    if (valToSpecies.get("Iris-virginica") < valToSpecies.get("Iris-versicolor")) {
                        max = "Iris-versicolor";
                    }
                }
                if (max.equals(testType.get(j))) {
                    accuracy++;
                }
                System.out.println("gatunek: " + max);
                valToSpecies.clear();
                valToSpecies.put("Iris-versicolor", 0);
                valToSpecies.put("Iris-virginica", 0);
                valToSpecies.put("Iris-setosa", 0);
            }
            total = testType.size();
            double per = ((double) accuracy / total) * 100;
            System.out.println("dokładność: " + per + "%");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("I/O Error");
        }
    }
}
package us.mattgreen;

import java.text.DecimalFormat;
import java.util.*;

public class Main {

    private final static FileInput cardAccts = new FileInput("movie_cards.csv");
    private static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        String line;
        String[] fields;
        double avg;
        int[] nums = new int[2];

        DecimalFormat df2 = new DecimalFormat("#.##");

        FileOutput writeRating = new FileOutput("movie_ratings_new.csv");
        System.out.format("%8s  %-18s %6s %6s %6s\n", "Account", "Name", "Movies", "Points", "Avg Rating");
        while ((line = cardAccts.fileReadLine()) != null) {
            fields = line.split(",");
            findPurchases(fields[0], nums);
            avg = findAvgRating(fields[0]);
            System.out.format("00%6s  %-18s  %2d   %4d      %4s\n", fields[0], fields[1], nums[0], nums[1], df2.format(avg));
            writeRating.fileWrite(
                    String.join(
                            ",",
                            new String[]{
                                    fields[0],
                                    fields[1],
                                    "" + nums[0],
                                    "" + nums[1],
                                    "" + avg
                            }
                    )
            );
        }
        writeRating.fileClose();

        countRatings();




        /*
         so question 8 is extremely ambiguous and there is nothing in blackboard saying it shouldnt be done-
         even though you implied we didnt have to. Heck, it wasnt even on blackboard until recently, soooo....

         i am interpreting question 8 to this:
         1) Modify “FileIOAlgorithmTwo” to create a CSV output file that includes [the previous] fields that are printed out.
         2) Sort [movie_rating.csv] by Movie Ratings – Look it up several ways to get it done.
         3) [Format a new file to look like this:]

         Rating Count
           1       22
           2       41
           3       64
        */


    }

    private static void countRatings() {
        ArrayList<String[]> outString = new ArrayList<>();
        String line;

        FileInput ratingFileIn = new FileInput("movie_rating.csv");
        while ((line = ratingFileIn.fileReadLine()) != null) {
            outString.add(line.split(","));
        }
        ratingFileIn.fileClose();

        outString.sort((Comparator.comparing(o -> o[1])));

        ArrayList<Integer> countArray = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
        ArrayList<String> valueArray = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        for (String[] anOutString : outString) {
            for (int j = 0; j < valueArray.size(); j++) {
                if (anOutString[1].equals(valueArray.get(j))) {
                    countArray.set(j, countArray.get(j) + 1);
                    break;
                }
            }
        }
        FileOutput fileCountOut = new FileOutput("movie_rating_count.csv");

        for (int i = 0; i < valueArray.size(); i++) {
            fileCountOut.fileWrite(
                    String.join(
                            ",",
                            new String[]{valueArray.get(i), "" + countArray.get(i)}
                    )
            );
        }
        fileCountOut.fileClose();
    }


    private static double findAvgRating(String acct) {
        FileInput cardRatings = new FileInput("movie_rating.csv");
        int ratingCount = 0;
        int ratingTotal = 0;
        String line;
        String[] fields;
        while ((line = cardRatings.fileReadLine()) != null) {
            fields = line.split(",");
            if (fields[0].equals(acct)) {
                ratingCount++;
                ratingTotal += Integer.parseInt(fields[1]);
            }
        }
        if (ratingCount > 0) {
            return (double) ratingTotal / ratingCount;
        }
        return 0;
    }

    private static void findPurchases(String acct, int[] nums) {
        FileInput cardPurchases = new FileInput("movie_purchases.csv");
        nums[0] = 0;
        nums[1] = 0;
        String line;
        String[] fields;
        while ((line = cardPurchases.fileReadLine()) != null) {
            fields = line.split(",");
            if (fields[0].equals(acct)) {
                nums[0]++;
                nums[1] += Integer.parseInt(fields[2]);
            }
        }

    }
}
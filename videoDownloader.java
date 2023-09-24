package url;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java. lang. Runtime;

public class videoDownloader {
    private static final String UT_ID = "1";
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner keyboard = new Scanner(System.in);
        String choice;

        while (true) {
            System.out.println("Would you like to download through a chunklist.m3u8 link or UT ID?");
            System.out.println("1. UT ID (only works with lecturecapture.la.utexas.edu links)");
            System.out.println("2. chunklist.m3u8 link (works with any chunklist.m3u8 link)");
            System.out.print("Enter a number (1/2): ");
            choice = keyboard.nextLine();

            if (choice.equals("1") || choice.equals("2")) {
                break; //exit the loop if user input is 1 or 2
            } else {
                System.out.println("Invalid choice. Please select 1 or 2.");
            }
        }

        // Rest of the code here
        if(choice.equals(UT_ID)) {
            enterID(keyboard);
            // further UT ID related operations.
        }
        else {
            enterLink(keyboard);
        }
    }

    private static void enterLink(Scanner keyboard) throws IOException {
        PrintStream output = new PrintStream(System.getProperty("user.dir") + "\\downloaded clips\\videoFileNames.txt");
        System.out.print("Enter chunklist.m3u8 link: ");
        String link = keyboard.nextLine();
        System.out.println("Obtaining video content...");
        String videoFile = "";
        URL video = new URL(link);
        BufferedReader videoScanner = new BufferedReader(
                new InputStreamReader(video.openStream()));
        for (int i = 0; i < 4; i++) {
            videoScanner.readLine();
        }
        while (!videoFile.equals("#EXT-X-ENDLIST")) {
            for (int i = 0; i < 2; i++) {
                if (!videoFile.equals("#EXT-X-ENDLIST")) {
                    videoFile = videoScanner.readLine();
                }
            }
            if (!videoFile.equals("#EXT-X-ENDLIST")) {
                output.println("file '" + videoFile + "'");
                int lastIndex = link.lastIndexOf("/");
                link = link.substring(0, lastIndex + 1) + videoFile;
                File out = new File(System.getProperty("user.dir")+ "\\downloaded clips\\" + videoFile);
                String filename = videoFile;
                new Thread(new Download(link, out, filename)).start();
                System.out.println("Successfully Downloaded: " + filename);
            }
        }
    }

    public static void enterID(Scanner keyboard) throws IOException {
        String t = "";
        PrintStream output = new PrintStream(System.getProperty("user.dir") + "\\downloaded clips\\videoFileNames.txt");
        System.out.print("Enter UT Video ID: ");
        String utVideoId = keyboard.nextLine();
        String link;
        System.out.println("Processing video...");
        link = "https://streaming-lectures.la.utexas.edu/lo/smil:engage-player_" + utVideoId + "_presentation.smil/playlist.m3u8";
        URL oracle = new URL(link);
        BufferedReader playlistScanner = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        while (t == null || !t.startsWith("chunklist")) {
            t = playlistScanner.readLine();
        }
        System.out.println("Obtaining video content...");
        String videoFile = "";
        link = "https://streaming-lectures.la.utexas.edu/lo/smil:engage-player_" + utVideoId + "_presentation.smil/" + t;
        URL videoChunk = new URL(link);
        BufferedReader oldvideoScanner = new BufferedReader(
                new InputStreamReader(videoChunk.openStream()));
        while (!videoFile.startsWith("media")) {
            videoFile = oldvideoScanner.readLine();
        }
        link = "https://streaming-lectures.la.utexas.edu/lo/smil:engage-player_" + utVideoId + "_presentation.smil/" + t;
        URL video = new URL(link);
        BufferedReader videoScanner = new BufferedReader(
                new InputStreamReader(video.openStream()));
        for (int i = 0; i < 4; i++) {
            videoScanner.readLine();
        }
        while (!videoFile.equals("#EXT-X-ENDLIST")) {
            for (int i = 0; i < 2; i++) {
                if (!videoFile.equals("#EXT-X-ENDLIST")) {
                    videoFile = videoScanner.readLine();
                }
            }
            if (!videoFile.equals("#EXT-X-ENDLIST")) {
                output.println("file '" + videoFile + "'");
                link = "https://streaming-lectures.la.utexas.edu/lo/smil:engage-player_" + utVideoId + "_presentation.smil/" + videoFile;
                File out = new File(System.getProperty("user.dir")+ "\\downloaded clips\\" + videoFile);
                String filename = videoFile;
                new Thread(new Download(link, out, filename)).start();
                System.out.println("Successfully Downloaded: " + filename);
            }
        }
    }
}

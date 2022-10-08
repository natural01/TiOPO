import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class Main {

    public static String BASE_URL = "http://links.qatl.ru/";

    public static void main(String[] args) throws IOException {
        File output_failed_base = new File("D:\\university\\testing2\\lw2\\untitled\\src\\output_failed.txt");
        File output_passed_base = new File("D:\\university\\testing2\\lw2\\untitled\\src\\output_passed.txt");
        if (output_failed_base.createNewFile() && output_passed_base.createNewFile())
        {
        }
        FileWriter output_failed = new FileWriter(output_failed_base);
        FileWriter output_passed = new FileWriter(output_passed_base);
        int countOfFail = 0;
        int countOfPassed = 0;

        try {
            var doc = Jsoup.connect(BASE_URL).get();
            var links = doc.select("a");
            for (var link : links)
            {
                String urlStr = "" + link.attr("abs:href");
                if (urlStr.isEmpty())
                {
                    output_failed.write("empty url\n");
                    countOfFail++;
                    continue;
                }
                URL validUrl = new URL(urlStr);
                HttpURLConnection httpURLConnect=(HttpURLConnection)validUrl.openConnection();
                httpURLConnect.setConnectTimeout(5000);
                httpURLConnect.connect();
                if (httpURLConnect.getResponseCode() < 400)
                {
                    writePassed(output_passed, urlStr, httpURLConnect.getResponseCode());
                    countOfPassed++;
                }
                else
                {
                    writeFailed(output_failed, urlStr, httpURLConnect.getResponseCode());
                    countOfFail++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date dateNow = new Date();
        output_failed.write("Urls failed: " + countOfFail + " (" + dateNow + ")");
        output_passed.write("Urls passed: " + countOfPassed + " (" + dateNow + ")");
        output_failed.close();
        output_passed.close();
    }

    public static void writeFailed(FileWriter output_falid, String url, int returnCode) throws IOException
    {
        output_falid.write(url + " \tis FAILED with return code: " + returnCode + "\n");
    }

    public static void writePassed(FileWriter output_passed, String url, int returnCode) throws IOException
    {
        output_passed.write(url + " \tis PASSED with return code: " + returnCode + "\n");
    }
}
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Convertor {
    public static void main(String[] args) {
        // Исходный файл контактов
        List<String> rawFileContent = readFile("./../yamacoparser_res/simpleres.txt");
        if (rawFileContent == null) {
            System.exit(1);
        }

        List<String> csvConvertedFile = convert(rawFileContent);

        try (var writer = new PrintWriter(new FileWriter("./../yamacoparser_res/res.csv"))) {
            for (var contact : csvConvertedFile) {
                writer.println(contact);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println(csvConvertedFile.size() + " contacts was found");
    }

    private static List<String> convert(List<String> rawFileContent) {
        List<String> csvConvertedFile = new ArrayList<>();
        List<String> contactBlock = new ArrayList<>();

        for (var line : rawFileContent) {
            if (line.contains("ещё")) {
                if (csvConvertedFile.size() > 0) {
                    System.out.println("See lines " + csvConvertedFile.get(csvConvertedFile.size() - 1));
                }
                continue;
            }
            contactBlock.add(line);
            if (line.contains("@")) {
                switch (contactBlock.size()) {
                    case 2, 3 -> csvConvertedFile.add(contactBlock.get(0) + ';' + line);
                    default -> System.out.println("See lines " + contactBlock);
                }
                contactBlock.clear();
            }
        }
        return csvConvertedFile;
    }

    private static List<String> readFile(String filePath) {
        File rawFile = new File(filePath);
        if (!rawFile.exists()) {
            System.out.println("File " + filePath + "don't exist");
            return null;
        }

        List<String> rawFileContent = new ArrayList<>();

        try (var reader = new BufferedReader(new FileReader(rawFile))) {
            for (var line = reader.readLine(); line != null; ) {
                rawFileContent.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rawFileContent;
    }
}
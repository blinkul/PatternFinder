import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PatternFinder {

    public Map<String, Integer> findPatterns(List<String> sequence, int patternSize) {
        if (sequence.size() < patternSize) {
            return null;
        }

        Map<String, Integer> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sequence.size() - patternSize + 1; i++) {
            String group = appendNextElements(i, patternSize, sequence);
            map.compute(group, (K, V) -> {
                if (V == null) {
                    return 1;
                } else {
                    return V + 1;
                }
            });
        }

        return  map.entrySet().stream().filter((v) -> v.getValue() > 1).collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));

    }

    private String appendNextElements(int index, int elementsToAppend, List<String> elements) {
        StringBuilder sb = new StringBuilder(elementsToAppend);
        for (int i = 0 ; i < elementsToAppend ; i++) {
            sb.append(elements.get(index + i));
            //add delimiters
            if (i < elementsToAppend -1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

}

class Main {

    public static void main(String[] args) throws IOException {

        List<String> acceptableElements = Arrays.asList(
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25");

        boolean isTest = false;
        List<String> randomElements = new ArrayList<>();
        PatternFinder pf = new PatternFinder();
        Map<String, Integer> result = null;
        if (isTest) {
            loadTestData(randomElements);
            result = pf.findPatterns(randomElements, 3);
            for (Map.Entry<String, Integer> e : result.entrySet()) {
                System.out.println("Pattern " + e.getKey() + " found " + e.getValue() + " times.");
            }
        } else {
            Path outFile = Paths.get("saves", "output.txt");
            if (!Files.exists(outFile)) {
                Files.createFile(outFile);
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    FileWriter writer = new FileWriter(outFile.toAbsolutePath().toString(), true)) {

                System.out.print("\nInput the pattern length: ");
                int patternSize = Integer.valueOf(in.readLine());

                writer.write("\n" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                       + " ---PATTERN OF " + patternSize + "---------------------------------------------------------\n");
                writer.flush();

                while (true) {
                    System.out.print("\nEnter new record: ");
                    String record = in.readLine();
                    if (record.equals("EXIT")) {
                        break;
                    }
                    if (acceptableElements.contains(record)) {
                        randomElements.add(record);
                        writer.append("REC = " + record + "\n");
                        writer.flush();
                    } else {
                        System.out.println("Bad value. Try to add again.");
                    }
                    result = pf.findPatterns(randomElements, patternSize);
                    if (result != null) {
                        for (Map.Entry<String, Integer> e : result.entrySet()) {
                            System.out.println("Pattern " + e.getKey() + " found " + e.getValue() + " times.");
                            writer.write("Pattern found " + e.getValue() + " times = " + e.getKey() + "\n");
                            writer.flush();
                        }
                    }
                }
            }
        }
    }

    private static void loadTestData(List<String> randomElements) {

        List<String> elements = Arrays.asList(
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25");
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            randomElements.add(elements.get(random.nextInt(25)));
        }
        randomElements.set(12, "1");
        randomElements.set(13, "6");
        randomElements.set(14, "11");
        randomElements.set(15, "16");

        randomElements.set(45, "1");
        randomElements.set(46, "6");
        randomElements.set(47, "11");
        randomElements.set(48, "16");

        randomElements.set(2, "5");
        randomElements.set(3, "10");
        randomElements.set(4, "15");
        randomElements.set(5, "20");

        randomElements.set(79, "5");
        randomElements.set(80, "10");
        randomElements.set(81, "15");
        randomElements.set(82, "20");
    }

}
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileService {
    private final Path baseFolder;

    public FileService(String folderName) throws IOException {
        this.baseFolder = Paths.get(folderName).toAbsolutePath().normalize();

        if (!Files.exists(baseFolder)) {
            Files.createDirectories(baseFolder);
        }
    }
    
        private Path safeResolve(String fileName) throws IOException {
        Path resolved = baseFolder.resolve(fileName).normalize();
        if (!resolved.startsWith(baseFolder)) {
            throw new IOException("Qasje e palejuar.");
        }
        return resolved;
    }

    public String listFiles() throws IOException {
        StringBuilder sb = new StringBuilder();
        Files.list(baseFolder).forEach(path -> sb.append(path.getFileName()).append("\n"));

        if (sb.length() == 0) {
            return "Folderi eshte bosh.";
        }

        return sb.toString();
    }
        public String readFile(String fileName) throws IOException {
        Path file = safeResolve(fileName);

        if (!Files.exists(file)) {
            return "File nuk ekziston.";
        }

        if (Files.isDirectory(file)) {
            return "Ky eshte folder, jo file.";
        }

        return Files.readString(file, StandardCharsets.UTF_8);
    }

    public String writeFile(String fileName, String content) throws IOException {
        Path file = safeResolve(fileName);

        Files.writeString(
                file,
                content + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );

        return "Shkrimi ne file u krye me sukses.";
    }
        public String executeFile(String fileName) throws IOException, InterruptedException {
        Path file = safeResolve(fileName);

        if (!Files.exists(file)) {
            return "File nuk ekziston.";
        }

        if (Files.isDirectory(file)) {
            return "Nuk mund te ekzekutohet folderi.";
        }

        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb;

        if (os.contains("win")) {
            pb = new ProcessBuilder("cmd", "/c", file.toString());
        } else {
            pb = new ProcessBuilder("sh", file.toString());
        }

        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();

        return "Ekzekutimi perfundoi. Exit code = " + exitCode +
                (output.length() > 0 ? "\nOutput:\n" + output : "");
    }
}
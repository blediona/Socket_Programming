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
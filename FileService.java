public class FileService {
    private final Path baseFolder;

    public FileService(String folderName) throws IOException {
        this.baseFolder = Paths.get(folderName).toAbsolutePath().normalize();

        if (!Files.exists(baseFolder)) {
            Files.createDirectories(baseFolder);
        }
    }
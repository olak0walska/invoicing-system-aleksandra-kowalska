package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import pl.futurecollars.invoicing.utils.FileService;

public class IdService {

  private final FileService fileService;
  private final Path path;
  private int nextId = 1;

  public IdService(Path path, FileService fileService) {
    this.path = path;
    this.fileService = fileService;

    try {
      List<String> lines = fileService.readAllLines(path);
      if (lines.isEmpty()) {
        fileService.appendLineToFile(path, "1");
      } else {
        nextId = Integer.parseInt(lines.get(0));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public int getNextIdAndIncrement() {
    try {
      fileService.writeLineToFile(path, String.valueOf(nextId + 1));
      return this.nextId++;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}

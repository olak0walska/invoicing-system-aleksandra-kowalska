package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

  private final Path dbPath;
  private final IdService idService;
  private final FileService fileService;
  private final JsonService jsonService;

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.getNextIdAndIncrement());
      fileService.appendLineToFile(dbPath, jsonService.toJson(invoice));
      return invoice.getId();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return fileService.readAllLines(dbPath)
          .stream()
          .map(json -> jsonService.toObject(json, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {

  }

  @Override
  public void delete(int id) {

  }
}

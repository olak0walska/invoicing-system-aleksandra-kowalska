package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
public class FileBasedDatabase<T extends WithId> implements Database<T> {

  private final Path dbPath;
  private final IdProvider idProvider;
  private final FileService fileService;
  private final JsonService jsonService;
  private final Class<T> clazz;

  @Override
  public long save(T item) {
    try {
      item.setId(idProvider.getNextIdAndIncrement());
      fileService.appendLineToFile(dbPath, jsonService.toJson(item));

      return item.getId();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Optional<T> getById(long id) {
    try {
      return fileService.readAllLines(dbPath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.toObject(line, clazz))
          .findFirst();
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to get item with id: " + id, ex);
    }
  }

  @Override
  public List<T> getAll() {
    try {
      return fileService.readAllLines(dbPath)
          .stream()
          .map(line -> jsonService.toObject(line,clazz))
          .collect(Collectors.toList());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read items from file", ex);
    }
  }

  @Override
  public Optional<T> update(long id, T updatedItem) {
    try {
      List<String> allItems = fileService.readAllLines(dbPath);
      var itemsWithoutItemWithGivenId = allItems
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      updatedItem.setId(id);
      itemsWithoutItemWithGivenId.add(jsonService.toJson(updatedItem));

      fileService.writeLinesToFile(dbPath, itemsWithoutItemWithGivenId);

      allItems.removeAll(itemsWithoutItemWithGivenId);
      return allItems.isEmpty() ? Optional.empty()
          : Optional.of(jsonService.toObject(allItems.get(0), clazz));.toObject(allInvoices.get(0), Invoice.class));

    } catch (IOException ex) {
      throw new RuntimeException("Failed to update item with id: " + id, ex);
    }

  }

  @Override
  public Optional<T> delete(long id) {
    try {
      var allItems = fileService.readAllLines(dbPath);

      var invoicesExceptDeleted = allItems
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      fileService.writeLinesToFile(dbPath, invoicesExceptDeleted);

      allItems.removeAll(invoicesExceptDeleted);

      return allItems.isEmpty() ? Optional.empty() :
          Optional.of(jsonService.toObject(allItems.get(0), clazz));

    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete item with id: " + id, ex);
    }
  }

  private boolean containsId(String line, long id) {
    return line.contains("{\"id\":" + id + ",");
  }
}

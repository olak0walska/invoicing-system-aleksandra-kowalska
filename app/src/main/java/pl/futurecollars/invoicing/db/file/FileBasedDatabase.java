package pl.futurecollars.invoicing.db.file;

import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class FileBasedDatabase implements Database {
  @Override
  public int save(Invoice invoice) {
    return 0;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Invoice> getAll() {
    return null;
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {

  }

  @Override
  public void delete(int id) {

  }
}

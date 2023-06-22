package pl.futurecollars.invoicing.db.file

import  pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.utils.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    Path dbPath

    @Override
    Database getDatabaseInstance() {
        def fileService = new FileService()

        def idPath = File.createTempFile('ids', '.txt').toPath()
        def idService = new IdProvider(idPath, fileService)

        dbPath = File.createTempFile('invoices', '.txt').toPath()
        new FileBasedDatabase(dbPath, idService, fileService, new JsonService())
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        Files.readAllLines(dbPath).size() == 1

        when:
        db.save(TestHelpers.invoice(5))

        then:
        Files.readAllLines(dbPath).size() == 2
    }
}

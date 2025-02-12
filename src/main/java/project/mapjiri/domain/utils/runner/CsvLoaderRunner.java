package project.mapjiri.domain.utils.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import project.mapjiri.domain.utils.service.CsvLoaderService;

@Component
@ConditionalOnProperty(name = "csv.loader.enabled", havingValue = "true")
@RequiredArgsConstructor
public class CsvLoaderRunner implements CommandLineRunner {

    private final CsvLoaderService csvLoaderService;

    @Override
    public void run(String... args) throws Exception {
        csvLoaderService.loadPlacesFromCsv();
        csvLoaderService.loadMenusFromCsv();
    }
}

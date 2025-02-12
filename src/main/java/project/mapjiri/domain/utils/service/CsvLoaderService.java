package project.mapjiri.domain.utils.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mapjiri.domain.menu.model.Menu;
import project.mapjiri.domain.menu.repository.MenuRepository;
import project.mapjiri.domain.place.model.Place;
import project.mapjiri.domain.place.repository.PlaceRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
@RequiredArgsConstructor
public class CsvLoaderService {

    private final PlaceRepository placeRepository;
    private final MenuRepository menuRepository;

    public void loadPlacesFromCsv() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/places.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV 파일: places.csv 를 찾을 수 없습니다.");
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("gu", "dong")
                .setSkipHeaderRecord(true)
                .build();
        CSVParser csvParser = new CSVParser(reader, csvFormat);

        for (CSVRecord record : csvParser) {
            String gu = record.get("gu");
            String dong = record.get("dong");

            if (placeRepository.existsByGuAndDong(gu, dong)) {
                continue;
            }

            Place place = new Place(gu, dong);
            placeRepository.save(place);
        }

    }

    public void loadMenusFromCsv() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("/menus.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV 파일: menus.csv 를 찾을 수 없습니다.");
        }
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("type", "name")
                .setSkipHeaderRecord(true)
                .build();
        CSVParser csvParser = new CSVParser(reader, csvFormat);

        for (CSVRecord record : csvParser) {
            String menuType = record.get("type");
            String menuName= record.get("name");

            if (menuRepository.existsByMenuTypeAndMenuName(menuType, menuName)) {
                continue;
            }

            Menu menu = new Menu(menuType, menuName);
            menuRepository.save(menu);
        }

    }

}


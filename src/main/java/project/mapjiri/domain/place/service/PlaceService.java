package project.mapjiri.domain.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.place.repository.PlaceRepository;
import project.mapjiri.global.dto.ResponseDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public List<String> getGuName(){
        List<String> guNameList = placeRepository.findDistinctGu();
        if (guNameList.isEmpty()) {
            throw new MyException(MyErrorCode.NOT_FOUND_PLACE);
        }
        return guNameList;
    }

    public List<String> getDongName(String gu){
        List<String> dongNameList = placeRepository.findDongByGu(gu);
        if (dongNameList.isEmpty()) {
            throw new MyException(MyErrorCode.NOT_FOUND_PLACE);
        }
        return dongNameList;
    }
}

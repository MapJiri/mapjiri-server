package project.mapjiri.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.mapjiri.global.dto.ResponseDto;

import java.net.URISyntaxException;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Integer>> handleConstraintViolationException(ConstraintViolationException ex){
        log.error("유효성 검사 예외 발생 msg:{}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,ex.getMessage()));
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ResponseDto<Integer>> MyException(MyException ex){
        log.error("예외 발생 msg:{}",ex.getErrorCode().getMessage());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(ResponseDto.of(-1,ex.getErrorCode().getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Integer>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getDefaultMessage();
        log.error("유효성 검사 예외 발생 msg:{}",message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,message));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto<Integer>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "요청한 JSON 데이터를 읽을 수 없습니다: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1,errorMessage));
    }

    @ExceptionHandler(URISyntaxException.class)
    public ResponseEntity<ResponseDto<Integer>> handleURISyntaxException(URISyntaxException ex) {
        String errorMessage = "잘못된 요청 URI 형식입니다: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.of(-1, errorMessage));
    }

}


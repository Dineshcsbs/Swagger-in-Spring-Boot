package com.swagger.exception;



import com.swagger.dto.ResponseDTO;
import com.swagger.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestServiceAlertException.class)
    public ResponseEntity<ResponseDTO> handleBadRequestServiceAlertException(BadRequestServiceAlertException exception, WebRequest request) {
        ResponseDTO responseDTO = new ResponseDTO();
        exception.printStackTrace();
        responseDTO.setStatusCode(400);
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());
        return ResponseEntity.ok().body(responseDTO);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleSecurityException(Exception exception) {
        ResponseDTO responseDTO = new ResponseDTO();

        // TODO send this stack trace to an observability tool
        exception.printStackTrace();

        responseDTO.setStatusCode(400);
        responseDTO.setMessage(Constants.NOT_FOUND);
        responseDTO.setData(exception.getMessage());


        return ResponseEntity.badRequest().body(responseDTO);


    }

}
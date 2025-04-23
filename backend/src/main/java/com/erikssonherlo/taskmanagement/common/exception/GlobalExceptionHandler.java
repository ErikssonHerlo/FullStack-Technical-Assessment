package com.erikssonherlo.taskmanagement.common.exception;

import com.erikssonherlo.taskmanagement.common.payload.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                         WebRequest webRequest){

        Map<String, String> mapErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String key = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            mapErrors.put(key, value);
        });
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(),mapErrors.toString(), HttpStatus.BAD_REQUEST,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handlerInternalErrorException(BadRequestException exception,
                                                                     WebRequest webRequest){
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(),exception.getMessage(), HttpStatus.BAD_REQUEST,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerException(Exception exception,
                                                        WebRequest webRequest){
        ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerUserNotFoundException(ResourceNotFoundException exception,
                                                                    WebRequest webRequest){
        ApiResponse apiResponse = new ApiResponse(HttpStatus.NOT_FOUND.value(),exception.getMessage(), HttpStatus.NOT_FOUND,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handlerUserAlreadyExistsException(ResourceAlreadyExistsException exception,
                                                                         WebRequest webRequest){
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CONFLICT.value(),exception.getMessage(), HttpStatus.CONFLICT,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(),"Invalid value for parameter '" + ex.getName() + "'. Expected a " + ex.getRequiredType().getSimpleName(), HttpStatus.BAD_REQUEST,null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiResponse> handleInvalidFormatException(InvalidFormatException ex) {
        String message = "Invalid value for enum: " + ex.getValue();
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), message, HttpStatus.BAD_REQUEST, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), HttpStatus.BAD_REQUEST, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());  // Convertimos HttpStatusCode a HttpStatus

        ApiResponse apiResponse = new ApiResponse(ex.getStatusCode().value(), ex.getReason(), status, null);
        return new ResponseEntity<>(apiResponse, ex.getStatusCode());
    }

    // Manejo de Expired JWT
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Token has expired",
                HttpStatus.UNAUTHORIZED,
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    // Manejo genérico para otras excepciones JWT
    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(io.jsonwebtoken.JwtException ex, WebRequest request) {
        ApiResponse apiResponse = new ApiResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid JWT token",
                HttpStatus.UNAUTHORIZED,
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password", HttpStatus.UNAUTHORIZED, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
}

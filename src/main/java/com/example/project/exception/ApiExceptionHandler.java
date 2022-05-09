package com.example.project.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ValidationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<?> handleApiRequestException(ApiRequestException e) {

        ApiException apiException = new ApiException(e.getMessage(),
                e.getHttpStatus(),
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle Form Validation Error Exception */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(BindException ex) {

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        ApiException apiException = new ApiException(fieldErrors.get(0).getDefaultMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle Form Validation Error Exception While Uploading File */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartResolutionFailException() {

        ApiException apiException = new ApiException("Please select a file to upload",
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle Content Type Value While Submitting The Request */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException() {

        ApiException apiException = new ApiException("Please add the right content type while submitting the request",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle Invalid Query Param Exception */
    @ExceptionHandler({PropertyReferenceException.class, ArrayIndexOutOfBoundsException.class})
    public ResponseEntity<?> handlePropertyReferenceExceptionAndArrayIndexOutOfBoundsException() {

        ApiException apiException = new ApiException("Invalid query param arguments. It should be (sort=attribute,order) while using sort in request param",
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle Invalid method while submitting the request Exception */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {

        ApiException apiException = new ApiException(ex.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle referential integrity exception event if the item has relationship with another item while submitting the request */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException() {

        ApiException apiException = new ApiException("Referential integrity exception that maintains the relationship of data across multiple tables for consistency purposes",
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle exception while submitting wrong key value pairs fields request body */
    @ExceptionHandler({ValidationException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> validationException() {

        ApiException apiException = new ApiException("Please provide correct json key pairs fields while submitting request to server",
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle referential integrity exception event if the item has relationship with another item while submitting the request */
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<?> badCredentialsException() {

        ApiException apiException = new ApiException("Invalid credentials",
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle password complexity */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (errors.containsKey(error.getField()))
                errors.put(error.getField(), String.format("%s, %s", errors.get(error.getField()), error.getDefaultMessage()));
            else
                errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /* Handle mistype of path param */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> HandleMethodArgumentTypeMismatchException() {

        ApiException apiException = new ApiException("Please check the correct type of path param",
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }

    /* Handle signal errors from database while calling the procedure to check the quantity product in stock */
    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<?> handleSqlExceptionHelper(JpaSystemException ex) {

        ApiException apiException = new ApiException(ex.getCause().getCause().getLocalizedMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("GMT")));

        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }


}

package com.priyanshu.elearningpriyanshu.exceptions;

import com.priyanshu.elearningpriyanshu.model.Response;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<String>> handleCustomException(CustomException ce){
        Response<String> userResponse=new Response<>();
        userResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        userResponse.setData(ce.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<Response<String>> handleCustomException(OperationNotAllowedException ce){
        Response<String> userResponse=new Response<>();
        userResponse.setStatus(HttpStatus.EXPECTATION_FAILED.value());
        userResponse.setData(ce.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<String>> handleResourceNotFoundException(ResourceNotFoundException re){
        Response<String> userResponse=new Response<>();
        userResponse.setStatus(HttpStatus.NOT_FOUND.value());
        userResponse.setData(re.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Response<String>> handleResourceNotFoundException(ResourceAlreadyExistsException re){
        Response<String> userResponse=new Response<>();
        userResponse.setStatus(HttpStatus.CONFLICT.value());
        userResponse.setData(re.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<String>> handleConstraintViolationException(ConstraintViolationException ce){
        Response<String> userResponse = new Response<>();
        userResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        userResponse.setData(ce.getMessage());
        log.error(userResponse.toString());
        return ResponseEntity.ok(userResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest){
        Map<String,String> validationErrors=new HashMap<>();
        /*
         * It will give us all validation errors that failed in the input data.
         * */
        List<ObjectError> validationErrorList=ex.getBindingResult().getAllErrors();

        validationErrorList.forEach(
                (error)->{
                    String fieldName=((FieldError) error).getField(); // field name
                    String validationMessage=error.getDefaultMessage(); // validation message related to the field
                    validationErrors.put(fieldName,validationMessage);
                }
        );
        log.info("validation errors -"+validationErrors.toString());

        return new ResponseEntity<>(new Response<>(HttpStatus.BAD_REQUEST.value(),validationErrors),HttpStatus.OK);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Response<String>> handleUnAuthException(ForbiddenException fe){
        Response<String> userResponse=new Response<>();

        userResponse.setStatus(HttpStatus.FORBIDDEN.value());
        userResponse.setData(fe.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }


    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Response<String>> handleUnAuthException(UnAuthorizedException ue){
        Response<String> userResponse=new Response<>();

        userResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        userResponse.setData(ue.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<String>> handleRunTimeException(RuntimeException runtimeException){
        Response<String> userResponse=new Response<>();

        userResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        userResponse.setData(runtimeException.getMessage());

        log.error(userResponse.getData());
        return ResponseEntity.ok().body(userResponse);
    }
}

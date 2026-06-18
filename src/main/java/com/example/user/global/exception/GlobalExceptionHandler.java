package com.example.user.global.exception;

import com.example.user.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<String>> badRequestHandleException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail("[ERROR: Request/BadRequest] " + e.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<String>> invalidPasswordHandleException(InvalidPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail("[ERROR: User/Auth/InvalidPassword] " + e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> userNotFoundHandleException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail("[ERROR: User/NotFound] " + e.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiResponse<String>> duplicateEmailHandleException(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail("[ERROR: User/Email/Duplicate] " + e.getMessage()));
    }

    @ExceptionHandler(DeletedUserException.class)
    public ResponseEntity<ApiResponse<String>> deletedUserHandleException(DeletedUserException e) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(ApiResponse.fail("[ERROR: User/Deleted] " + e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<String>> userHandleException(UserException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("[ERROR: User/?] " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> globalHandleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail("[ERROR: ?/?] 서버 내부 오류가 발생했습니다."));
    }
}

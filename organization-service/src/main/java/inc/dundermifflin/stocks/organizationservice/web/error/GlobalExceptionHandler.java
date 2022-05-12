package inc.dundermifflin.stocks.organizationservice.web.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@ControllerAdvice
@Order
@Slf4j
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onException(Exception e, HttpServletRequest request) {
        log.error("Exception while handling request", e);
        var errorMessages = Set.of(e.getMessage());
        ErrorResponse errorModel = ErrorResponse.builder()
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.internalServerError()
                .body(errorModel);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> onIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.warn("Illegal state occurred: {}", e.getMessage());
        var errorMessages = Set.of(e.getMessage());
        ErrorResponse errorModel = ErrorResponse.builder()
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorModel);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> onMissingServletRequestParameterException(
            MissingServletRequestParameterException e,
            HttpServletRequest request) {
        String message = "Parameter: '" + e.getParameterName() + "' of type " + e.getParameterType() +
                " is required but is missing";
        log.warn("Exception while handling request: {}", message);
        var errorMessages = Set.of(message);
        ErrorResponse errorModel = ErrorResponse.builder()
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorModel);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                           HttpServletRequest request) {
        log.warn("Exception while handling request: {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        Set<String> errorMessages = new HashSet<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            String resolvedMessage = messageSource.getMessage(error, Locale.US);
            if (error instanceof FieldError) {
                errorMessages.add(
                        String.format("Field '%s' %s but value was '%s'", ((FieldError) error).getField(), resolvedMessage,
                                ((FieldError) error).getRejectedValue()));
            } else {
                errorMessages.add(resolvedMessage);
            }
        }
        ErrorResponse errorModel = ErrorResponse.builder()
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorModel);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                               HttpServletRequest request) {
        MethodParameter parameter = e.getParameter();
        String message = "Parameter: '" + parameter.getParameterName() + "' is not valid. " +
                "Value '" + e.getValue() + "' could not be bound to type: '" + parameter.getParameterType().getSimpleName().toLowerCase(Locale.ROOT) + "'";
        log.warn("Exception while handling request: {}", message);
        var errorMessages = Set.of(message);
        ErrorResponse errorModel = ErrorResponse.builder()
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorModel);
    }

}

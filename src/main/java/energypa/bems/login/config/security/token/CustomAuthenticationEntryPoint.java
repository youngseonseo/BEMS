package energypa.bems.login.config.security.token;


import com.fasterxml.jackson.databind.ObjectMapper;
import energypa.bems.login.advice.payload.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if(request.getAttribute("error") != null) {
            log.info("인증 에러1");
            ErrorCode error = (ErrorCode) request.getAttribute("error");
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, error.getCode());
            setResponse(request,response,error);
        }else {
            log.info("인증 에러2");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
        }
    }
    private void setResponse(HttpServletRequest request, HttpServletResponse response, ErrorCode error) throws IOException {
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        final Map<String, Object> body = new HashMap<>();
        body.put("timestamp", format);
        body.put("status", error.getCode());
        body.put("message", error.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(401);
        mapper.writeValue(response.getOutputStream(), body);
    }
}
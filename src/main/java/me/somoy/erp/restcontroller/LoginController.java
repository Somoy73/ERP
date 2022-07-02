package me.somoy.erp.restcontroller;


import me.somoy.erp.model.user.CurrentUser;
import me.somoy.erp.model.user.User;
import me.somoy.erp.utils.error.ApiError;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;


@RestController

public class LoginController {


    @RequestMapping(value="api/login", method = RequestMethod.POST)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    Map<String, Object> handleLogin(@CurrentUser User loggedInUser){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", loggedInUser.getId());
        userInfo.put("username", loggedInUser.getUsername());
        userInfo.put("email", loggedInUser.getEmail());
        userInfo.put("accessLevel", loggedInUser.getAccessLevel());
        userInfo.put("Authorities", loggedInUser.getAuthorities());
        return userInfo;
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
    ApiError handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(401, "Unauthorized", request.getServletPath());
        return apiError;
    }
}

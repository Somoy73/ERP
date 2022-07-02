package me.somoy.erp.restcontroller;

import me.somoy.erp.model.user.User;
import me.somoy.erp.service.UserService;
import me.somoy.erp.utils.error.ApiError;
import me.somoy.erp.utils.exception.DuplicateDataException;
import me.somoy.erp.utils.exception.EmployeeAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "api/register", method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) throws EmployeeAlreadyExistsException {
        userService.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }

    @RequestMapping(value= "api/users", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> getAllUser() {
        return new ResponseEntity<>(userService.getRepository().findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.GET)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> getUserById(@PathVariable("id") String id) {
        return new ResponseEntity<>(userService.getUserById(Long.parseLong(id)), HttpStatus.OK);
    }

    @RequestMapping(value="api/edit/{id}", method = RequestMethod.PUT)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> updateUser(@PathVariable("id") String id,@Valid @RequestBody User user){
       User userUpdated = userService.getUserById(Long.parseLong(id));
       if(userUpdated != null){
           userUpdated.setUsername(user.getUsername());
           userUpdated.setDateOfJoining(user.getDateOfJoining());
           userUpdated.setAddress(user.getAddress());
           userUpdated.setEmail(user.getEmail());
       }
       return new ResponseEntity<>(userService.getUserById(Long.parseLong(id)), HttpStatus.OK);
    }

    @RequestMapping(value="api/delete/{id}", method=RequestMethod.DELETE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String id){
        userService.deleteUserById(Long.parseLong(id));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        ApiError apiError = new ApiError(400, "Validation error", request.getServletPath());
//        BindingResult result = ex.getBindingResult();
//        Map<String, String> validationError = new HashMap<>();
//
//        for(FieldError fieldError: result.getFieldErrors()) {
//            validationError.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        apiError.setValidationErrors(validationError);
//        return apiError;
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateDataException.class)
    public ApiError handleDuplicateDataException(DuplicateDataException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(400, "Duplicate Data", request.getServletPath());
        Map<String, String> validationError = new HashMap<>();
        validationError.put("message", ex.getMessage());
        apiError.setValidationErrors(validationError);
        return apiError;
    }

}

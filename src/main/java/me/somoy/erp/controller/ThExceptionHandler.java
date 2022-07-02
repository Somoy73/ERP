package me.somoy.erp.controller;

import me.somoy.erp.utils.exception.AccessLevelLowException;
import me.somoy.erp.utils.exception.EmployeeAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ThExceptionHandler {
    @ExceptionHandler({Exception.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("exception", "");
        return "error";
    }

    @ExceptionHandler({AccessLevelLowException.class})
    public String handleAccessLevelLowException(AccessLevelLowException ex, Model model) {
        model.addAttribute("exception", "You are not authorized to perform this action");
        return "error";
    }
    @ExceptionHandler({EmployeeAlreadyExistsException.class})
    public String handleEmployeeAlreadyExistsException(Exception ex, Model model, HttpServletRequest request) {
        model.addAttribute("exception", "Employee Already Exists.");
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Model model, HttpServletRequest request) {
        model.addAttribute("exception", "Invalid Data.");
        return "error";
    }

}

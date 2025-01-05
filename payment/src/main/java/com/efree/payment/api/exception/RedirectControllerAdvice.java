package com.efree.payment.api.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RedirectControllerAdvice {

    @ExceptionHandler(RedirectToPageException.class)
    public String handleRedirectToPageException(RedirectToPageException ex, Model model) {
        // Add any data to the model if required
        model.addAttribute("errorMessage", "The requested order was not found.");
        return ex.getPage(); // This corresponds to the HTML page name
    }

}
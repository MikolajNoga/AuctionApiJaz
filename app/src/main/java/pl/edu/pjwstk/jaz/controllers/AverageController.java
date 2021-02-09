package pl.edu.pjwstk.jaz.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
public class AverageController {

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @GetMapping("average")
    public String getAverage(@RequestParam(value = "numbers", required = false) String numbers) {
        if (numbers == null) {
            return "Please put parameters";
        }
        String[] numbersArray = numbers.split(",");
        float sum = 0;
        for (int i = 0; i < numbersArray.length; i++) {
            sum += Float.parseFloat(numbersArray[i]);
        }
        BigDecimal average = new BigDecimal(sum / numbersArray.length);
        average = average.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
        return "Average equals: " + average;

    }
}

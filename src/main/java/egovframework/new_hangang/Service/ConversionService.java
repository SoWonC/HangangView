package egovframework.new_hangang.Service;

import org.springframework.stereotype.Service;

@Service
public class ConversionService {

    public double conversion(String String) {

        try {
            if (String == null || String.trim().isEmpty()) {
                throw new IllegalArgumentException("???? ?? ?? ?: " + String);
            }
            String[] parts = String.split("-");
            double degrees = Double.parseDouble(parts[0]);
            double minutes = Double.parseDouble(parts[1]);
            double seconds = Double.parseDouble(parts[2]);

            double decimalDegrees = degrees + (minutes / 60) + (seconds / 3600);
            if (String.contains("S")) {
                decimalDegrees *= -1;
            }
            return decimalDegrees;
        } catch (IllegalArgumentException e) {

        }
        return 0;
    }


}

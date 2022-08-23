package home;
import java.time.LocalDate;
import java.util.ArrayList;

public interface Tracker {

    public ArrayList generateReport(LocalDate startDate, LocalDate endDate);
    public void coaching();
}

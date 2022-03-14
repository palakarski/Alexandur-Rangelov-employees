import java.time.LocalDate;

public class Employee {
    private String id;
    private String projectId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Employee(String id,String projectId, LocalDate startDate,LocalDate endDate) {
        this.id = id;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "EmployeeID: "+ id+" "+"ProjectID: "+projectId+" "+"Start date: "+startDate+" "+"End date: "+endDate;
    }
}

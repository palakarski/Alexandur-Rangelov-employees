import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        HashMap<String, ArrayList<Employee>> map = new HashMap<>();
        CSVReader reader;
        TreeMap<Long,ArrayList<Employee>>timeline =  new TreeMap<>((o1, o2) -> (int) (o2-o1));
        DateTimeFormatter dtfInput = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()// For case-insensitive parsing
                .appendPattern("[d-M-uuuu[ H[:m[:s]]]]")
                .appendPattern("[uuuu-M-d[ H[:m[:s]]]]")
                .appendPattern("[uuuu/M/d[ H[:m[:s]]]]")
                .appendPattern("[d/M/uuuu[ H[:m[:s]]]]")
                .appendPattern("[d-MMM-uuuu[ H[:m[:s[.SSSSSS]]]]]")
                .appendPattern("[d-MMM-uu[ H[:m[:s[.SSSSSS]]]]]")
                .appendPattern("[uuuu-MMM-d[ H[:m[:s[.SSSSSS]]]]]")
                .appendPattern("[uu-MMM-d[ H[:m[:s[.SSSSSS]]]]]")
                .appendPattern("[MMMM d,uuuu[ H[:m[:s[.SSSSSS]]]]]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter(Locale.ENGLISH);
        new Frame();
        while(true){
            if(Frame.selectedFile!=null){
                break;
            }
            Thread.sleep(1000);
        }
        {
            try {
                reader = new CSVReader(new FileReader(Frame.selectedFile.getAbsolutePath()));
                List<String[]> list = reader.readAll();
                for (int i = 0; i < list.size(); i++) {
                    String[] e = list.get(i);
                    LocalDate start = LocalDate.parse(e[2],dtfInput);
                    LocalDate end;
                    if(e[3].equals("null")){
                        end = LocalDate.now();
                    }
                    else {
                        end = LocalDate.parse(e[3],dtfInput);

                    }
                    Employee emp = new Employee(e[0],e[1], start,end);
                    if(!map.containsKey(e[1])) {
                        map.put(e[1], new ArrayList<>());
                    }
                    map.get(e[1]).add(emp);
                }

                for (Map.Entry<String,ArrayList<Employee>> e : map.entrySet()) {
                        String e1 = e.getKey();
                    for (int j = 0; j < map.get(e1).size(); j++) {
                        Employee first = e.getValue().get(j);
                        for (int i = j+1; i < map.get(e1).size(); i++) {
                            if (map.get(e1).size() > j + 1 && map.get(e1).size() > 1) {
                                Employee second = e.getValue().get(i);
                                if (first.getStartDate().isBefore(second.getEndDate())) {
                                    LocalDate start = first.getStartDate().isAfter(second.getStartDate()) ? first.getStartDate() : second.getStartDate();
                                    LocalDate end = first.getEndDate().isAfter(second.getEndDate()) ? second.getEndDate() : first.getEndDate();
                                    long duration = ChronoUnit.DAYS.between(start, end);
                                    timeline.put(duration, new ArrayList<>());
                                    timeline.get(duration).add(first);
                                    timeline.get(duration).add(second);
                                }
                            }
                        }
                    }
                }
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<Long,ArrayList<Employee>> e : timeline.entrySet()) {
            System.out.println("Days on the project: "+e.getKey());
            System.out.println(e.getValue().get(0) +" \n"+ e.getValue().get(1));
           break;
        }
    }




}

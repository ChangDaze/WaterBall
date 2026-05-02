package v2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class RealDatabase implements Database {
    @Override
    public Employee getEmployeeById(int id) {

        int targetLine = id + 1;
        String line = null ;
        try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            {
                int currentLine = 0;
                while ((line = br.readLine()) != null){
                    currentLine ++;
                    if(currentLine == targetLine){
                        System.out.println(line);
                        break;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        if(line != null){
            String[] parts = line.split(" ");
            if(parts.length == 4){
                return new RealEmployee(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        Integer.parseInt(parts[2]),
                        Arrays.stream(parts[3].split(",")).map(Integer::parseInt).toList(),
                        this);
            }else {
                return new RealEmployee(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        Integer.parseInt(parts[2]),
                        null,
                        this);
            }
        }

        return null;
    }
}

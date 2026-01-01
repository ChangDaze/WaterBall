package PrescriptionHandler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Prescription {
    private String name;
    private String potentialDisease;
    private List<String> medicines;
    private String usage;

    @Override
    public String toString() {
        return "{ "+
                "name='" + name + '\'' +
                ", potentialDisease='" + potentialDisease + '\'' +
                ", medicines='" + medicines + '\'' +
                ", usage=" + usage +
                " }";
    }
}

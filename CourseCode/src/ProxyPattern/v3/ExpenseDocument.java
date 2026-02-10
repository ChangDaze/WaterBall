package ProxyPattern.v3;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//單純用來字串轉物件的中繼型別的樣子
public class ExpenseDocument {
    public String id;
    public String description;
    public BigDecimal cost;
    public LocalDateTime createdTime;

    public Expense convert(){
        return new Expense(id, description,cost, createdTime);
    }
}

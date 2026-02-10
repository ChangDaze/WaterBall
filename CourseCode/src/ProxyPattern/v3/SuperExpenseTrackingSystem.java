package ProxyPattern.v3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SuperExpenseTrackingSystem implements ExpenseTrackingSystem {
    private static final String FILE_NAME = "expenses.json";
    private final List<Expense> expenses;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //設定ObjectMapper
    static {
        //預設情況下，Jackson 會把日期轉成一串數字（Timestamp）。關閉此功能後，日期會顯示為 ISO-8601 格式（如 2026-02-09），可讀性更高。
        MAPPER.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        //啟用 Pretty Printing。產出的 JSON 會自動縮排和換行，而不是全部擠成一行。
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        //Java 8 之後的日期 API（如 LocalDate, LocalDateTime）Jackson 原生並不支援。註冊這個模組後，Jackson 才能正確處理這些型別。
        MAPPER.registerModule(new JavaTimeModule());
    }

    public SuperExpenseTrackingSystem(){
        try {
            expenses = readExpenseFromFile();
            System.out.printf("[%d 筆帳目] ✓\n", expenses.size());
        }
        catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    private List<Expense> readExpenseFromFile() throws IOException{
        System.out.print("載入資料中......");
        return MAPPER.readValue(new File(FILE_NAME),
                new TypeReference<List<ExpenseDocument>>() {
                }).stream()
                .map(ExpenseDocument::convert).collect(toList());
    }

    @Override
    public List<Expense> getExpenses(){
        return new ArrayList<>(expenses); //應該是為了避免傳回參考影響原資料，所以回傳new
    }

    @Override
    public void addExpense(Expense expense) {
        expenses.add(expense);
        saveData();
    }

    @Override
    public void editExpense(Expense expense) {
        for(int i = 0; i< expenses.size(); i++) {
            if(expenses.get(i).getId().equals(expense.getId())){
                expenses.remove(i);
                expenses.add(i, expense);
                break;
            }
        }
        saveData();
    }

    @Override
    public void exportCSV(String filename){
        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader("id", "description", "cost", "createTime")
                .setAutoFlush(true).build();
        try(CSVPrinter printer = csvFormat.print(new File(filename), StandardCharsets.UTF_8))
        {
            for (Expense expense : expenses){
                printer.printRecord(
                        expense.getId(),
                        expense.getDescription(),
                        expense.getCost(),
                        expense.getCreatedTime().format(DateTimeFormatter.ISO_DATE));
            }

        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    //每次更新完會更新原始資料檔
    private void saveData(){
        try{
            MAPPER.writeValue(new File(FILE_NAME), expenses);
        }catch (IOException e){
            throw new IllegalStateException("Can't save data.");
        }
    }

}

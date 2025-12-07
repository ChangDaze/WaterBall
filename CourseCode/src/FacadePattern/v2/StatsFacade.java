package FacadePattern.v2;

import java.io.IOException;

public class StatsFacade {
    //這兩行其實就是標準的「位元旗標（bit flag）」專業寫法，只是你現在看到的是用「16 進位 + 位移運算」來寫，所以看起來比較抽象
    //0x 開頭代表：這是一個「16 進位數字（hexadecimal）」
    //0x01（16進位） = 1（10進位） = 0001（二進位）
    //<< 是「左位移運算子（left shift）」
    //0x01 << 1 = 0010（二進位）
    public static final int TOTAL_COST = 0x01;
    public  static final int COUNT_MEMBERS = 0x01 << 1;
    private final MarkdownParser parser = new MarkdownParser();

    public void printMarkdownTableStats(String fileName, int statsOpsFlag) throws IOException{
        Table table = parser.parseTableFromFile(fileName);

        TableStatsPerformer statsPerformer = new TableStatsPerformer();

        //這一段其實是 「用位元旗標（bit mask）控制要做哪些統計
        //所以帶入的參數都是不同的2進位才能用&比對
        if((statsOpsFlag & TOTAL_COST) != 0) {
            statsPerformer.addStatsOperation(new TotalColumn("Cost"));
        }

        if((statsOpsFlag & COUNT_MEMBERS) != 0) {
            statsPerformer.addStatsOperation(new CountRows("Members"));
        }

        statsPerformer.print(table);
    }
}

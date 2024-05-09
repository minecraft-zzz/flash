package com.google.mlkit.vision.demo.java;
import android.os.Build;

import java.util.HashMap;

public class VideoNameConverter {
    private static final HashMap<String, String> nameMap = new HashMap<>();

    static {
        // 添加映射关系
        nameMap.put("a1", "上斜杠铃卧推");
        nameMap.put("a2", "杠铃卧推");
        nameMap.put("a3", "双杠臂屈伸1");
        nameMap.put("a4", "宽距杠铃卧推");
        nameMap.put("a5", "悍马机推胸");
        nameMap.put("a6", "平躺哑铃飞鸟");
        nameMap.put("a7", "双杠臂屈伸2");
        nameMap.put("a8", "上斜哑铃卧推");
        nameMap.put("a9", "上斜史密斯机卧推");
        nameMap.put("a10", "史密斯机卧推");
        nameMap.put("a11", "俯卧撑");
        nameMap.put("a12", "器械推胸");
        nameMap.put("a13", "暂停卧推");
        nameMap.put("a14", "仰卧旋转曲臂上拉");
        nameMap.put("a15", "蝴蝶机飞鸟");
        nameMap.put("a16", "哑铃卧推");
        nameMap.put("a17", "双杠臂屈伸3");
        nameMap.put("a18", "下斜史密斯机卧推");
        nameMap.put("b1", "上斜哑铃飞鸟");
        nameMap.put("b2", "器械坐姿推举");
        nameMap.put("b3", "前平举");
        nameMap.put("b4", "史密斯机推举");
        nameMap.put("b5", "站姿杠铃推举");
        nameMap.put("b6", "器械前侧提拉");
        nameMap.put("b7", "绳索侧平举");
        nameMap.put("b8", "半俯身侧平举");
        nameMap.put("b9", "史密斯机提拉");
        nameMap.put("b10", "站姿哑铃推举");
        nameMap.put("b11", "哑铃推荐");
        nameMap.put("b12", "哑铃片前平举");
        nameMap.put("b13", "侧平举");
        nameMap.put("b14", "杠铃直立划船");
        nameMap.put("b15", "斯科特推举");
        nameMap.put("b16", "器械坐姿推举");
        nameMap.put("c1", "反手引体向上");
        nameMap.put("c2", "杠铃划船");
        nameMap.put("c3", "宽距下拉");
        nameMap.put("c4", "站姿哑铃划船");
        nameMap.put("c5", "坐姿划船");
        nameMap.put("c6", "硬拉器耸肩");
        nameMap.put("c7", "史密斯辅助引体");
        nameMap.put("c8", "器械划船");
        nameMap.put("c9", "哑铃划船");
        nameMap.put("c10", "引体向上");
        nameMap.put("c11", "俯卧哑铃划船");
        nameMap.put("c12", "拉杆坐姿划船");
        nameMap.put("c13", "哑铃耸肩");
        nameMap.put("c14", "杠铃耸肩");
        nameMap.put("d1", "箭步蹲");
        nameMap.put("d2", "杠铃箭步蹲");
        nameMap.put("d3", "悬挂抬腿");
        nameMap.put("d4", "哈克机深蹲");
        nameMap.put("d5", "器械倒蹬");
        nameMap.put("d6", "哑铃箭步蹲");
        nameMap.put("d7", "硬拉");
        nameMap.put("d8", "腿弯举");
        nameMap.put("d9", "六角杆硬拉");
        nameMap.put("d10", "史密斯机提踵");
        nameMap.put("d11", "平躺跪姿转体");
        nameMap.put("d12", "跑步机");
        nameMap.put("d13", "跑步");
        nameMap.put("d14", "站姿哑铃提踵");
        nameMap.put("d15", "坐姿髋内收");
        nameMap.put("d16", "腿举机提踵");
        nameMap.put("d17", "前蹲");
        nameMap.put("d18", "坐姿腿屈伸");
        nameMap.put("d19", "腿举");
        nameMap.put("d20", "动感单车");
        nameMap.put("d21", "坐姿髋外展");
        nameMap.put("d22", "杠铃直腿硬拉");
        nameMap.put("d23", "史密斯机深蹲");
        nameMap.put("d24", "深蹲跳");
        nameMap.put("d25", "罗马尼亚硬拉");
        nameMap.put("d26", "深蹲");
        nameMap.put("d27", "早安");
        nameMap.put("d28", "水平山羊挺身");
        nameMap.put("d29", "站姿器械提踵");
        nameMap.put("e1", "坐姿牧师凳哑铃弯举");
        nameMap.put("e2", "器械臂屈伸");
        nameMap.put("e3", "正手杠铃弯举");
        nameMap.put("e4", "碎颅者");
        nameMap.put("e5", "坐姿牧师凳佐特曼弯举");
        nameMap.put("e6", "直杆绳索弯举");
        nameMap.put("e7", "哑铃轮换弯举");
        nameMap.put("e8", "EZ杆二头弯举");
        nameMap.put("e9", "平板哑铃臂屈伸");
        nameMap.put("e10", "绳索臂屈伸");
        nameMap.put("e11", "绳索过头臂屈伸");
        nameMap.put("e12", "锤式弯举");
        nameMap.put("e13", "器械弯举");
        nameMap.put("e14", "坐姿杠铃过头臂屈伸");
        nameMap.put("e15", "集中弯举");
        nameMap.put("e16", "哑铃过头臂屈伸");
        nameMap.put("f1", "坐姿器械卷腹");
        nameMap.put("f2", "平躺抬腿");
        nameMap.put("f3", "卷腹");
        nameMap.put("f4", "3/4仰卧起坐");
        nameMap.put("f5", "平板支撑");
        nameMap.put("f6", "双头仰卧起");
        nameMap.put("f7", "平躺卷腹");
        nameMap.put("f8", "仰卧顶腿");
        nameMap.put("f9", "跪姿健腹轮");
        nameMap.put("f10", "瑜伽球卷腹");
        nameMap.put("f11", "抬腿");
        nameMap.put("f12", "排球俄罗斯转体");
        nameMap.put("g1", "高翻");
        nameMap.put("g2", "实力推");
        nameMap.put("g3", "举重");
        nameMap.put("g4", "硬拉耸肩");
        nameMap.put("g5", "椭圆机");
        nameMap.put("g6", "波比跳");
        nameMap.put("g7", "仰卧提臀");
    }

    public static String convertToChinese(String originalName) {
        // 获取对应的汉字名称，如果不存在则返回原始名称
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return nameMap.getOrDefault(originalName, originalName);
        }
        return originalName;
    }
}

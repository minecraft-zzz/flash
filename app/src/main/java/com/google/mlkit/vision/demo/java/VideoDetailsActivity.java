package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;

import java.util.HashMap;
public class VideoDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        // Initialize HashMap and populate with videoName and text content
        HashMap<String, String> videoNameMap = new HashMap<>();
        videoNameMap.put("a1", "上斜杠铃卧推\n\n调整训练台至15-30度角，平躺，脚平放地面。握杠铃，手距比肩稍宽，从架上解放后稳定于胸部上方。缓慢下降杠铃至胸部轻触，然后推举直到手臂完全伸直。");
        videoNameMap.put("a2", "杠铃卧推\n\n平躺于训练台，脚平放地面，握杠铃手距与肩同宽。从架上取下杠铃稳定于胸部上方，缓慢降至胸部轻触后推起至完全伸直。");
        videoNameMap.put("a3", "双杠臂屈伸1\n\n使用双杠，握住两个把手，臂部伸直承载身体重量。缓慢弯曲肘部，下降身体直到肘部约成90度角。再将身体推起至起始位置。");
        videoNameMap.put("a4", "宽距杠铃卧推\n\n平躺在训练台上，握杠铃的距离超过肩宽。杠铃从架上解放后，缓慢下降至胸部稍低位置，然后力量推起。");
        videoNameMap.put("a5", "悍马机推胸\n\n调整悍马机座椅和把手适合自己的身体高度。抓住把手，平推直到手臂伸直，注意背部始终贴着座椅。");
        videoNameMap.put("a6", "平躺哑铃飞鸟\n\n平躺在训练台上，手持哑铃直臂锁定，哑铃在胸部正上方。缓慢打开双臂，直到肩部水平，然后缓慢将哑铃合拢回起始位置。");
        videoNameMap.put("a7", "双杠臂屈伸2\n\n与a3类似，但可以尝试改变握把距离或身体角度以增加难度。");
        videoNameMap.put("a8", "上斜哑铃卧推\n\n将训练台调整为上斜角度，平躺持哑铃在胸部正上方。下降哑铃至胸部，然后推起至手臂伸直。");
        videoNameMap.put("a9", "上斜史密斯机卧推\n\n调整史密斯机的角度和座位，使其在上斜位置。执行卧推动作，注意控制好重量稳定上升下降。");
        videoNameMap.put("a10", "史密斯机卧推\n\n调整史密斯机座位，平躺进行卧推。控制杠铃稳定上升和下降，保持动作流畅。");
        videoNameMap.put("a11", "俯卧撑\n\n双手比肩宽，身体成一直线，脚尖支撑。身体一直保持直线，肘部弯曲使身体下降至接近地面，然后推身体回起始位置。");
        videoNameMap.put("a12", "器械推胸\n\n调整座椅使把手处于胸部中部高度。推动把手直至手臂伸直，控制重量缓慢返回。");
        videoNameMap.put("a13", "暂停卧推\n\n执行杠铃卧推，但在胸部停留1-2秒后再推起。");
        videoNameMap.put("a14", "仰卧旋转曲臂上拉\n\n仰卧，手持哑铃，从胸部向头部方向旋转拉起至完全伸直。");
        videoNameMap.put("a15", "蝴蝶机飞鸟\n\n调整座椅高度使把手与胸部同高。双手抓把手，向后拉至胸前合拢，控制重量缓慢回到开放状态。");
        videoNameMap.put("a16", "哑铃卧推\n\n平躺训练台，哑铃举在胸部正上方，缓慢降低到胸部，再推起。");
        videoNameMap.put("a17", "双杠臂屈伸3\n\n与a3和a7类似，继续增加身体的倾斜角度或调整握距来增加挑战。");
        videoNameMap.put("a18", "下斜史密斯机卧推\n\n调整史密斯机为下斜角度，控制杠铃缓慢下降并推起。");

// b系列
        videoNameMap.put("b1", "上斜哑铃飞鸟\n\n在上斜训练台上平躺，双手持哑铃，臂部微弯。缓慢打开双臂至与肩平行，然后缓慢合拢至起始位置。");
        videoNameMap.put("b2", "器械坐姿推举\n\n调整座椅使肩部与把手对齐。推举把手直到手臂完全伸直，然后缓慢降低回起始位置。");
        videoNameMap.put("b3", "前平举\n\n站立，双脚与肩同宽，手持哑铃垂于体侧。将哑铃举至前方肩部高度，然后缓慢降低。");
        videoNameMap.put("b4", "史密斯机推举\n\n调整史密斯机高度，站立下方。把杠铃推举至头顶直至手臂伸直，然后控制重量缓慢降回。");
        videoNameMap.put("b5", "站姿杠铃推举\n\n站立，脚与肩同宽，杠铃持于肩前。推举杠铃至头顶，手臂伸直，然后控制降回。");
        videoNameMap.put("b6", "器械前侧提拉\n\n调整器械合适高度，立姿使用。拉动器械把手向前上方，直到肩部高度，然后缓慢还原。");
        videoNameMap.put("b7", "绳索侧平举\n\n站立，绳索把手持于一侧。平举至肩部高度，然后控制缓慢降低。");
        videoNameMap.put("b8", "半俯身侧平举\n\n上身前倾，背部保持直，手持哑铃。将哑铃侧举至肩部高度，然后缓慢降低。");
        videoNameMap.put("b9", "史密斯机提拉\n\n站立在史密斯机下，手持杠铃。进行直臂提拉，直到肩部高度，然后缓慢放下。");
        videoNameMap.put("b10", "站姿哑铃推举\n\n站立，双手持哑铃于肩侧。推举哑铃至头顶，手臂伸直，然后缓慢降回。");
        videoNameMap.put("b11", "哑铃推荐\n\n站立，手持哑铃于肩侧。推举哑铃至头顶，手臂伸直，然后缓慢降回。");
        videoNameMap.put("b12", "哑铃片前平举\n\n站立，手持哑铃片，手臂伸直。将哑铃片举至前方直到眼睛高度，然后缓慢降回。");
        videoNameMap.put("b13", "侧平举\n\n站立，手持哑铃，手臂伸直。平举哑铃至肩部高度，然后控制降回。");
        videoNameMap.put("b14", "杠铃直立划船\n\n站立，脚与肩同宽，手持杠铃。将杠铃拉至胸前，肘部向侧上方拉起，然后缓慢放下。");
        videoNameMap.put("b15", "斯科特推举\n\n使用斯科特机进行推举，确保背部紧靠座椅。推举重量至头顶，然后控制下降。");
        videoNameMap.put("b16", "器械坐姿推举\n\n调整座椅高度，确保肩膀与把手对齐。推举至手臂伸直，然后缓慢降低回起始位置。");

// c系列
        videoNameMap.put("c1", "反手引体向上\n\n使用反手握法（掌心朝向自己）握住横杆。从完全悬垂的位置开始，拉起身体直到下巴超过横杆。缓慢降低回到初始位置。");
        videoNameMap.put("c2", "杠铃划船\n\n站立，脚与肩同宽，轻微弯曲膝盖，上身前倾约45度。手持杠铃，从地面拉至腹部，肘部贴近身体。控制杠铃缓慢下降回地面。");
        videoNameMap.put("c3", "宽距下拉\n\n调整拉力机把手至宽握距离。从完全伸展的双臂开始，拉下把手至胸前，肘部向下压。控制把手缓慢回到起始位置。");
        videoNameMap.put("c4", "站姿哑铃划船\n\n站立，一只脚前置，另一只脚后撑，上身前倾。一手支撑身体，另一手持哑铃，从下方拉至侧腹。控制哑铃缓慢下降回初始位置。");
        videoNameMap.put("c5", "坐姿划船\n\n在划船机上坐好，抓住把手。用力将把手拉向腹部，同时背部挺直。控制把手缓慢返回至伸展位置。");
        videoNameMap.put("c6", "硬拉器耸肩\n\n站立，脚与肩同宽，手持硬拉器。仅用肩膀力量将器械耸起，避免使用手臂。控制缓慢降低肩膀回到放松状态。");
        videoNameMap.put("c7", "史密斯辅助引体\n\n设置史密斯机至合适的高度以进行辅助引体。握住杠铃，使用脚部辅助，拉身体至杠铃下。控制下降回到初始悬垂位置。");
        videoNameMap.put("c8", "器械划船\n\n调整坐姿使得手可以舒适地抓住把手。把手拉向腹部，背部保持挺直。缓慢让把手回到开始位置。");
        videoNameMap.put("c9", "哑铃划船\n\n站立，一只脚前置，一只脚后撑，手持哑铃。哑铃从地面划至腹部，保持肘部贴身体侧。控制哑铃缓慢下降回起始位置。");
        videoNameMap.put("c10", "引体向上\n\n握住引体向上的横杆，手掌朝外。从悬垂状态开始，拉身体上升直至下巴超过横杆。控制下降回到初始位置。");
        videoNameMap.put("c11", "俯卧哑铃划船\n\n俯卧在训练台上，一只手持哑铃。将哑铃拉向侧腹，肘部向上方移动。控制下降哑铃回到地面或训练台边缘。");
        videoNameMap.put("c12", "拉杆坐姿划船\n\n坐在拉杆机前，双脚放在前面的支撑上，背部挺直。把手从前方拉向腹部，肘部向后拉。控制把手缓慢返回至开始位置。");
        videoNameMap.put("c13", "哑铃耸肩\n\n站立，手持哑铃，双手自然下垂。仅用肩部力量耸肩向上，尽量高。控制肩部缓慢回到放松状态。");
        videoNameMap.put("c14", "杠铃耸肩\n\n站立，脚与肩同宽，手持杠铃。用肩部力量耸肩向上，避免弯曲手肘。缓慢放下肩膀回到初始位置。");

        videoNameMap.put("d1", "箭步蹲\n\n站立，脚与肩同宽，迈出一步进行前蹲。前腿膝盖弯曲直到大腿与地面平行，后腿膝盖接近地面。然后推回到起始站立位置。");
        videoNameMap.put("d2", "杠铃箭步蹲\n\n与箭步蹲类似，但背负杠铃增加负重。确保背部挺直，控制下蹲和上升动作。");
        videoNameMap.put("d3", "悬挂抬腿\n\n悬挂在高杠上，手臂和肩膀宽度一致。抬起腿部直到与躯干成90度角，然后缓慢放下。");
        videoNameMap.put("d4", "哈克机深蹲\n\n进入哈克机，背靠背垫，脚平放于稍宽于肩的位置。下蹲直到大腿低于平行地面，然后推起回到起始位置。");
        videoNameMap.put("d5", "器械倒蹬\n\n调整倒蹬机器的座椅和蹬板。用脚蹬板，控制腿部推动并缓慢还原。");
        videoNameMap.put("d6", "哑铃箭步蹲\n\n手持哑铃进行箭步蹲，增加负重。控制平衡，保持背部挺直。");
        videoNameMap.put("d7", "硬拉\n\n站立，脚与肩同宽，弯腰握住杠铃。用腿和背部的力量将杠铃提起到站立位置，然后控制下降。");
        videoNameMap.put("d8", "腿弯举\n\n使用腿弯举机，腿后方挂上重量。膝盖弯曲拉起重量，然后缓慢放下。");
        videoNameMap.put("d9", "六角杆硬拉\n\n站在六角杆中心，弯腰握住杆柄。使用腿部和核心力量将杆体提起，然后控制下降。");
        videoNameMap.put("d10", "史密斯机提踵\n\n站在史密斯机下，脚跟悬空。用脚尖抬起身体，提高踵部，然后缓慢下降。");
        videoNameMap.put("d11", "平躺跪姿转体\n\n平躺，膝盖弯曲抬高，双手平放。将膝盖向一侧转动接近地面，保持肩膀贴地，然后回中。");
        videoNameMap.put("d12", "跑步机\n\n在跑步机上设定合适的速度。保持稳定的步态，注意呼吸均匀。");
        videoNameMap.put("d13", "跑步\n\n选择合适的路线和鞋子。保持正确的姿势，腕部自然摆动，注意呼吸。");
        videoNameMap.put("d17", "前蹲\n\n杠铃置于肩前，双手支撑。下蹲直到大腿低于平行地面，然后推起。");
        videoNameMap.put("d18", "坐姿腿屈伸\n\n坐在腿屈伸机上，调整座椅和垫脚位置。控制腿部推伸，然后缓慢还原。");
        videoNameMap.put("d19", "腿举\n\n悬挂或躺卧，提膝至胸前。控制下降，使腿部缓慢伸直。");
        videoNameMap.put("d20", "动感单车\n\n调整座椅和把手适合自己的身高。保持均匀的踏频，调节阻力适合训练需求。");
        videoNameMap.put("d21", "坐姿髋外展\n\n坐在髋外展机上，调整适合的座位和脚位。控制腿部向外展开，然后缓慢还原。");
        videoNameMap.put("d22", "杠铃直腿硬拉\n\n站立，脚与肩同宽，杠铃前置。保持腿部略微弯曲，向下弯腰，控制杠铃下降至接近地面。");
        videoNameMap.put("d23", "史密斯机深蹲\n\n进入史密斯机，脚稍宽于肩。下蹲至大腿低于平行地面，然后推起。");
        videoNameMap.put("d24", "深蹲跳\n\n深蹲至大腿低于平行地面，然后爆发力跳起。着地时控制下蹲，吸收冲击。");
        videoNameMap.put("d25", "罗马尼亚硬拉\n\n杠铃前置，腿部保持微曲。上体向前倾斜，保持背部直，杠铃降至膝盖下方。使用臀部和腿后肌群力量拉起身体。");
        videoNameMap.put("d26", "深蹲\n\n脚稍宽于肩，杠铃置于颈后。下蹲至大腿低于平行地面，然后用力推起。");
        videoNameMap.put("d27", "早安\n\n杠铃置于颈后，站立脚与肩同宽。保持腿部微曲，上体向前倾斜至平行地面，然后用腿和背力量复原。");
        videoNameMap.put("d28", "水平山羊挺身\n\n使用山羊挺身机器，腰部以下悬空。上体向下压，然后用腰部力量挺身回到水平位置。");
        videoNameMap.put("d29", "站姿器械提踵\n\n站在提踵机上，脚跟悬空。提起踵部，使用小腿肌肉，然后缓慢放下。");
        videoNameMap.put("e1", "坐姿牧师凳哑铃弯举\n\n坐在牧师凳上，手臂靠在垫子上，手持哑铃。弯曲肘部，将哑铃向肩部提起，然后缓慢放下至初始位置。");
        videoNameMap.put("e2", "器械臂屈伸\n\n使用臂屈伸机，手臂置于机器支撑上。执行臂屈伸动作，手臂完全伸直后再弯曲回到起始位置。");
        videoNameMap.put("e3", "正手杠铃弯举\n\n站立，手持杠铃，手掌朝上。弯曲肘部，将杠铃提升至胸前，然后缓慢降低回到初始位置。");
        videoNameMap.put("e4", "碎颅者\n\n平躺在训练台上，手持杠铃直臂持于胸上方。仅肘部活动，将杠铃缓慢降低至头部附近，然后推回至初始位置。");
        videoNameMap.put("e5", "坐姿牧师凳佐特曼弯举\n\n坐在牧师凳上，前臂固定在垫子上。手持哑铃进行弯举，顶部旋转手腕使手掌朝下，然后缓慢还原。");
        videoNameMap.put("e6", "直杆绳索弯举\n\n使用带有直杆的拉力器，手掌朝上握住杆。弯曲肘部，将杆向上拉至胸前，然后控制杆缓慢返回。");
        videoNameMap.put("e7", "哑铃轮换弯举\n\n站立，手持哑铃，轮流进行单臂弯举。一手执行弯举时，另一手保持在初始位置等待。");
        videoNameMap.put("e8", "EZ杆二头弯举\n\n站立或坐，使用EZ杆。弯曲肘部，杆子提升至胸前，然后缓慢放下回到初始位置。");
        videoNameMap.put("e9", "平板哑铃臂屈伸\n\n平躺在训练台上，手持哑铃在头顶处。执行臂屈伸，直臂举高然后弯曲回头顶。");
        videoNameMap.put("e10", "绳索臂屈伸\n\n使用绳索拉力器站立，手掌朝下握绳。执行臂屈伸动作，臂部伸直然后弯曲。");
        videoNameMap.put("e11", "绳索过头臂屈伸\n\n使用绳索拉力器，面对机器，双手持绳过头。执行臂屈伸，将绳拉至头顶后方再还原到初始位置。");
        videoNameMap.put("e12", "锤式弯举\n\n站立，手持哑铃，手掌相对。交替弯曲肘部，将哑铃提至肩部，然后缓慢降低。");
        videoNameMap.put("e13", "器械弯举\n\n使用二头弯举机，手臂置于支撑上。控制器械弯举至最大幅度，然后缓慢放下。");
        videoNameMap.put("e14", "坐姿杠铃过头臂屈伸\n\n坐在训练台上，手持杠铃过头。执行臂屈伸，从后脑勺位置将杠铃推至顶端，然后缓慢还原。");
        videoNameMap.put("e15", "集中弯举\n\n坐姿，一手持哑铃，前臂放在相同侧腿上。执行弯举，臂部尽可能不动，仅通过肘部力量拉动哑铃。");
        videoNameMap.put("e16", "哑铃过头臂屈伸\n\n坐姿，手持哑铃过头。执行臂屈伸，缓慢推举哑铃直至手臂伸直，然后还原到起始位置。");
        videoNameMap.put("f1", "坐姿器械卷腹\n\n坐在卷腹机上，调整座椅使膝盖稍微弯曲。抓住机器的把手或垫子，用腹部力量将上身向前压低。控制上身缓慢回到起始位置。");
        videoNameMap.put("f2", "平躺抬腿\n\n平躺在地上，双手放置于身体两侧或下臀下方。抬起双腿直至垂直于地面，然后缓慢降低回到接近地面但不触碰的位置。");
        videoNameMap.put("f3", "卷腹\n\n平躺在垫子上，膝盖弯曲，脚平放在地上。双手轻放于头后，用腹部力量将肩膀抬离地面向膝盖方向卷起。控制下降，肩膀回到垫子上。");
        videoNameMap.put("f4", "3/4仰卧起坐\n\n平躺，膝盖弯曲，双手轻放头后。抬起上身至约三分之三的高度，感受腹部收紧，然后缓慢回到起始位置。");
        videoNameMap.put("f5", "平板支撑\n\n面朝下趴在地上，用肘部和脚尖支撑身体。保持身体一直线，臀部不要抬高或下沉。保持这个姿势一定时间。");
        videoNameMap.put("f6", "双头仰卧起\n\n平躺，双腿抬高形成90度角，双手放于头后。同时抬起上身和膝盖，使肩膀和胸部向膝盖移动。控制下降回到起始位置。");
        videoNameMap.put("f7", "平躺卷腹\n\n平躺，膝盖弯曲，手放于头后或交叉在胸前。使用腹部力量将上身向膝盖方向卷起，然后缓慢下降。");
        videoNameMap.put("f8", "仰卧顶腿\n\n平躺，双腿直，双手放在身体两侧。抬高双腿至垂直地面，然后尝试用腹部力量将臀部推向天花板。缓慢下降双腿回到初始位置。");
        videoNameMap.put("f9", "跪姿健腹轮\n\n跪在地上，手持健腹轮。推动健腹轮向前滚动至尽可能远的地方，同时保持背部直和核心紧绷。控制健腹轮回到起始位置。");
        videoNameMap.put("f10", "瑜伽球卷腹\n\n躺在瑜伽球上，球支撑住你的下背。双脚固定，双手轻放头后，用腹部力量抬起上身然后缓慢回到初始位置。");
        videoNameMap.put("f11", "抬腿\n\n平躺，双腿直，手放于身体两侧或下臀下方。抬起双腿直至垂直地面，保持几秒，然后缓慢降低。");
        videoNameMap.put("f12", "排球俄罗斯转体\n\n坐在地上，膝盖弯曲，脚跟轻触地面。向后仰至脊柱成45度角，双手合十，执行向左右转体。");
        videoNameMap.put("g1", "高翻\n\n站立，脚与肩同宽，迅速下蹲并用力跳起。在空中尝试执行一个小翻滚动作（确保安全或在专业指导下进行）。");
        videoNameMap.put("g2", "实力推\n\n站立，面对墙，手持医用球。用力将球推向墙，接住反弹后再次推出。");
        videoNameMap.put("g3", "举重\n\n选择适当重量的杠铃，确保姿势正确。执行清举、挺举等动作，控制好杠铃的稳定。");
        videoNameMap.put("g4", "硬拉耸肩\n\n站立，双脚与肩同宽，手持杠铃。执行硬拉至膝盖以上位置，然后进行耸肩动作。");
        videoNameMap.put("g5", "椭圆机\n\n站立在椭圆机上，调整合适的阻力。执行前进或后退踏步，保持均匀的速度。");
        videoNameMap.put("g6", "波比跳\n\n从站立开始，快速下蹲，手放地，跳至俯卧撑位置。执行一个俯卧撑，然后跳回蹲姿，再跳起伸直双手。");
        videoNameMap.put("g7", "仰卧提臀\n\n平躺，膝盖弯曲，脚平放在地上。抬高臀部直到腰部和腿部形成直线，保持几秒，然后缓慢降低。");

        // Find views
        VideoView mVideoView = findViewById(R.id.videoView);
        TextView mTextView = findViewById(R.id.TextView);

        // Get video information from intent
        Intent intent = getIntent();
        if (intent != null) {
            String videoName = intent.getStringExtra("videoName");
            if (videoName != null) {
                int videoResourceId = getResources().getIdentifier(videoName, "raw", getPackageName());
                String uri = "android.resource://" + getPackageName() + "/" + videoResourceId;
                mVideoView.setVideoURI(Uri.parse(uri));
                mVideoView.start();

                // Set text content based on videoName
                String textContent = videoNameMap.get(videoName);
                if (textContent != null) {
                    mTextView.setText(textContent);
                }
            }
        }
    }
}

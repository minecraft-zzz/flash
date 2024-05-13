package com.google.mlkit.vision.demo.java;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.mlkit.vision.demo.R;

import java.util.ArrayList;

public class ChartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chart, container, false);

        LineChart weeklineChart = view.findViewById(R.id.weeklyLineChart);

        // 创建一些示例数据
        ArrayList<Entry> weekentries = new ArrayList<>();
        weekentries.add(new Entry(0, 30));
        weekentries.add(new Entry(1, 50));
        weekentries.add(new Entry(2, 40));
        weekentries.add(new Entry(3, 70));
        weekentries.add(new Entry(4, 60));

        // 创建数据集合并设置样式
        LineDataSet weekdataSet = new LineDataSet(weekentries, "Sample Data");
        weekdataSet.setColor(Color.BLUE);
        weekdataSet.setValueTextColor(Color.BLACK);
        weekdataSet.setDrawFilled(true); // 绘制平面填充区域

        weekdataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        Drawable weekdrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_bg);
        weekdataSet.setFillDrawable(weekdrawable);

        // 创建数据对象并添加数据集合
        LineData lineData = new LineData(weekdataSet);
        weeklineChart.setData(lineData);

        // 自定义 X 轴和 Y 轴
        XAxis weekxAxis = weeklineChart.getXAxis();
        weekxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis weekyAxisRight = weeklineChart.getAxisRight();
        weekyAxisRight.setEnabled(false);

        // 刷新图表
        weeklineChart.invalidate();

        LineChart monthlineChart = view.findViewById(R.id.monthlyLineChart);

        // 创建月报示例数据
        // 创建月报示例数据
        ArrayList<Entry> monthlyEntries = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            // 随机生成数据，你也可以根据你的需求修改这里的数据生成逻辑
            float value = (float) (Math.random() * 100); // 生成 100 到 200 之间的随机数
            monthlyEntries.add(new Entry(i, value));
        }

        // 创建数据集并设置样式
        LineDataSet monthlyDataSet = new LineDataSet(monthlyEntries, "Monthly Data");
        monthlyDataSet.setColor(Color.RED);
        monthlyDataSet.setValueTextColor(Color.BLACK);
        monthlyDataSet.setDrawFilled(true); // 绘制填充区域

        // 设置填充区域的背景色
        Drawable monthlyDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_bg1);
        monthlyDataSet.setFillDrawable(monthlyDrawable);

        // 创建数据对象并添加数据集
        LineData monthlyLineData = new LineData(monthlyDataSet);
        monthlineChart.setData(monthlyLineData);

        // 自定义 X 轴和 Y 轴
        XAxis monthxAxis = monthlineChart.getXAxis();
        monthxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis monthyAxisRight = monthlineChart.getAxisRight();
        monthyAxisRight.setEnabled(false);

        // 刷新图表
        monthlineChart.invalidate();


        return view;
    }
}

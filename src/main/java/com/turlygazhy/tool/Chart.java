package com.turlygazhy.tool;


import com.turlygazhy.entity.SavedResult;
import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by user on 3/2/17.
 */
public class Chart {
    private static final String PATH = "C:\\charts";
    //    private String PATH = "/home/user/Documents/charts";
    private String fileName;
    private Map<Date, Integer> data = new LinkedHashMap<>();

    public String getChart(String goalName) {
        List<Double> yData = new ArrayList<>();
        List<Date> xData = new ArrayList<>();

        for (Map.Entry<Date, Integer> entry : data.entrySet()) {
            xData.add(entry.getKey());
            yData.add(Double.valueOf(entry.getValue()));
        }

        // Create Chart
        XYChart chart = new XYChart(500, 400);
        chart.setTitle(goalName);
        XYSeries series = chart.addSeries("y(x)", xData, yData);
        series.setMarker(SeriesMarkers.CIRCLE);

        String fullPath = PATH + "/" + this.fileName;
        try {
            BitmapEncoder.saveBitmap(chart, fullPath, BitmapEncoder.BitmapFormat.JPG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void addPair(Date date, int result) {
        data.put(date, result);
    }

    public String getChart(String goalName, Map<String, List<SavedResult>> userNameAndResults) {
        String fileName = /*goalName +*/ "_month";

        // Create Chart
        XYChart chart = new XYChart(500, 400);
        chart.setTitle(goalName);

        //todo ihere
        for (Map.Entry<String, List<SavedResult>> entry : userNameAndResults.entrySet()) {
            List<SavedResult> results = entry.getValue();
            chart.addSeries(entry.getKey(), getXData(results), getYData(results));

        }
//        XYSeries series = chart.addSeries("y(x)", xData, yData);
//        series.setMarker(SeriesMarkers.CIRCLE);

        String fullPath = PATH + "/" + fileName;
        try {
            BitmapEncoder.saveBitmap(chart, fullPath, BitmapEncoder.BitmapFormat.JPG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    private List<Double> getYData(List<SavedResult> results) {
        List<Double> yData = new ArrayList<>();
        double prev = 0;
        for (SavedResult result : results) {
            prev = prev + (double) result.getResult();
            yData.add(prev);
        }
        return yData;
    }

    private List<Date> getXData(List<SavedResult> results) {
        List<Date> xData = new ArrayList<>();
        for (SavedResult result : results) {
            xData.add(result.getDate());
        }
        return xData;
    }

    public String getPieChart(String name, Map<String, List<SavedResult>> stringListMap) {
        /*PieChart chart = new PieChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();
        int allReadCount = 0;
        for (Map.Entry<String, List<SavedResult>> entry : stringListMap.entrySet()) {
            List<SavedResult> value = entry.getValue();
            int onePersonRead = 0;
            for (SavedResult savedResult : value) {
                onePersonRead = onePersonRead + savedResult.getResult();
            }
            chart.addSeries(entry.getKey(), onePersonRead);
            allReadCount = allReadCount + onePersonRead;
        }

        // Create Chart
        chart.setTitle("Сделано " + allReadCount + " звонков");

        // Customize Chart
        Color[] sliceColors = new Color[]{new Color(224, 68, 14), new Color(230, 105, 62), new Color(236, 143, 110), new Color(243, 180, 159), new Color(246, 199, 182)};
        chart.getStyler().setSeriesColors(sliceColors);*/


        // Create Chart
        PieChart chart = new PieChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

        int allReadCount = 0;
        for (Map.Entry<String, List<SavedResult>> entry : stringListMap.entrySet()) {
            List<SavedResult> value = entry.getValue();
            int onePersonRead = 0;
            for (SavedResult savedResult : value) {
                onePersonRead = onePersonRead + savedResult.getResult();
            }
            chart.addSeries(entry.getKey() + "(" + onePersonRead + ")", onePersonRead);
            allReadCount = allReadCount + onePersonRead;
        }

        // Create Chart
        chart.setTitle("Сделано " + allReadCount + " звонков");

        // Customize Chart
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAnnotationType(PieStyler.AnnotationType.Label);
//        chart.getStyler().setAnnotationDistance(.82);
//        chart.getStyler().setPlotContentSize(.9);
//        chart.getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
        // chart.getStyler().setCircular(false);

        String fullPath = PATH + "/" + 12313;
        try {
            BitmapEncoder.saveBitmap(chart, fullPath, BitmapEncoder.BitmapFormat.JPG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullPath;
    }
}
package bsu.rfe.java.group6.lab4.Timonovich.A7;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
@SuppressWarnings("serial")

public class GraphicsDisplay extends JPanel {
    // Список координат точек для построения графика
    private Double[][] graphicsData;
    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true;
    private boolean showMarkers = true;
    // Границы диапазона пространства, подлежащего отображению
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    // Используемый масштаб отображения
    private double scale;
    // Различные стили черчения линий
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    // Различные шрифты отображения надписей
    private Font axisFont;
    public GraphicsDisplay() {
// Цвет заднего фона области отображения - белый
        setBackground(Color.WHITE);
// Сконструировать необходимые объекты, используемые в рисовании
// Перо для рисования графика
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f);// Перо для рисования осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// Перо для рисования контуров маркеров
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
// Шрифт для подписей осей координат
        axisFont = new Font("Serif", Font.BOLD, 36);
    }
    // Метод вызывается из обработчика элемента меню "Открыть файл с графиком"
// главного окна приложения в случае успешной загрузки данных
    public void showGraphics(Double[][] graphicsData) {
// Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;
// Запросить перерисовку компонента (неявно вызвать paintComponent())
        repaint();
    }
    // Методы-модификаторы для изменения параметров отображения графика
// Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    

}


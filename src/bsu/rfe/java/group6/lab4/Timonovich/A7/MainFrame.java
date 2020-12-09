package bsu.rfe.java.group6.lab4.Timonovich.A7;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class MainFrame extends JFrame {

    // Объект диалогового окна для выбора файлов
    private JFileChooser fileChooser;

    // Флаг, указывающий на загруженность данных графика
    private boolean fileLoaded;

    // Компонент-отображатель графика
    private GraphicsDisplay display = new GraphicsDisplay();

    static private final int height = 800;
    static private final int width = 600;

    // Пункты меню
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem turnGraphMenuItem;

    public MainFrame(){
        super("Построение графиков функций на основе подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
        // Развѐртывание окна на весь экран
        setExtendedState(MAXIMIZED_BOTH);
        // Создать и установить полосу меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // Добавить пункт меню "Файл"
        JMenu fileMenu = new JMenu("файл");
        menuBar.add(fileMenu);
        // Создать действие по открытию файла
        Action openGraphicsAction = new AbstractAction("Открыть файл"){
            public void actionPerformed(ActionEvent arg0) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);
                openGraphics(fileChooser.getSelectedFile());
            }
        };
        // Добавить соответствующий элемент меню
        fileMenu.add(openGraphicsAction);
        // Создать пункт меню "График"
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);
        // Создать действие для реакции на активацию элемента
        // "Показывать оси координат"
        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            public void actionPerformed(ActionEvent e) {
                // свойство showAxis класса GraphicsDisplay истина,
                // если элемент меню showAxisMenuItem отмечен флажком,
                // ложь - в противном случае
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        // Добавить соответствующий элемент в меню
        graphicsMenu.add(showAxisMenuItem);
        // Элемент по умолчанию включен (отмечен флажком)
        showAxisMenuItem.setSelected(true);


        Action turnAction = new AbstractAction("Поворот графика на 90 градусов") {
            public void actionPerformed(ActionEvent e) {
            }
        };
        turnGraphMenuItem = new JCheckBoxMenuItem(turnAction);
        graphicsMenu.add(turnGraphMenuItem);
        turnGraphMenuItem.setEnabled(false);
        graphicsMenu.addSeparator();



        // Повторить действия для элемента "Показывать маркеры точек"
        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {

            public void actionPerformed(ActionEvent e) {
                // по аналогии с showAxisMenuItem
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);
        // Зарегистрировать обработчик событий, связанных с меню "График"
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        // Установить GraphicsDisplay в цент граничной компоновки
        getContentPane().add(display, BorderLayout.CENTER);
    }

    // Считывание данных графика из существующего файла
    protected void openGraphics(File selectedFile) {
        try {
            // Шаг 1 - Открыть поток чтения данных, связанный с файлом
            DataInputStream in = new DataInputStream(
                    new FileInputStream(selectedFile));
            /* Шаг 2- Зная объѐм данных в потоке ввода можно вычислить,
             * сколько памяти нужно зарезервировать в массиве:
             * Всего байт в потоке - in.available() байт;
             * Размер числа Double - Double.SIZE бит, или Double.SIZE/8 байт;
             * Так как числа записываются парами, то число пар меньше в 2 раза */
            Double[][] graphicsData = new
                    Double[in.available()/(Double.SIZE/8)/2][];
            // Шаг 3 – Цикл чтения данных (пока в потоке есть данные)
            int i = 0;
            while (in.available() > 0) {
                // Первой из потока читается координата точки X
                Double x = Double.valueOf(in.readDouble());
                // Затем - значение графика Y в точке X
                Double y = Double.valueOf(in.readDouble());
                // Прочитанная пара координат добавляется в массив
                graphicsData[i++] = new Double[] {x, y};
            }
            // Шаг 4 - Проверка, имеется ли в списке в результате чтения
            // хотя бы одна пара координат
            if (graphicsData!=null && graphicsData.length>0) {
                // Да - установить флаг загруженности данных
                fileLoaded = true;
                // Вызывать метод отображения графика
                display.showGraphics(graphicsData);
            }
            // Шаг 5 - Закрыть входной поток
            in.close();
        }catch (FileNotFoundException e){
            // В случае исключительной ситуации типа "Файл не найден"
            // показать сообщение об ошибке
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Указанный файл не найден", "Ошибка загрузки данных",
                    JOptionPane.WARNING_MESSAGE);
            return;

        }catch (IOException e){
            // В случае ошибки ввода из файлового потока
            // показать сообщение об ошибке
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Ошибка чтения координат точек из файла",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    // Класс-слушатель событий, связанных с отображением меню
    private class GraphicsMenuListener implements MenuListener {
        // Обработчик, вызываемый перед показом меню
        public void menuSelected(MenuEvent e) {
            // Доступность или недоступность элементов меню "График"
            // определяется загруженностью данных
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }

        // Обработчик, вызываемый в случае отмены выбора пункта меню
        // (очень редкая ситуация)
        public void menuCanceled(MenuEvent e) {

        }

    }
}
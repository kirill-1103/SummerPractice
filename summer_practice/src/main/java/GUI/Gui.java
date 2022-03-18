package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Filter;

import SaveLoad.Unloader;
import org.jetbrains.annotations.NotNull;

public class Gui extends JFrame {
    private JPanel mainContainer;
    private BorderLayout mainLayout;
    private GraphVisualization graphVisualization;

    private JButton buttonNext;
    private JButton buttonPrev;
    private JButton buttonStart;
    private JButton buttonEnd;
    private JButton buttonAddVertex;
    private JButton buttonAddEdge;
    private JButton buttonInputGraph;
    private JButton buttonGenerateGraph;
    private JButton buttonStop;

    private JLabel emptyLabelAddVertex;
    private JTextArea textInputAddGraph;
    private JTextField textInputCountVertexesForGenerate;
    private JTextField textInputCountEdgesForGenerate;
    private JTextField textInputMinWeightForGenerate;
    private JTextField textInputMaxWeightForGenerate;
    private JTextField textHintCountVertexesForGenerate;
    private JTextField textHintCountEdgesForGenerate;
    private JTextField textHintMinWeightForGenerate;
    private JTextField textHintMaxWeightForGenerate;
    private JScrollPane inputGraphScrollPane;

    private JTextField textInputVertex1;
    private JTextField textInputVertex2;
    private JTextField textInputWeight;
    private JTextField textHintVertex1;
    private JTextField textHintVertex2;
    private JTextField textHintWeight;

    private JTextField textHintMode;
    private JEditorPane textMode;

    private JTextField inputGraphHint;
    private JMenuBar menuBar;
    private JMenu info;
    private JMenu file;
    private JMenuItem load;
    private JMenuItem save;

    private boolean modeEdited = true;


    public Gui(GraphVisualization graphVisualization) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.graphVisualization = graphVisualization;

        init();
        initWindowSettings();
        setElements();
        createMenu();
    }

    private void setElements() {

        mainContainer.setSize(900, 700);
        mainContainer.setLayout(mainLayout);

        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel southPanel = new JPanel();

        setEastPanel(eastPanel);
        setWestPanel(westPanel);
        setSouthPanel(southPanel);
        setMainContainer(eastPanel, westPanel, southPanel);
        setContentPane(mainContainer);
    }

    private void init() {
        mainContainer = new JPanel();
        mainLayout = new BorderLayout();

        buttonNext = new JButton(">");
        buttonPrev = new JButton("<");
        buttonStart = new JButton("START");

        buttonAddEdge = new JButton("Add edge");
        buttonAddVertex = new JButton("Add vertex");

        buttonGenerateGraph = new JButton("Generate Graph");
        textInputCountVertexesForGenerate = new JTextField();
        textInputCountEdgesForGenerate = new JTextField();
        textInputMaxWeightForGenerate = new JTextField();
        textInputMinWeightForGenerate = new JTextField();
        textHintCountVertexesForGenerate = new JTextField("Количество вершин:");
        textHintCountVertexesForGenerate.setEditable(false);
        textHintCountEdgesForGenerate = new JTextField("Количество ребер:");
        textHintCountEdgesForGenerate.setEditable(false);
        textHintMaxWeightForGenerate = new JTextField("Максимальный вес:");
        textHintMaxWeightForGenerate.setEditable(false);
        textHintMinWeightForGenerate = new JTextField("Минимальный вес:");
        textHintMinWeightForGenerate.setEditable(false);

        buttonInputGraph = new JButton("Input graph");
        textInputAddGraph = new JTextArea();

        textInputVertex1 = new JTextField();
        textInputVertex2 = new JTextField();
        textInputWeight = new JTextField();
        textHintVertex1 = new JTextField("Первая вершина:");
        textHintVertex2 = new JTextField("Вторая вершина:");
        textHintWeight = new JTextField("Вес:");
        textHintVertex1.setEditable(false);
        textHintVertex2.setEditable(false);
        textHintWeight.setEditable(false);


        emptyLabelAddVertex = new JLabel();
        inputGraphScrollPane = new JScrollPane(textInputAddGraph);
        buttonEnd = new JButton("TO END");
        buttonStop = new JButton("STOP");

        inputGraphHint = new JTextField("Формат:матрица смежности");
        inputGraphHint.setEditable(false);

        textMode = new JEditorPane();
        textMode.setText("Редактирование графа");
        Font font = new Font("Serif", Font.ITALIC, 12);
        textMode.setFont(font);
        textHintMode = new JTextField("Режим работы:");
        textMode.setEditable(false);
        textHintMode.setEditable(false);
    }

    private void initWindowSettings() {
        setSize(1300, 750);
        setResizable(false);
        setTitle("Boruvka");
    }


    private void setEastPanel(JPanel eastPanel) {
        eastPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagLayout eastLayout = new GridBagLayout();
        eastPanel.setLayout(eastLayout);

        GridBagConstraints eastConstraints = new GridBagConstraints();
        eastConstraints.fill = GridBagConstraints.BOTH;
        eastConstraints.gridx = 0;
        eastConstraints.gridy = 0;
        eastConstraints.weighty = 0.5;
        eastPanel.add(buttonAddVertex, eastConstraints);

        eastConstraints.weighty = 2;
        eastConstraints.gridy = 1;
        eastPanel.add(emptyLabelAddVertex, eastConstraints);

        eastConstraints.weighty = 0.01;
        eastConstraints.gridy = 2;
        eastPanel.add(textHintVertex1, eastConstraints);
        eastConstraints.gridy = 3;
        eastPanel.add(textInputVertex1, eastConstraints);
        eastConstraints.gridy = 4;
        eastPanel.add(textHintVertex2, eastConstraints);
        eastConstraints.gridy = 5;
        eastPanel.add(textInputVertex2, eastConstraints);
        eastConstraints.gridy = 6;
        eastPanel.add(textHintWeight, eastConstraints);
        eastConstraints.gridy = 7;
        eastPanel.add(textInputWeight, eastConstraints);

        eastConstraints.weighty = 0.5;
        eastConstraints.gridy = 8;
        eastPanel.add(buttonAddEdge, eastConstraints);
    }

    private void setWestPanel(JPanel westPanel) {
        westPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        GridBagLayout westLayout = new GridBagLayout();
        westPanel.setLayout(westLayout);

        GridBagConstraints westConstraints = new GridBagConstraints();
        westConstraints.weighty = 0.5;
        westConstraints.fill = GridBagConstraints.BOTH;
        westConstraints.gridx = 0;
        westConstraints.gridy = 0;
        westConstraints.weightx = 1;

        westPanel.add(buttonInputGraph, westConstraints);

        westConstraints.weighty = 0.1;
        westConstraints.gridy = 1;
        westPanel.add(inputGraphHint, westConstraints);

        westConstraints.weighty = 2;
        westConstraints.gridy = 2;
        westPanel.add(inputGraphScrollPane, westConstraints);

        JLabel empty1 = new JLabel();
        JLabel empty2 = new JLabel();

        westConstraints.weighty = 0.8;
        westConstraints.gridy = 3;
        westPanel.add(empty1, westConstraints);
        westConstraints.gridy = 6;
        westPanel.add(empty2, westConstraints);

        westConstraints.weighty = 0.3;
        westConstraints.gridy = 4;
        westPanel.add(textHintMode, westConstraints);
        westConstraints.gridy = 5;
        westPanel.add(textMode, westConstraints);

        westConstraints.weighty = 0.05;

        westConstraints.gridy = 7;
        westPanel.add(textHintCountVertexesForGenerate, westConstraints);
        westConstraints.gridy = 8;
        westPanel.add(textInputCountVertexesForGenerate, westConstraints);

        westConstraints.gridy = 9;
        westPanel.add(textHintCountEdgesForGenerate, westConstraints);
        westConstraints.gridy = 10;
        westPanel.add(textInputCountEdgesForGenerate, westConstraints);

        westConstraints.gridy = 11;
        westPanel.add(textHintMinWeightForGenerate, westConstraints);
        westConstraints.gridy = 12;
        westPanel.add(textInputMinWeightForGenerate, westConstraints);

        westConstraints.gridy = 13;
        westPanel.add(textHintMaxWeightForGenerate, westConstraints);
        westConstraints.gridy = 14;
        westPanel.add(textInputMaxWeightForGenerate, westConstraints);

        westConstraints.weighty = 0.5;
        westConstraints.gridy = 15;
        westPanel.add(buttonGenerateGraph, westConstraints);
    }

    private void setSouthPanel(JPanel southPanel) {
        southPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        GridBagLayout southLayout = new GridBagLayout();
        southPanel.setLayout(southLayout);

        GridBagConstraints southConstraints = new GridBagConstraints();
        southConstraints.weightx = 0.5;
        southConstraints.fill = GridBagConstraints.BOTH;
        southConstraints.gridx = 0;
        southConstraints.gridy = 0;

        southPanel.add(buttonStart, southConstraints);

        southConstraints.gridx = 1;
        southPanel.add(buttonPrev, southConstraints);

        southConstraints.gridx = 2;
        southPanel.add(buttonNext, southConstraints);

        southConstraints.gridx = 3;
        southPanel.add(buttonEnd, southConstraints);

        southConstraints.gridx = 4;
        southPanel.add(buttonStop, southConstraints);
    }

    private void setMainContainer(JPanel eastPanel, JPanel westPanel, JPanel southPanel) {
        mainContainer.add(graphVisualization, BorderLayout.CENTER);
        mainContainer.add(eastPanel, BorderLayout.EAST);
        mainContainer.add(westPanel, BorderLayout.WEST);
        mainContainer.add(southPanel, BorderLayout.SOUTH);
    }

    public String getTextAtInputAddGraph() {
        return textInputAddGraph.getText();
    }


    public String getTextAtInputLimits() {
        return textInputCountEdgesForGenerate.getText()
                .trim() + " " + textInputCountVertexesForGenerate.getText()
                .trim() + " "
                + textInputMinWeightForGenerate.getText()
                .trim() + " " + textInputMaxWeightForGenerate.getText()
                .trim();
    }

    public String getTextAtInputAddEdge() {
        return textInputVertex1.getText() + " " + textInputVertex2.getText() + " " + textInputWeight.getText();
    }

    public void setGraphVisualization(GraphVisualization visualization) {
        mainContainer.remove(graphVisualization);
        graphVisualization = visualization;
        mainContainer.add(graphVisualization, BorderLayout.CENTER);
        mainContainer.repaint();
        mainContainer.revalidate();
    }

    public void setButtonInputGraphListener(ActionListener listener) {
        buttonInputGraph.addActionListener(listener);
    }

    public void setButtonInputAddVertexListener(ActionListener listener) {
        buttonAddVertex.addActionListener(listener);
    }

    public void setButtonInputAddEdgeListener(ActionListener listener) {
        buttonAddEdge.addActionListener(listener);
    }

    public void setButtonGenerateGraphListener(ActionListener listener) {
        buttonGenerateGraph.addActionListener(listener);
    }

    public void setButtonStartListener(ActionListener listener) {
        buttonStart.addActionListener(listener);
    }

    public void setButtonStopListener(ActionListener listener) {
        buttonStop.addActionListener(listener);
    }

    public void setButtonNextListener(ActionListener listener) {
        buttonNext.addActionListener(listener);
    }

    public void setButtonPrevListener(ActionListener listener) {
        buttonPrev.addActionListener(listener);
    }

    public void setButtonEndListener(ActionListener listener) {
        buttonEnd.addActionListener(listener);
    }

    private void createMenu() {
        menuBar = new JMenuBar();

        info = new JMenu("info");
        JMenuItem information = new JMenuItem("info");
        info.add(information);

        file = new JMenu("File");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        file.add(save);
        file.add(load);


        information.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Info inf = new Info();
                inf.setVisible(true);
            }
        });

        menuBar.add(file);
        menuBar.add(info);
        setJMenuBar(menuBar);
    }

    public void changeMode() {
        if (modeEdited) {
            setAllEnable(false);
            buttonEnd.setEnabled(true);
            buttonPrev.setEnabled(true);
            buttonNext.setEnabled(true);
            buttonStop.setEnabled(true);
            info.setEnabled(true);
            textMode.setText("Работа алгоритма");
        } else {
            textMode.setText("Редактирование графа");
            setAllEnable(true);
        }

        modeEdited = !modeEdited;
    }

    public void setButtonsEnable(boolean b) {
        buttonInputGraph.setEnabled(b);
        buttonAddVertex.setEnabled(b);
        buttonAddEdge.setEnabled(b);
        buttonGenerateGraph.setEnabled(b);
        buttonStart.setEnabled(b);
        buttonStop.setEnabled(b);
        buttonNext.setEnabled(b);
        buttonPrev.setEnabled(b);
        buttonEnd.setEnabled(b);
    }

    public void setAllEnable(boolean b) {
        graphVisualization.setEnabled(b);
        setButtonsEnable(b);
        menuBar.setEnabled(b);
        info.setEnabled(b);
        save.setEnabled(b);
        load.setEnabled(b);
        file.setEnabled(b);
        textInputAddGraph.setEnabled(b);
        textInputWeight.setEnabled(b);
        textInputVertex1.setEnabled(b);
        textInputVertex2.setEnabled(b);
        textInputMaxWeightForGenerate.setEnabled(b);
        textInputMinWeightForGenerate.setEnabled(b);
        textInputCountEdgesForGenerate.setEnabled(b);
        textInputCountVertexesForGenerate.setEnabled(b);
    }

    public void setLoadListener(ActionListener listener) {
        this.load.addActionListener(listener);
    }

    public void setSaveListener(ActionListener listener) {
        this.save.addActionListener(listener);
    }
}

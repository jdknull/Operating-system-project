/*

PROJECT: First-Come First-Served (FCFS) Scheduling
COURSE: CPIT-260
SECTION: A1
INSTRUCTOR: Dr. Iftikhar Ahmad 
STUDENT: Ahmed Essam Taj / Mohammad Hamdan Alsefri / Fahad Hamad AlSifri 

 */
package fcfs;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FCFS {

    static int minimumArrivalTime, sumCPUBurstTime;
    static int lengthOfEachBlock;
    static final int SCREEN_WIDTH = 700, SCREEN_HEIGHT = 280;
    static final int rectangleUpperPadding = 50, rectangleHeight = 100;
    static int numberOfProcesses;
    static int CPUBurstTime[], arrivalTime[];
    static BufferedReader br;
    static FCFS obj;
    static ImageIcon icon = new ImageIcon("icon.jpg");

    FCFS() {
        this.obj = this;
    }

    public static void main(String[] args) throws NumberFormatException, IOException {

        br = new BufferedReader(new InputStreamReader(System.in));

        Object pNum = JOptionPane.showInputDialog(null, "Enter Number of Process", "First-Come First-Served (FCFS) Scheduling", JOptionPane.CANCEL_OPTION, icon, null, "");
        numberOfProcesses = Integer.parseInt((String) pNum);
        CPUBurstTime = new int[numberOfProcesses];
        arrivalTime = new int[numberOfProcesses];
        int ct[] = new int[numberOfProcesses];     // completion times
        int ta[] = new int[numberOfProcesses];     // turn around times
        int wt[] = new int[numberOfProcesses];     // waiting times
        int pid[] = new int[numberOfProcesses];   // process ids
        float avgwt = 0, avgta = 0;
        int option;

        Object userChoice = JOptionPane.showInputDialog(null, "Enter (1) for FCFS with Ignoring Arrival Time" + "\n" + "Enter (2) for FCFS without Ignoring Arrival Time",
                "First-Come First-Served (FCFS) Scheduling",
                JOptionPane.CANCEL_OPTION, icon, null, "");
        option = Integer.parseInt((String) userChoice);
        switch (option) {
            case 1:
                withoutArrivalTime(ct, ta, wt, pid, avgwt, avgta);
                break;
            case 2:
                withArrivalTime(ct, ta, wt, pid, avgwt, avgta);
                break;

            default:
                JOptionPane.showMessageDialog(null, "Wrong Choice, Try Again");
        }

    }

    public static void withArrivalTime(int ct[], int ta[], int wt[], int pid[], float avgwt, float avgta) throws NumberFormatException, IOException {
        for (int i = 0; i < numberOfProcesses; i++) {

            JTextField text1 = new JTextField();
            JTextField text2 = new JTextField();
            Object[] fields = {
                "Arrival Time for Process " + (i + 1) + " : ", text1,
                "Burst Time for Process " + (i + 1) + " : ", text2
            };

            int input = JOptionPane.showConfirmDialog(null, fields, "First-Come First-Served (FCFS) Scheduling", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);

            arrivalTime[i] = Integer.parseInt(text1.getText());
            CPUBurstTime[i] = Integer.parseInt(text2.getText());
        }

        while (true) {
            int expression;
            Object choice = JOptionPane.showInputDialog(null, "Enter (1) to Draw the Gantt Chart" + "\n" + "Enter (2) to calculate the AVG Waiting Time & TurnAround Time" + "\n" + "Enter (3) to Exit",
                    "First-Come First-Served (FCFS) Scheduling", JOptionPane.CANCEL_OPTION, icon, null, "");
            expression = Integer.parseInt((String) choice);
            switch (expression) {
                case 1:
                    drawGanttChart();
                    break;
                case 2:
                    calculateWaiting_turnAround_Time(ct, ta, wt, pid, CPUBurstTime, arrivalTime, numberOfProcesses, avgwt, avgta);
                    break;
                case 3:
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Wrong Choice, Try Again");
            }
        }
    }

    public static void withoutArrivalTime(int ct[], int ta[], int wt[], int pid[], float avgwt, float avgta) throws NumberFormatException, IOException {

        for (int i = 0; i < numberOfProcesses; i++) {

            JTextField text2 = new JTextField();
            Object[] fields = {
                "Burst Time for Process " + (i + 1) + " : ", text2
            };

            int input = JOptionPane.showConfirmDialog(null, fields, "First-Come First-Served (FCFS) Scheduling", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);

            arrivalTime[i] = 0;
            CPUBurstTime[i] = Integer.parseInt(text2.getText());
        }

        while (true) {
            int expression;
            Object choice = JOptionPane.showInputDialog(null, "Enter (1) to Draw the Gantt Chart" + "\n" + "Enter (2) to calculate the AVG Waiting Time & TurnAround Time" + "\n" + "Enter (3) to Exit",
                    "First-Come First-Served (FCFS) Scheduling", JOptionPane.CANCEL_OPTION, icon, null, "");
            expression = Integer.parseInt((String) choice);
            switch (expression) {
                case 1:
                    drawGanttChart();
                    break;
                case 2:
                    calculateWaiting_turnAround_Time_without_arrival(ct, ta, wt, pid, CPUBurstTime, arrivalTime, numberOfProcesses, avgwt, avgta);
                    break;
                case 3:
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Wrong Choice, Try Again");
            }
        }
    }

    public static void drawGanttChart() throws NumberFormatException, IOException {
        //  int choice;
        sumCPUBurstTime = 0;


        /* calculating the sum of all cpu burst time */
        for (int i = 0; i < numberOfProcesses; i++) {
            sumCPUBurstTime += CPUBurstTime[i];
        }

        /* now the total width of the screen is to be divided into sumCPUBurstTime equal parts */
        lengthOfEachBlock = SCREEN_WIDTH / sumCPUBurstTime;

        /*
                     * claculating the minimum arrival time
         */
        minimumArrivalTime = Integer.MAX_VALUE;
        for (int i = 0; i < numberOfProcesses; i++) {
            if (minimumArrivalTime > arrivalTime[i]) {
                minimumArrivalTime = arrivalTime[i];
            }
        }

        drawGanttChartForFCFS();

    }

    public static void calculateWaiting_turnAround_Time_without_arrival(int ct[], int ta[], int wt[], int pid[], int CPUBurstTime[],
            int arrivalTime[], int numberOfProcesses, float avgwt, float avgta) {

        int temp = 0;

        int total_tt = 0;
        for (int i = 0; i < numberOfProcesses; i++) {
            temp += CPUBurstTime[i];
            total_tt += temp;
        }

        int temp_wait = 0;
        int before = 0;
        int total_wt = 0;
        for (int i = 0; i < numberOfProcesses; i++) {

            if (i == 0) {
                before = CPUBurstTime[i];
                continue;
            }
            temp_wait += before;
            before = CPUBurstTime[i];
            total_wt += temp_wait;
        }
        avgta = total_tt;
        avgwt = total_wt;

        int input = JOptionPane.showConfirmDialog(null, "average waiting time: " + (avgwt / numberOfProcesses) + " m/s " + "\naverage turnaround time: " + (avgta / numberOfProcesses) + " m/s ", "Results",
                JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
    }

    public static void calculateWaiting_turnAround_Time(int ct[], int ta[], int wt[], int pid[], int CPUBurstTime[], int arrivalTime[], int numberOfProcesses, float avgwt, float avgta) {

        int temp;
        //sorting according to arrival times
        for (int i = 0; i < numberOfProcesses; i++) {
            for (int j = 0; j < numberOfProcesses - (i + 1); j++) {
                if (arrivalTime[j] > arrivalTime[j + 1]) {
                    temp = arrivalTime[j];
                    arrivalTime[j] = arrivalTime[j + 1];
                    arrivalTime[j + 1] = temp;
                    temp = CPUBurstTime[j];
                    CPUBurstTime[j] = CPUBurstTime[j + 1];
                    CPUBurstTime[j + 1] = temp;
                    temp = pid[j];
                    pid[j] = pid[j + 1];
                    pid[j + 1] = temp;
                }
            }
        }
        // finding completion times
        for (int i = 0; i < numberOfProcesses; i++) {
            if (i == 0) {
                ct[i] = arrivalTime[i] + CPUBurstTime[i];
            } else {
                if (arrivalTime[i] > ct[i - 1]) {
                    ct[i] = arrivalTime[i] + CPUBurstTime[i];
                } else {
                    ct[i] = ct[i - 1] + CPUBurstTime[i];
                }
            }
            ta[i] = ct[i] - arrivalTime[i];          // turnaround time= completion time- arrival time
            wt[i] = ta[i] - CPUBurstTime[i];          // waiting time= turnaround time- burst time
            avgwt += wt[i];               // total waiting time
            avgta += ta[i];               // total turnaround time
        }
        int input = JOptionPane.showConfirmDialog(null, "average waiting time: " + (avgwt / numberOfProcesses) + " m/s " + "\naverage turnaround time: " + (avgta / numberOfProcesses) + " m/s ", "Results",
                JOptionPane.CLOSED_OPTION, JOptionPane.QUESTION_MESSAGE, icon);

    }

    public static void drawGanttChartForFCFS() {
        FrameForFCFS frameForFCFS = new FrameForFCFS(obj);
    }

    static class FrameForFCFS extends JFrame {

        int arrivalTime[];
        FCFS obj;

        FrameForFCFS(FCFS obj) {
            super("First-Come First-Served (FCFS) Scheduling");
            this.obj = obj;
            //this.setResizable(false);
            this.setVisible(true);
            this.setSize(obj.SCREEN_WIDTH + 100, obj.SCREEN_HEIGHT);
            arrivalTime = obj.arrivalTime.clone();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            this.getContentPane().setBackground(Color.white);

            int currentTime = obj.minimumArrivalTime;
            arrivalTime = obj.arrivalTime.clone();

            int i, j, min, mini = 0;
            int leftStart = 50;
            g = this.getContentPane().getGraphics();
            g.drawString("" + obj.minimumArrivalTime, leftStart, obj.rectangleUpperPadding + obj.rectangleHeight + 20);
            for (j = 0; j < obj.numberOfProcesses; j++) {
                min = Integer.MAX_VALUE;
                for (i = 0; i < obj.numberOfProcesses; i++) {
                    if (min > arrivalTime[i]) {
                        min = arrivalTime[i];
                        mini = i;
                    }
                }
                arrivalTime[mini] = Integer.MAX_VALUE;

                g = this.getContentPane().getGraphics();
                g.drawRect(leftStart, obj.rectangleUpperPadding, obj.lengthOfEachBlock * obj.CPUBurstTime[mini], obj.rectangleHeight);
                g.drawString("P" + (mini + 1), leftStart + 5, obj.rectangleUpperPadding + 50);
                leftStart += obj.lengthOfEachBlock * obj.CPUBurstTime[mini];

                currentTime += obj.CPUBurstTime[mini];
                g.drawString("" + currentTime, leftStart, obj.rectangleUpperPadding + obj.rectangleHeight + 20);

            }
        }
    }

}

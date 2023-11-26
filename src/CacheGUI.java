import javax.swing.*;
import java.awt.*;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class CacheGUI implements ActionListener {
    static Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    static Font bfont = new Font(Font.MONOSPACED, Font.BOLD, 14);
    static ArrayList<JLabel> memblocks = new ArrayList<JLabel>();
    static ArrayList<Integer> seq = new ArrayList<Integer>();
    static int count = 0;
    static NumberFormat formatter = new DecimalFormat("00");
    static NumberFormat formatter3 = new DecimalFormat("000");
    private JButton start;
    private JButton next;
    private JButton end;
    private JTextArea ta;
    private JButton test1;
    private JButton test2;
    private JButton test3;
    private JLabel macValue;
    private JLabel chcValue;
    private JLabel cmcValue;
    private JLabel chrValue;
    private JLabel cmrValue;
    private JLabel amatValue;
    private JLabel tmatValue;
    private Simulation sim;

    public static void main (String[] args) {
        CacheGUI c = new CacheGUI();
    }

    CacheGUI() {
        sim = new Simulation();
        JFrame frame = new JFrame();
        frame.setTitle("S12 Group 3, made by group members, FA + MRU sim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1280,720);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#EBE3D5"));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 1280, 720);
        panel.setBackground(Color.decode("#EBE3D5"));

        JPanel left = new JPanel();
        left.setLayout(new GridLayout(32,1,0,2));
        left.setBackground(Color.decode("#EBE3D5"));
        left.setBounds(50,15,500,650);
        left.setBorder(BorderFactory.createLineBorder(Color.decode("#854b15"), 3));

        JPanel right = new JPanel();
        right.setLayout(new GridLayout(4,2,20,20));
        right.setBackground(Color.decode("#EBE3D5"));
        right.setBounds(600,40,600,600);

        // Cache Blocks
        for(int i = 0; i < 32;i++) {
            JLabel lab = new JLabel("Block " + formatter.format(i));
            lab.setOpaque(true);
            lab.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            lab.setBackground(Color.white);
            lab.setForeground(Color.black);
            lab.setFont(font);
            lab.setHorizontalAlignment(SwingConstants.CENTER);
            lab.setVerticalAlignment(SwingConstants.CENTER);
            left.add(lab);
            memblocks.add(lab);
        }

        // Input Box
        ta = new JTextArea();
        ta.setFont(new Font("Monospaced", Font.PLAIN, 20));

        JScrollPane scrollPane = new JScrollPane(ta);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border lineBorder = BorderFactory.createLineBorder(Color.decode("#854b15"), 3, true);
        CompoundBorder compoundBorder = new CompoundBorder(lineBorder, paddingBorder);

        scrollPane.setBorder(compoundBorder);
        scrollPane.setPreferredSize(new Dimension(200, 500));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        right.add(scrollPane, BorderLayout.CENTER);

        // Right Side Start, Next, End Button
        JPanel rightButtons = new JPanel();

        start = new RoundedButton("Start", 20);
        next = new RoundedButton("Next", 20);
        end = new RoundedButton("End", 20);

        start.setBackground(Color.decode("#854b15"));
        start.setForeground(Color.WHITE);
        start.setFont(bfont);
        start.addActionListener(this);

        next.setBackground(Color.decode("#854b15"));
        next.setForeground(Color.WHITE);
        next.setFont(bfont);
        next.addActionListener(this);

        end.setBackground(Color.decode("#854b15"));
        end.setForeground(Color.WHITE);
        end.setFont(bfont);
        end.addActionListener(this);

        next.setEnabled(false);
        end.setEnabled(false);

        rightButtons.setLayout(new GridLayout(3, 1, 10, 10));
        rightButtons.setBackground(Color.decode("#EBE3D5"));
        rightButtons.add(start);
        rightButtons.add(next);
        rightButtons.add(end);

        // Test Cases 1-3
        JPanel tests = new JPanel();
        tests.setLayout(new GridLayout(1, 3,20,20));
        tests.setBackground(Color.decode("#EBE3D5"));

        test1 = Teststyle("Test Case 1");
        test2 = Teststyle("Test Case 2");
        test3 = Teststyle("Test Case 3");

        tests.add(test1);
        tests.add(test2);
        tests.add(test3);

        JPanel output = new JPanel();
        output.setLayout(new GridLayout(7,1));
        output.setBackground(Color.white);
        output.setBorder(BorderFactory.createLineBorder(Color.decode("#854b15"), 3));
        output.setLayout(new GridLayout(7,2,5,5));

        String[] labelNames = {
            "Memory Access Count:",
            "Cache Hit Count:",
            "Cache Miss Count:",
            "Cache Hit Rate:",
            "Cache Miss Rate:",
            "Average Memory Access Time:",
            "Total Memory Access Time:"
        };

        macValue = new JLabel("0");
        chcValue = new JLabel("0");
        cmcValue = new JLabel("0");
        chrValue = new JLabel("0");
        cmrValue = new JLabel("0");
        amatValue = new JLabel("0");
        tmatValue = new JLabel("0");

        JLabel[] labelValues = {
            macValue,
            chcValue,
            cmcValue,
            chrValue,
            cmrValue,
            amatValue,
            tmatValue
        };

        for (int i = 0; i < labelNames.length; i++) {
            JLabel label = new JLabel(labelNames[i]);
            label.setFont(bfont);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            output.add(label);

            labelValues[i].setFont(font);
            labelValues[i].setHorizontalAlignment(SwingConstants.CENTER);
            output.add(labelValues[i]);
        }

        right.add(tests);
        right.add(rightButtons);
        right.add(output);

        panel.add(left);
        panel.add(right);

        frame.add(panel,JFrame.CENTER_ALIGNMENT);
        frame.setVisible(true);
        frame.setEnabled(true);
    }

    // Test Cases button style
    private JButton Teststyle(String text) {
        RoundedButton button = new RoundedButton(text, 20);
        button.setBackground(Color.decode("#854b15"));
        button.setForeground(Color.WHITE);
        button.addActionListener(this);
        button.setFont(bfont);
        return button;
    }

    public class RoundedButton extends JButton {
        private int cornerRadius;

        public RoundedButton(String label, int radius) {
            super(label);
            this.cornerRadius = radius;
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public void changeBlocks(int[] arr, int[] intout, double[] doubOut) {
        //remember to set font
        int i =0;
        for(i = 0; i< arr.length; i++) {
            if(arr[i]!=-1) {
                memblocks.get(i).setText("" + arr[i]);
            } else {
                memblocks.get(i).setText("Block " + formatter.format(i));
            }
            memblocks.get(i).setFont(font);
            memblocks.get(i).setBackground(Color.white);
        }

        memblocks.get(intout[3]).setBackground(Color.decode("#f7ba3e"));
        macValue.setText(String.valueOf(intout[0]));
        chcValue.setText(String.valueOf(intout[1]));
        cmcValue.setText(String.valueOf(intout[2]));
        chrValue.setText(String.format("%.2f", doubOut[0]));
        cmrValue.setText(String.format("%.2f", doubOut[1]));
        amatValue.setText(String.format("%.2f T", doubOut[2]));
        tmatValue.setText(String.format("%.2f T", doubOut[3]));

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==this.start) {
            count=0;
            Arrays.fill(sim.cacheBlockArray, -1);
            seq.clear();
            String s = ta.getText();
            s = s.replace("\n", "");
            s = s.replace('\r', ' ');

            try
            {
                s=s.trim();
                String[] stringseq = s.split(" ");
                s=s.trim();
                for(int i = 0; i<stringseq.length; i++)
                {
                    if(stringseq[i]!="") seq.add(Integer.parseInt(stringseq[i]));
                }
                start.setEnabled(false);
                next.setEnabled(true);
                end.setEnabled(true);
                int[] temp = {0,0,0,0};
                double[] dtemp = {0,0,0,0};
                changeBlocks(sim.cacheBlockArray,temp,dtemp);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else if(e.getSource()==this.next)
        {
            count++;
            //System.out.println("Next");
            Integer[] arr = new Integer[count];
            for(int i = 0; i<count; i++) {
                arr[i] = seq.get(i);
            }

            if(count==seq.size()) {
                next.setEnabled(false);
                end.setEnabled(false);
                start.setEnabled(true);
                count=0;
                sim.mapping(arr, 1);
            } else {
                sim.mapping(arr, 0);
            }
            int[] intout = sim.getIntOutputs();
            double[] doubOut = sim.getDoubOutputs();
            changeBlocks(sim.cacheBlockArray, intout,doubOut);
        }
        else if(e.getSource()==this.end)
        {
            Integer[] arr = new Integer[seq.size()];
            arr = seq.toArray(arr);
            sim.mapping(arr,1);
            end.setEnabled(false);
            next.setEnabled(false);
            start.setEnabled(true);
            int[] intout = sim.getIntOutputs();
            double[] doubOut = sim.getDoubOutputs();
            changeBlocks(sim.cacheBlockArray,intout,doubOut);
        }
        else if(e.getSource()==this.test1)
        {
            //System.out.println("Testing 1");
            Integer[] seqSeq = new Integer[8*sim.cacheBlocks];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2*sim.cacheBlocks; j++) {
                    seqSeq[(2*i*sim.cacheBlocks) + j] = j;
                }
            }
            String s = "";
            for(int i =0; i<seqSeq.length;i++)
            {
                s = s + " "+ formatter.format(seqSeq[i]);
                if(i%15==0)
                s = s+"\n";
            }
            ta.setText(s);
        }
        else if(e.getSource()==this.test2)
        {
            //System.out.println("Testing 2");
            Integer[] ranSeq = new Integer[4*sim.cacheBlocks];
            for (int i = 0; i < 4*sim.cacheBlocks; i++)
            {
                Random rand = new Random();
                ranSeq[i] = rand.nextInt(4*sim.cacheBlocks + 1);
            }
            String s = "";
            for(int i =0; i<ranSeq.length;i++)
            {
                s = s + " "+ formatter3.format(ranSeq[i]);
                if(i%11==0)
                s = s+"\n";
            }
            ta.setText(s);
        }
        else if(e.getSource()==this.test3)
        {
            //System.out.println("Testing 3");
            Integer[] midSeq = new Integer[4*(2*sim.cacheBlocks+(sim.cacheBlocks-2))];
            int midSeqIndex = 0;
            for (int i = 0; i < 4; i++) {
                // First half of the sequence
                for (int j = 0; j < sim.cacheBlocks-1; j++) {
                    midSeq[midSeqIndex++] = j;
                }
                // Mid-repeat sequence
                for (int j = 1; j < sim.cacheBlocks-1; j++) {
                    midSeq[midSeqIndex++] = j;
                }
                // Second half of the sequence
                for (int j = sim.cacheBlocks-1; j < 2 * sim.cacheBlocks; j++) {
                    midSeq[midSeqIndex++] = j;
                }
            }
            String s = "";
            for(int i =0; i<midSeq.length;i++){
                s = s+" "+formatter.format(midSeq[i]);
                if(i%15==0)
                s = s+"\n";
            }
            ta.setText(s);
        }
    }
}

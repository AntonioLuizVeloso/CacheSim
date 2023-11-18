import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CacheGUI implements ActionListener {
    static Font font = new Font(Font.SANS_SERIF, Font.ITALIC, 14);
    static ArrayList<JLabel> memblocks = new ArrayList<JLabel>();
    static ArrayList<Integer> seq = new ArrayList<Integer>();
    static int count = 0;
    private JButton start;
    private JButton next;
    private JButton end;
    private TextArea ta;
    private JButton test1;
    private JButton test2;
    private JButton test3;
    private JLabel mac ;
    private JLabel chc ;
    private JLabel cmc ;
    private JLabel chr ;
    private JLabel cmr ;
    private JLabel amat;
    private JLabel tmat;
    private Simulation sim;
    
    public static void main (String[] args)
    {
        CacheGUI c = new CacheGUI();
    }
    CacheGUI()
    {
        sim = new Simulation();
        JFrame frame = new JFrame();
        frame.setTitle("S12 Group 3, made by group members, FA + MRU sim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1270,720);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#EBE3D5"));
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(25, 25, 1200, 630);
        //panel.setSize(1200,650);
        panel.setBackground(Color.decode("#776B5D"));


        JPanel left = new JPanel();
        left.setLayout(new GridLayout(32,1,5,5));
        left.setBackground(Color.decode("#EBE3D5"));
        left.setBounds(0,15,600,600);

        JPanel right = new JPanel();
        right.setLayout(new GridLayout(4,2,20,20));
        right.setBackground(Color.decode("#EBE3D5"));
        right.setBounds(600,15,600,600);

        for(int i = 0; i<32;i++)
        {
            JLabel lab = new JLabel("Block "+i);
            lab.setOpaque(true);
            lab.setBorder(new LineBorder(Color.decode("#EBE3D5")));
            lab.setBackground(Color.white);
            lab.setPreferredSize(new Dimension(50,10));
            lab.setFont(font);
            lab.setHorizontalAlignment(SwingConstants.CENTER);
            left.add(lab);
            memblocks.add(lab);
        }

        JPanel rightButtons = new JPanel();

        ta = new TextArea();
        //ta.setBounds(0, 0, 200, 500);
        ta.setSize(200, 500);

        start = new JButton("Start");
        next = new JButton("Next");
        end = new JButton("End");

        start.addActionListener(this);
        next.addActionListener(this);
        end.addActionListener(this);

        next.setEnabled(false);
        end.setEnabled(false);

        rightButtons.setLayout(new GridLayout(3, 1, 10, 10));
        rightButtons.add(start);
        rightButtons.add(next);
        rightButtons.add(end);

        JPanel tests = new JPanel();
        tests.setLayout(new GridLayout(1, 3,20,20));

        test1 = new JButton("Test Case 1");
        test2 = new JButton("Test Case 2");
        test3 = new JButton("Test Case 3");

        test1.addActionListener(this);
        test2.addActionListener(this);
        test3.addActionListener(this);

        tests.add(test1);
        tests.add(test2);
        tests.add(test3);

        JPanel output = new JPanel();
        output.setLayout(new GridLayout(7,1));
        mac = new JLabel("Memory Access Count: ");
        chc = new JLabel("Cache Hit Count: ");
        cmc = new JLabel("Cache Miss Count: ");
        chr = new JLabel("Cache Hit Rate: ");
        cmr = new JLabel("Cache Miss Rate: ");
        amat = new JLabel("Average Memory Access Time:");
        tmat = new JLabel("Total Memory Access Time:");

        output.add(mac);
        output.add(chc);
        output.add(cmc);
        output.add(chr);
        output.add(cmr);
        output.add(amat);
        output.add(tmat);

        right.add(ta);
        right.add(tests);
        right.add(rightButtons);
        right.add(output);
       

        panel.add(left);
        panel.add(right);

        frame.add(panel,JFrame.CENTER_ALIGNMENT);
        frame.setVisible(true);
        frame.setEnabled(true);
    }

    public void changeBlocks(int[] arr, int[] intout, double[] doubOut)
    {
        //remember to set font
        int i =0;
        for(i = 0; i< arr.length; i++)
        {

            if(arr[i]!=-1)
            {
                memblocks.get(i).setText(""+arr[i]);
                memblocks.get(i).setBackground(Color.white);
            }
                
            else
                memblocks.get(i).setText("Block "+i);
        }
            
        memblocks.get(intout[3]).setBackground(Color.green);
        mac.setText("Memory Access Count: "+intout[0]);
        chc.setText("Cache Hit Count: "+intout[1]);
        cmc.setText("Cache Miss Count: "+intout[2]);
        chr.setText("Cache Hit Rate: "+doubOut[0]);
        cmr.setText("Cache Miss Rate: "+doubOut[1]);
        amat.setText("Average Memory Access Time: "+doubOut[2]+" T");
        tmat.setText("Total Memory Access Time: "+doubOut[3]+" T");
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==this.start)
        {
            count=0;
            Arrays.fill(sim.cacheBlockArray, -1);
            seq.clear();
            System.out.println("Started");
            String s = ta.getText();
            s= s.replace("\n", "");
            s= s.replace('\r', ' ');
            System.out.println(s);

            try
            {
                s=s.trim();
                String[] stringseq = s.split(" ");
                s=s.trim();
                for(int i = 0; i<stringseq.length; i++)
                {
                    if(stringseq[i]!="")
                        seq.add(Integer.parseInt(stringseq[i]));
                    //System.out.println(seq.get(i));
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
            System.out.println("Next");
            Integer[] arr = new Integer[count];
            for(int i = 0; i<count; i++)
            {
                arr[i] = seq.get(i);
            }

            if(count==seq.size())
            {
                next.setEnabled(false);
                end.setEnabled(false);
                start.setEnabled(true);
                count=0;
                sim.mapping(arr, 1);
            }
            else
                sim.mapping(arr, 0);     
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
            System.out.println("Testing 1");
            Integer[] seqSeq = new Integer[8*sim.cacheBlocks];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 2*sim.cacheBlocks; j++) {
                    seqSeq[(2*i*sim.cacheBlocks) + j] = j;
                }
            }
            String s = "";
            for(int i =0; i<seqSeq.length;i++)
            {
                s = s+" "+seqSeq[i];
                if(i%7==0)
                s = s+"\n";
            }
            ta.setText(s);
            
        }
        else if(e.getSource()==this.test2)
        {
            System.out.println("Testing 2");
            Integer[] ranSeq = new Integer[4*sim.cacheBlocks];
            for (int i = 0; i < 4*sim.cacheBlocks; i++)
            {
                Random rand = new Random();
                ranSeq[i] = rand.nextInt(4*sim.cacheBlocks + 1);
            }
            String s = "";
            for(int i =0; i<ranSeq.length;i++)
            {
                s = s+" "+ranSeq[i];
                if(i%7==0)
                s = s+"\n";
            }
            ta.setText(s);
        }
        else if(e.getSource()==this.test3)
        {
            System.out.println("Testing 3");
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
            for(int i =0; i<midSeq.length;i++)
            {
                s = s+" "+midSeq[i];
                if(i%7==0)
                s = s+"\n";
            }
            ta.setText(s);
        }
    }
}

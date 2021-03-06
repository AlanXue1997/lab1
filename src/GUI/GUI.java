package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
/*
 * To do list:
 * 修复coordination函数有可能出现的顺序错误——不仅处理子节点，也处理所有子节点之前的节点
 */

// hard to understand some of the functions
public class GUI {
  JFrame frame;
  Picture G;
  GraphDraft g;
  DrawPanel panel;
  String path;
  JScrollPane scrollPane;
  JScrollPane scrollPane2;
  boolean flag;
  int nowstep;
  int[] walks;
  private JTextField textField1;
  private JTextField textField2;
  private JTextField textField3;
  private JTextField textField4;
  private JTextArea textField5;

  public void setG(Picture G) {
	  this.G = G;
  }
  
  public void setg(GraphDraft g) {
	  this.g = g;
  }
  
  public Picture getG() {
	  return G;
  }
  
  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          GUI window = new GUI();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public GUI() {
    initialize();
  }

  public void showDirectedGraph(Picture g) {
    return;
  }

  public String queryBridgeWords(String word1, String word2) {
	  for (int i = 0; i < g.getPoints().length; i++) {
          if (g.getPoints()[i].getSt() != null
              && (g.getPoints()[i].getSt().equals(word1)
                  || g.getPoints()[i].getSt().equals(word2))) {
            g.getPointstate()[i] = 2;
          } else {
            g.getPointstate()[i] = 0;
          }
          for (int j = 0; j < g.getPoints().length; j++) {
            g.getEdgestate()[i][j] = 0;
          }
        }
        int[] result = G.findBridge(word1, word2);
        if (result[0] == -1) {
          return "“" + word1 + "” doesn't exist!";
        } else if (result[0] == -2) {
          return "“" + word2 + "” doesn't exist!";
        } else if (result[0] == -3) {
          return "“" + word1 + "” and “" + word2 + "” doesn't exist!";

        } else if (result[0] > 0) {
          String text = "The bridge words from “" + word1 + "” to “" + word2 + "” is:";
          for (int i = 0; i < result[0]; i++) {
            text += g.getPoints()[result[i + 1]].getSt();
            text += " ";
            g.getPointstate()[result[i + 1]] = 1;
          }
          return text;
        }
        return "No bridge words from “" + word1 + "” to “" + word2 + "”!";
  }

  public String gernerateNewText(String inputText) {
    return null;
  }

  
  public String calcShortestPath(String word1, String word2) {
	  if (word2 != null && !word2.equals("")) {
          int[] result = G.shortestPath(word1, word2);

          for (int i = 0; i < g.getPoints().length; i++) {
            g.getPointstate()[i] = 0;
            for (int j = 0; j < g.getPoints().length; j++) {
              g.getEdgestate()[i][j] = 0;
            }
          }

          if (result[0] == 0) {
            return word1 + " and " + word2 + " are not reachable!";
          } else if (result[0] == -1) {
            return word1 + " doesn't exist!";
          } else if (result[0] == -2) {
            return word2 + " doesn't exist!";
          } else if (result[0] == -3) {
            return word1 + " and " + word2 + " doesn't exist!";
          } else {

            String shortpath = g.getPoints()[result[1]].getSt(); // word1
            g.getPointstate()[result[1]] = 1;
            for (int i = 2; i <= result[0]; i++) {
              g.getEdgestate()[result[i - 1]][result[i]] = 1;
              if (g.getPoints()[result[i]].getSt() != null) {
                g.getPointstate()[result[i]] = 1;
                shortpath += " => " + g.getPoints()[result[i]].getSt();
              }
            }
            shortpath += " 路径长度为： " + result[result[0] + 1];
            return shortpath;
          }
        } else {
          boolean have = false;
          for (int i = 0; i < g.getPoints().length; i++) {
            if (g.getPoints()[i].getSt() != null
                && g.getPoints()[i].getSt().equals(word1)) {
              have = true;
            }
          }
            int[] result = G.shortestPath(word1);
            String s = "";
            for (int i = 0; i < result.length; i++) {
              if (g.getPoints()[i].getSt() != null) {
                s += g.getPoints()[i].getSt() + ":" + result[i] + "\n";
              }
            }
            return s;
        }
  }

  public String randomWalk() {
    return null;
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 1339, 871);
    // frame.setBounds(0,0,2000,2000);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);

    JButton btnNewButton = new JButton("选择文件");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
          G = new Picture(jfc.getSelectedFile().getAbsolutePath());
          path = jfc.getSelectedFile().getAbsolutePath();

          // 获取图的信息，里面包含所有需要画的点的信息和边的信息
          g = G.getGraphDraft();
          panel.display(g);
          walks = G.randomWalk();
          nowstep = 1;
          flag = false;
          // 这里是如何读取一个点的信息，x和y是坐标，这个坐标只是相对位置，实际要放在哪要具体计算。
          // 普通的点st存储字符串内容。有些点st=null，这样的点不需要显示出来，但他们的坐标在画边时有用
        }
      }
    });
    btnNewButton.setBounds(1194, 7, 127, 39);
    frame.getContentPane().add(btnNewButton);

    JLabel lblNewLabel = new JLabel("第一个单词：");
    lblNewLabel.setBounds(905, 57, 84, 27);
    frame.getContentPane().add(lblNewLabel);

    textField1 = new JTextField();
    textField1.setBounds(1016, 57, 105, 26);
    frame.getContentPane().add(textField1);
    textField1.setColumns(10);

    JLabel lblNewLabel1 = new JLabel("第二个单词");
    lblNewLabel1.setBounds(1141, 62, 65, 16);
    frame.getContentPane().add(lblNewLabel1);

    textField2 = new JTextField();
    textField2.setBounds(1233, 57, 88, 26);
    frame.getContentPane().add(textField2);
    textField2.setColumns(10);

    JButton btnNewButton1 = new JButton("计算最短路");
    btnNewButton1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String word1 = textField1.getText().replace(" ", "");
        String word2 = textField2.getText().replace(" ", "");
        textField5.setText(calcShortestPath(word1, word2));
        panel.display(g);
      }
    });
    btnNewButton1.setBounds(1188, 107, 133, 29);
    frame.getContentPane().add(btnNewButton1);

    JButton btnNewButton2 = new JButton("查询桥接词");
    btnNewButton2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String word1 = textField1.getText().replace(" ", "");
        String word2 = textField2.getText().replace(" ", "");
        String text = queryBridgeWords(word1, word2);
          textField5.setText(text);
          panel.display(g);
        }
      });
    btnNewButton2.setBounds(966, 107, 133, 29);
    frame.getContentPane().add(btnNewButton2);

    textField3 = new JTextField();
    textField3.setBounds(905, 431, 297, 74);
    frame.getContentPane().add(textField3);
    textField3.setColumns(10);

    JButton btnNewButton3 = new JButton("提交");
    btnNewButton3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String line = textField3.getText();
        String result = G.newText(line);
        textField4.setText(result);
      }
    });
    btnNewButton3.setBounds(1216, 454, 105, 51);
    frame.getContentPane().add(btnNewButton3);

    textField4 = new JTextField();
    textField4.setBounds(895, 568, 423, 133);
    frame.getContentPane().add(textField4);
    textField4.setColumns(10);

    JLabel lblNewLabel2 = new JLabel("查询结果");
    lblNewLabel2.setBounds(905, 279, 65, 33);
    frame.getContentPane().add(lblNewLabel2);

    JLabel lblNewLabel3 = new JLabel("新生成文本");
    lblNewLabel3.setBounds(895, 529, 88, 27);
    frame.getContentPane().add(lblNewLabel3);

    JButton btnNewButton4 = new JButton("随机游走");
    btnNewButton4.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String s;
        if (flag) {
          s = textField4.getText();
        } else {
          s = "";
        }
        while (g.getPoints()[walks[nowstep]].getSt() == null) {
          nowstep += 1;
          if (nowstep > walks[0]) {
            walks = G.randomWalk();
            nowstep = 1;
            s = "";
            System.out.println("hahha");
          }
        }
        s += " " + g.getPoints()[walks[nowstep]].getSt();
        nowstep += 1;
        flag = true;
        if (nowstep > walks[0]) {
          walks = G.randomWalk();
          nowstep = 1;
          flag = false;
        }
        textField4.setText(s);
      }
    });
    btnNewButton4.setBounds(1160, 727, 161, 41);
    frame.getContentPane().add(btnNewButton4);

    scrollPane = new JScrollPane();
    scrollPane.setBounds(6, 6, 877, 795);
    frame.getContentPane().add(scrollPane);

    scrollPane2 = new JScrollPane();
    scrollPane2.setBounds(993, 186, 328, 204);
    frame.getContentPane().add(scrollPane2);

    JButton btnNewButton5 = new JButton("保存图片");
    btnNewButton5.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.savePicture(scrollPane.getPreferredSize().width,
            scrollPane.getPreferredSize().height);
        System.out
            .println("" + scrollPane.getWidth() + " " + scrollPane.getHeight());
      }
    });
    btnNewButton5.setBounds(966, 6, 117, 40);
    frame.getContentPane().add(btnNewButton5);

    panel = new DrawPanel();
    panel.setBounds(25, 25, 3000, 3000);
    frame.getContentPane().add(panel);
    scrollPane.setViewportView(panel);

    textField5 = new JTextArea();
    textField5.setBounds(993, 186, 328, 204);
    frame.getContentPane().add(textField5);
    textField5.setColumns(10);

    scrollPane2.setViewportView(textField5);
  }
}

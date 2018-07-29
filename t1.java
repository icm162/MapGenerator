import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class t1 {
	public static JFrame y;
	public static JLabel process;
	public static Container c;
	public static void main(String[] args) {
		y = new JFrame();
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		y.setBounds(0, 0, width, height - 40);
		y.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		y.setResizable(false);
		y.setVisible(true);
		
		c = y.getContentPane();
		c.setLayout(null);
		
		//实验参数
		final int dls = 8;
		
		ImageIcon i1 = new GlobalMapGenerator().generateWithLand(900, 900, dls);
		ImageIcon i2 = new GlobalMapGenerator().randLand(2, 100);
		
		final JLabel l1 = new JLabel(i1);
		l1.setBounds(100, 50, 900, 900);
		
		final JLabel l2 = new JLabel(i2);
		l2.setBounds(1170, 300, 100, 100);
		
		JLabel regen = new JLabel("重 新 绘 制");
		regen.setLayout(null);
		regen.setBounds(1200, 600, 120, 50);
		regen.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				l1.setIcon(new GlobalMapGenerator().generateWithLand(900, 900, dls));
				l2.setIcon(new GlobalMapGenerator().randLand(2, 100));
			}
		});
			
		c.add(l1);
		c.add(l2);
		c.add(regen);
		c.repaint();
		
		System.out.println("done");
		
	}
}

package myself;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NoteBook extends JPanel {
	private static final long serialVersionUID = 5692290911249557954L;
	
	JTextArea jTextArea = new JTextArea();
	JScrollPane JScrollPane = new JScrollPane(jTextArea);

	JButton jButton = new JButton("����");

	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();

	public NoteBook() {
		open();
		jTextArea.setFont(new Font("����", 1, 15));
		jButton.setContentAreaFilled(false);
		jButton.setFocusPainted(false);
		JScrollPane.setVisible(true);
		jPanel1.setLayout(new BorderLayout());
		jPanel1.add(JScrollPane, BorderLayout.CENTER);
		jPanel1.setPreferredSize(new Dimension(500, 230));

		jPanel2.setLayout(new BorderLayout());
		jPanel2.add(jButton, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		add(jPanel1, BorderLayout.NORTH);
		add(jPanel2, BorderLayout.SOUTH);

		jButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					save();
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});
	}

	 //�򿪵ľ��巽��
	  private void open() {
	    try {
	      File file = new File(System.getProperty("user.dir") + "\\source\\note.txt");
	      BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
	      byte[] b = new byte[in.available()];
	      in.read(b, 0, b.length);
	      jTextArea.append(new String(b, 0, b.length));
	      in.close();


	    }
	    catch (IOException ex) {
	    	jTextArea.setText("Error opening ");
	    }
	  }
	  
	// �������
	private void save() {
		try {
			File file = new File(System.getProperty("user.dir") + "\\source\\note.txt");
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			byte[] b = (jTextArea.getText()).getBytes();
			out.write(b, 0, b.length);
			out.close();
			JOptionPane.showMessageDialog(null, "����ɹ�");
		} catch (IOException ex) {
			jTextArea.setText("Error saving ");
		}
	}

}
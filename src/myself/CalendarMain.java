package myself;

import javax.swing.JFrame;

public class CalendarMain {
	public static void main(String[] args) {
		CalendarsView frame = new CalendarsView();
		frame.setTitle("��������");
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
}

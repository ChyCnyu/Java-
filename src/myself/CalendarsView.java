package myself;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;


public class CalendarsView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton[] button_day = new JButton[42];
	private JButton[] button_week = new JButton[7];
	
	private Date now_date = new Date();
	private int now_year = now_date.getYear();
	private int now_month = now_date.getMonth();
	
	private String year_int = null; //�����û�ѡ������
	private int month_int; 
	
	//private String[] lunar_day = new String[42];
	static Calendar c = Calendar.getInstance();
	static int year = c.get(Calendar.YEAR);
	static int month = c.get(Calendar.MONTH) + 1;
	int monthday = 0;
	
	private boolean todayFlag;
	String[] weekdaystr = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };
	Object[][] monthview = new String[6][7];
	JPanel dayPanel = new JPanel();
	JPanel panel1 = new JPanel();
	JPanel calPanel = new JPanel();
	JPanel alarmDemo = new AlarmDemo();
	JPanel noteBook = new NoteBook();
	JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP);

	JButton Prebutton = new JButton("��һ��");
	JButton Nextbutton = new JButton("��һ��");
	JButton PreYear = new JButton("��һ��");
	JButton NextYear = new JButton("��һ��");
	JButton Change = new JButton(year + "��" + month + "��");

	JLabel nowLabel = new JLabel(year + "��" + month+ "��" + (c.get(Calendar.DATE)) + "��", JLabel.CENTER);
	JLabel timeLabel = new JLabel("---", JLabel.LEFT);
	
	DefaultTableModel model = new DefaultTableModel();
	JTable JTable;
	JScrollPane JScrollPane;

	TrayIcon trayIcon;//����ͼ��
    SystemTray systemTray;//ϵͳ����
	public CalendarsView() {
		//���̻�
		systemTray = SystemTray.getSystemTray();//���ϵͳ���̵�ʵ��   
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File(System.getProperty("user.dir")+"\\source\\360.png")));
        }
        catch (IOException e1) {
        	e1.printStackTrace();
        }
       
        
        this.addWindowListener(
                new WindowAdapter() {   
                    public void windowIconified(WindowEvent e) {   
                    	int n = JOptionPane.showConfirmDialog(null, "�Ƿ���С������?", "��С", JOptionPane.YES_NO_OPTION);  
                        if (n == JOptionPane.YES_OPTION) {  
                        	dispose();//������С��ʱdispose�ô��� 
                        	try {
								systemTray.add(trayIcon);
							} catch (AWTException e1) {
								e1.printStackTrace();
							}//�������̵�ͼ��
                        } else if (n == JOptionPane.NO_OPTION) {  
                        	systemTray.remove(trayIcon);
                        } 
                        
                    }   
                });
        
        trayIcon.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if(e.getClickCount() == 2)//˫�����̴�������
                            setExtendedState(Frame.NORMAL);
                            setVisible(true);
                            systemTray.remove(trayIcon);
                    }
                });        
        
		
		//ʱ����
		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				timeLabel.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
			}
		});
		timer.start();

		nowLabel.setFont(new java.awt.Font("Dialog", 1, 15));
		nowLabel.setForeground(Color.blue);
		dataOfDay();
		dayPanel.setLayout(new BorderLayout());
		dayPanel.add(nowLabel, BorderLayout.NORTH);
		//dayPanel.add(JScrollPane, BorderLayout.CENTER);
		// �Ϸ���ť
		panel1.setLayout(new GridLayout(1, 3));
		panel1.setBackground(Color.white);
		Prebutton.setContentAreaFilled(false);
		Prebutton.setFocusPainted(false);
		Change.setContentAreaFilled(false);
		Change.setFocusPainted(false);
		Nextbutton.setContentAreaFilled(false);
		Nextbutton.setFocusPainted(false);

		panel1.add(Prebutton);
		panel1.add(Change);
		panel1.add(Nextbutton);

		calPanel.setLayout(new BorderLayout());
		calPanel.add(panel1, BorderLayout.NORTH);
		calPanel.add(dayPanel, BorderLayout.CENTER);
		calPanel.add(timeLabel, BorderLayout.SOUTH);

		jTabbedPane.add(calPanel, "����");
		jTabbedPane.add(alarmDemo,"����"); 
		jTabbedPane.add(noteBook,"���±�"); 

		add(jTabbedPane, BorderLayout.NORTH);

		// ��ť������
		Prebutton.addActionListener(new PressButton());
		Nextbutton.addActionListener(new PressButton());
	}

	public void dataOfDay() {
		JPanel panel_day = new JPanel();
		panel_day.setLayout(new GridLayout(7, 7, 3, 3));
		for(int i = 0; i < 7; i++) {
			button_week[i] = new JButton("");
			button_week[i].setText(weekdaystr[i]);
			button_week[i].setForeground(Color.red);
			panel_day.add(button_week[i]);
			
		}
		button_week[0].setForeground(Color.red);
		button_week[6].setForeground(Color.red);
		for(int i = 0; i < 42; i++) {
			button_day[i] = new JButton("");
			panel_day.add(button_day[i]);
		}  //������ڣ��ŵ��������
		this.paintDay();
		JPanel panel_main = new JPanel();  //���������������
		panel_main.setLayout(new BorderLayout());  //�߽粼�ֹ�����
		panel_main.add(panel_day,BorderLayout.SOUTH);
		getContentPane().add(panel_main);
	}
	
	private void paintDay() {
	
		if(this.todayFlag) {
			month_int = now_month;//Ҫ����ʾ��������� 
		} else {
			JButton button = new JButton();
			if (button == Prebutton) {
				month_int = month - 1;
				if (month <= 0) {
					year--;
					month_int = month + 12;
				}
			} else {
				month_int = month + 1;
				if (month > 12) {
					year++;
					month_int = month - 12;
				}
			}
		}
		Date firstDay = new Date(now_year, month_int, -2);//������µĵ�һ��
		GregorianCalendar cal = new GregorianCalendar();//����һ��Calendar��ʵ��
		cal.setTime(firstDay);
		
		int days = 0;//���ĳ���·ݵ�����
		int day_week = 0;//���ĳ���·ݵĵ�һ��ʹ���ڼ�����ֵ
		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10  || month == 12) {
			days = 31; 
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			days = 30;
		} else {
			if(cal.isLeapYear(now_year)){
				days = 29;
			} else {
				days = 28;
			}//���£�������꣬����29�죬������28��
		}//�ж��Ǽ��·ݣ����������趨day��ֵ�����ж��·�Ҫ�ж��Ƿ�����
		day_week = firstDay.getDay();
		int count = 1;
  /**
   * ���ư�ť������������Ҫ����ѡ�����·ݵĵ�һ�������ڼ���ȷ�����ư�ť����ʼλ��
   * ����day_week��������Ҫ���Ƶ���ʼλ�ã�������Щû����ֵ������ʾ�İ�ťҪ�ÿ�
   */
		String[] LunarDate=new String[42];
		Lunar lunar = new Lunar();  
		
		for(int i = day_week; i < day_week + days; count++, i++){
			LunarDate[i]=lunar.getLunarDate( year,  month_int - 1, i); 
			if(i%7 == 0|| i == 6 || i == 13 || i == 20 || i == 27 || i == 34 || i == 41) {
    //����Ǳ߽��ϵİ�ť�������ú�ɫ��������ʾ��ĩ
				if(i == day_week + now_date.getDate() - 1) {
					button_day[i].setForeground(Color.blue);//�������һ������������ɫ��ʾ
					button_day[i].setText(count + LunarDate[i]);
				} else {
					button_day[i].setForeground(Color.red);//�����߽��ϵİ�ť�е������ú�ɫ
					button_day[i].setText(count + LunarDate[i]);
				}
			} else {
				if(i == day_week + now_date.getDate() - 1){
					button_day[i].setForeground(Color.blue);//�������һ������������ɫ��ʾ
					button_day[i].setText(count + LunarDate[i]);
				} else {
					button_day[i].setForeground(Color.black);//һ��λ�õİ�ť�ϵ������ú�ɫ��ʾ
					button_day[i].setText(count + LunarDate[i]);    
				}
			}
		}
			if(day_week == 0){
				//����û��������ֵ��ʾ�İ�ť�����ÿմ���
				for(int i =days; i < 42; i++){
					button_day[i].setText("");//�����һ�������գ��򽫵�һ��ǰ��İ�ť�ÿ�
				}
			} else{
		  for(int i = 0; i < day_week; i++){
			  button_day[i].setText("");//�����һ�첻�����գ��򽫵�һ��ǰ��İ�ť�ÿ�
		  }  
	  }
			/**/
	}

	class PressButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			if (button == Prebutton) {
				month--;
				if (month <= 0) {
					year--;
					month += 12;
				}
			} else {
				month++;
				if (month > 12) {
					year++;
					month -= 12;
				}
			}
			dataOfDay();
			Change.setText(year + "��" + month + "��");
		}
	}
}

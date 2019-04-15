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
	
	private String year_int = null; //保存用户选择的年份
	private int month_int; 
	
	//private String[] lunar_day = new String[42];
	static Calendar c = Calendar.getInstance();
	static int year = c.get(Calendar.YEAR);
	static int month = c.get(Calendar.MONTH) + 1;
	int monthday = 0;
	
	private boolean todayFlag;
	String[] weekdaystr = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	Object[][] monthview = new String[6][7];
	JPanel dayPanel = new JPanel();
	JPanel panel1 = new JPanel();
	JPanel calPanel = new JPanel();
	JPanel alarmDemo = new AlarmDemo();
	JPanel noteBook = new NoteBook();
	JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP);

	JButton Prebutton = new JButton("上一月");
	JButton Nextbutton = new JButton("下一月");
	JButton PreYear = new JButton("上一年");
	JButton NextYear = new JButton("下一年");
	JButton Change = new JButton(year + "年" + month + "月");

	JLabel nowLabel = new JLabel(year + "年" + month+ "月" + (c.get(Calendar.DATE)) + "日", JLabel.CENTER);
	JLabel timeLabel = new JLabel("---", JLabel.LEFT);
	
	DefaultTableModel model = new DefaultTableModel();
	JTable JTable;
	JScrollPane JScrollPane;

	TrayIcon trayIcon;//托盘图标
    SystemTray systemTray;//系统托盘
	public CalendarsView() {
		//托盘化
		systemTray = SystemTray.getSystemTray();//获得系统托盘的实例   
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File(System.getProperty("user.dir")+"\\source\\360.png")));
        }
        catch (IOException e1) {
        	e1.printStackTrace();
        }
       
        
        this.addWindowListener(
                new WindowAdapter() {   
                    public void windowIconified(WindowEvent e) {   
                    	int n = JOptionPane.showConfirmDialog(null, "是否缩小到托盘?", "缩小", JOptionPane.YES_NO_OPTION);  
                        if (n == JOptionPane.YES_OPTION) {  
                        	dispose();//窗口最小化时dispose该窗口 
                        	try {
								systemTray.add(trayIcon);
							} catch (AWTException e1) {
								e1.printStackTrace();
							}//设置托盘的图标
                        } else if (n == JOptionPane.NO_OPTION) {  
                        	systemTray.remove(trayIcon);
                        } 
                        
                    }   
                });
        
        trayIcon.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if(e.getClickCount() == 2)//双击托盘窗口再现
                            setExtendedState(Frame.NORMAL);
                            setVisible(true);
                            systemTray.remove(trayIcon);
                    }
                });        
        
		
		//时间栏
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
		// 上方按钮
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

		jTabbedPane.add(calPanel, "日历");
		jTabbedPane.add(alarmDemo,"闹钟"); 
		jTabbedPane.add(noteBook,"记事本"); 

		add(jTabbedPane, BorderLayout.NORTH);

		// 按钮监听器
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
		}  //添加日期，放到面板里面
		this.paintDay();
		JPanel panel_main = new JPanel();  //放置以上两个面板
		panel_main.setLayout(new BorderLayout());  //边界布局管理器
		panel_main.add(panel_day,BorderLayout.SOUTH);
		getContentPane().add(panel_main);
	}
	/**
     *
     * @param y 年份
     * @param m 月份
     * @param d 日期
     * @return 星期几
     */
    public int dayOfWeek(int y, int m, int d){
        if (m < 1 || m > 12 || d < 1 || d > 31) return -1;
        if (m == 1) {
            m = 13;
            y--;
        } else if (m == 2) {
            m = 14;
            y--;
        }
        int c = y / 100;
        y = y - c * 100;
        int week = y + y / 4 + c / 4 - 2 * c + 26 * (m + 1) / 10 + d - 1;
        week %= 7;
        week = week < 0 ? week + 7 : week;
        return week;
    }
	private void paintDay() {
	
		if(this.todayFlag) {
			month_int = now_month;//要求显示今天的日期 
		} else {
			JButton button = new JButton();
			if (button == Prebutton) {
				month_int = month - 1;
				if (month < 1) {
					year--;
					month_int = 1;
				}
			} else {
				month_int = month + 1;
				if (month > 12) {
					year++;
					month_int = 1;
				}
			}
		}
		Date firstDay = new Date(now_year, month_int, -2);//构造该月的第一天
		GregorianCalendar cal = new GregorianCalendar();//创建一个Calendar的实例
		cal.setTime(firstDay);
		
		int days = 0;//存放某个月份的天数
		int day_week = 0;//存放某个月份的第一天使星期几的数值
		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10  || month == 12) {
			days = 31; 
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			days = 30;
		} else {
			if(cal.isLeapYear(now_year)){
				days = 29;
			} else {
				days = 28;
			}//二月，如果闰年，则有29天，否则有28天
		}//判断是几月份，根据它来设定day的值，其中二月份要判断是否闰年
		day_week = dayOfWeek(year, month_int, 1);
		//int count = 1;
  /**
   * 绘制按钮。在这里首先要根据选定的月份的第一天是星期几来确定绘制按钮的起始位置
   * 其中day_week就是我们要绘制的起始位置，对于那些没有数值可以显示的按钮要置空
   */
		String[] LunarDate=new String[42];
		Lunar lunar = new Lunar();  
		
		for(int i = day_week; i < day_week + days;  i++){
			//此处应该用  i - (day_week - 1) 否则会出现阴历对应错误的问题,不需要month_int - 1 ,导致阴历不对应
			LunarDate[i]=lunar.getLunarDate( year,  month_int , i - (day_week - 1)); 
			if(i%7 == 0|| i == 6 || i == 13 || i == 20 || i == 27 || i == 34 || i == 41) {
    //如果是边界上的按钮，文字用红色，以来标示周末
				if(i == day_week + now_date.getDate() - 1) {
					button_day[i].setForeground(Color.blue);//将与今天一样的日期用蓝色标示
					button_day[i].setText(count + LunarDate[i]);
				} else {
					button_day[i].setForeground(Color.red);//其它边界上的按钮中的文字用红色
					button_day[i].setText(count + LunarDate[i]);
				}
			} else {
				if(i == day_week + now_date.getDate() - 1){
					button_day[i].setForeground(Color.blue);//将与今天一样的日期用蓝色标示
					button_day[i].setText(count + LunarDate[i]);
				} else {
					button_day[i].setForeground(Color.black);//一般位置的按钮上的文字用黑色标示
					button_day[i].setText(count + LunarDate[i]);    
				}
			}
		}
			if(day_week == 0){
				//对于没有日期数值显示的按钮进行置空处理
				for(int i =days; i < 42; i++){
					button_day[i].setText("");//如果第一天是周日，则将第一天前面的按钮置空
				}
			} else{
		  for(int i = 0; i < day_week; i++){
			  button_day[i].setText("");//如果第一天不是周日，则将第一天前面的按钮置空
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
			Change.setText(year + "年" + month + "月");
		}
	}
}

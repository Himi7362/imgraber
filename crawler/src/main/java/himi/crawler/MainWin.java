package himi.crawler;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jsoup.helper.StringUtil;
/**
 * 
 * @ClassName: MainWin 
 * @Description: 程序窗口类
 * @author penny
 * @date 2018年3月12日 下午8:12:20 
 *
 */
public class MainWin extends JFrame {
	
	/** 
	* @Fields serialVersionUID : 6865199862525660468L
	*/ 
	private static final long serialVersionUID = 6865199862525660468L;
	private JTextField urlStr;
	private JButton txtBtn;
	private JButton imgBtn;
	private JTextArea textArea;
	private StringBuilder status=new StringBuilder();		//状态信息
	private JScrollPane scrollPane; 
	private Toolkit kit;
	ImgTool crawler;
	JOptionPane loginPane;
	InfiniteProgressPanel glasspane;
	private int flag=crawler.downloadMsg.size();
	/**
	 * 程序主窗口
	 */
	public MainWin() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		kit = Toolkit.getDefaultToolkit();
		setTitle("Imgraber");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds((kit.getScreenSize().width-650)/2, (kit.getScreenSize().height-450)/2, 650, 450);//窗口位置  x,y ,width and height.
		setResizable(false);
		
		txtBtn = new JButton("Create Url Txt");txtBtn.setFont(new Font("宋体",Font.BOLD,18));txtBtn.setPreferredSize(new Dimension(70,50));txtBtn.setBorderPainted(false);txtBtn.setEnabled(false);
		imgBtn = new JButton("Create Images");imgBtn.setFont(new Font("宋体",Font.BOLD,18));imgBtn.setPreferredSize(new Dimension(70,50)); imgBtn.setBorderPainted(false);imgBtn.setEnabled(false);
		urlStr = new JTextField();urlStr.setText("Input URL of the images");
		textArea = new JTextArea(15,64);
		textArea.setEditable(false);
		textArea.setFont(new Font("宋体", Font.BOLD,15));
		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setLayout(new FlowLayout());
        Box box1,box2,box3,boxBase = null;
        boxBase =Box.createVerticalBox();
        box1=Box.createHorizontalBox();
        box2=Box.createHorizontalBox();
        box3=Box.createVerticalBox();
        box1.add(new JLabel("URL="));
        box1.add(urlStr);
        box1.setMaximumSize(new Dimension(630,130));
        box2.add(txtBtn);
        box2.add(imgBtn);
        box3.setMaximumSize(new Dimension(630,430));
        box3.add(scrollPane);
        boxBase.add(box1);
        boxBase.add(box2);
        boxBase.add(box3);
        add(boxBase);
        
        addStatus("Ready!");
        addStatus("1、Input images URL");
        addStatus("2、Click 'Create Url Txt' Button");
        addStatus("3、Click 'Create Images' Button");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        glasspane = new InfiniteProgressPanel();
		glasspane.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
		setGlassPane(glasspane);
        
        crawler=ImgTool.getInstance();//创建crawler 工具类实例;
        //url 输入框事件
        urlStr.addMouseListener(new MouseListener() {
        	String inputStr=urlStr.getText();
        	String msg="";
			public void mouseClicked(MouseEvent mouseevent) {
			}
			public void mousePressed(MouseEvent mouseevent) {
				if(urlStr.getText().equals("Input URL of the images"))
				urlStr.setText("");
			}	
			public void mouseEntered(MouseEvent mouseevent) {}
			public void mouseExited(MouseEvent mouseevent) {
				if(urlStr.getText().equals("Input URL of the images")||StringUtil.isBlank(urlStr.getText())){
					urlStr.setText("Input URL of the images");
				}else{
					if(!crawler.isURL(urlStr.getText())){
						msg="Input correct url!";
					}else{
						txtBtn.setEnabled(true);
					}
				}
				addStatus(msg);
			}
			public void mouseReleased(MouseEvent mouseevent) {
			} 
        });
        
        //创建 文本按钮
        txtBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginPane = new JOptionPane();
				String inputStr=urlStr.getText();
				if(!StringUtil.isBlank(inputStr)){
					glasspane.start();
					if(crawler.isURL(inputStr)){//校验是否输入了URL
						crawler.URL=urlStr.getText();
						crawler.createImgURLTxt("img");
						List<String> list=crawler.getURLs();
						if(null!=list){
						for (String str : list) {
							addStatus(str);
						}
						imgBtn.setEnabled(true);
						addStatus("OK!Image Urls txt files have been created!\n");
						}else{
							addStatus("Err:Create txt failed");
						}
					}
				}
				glasspane.stop();
			}
		});
        //下载图片按钮
        imgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				glasspane.start();
				try {
//					new shwoDownloadMsg().run();
					Timer timer = new Timer();
					Date d2 = new Date(System.currentTimeMillis()); 
					timer.schedule(new TimerTask(){ 
					public void run(){ 
						List<String> temp=crawler.downloadMsg;
						addStatus(temp.get(temp.size()-1));
					}},d2,50L); 
					crawler.createImgs(null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				glasspane.stop();
				textArea.setText(status.toString());
			}
		});
	}
	/**
	 * 
	 * @Title: addStatus 
	 * @Description: 追加提示文本
	 * @param @param msg    
	 * @throws
	 */
	public void addStatus(String msg){
		
		if(!StringUtil.isBlank(msg)&&status.lastIndexOf(msg)<0){
			status.append(msg+"\n");
			textArea.setText(status.toString());
		}
	}
	/**
	 * 控制关闭按钮，添加防卡死，默认30s认为卡死！
	 */
	public void setClosedBtn(){
//		if(System.currentTimeMillis()-startTime>3*1000);
		//考虑设计模式 或者其他方法中！！TODO  FIXME
	}
	class shwoDownloadMsg implements Runnable{
		public void run() {
			System.out.println("i run");
			List<String> temp = crawler.downloadMsg;
			if(flag!=temp.size()){
				status.append(temp.get(crawler.downloadMsg.size()-1));
			}
		}
	}
	/**
	 * 
	 * @Title: main 
	 * @Description: 主方法
	 * @param @param args    
	 * @throws
	 */
	public static void main(String[] args) {
		new MainWin().show();
	}
	
	
}

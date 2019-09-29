import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Client extends JFrame {

	CardLayout card = new CardLayout();
	
	public User nowuser = new User();
	
	public Client() throws IOException {
		
		setTitle("SSNS");
		
		LoginPage lp = new LoginPage();
		SignupPage sup = new SignupPage();
		LoginFailPage lfp = new LoginFailPage();
		
		getContentPane().setLayout(card);
		getContentPane().add("Login", lp);
		getContentPane().add("Signup",sup);
		getContentPane().add("LoginFail", lfp);
		
		card.show(getContentPane(), "Login");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(750, 750);
	    setVisible(true);
	}
	
	public class LoginPage extends JPanel implements ActionListener {

		private JButton signup = new JButton("회원가입");
		private JButton signin = new JButton("로그인");
		private JTextField textid = new JTextField(20);
		private JPasswordField textpw = new JPasswordField(20);
		private String id;
		private String pw;
		
		public LoginPage() throws IOException{

			setBackground(Color.cyan);
			JPanel c = new JPanel();
			
			setLayout(new GridLayout(3,3));
			add(new JLabel(" "));
			
			add(c);
			
			c.setLayout(new FlowLayout(FlowLayout.CENTER, 700, 10));
			JLabel SSNS = new JLabel("SSNS");
			SSNS.setFont(new Font("Arial", 0, 50));
			c.add(SSNS);
			c.add(new JLabel("ID"));
			c.add(textid);
			textpw.setEchoChar('*');
			c.add(new JLabel("PW"));
			c.add(textpw);
			JPanel sign = new JPanel();
			sign.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 0));
			sign.add(signin);
			signin.addActionListener(this);
			sign.add(signup);
			signup.addActionListener(this);
			
			c.add(sign);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			this.id = textid.getText();
			this.pw = textpw.getText();
			
			textid.setText("");
			textpw.setText("");
			
			//사인업 화면으로 이동
			if(e.getSource().equals(signin)){

				try {
					Socket client = new Socket("127.0.0.1", 5500);
					
					DataOutputStream outmessage = new DataOutputStream(client.getOutputStream());
					outmessage.writeUTF("login");
					
					User user = new User(id, pw);
					ObjectOutputStream outuser = new ObjectOutputStream(client.getOutputStream());
					outuser.writeObject(user);
					
					DataInputStream inmessage = new DataInputStream(client.getInputStream());
					String message = inmessage.readUTF();
					
					if(message.equals("certified")) {
						ObjectInputStream inuser = new ObjectInputStream(client.getInputStream());
						User receiveuser = (User) inuser.readObject();
						String email = receiveuser.getEmail();
						String name = receiveuser.getName();
						String age = receiveuser.getAge();
						String image = receiveuser.getImage();
						nowuser = new User(id,pw,email,name,age,image);
						MainPage mp1 = new MainPage();
						getContentPane().add("Main", mp1);
						card.show(getContentPane(), "Main");
						inuser.close();
					}
					if(message.equals("denied")) {
						card.show(getContentPane(), "LoginFail");
					}
					
					outuser.flush();
					outuser.close();
					outmessage.close();
					inmessage.close();
					client.close();
					
					} catch (UnknownHostException ex) {
						ex.printStackTrace();
						System.exit(1);
					} catch (IOException i) {
						i.printStackTrace();
						System.exit(1);
					} catch (ClassNotFoundException j) {
						j.printStackTrace();
						System.exit(1);
					}

			}
			if(e.getSource().equals(signup)){
				card.show(getContentPane(), "Signup");
			}
			
		}
	}
	
	public class SignupPage extends JPanel implements ActionListener{

		private JButton signup = new JButton("회원가입");
		private JTextField textid = new JTextField(20);
		private JPasswordField textpw = new JPasswordField(20);
		private JTextField textemail = new JTextField(20);
		private JTextField textname = new JTextField(20);
		private JTextField textage = new JTextField(20);
		private JTextField textimage = new JTextField(20);
		
		private String id;
		private String pw;
		private String email;
		private String name;
		private String age;
		private String image;
		
		public SignupPage() {
	
			setLayout(new BorderLayout(0,150));
			add(new JLabel(""),BorderLayout.NORTH);

			JPanel c = new JPanel();
			add(c);
			c.setLayout(new FlowLayout(FlowLayout.CENTER, 700, 10));
			JLabel SSNS = new JLabel("회원가입");
			SSNS.setFont(new Font("", 0, 30));
			c.add(SSNS);
			c.add(new JLabel("ID"));
			c.add(textid);
			textpw.setEchoChar('*');
			c.add(new JLabel("PW"));
			c.add(textpw);
			c.add(new JLabel("E-mail"));
			c.add(textemail);
			c.add(new JLabel("Name"));
			c.add(textname);
			c.add(new JLabel("Age"));
			c.add(textage);
			c.add(new JLabel("Image Path"));
			c.add(textimage);
			c.add(signup);
			
			signup.addActionListener(this);
		}
		public void actionPerformed(ActionEvent e) {
			
			id = textid.getText();
			pw = textpw.getText();
			email = textemail.getText();
			name = textname.getText();
			age = textage.getText();
			image = textimage.getText();
			
			textid.setText("");
			textpw.setText("");
			textemail.setText("");
			textname.setText("");
			textage.setText("");
			textimage.setText("");
			
			setVisible(true);
			
			if(e.getSource().equals(signup)) {
				try {
					Socket client = new Socket("127.0.0.1", 5500);
					
					DataOutputStream outmessage = new DataOutputStream(client.getOutputStream());
					outmessage.writeUTF("signup");
					
					ObjectOutputStream outuser = new ObjectOutputStream(client.getOutputStream());
					User user = new User(id, pw, email, name, age, image);
					outuser.writeObject(user);
					
					DataInputStream inmessage = new DataInputStream(client.getInputStream());
					
					String returnmessage = inmessage.readUTF();
					
					if(returnmessage.equals("certified")) {
						nowuser = new User(id, pw, email, name, age, image);
						MainPage mp = new MainPage();
						getContentPane().add("Main", mp);
						card.show(getContentPane(), "Main");
					}
					
					outuser.flush();
					outmessage.close();
					inmessage.close();
					outuser.close();
					client.close();

					} catch (UnknownHostException ex) {
						ex.printStackTrace();
						System.exit(1);
					} catch (IOException i) {
						i.printStackTrace();
						System.exit(1);
					}
			}
		}
		
	}	
	
	public class LoginFailPage extends JPanel implements ActionListener{

		public LoginFailPage() {
			for(int i=0; i<10; i++)
				add(new JLabel(" "));
			setLayout(new FlowLayout(FlowLayout.CENTER, 700, 10));
			JLabel message = new JLabel("일치하는 회원 정보가 없습니다.");
			message.setFont(new Font("",0, 20));
			add(message);
			JButton confirm = new JButton("확인");
			add(confirm);
			confirm.addActionListener(this);
		}
		public void actionPerformed(ActionEvent e) {
			card.show(getContentPane(), "Login");
		}
	}

	public class MainPage extends JPanel implements ActionListener{

		public MainPage() {
			
			setLayout(null);
			
			JPanel profile = new JPanel();
			profile.setLayout(null);
			profile.setBounds(50,50,640,200);
			JPanel texts = new JPanel();
			JPanel friends = new JPanel();
			add(profile);
			add(texts);
			add(friends);
			profile.add(new JLabel(""));
			profile.add(new JLabel(""));
			JButton logout = new JButton("로그아웃");
			JLabel profiletext = new JLabel(nowuser.getName());
			String image = nowuser.getImage();
			ImageIcon icon = new ImageIcon(image);
			JLabel profileimage = new JLabel(icon);
			profile.add(profiletext).setBounds(220,140,150,50);
			profiletext.setFont(new Font("",0,25));
			profile.add(profileimage).setBounds(10,0,200,200);
			logout.addActionListener(this);
			profile.add(logout);
			logout.setBounds(520,20,90,40);
			
			texts.setBounds(50,280,400,380);
			
			JLabel posting = new JLabel("Posting");
			posting.setFont(new Font("", 0 ,20));
			texts.add(posting).setBounds(50,50,100,50);
			
			friends.setBounds(485,280,205,380);
			JLabel friendslist = new JLabel("Friends");
			friends.add(friendslist).setBounds(50,50,100,50);
			friendslist.setFont(new Font("", 0 ,20));
			
			profile.setBackground(Color.WHITE);
			texts.setBackground(Color.WHITE);
			friends.setBackground(Color.WHITE);
			
			setVisible(true);
			}
		
		public void actionPerformed(ActionEvent e) {
			card.show(getContentPane(), "Login");
		}
		
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		new Client();
	}
}

package anpp;
import java.util.Scanner;
public class Router implements Runnable{
	Thread t;
	static Thread t2;
	String rmsg,dip;					
	/*rmsg-Received Message this is where the received message of every node will be saved and updated with new messages
	 *As it is class variable every node has its own rmsg variable and dip (Destination IP ) which is used to store the 
	 *DIP of this received messages
	 *This DIP is important because in case of forwarding to gateways the DIP of the msg will be different and we need forward it to
	 *That particular host hence we must store the DIP of a Received Message */
	String mac,gateway,nid;
	String sm="255.255.255.0";
	static Scanner s=new Scanner(System.in);
	volatile boolean send=false,receive=false;
	Router(int i){                                 //Initializer to set the values of nid ,gateway,mac
		if(i==1) {
			t=new Thread(this,"Router 1");			//Creating and Setting a name to the Thread
													
													//Router 1 and 2 must have different values of mac and 
			nid="124.123.1.0";						//SubnetId this can be done with parameterized constructor
			gateway="124.123.2.0";					//This parameterized Constructor will do assign the values for 2 routers accordingly
			mac="D4:6A:6A:BA:10:19";
			Protocol.val[Protocol.v][0]=nid;       //During every creation the nid,Mac are stored in a 2-D array of string 
			Protocol.val[Protocol.v][1]=mac;		
			Protocol.Oid[Protocol.v++]=this;		//Object ID is inserted into the Oid array which is used to simulate the purpose of ARP
			t.start();								
		}
		else if(i==2) {
			t=new Thread(this,"Router 2");			//Creating and Setting a name to the Thread
			nid="124.123.2.0";
			gateway="124.123.1.0";
			mac="D4:2E:4C:AD:14:10";
			Protocol.val[Protocol.v][0]=nid;
			Protocol.val[Protocol.v][1]=mac;
			Protocol.Oid[Protocol.v++]=this;
			t.start();										//start() will start the execution of CurrentThread
															//and the method run() is the one that is executed first
		}
	}
	
	/*This run method have an infinite loop so that the threads will be alive through out the Execution of the program
	 * The router may received messages as it is gateway for the hosts and other routers in that case it will receive it 
	 * and then forward it to its appropriate destination so the routers will constantly check for receive flags it is
	 * forwarded to the respective destination just like how it happen in case of hosts (Refer to Node.java file for more details)
	 *  
	 * */
	public void run() {
		while(true) {								 
			if(receive==true) {
				System.out.println(Thread.currentThread()+" ");
				System.out.println("Received msg is : "+rmsg);
				if(!dip.equals(nid)) {
					if(Protocol.Cid(nid,dip,sm)) {
						System.out.println("Dip is in same Sub net as source");
						int i=Protocol.Arp(dip);
						this.sendMsg(dip,rmsg,i);
						}
					else {
						System.out.println("Dip is not in this Sub-net So sending to gateway");
						int i=Protocol.Arp(gateway);
						this.sendMsg(dip,rmsg,i);
						}
					
				}
				receive=false;
				
			}
		}
	}
	/*This method will take destination IP,message , and an index where the object id is stored in a static array
	 * This oid will be used to set the rmsg (received message)
	 * */
	public void sendMsg(String dip,String msg,int i) {
		if(i<2) {
		Router p=(Router)Protocol.Oid[i];
		p.rmsg=msg;
		p.receive=true;
		p.dip=dip;
		}
		else {
			Node p=(Node)Protocol.Oid[i];
			p.rmsg=msg;
			p.receive=true;
			p.dip=dip;
			
		}
		
		
	}
/*
 * IN main method First Two Routers are created with parameters 1 and 2 so that the routers will get their individual Subnet mask,Ip,MAC
 * after that Nodes are created with their subnet name as argument i.e.
 * Node ex=new Node('a');
 * will create a Node and assigns values of Subnet mask,ip,mac according to subnet A,Like this all hosts are created
 * After That the main Thread will display the menu accordingly 
 * */
	public static void main(String[] args) throws InterruptedException {
		Router r=new Router(1);
		Router r2=new Router(2);
		Node a1=new Node('a');       //Creating new nodes as they all extend Thread class they will start
		Node a2=new Node('a');		 //They will be in separate threads of execution
		Node a3=new Node('a');
		Node b1=new Node('b');
		Node b2=new Node('b');
		Node b3=new Node('b');
		Node c1=new Node('c');
		Node c2=new Node('c');
		t2=Thread.currentThread();
		int input;
		while(true) {                                          //Menu Driven Main method to select the sender ,Dest Ip,MSG
			System.out.println(Thread.currentThread()+" ");
			System.out.println("Press 1 to send a message :");
			System.out.println("Press anything other to exit : ");
			input=s.nextInt();
			if(input==1) {
				System.out.println("Select Sender : \n1: Node 1 from Subnet A\n2: Node 2 from Subnet A\n3: Node 3 from Subnet A\n4: Node 1 from Subnet B\n5: Node 2 from Subnet B\n6: Node 3 from Subnet B\n7: Node 1 from Subnet C\n8: Node 2 from Subnet C\nAny other to go back");
				int j=s.nextInt();				//The above line is menu for sender selection
				switch(j) {
				case 1:a1.send=true;			//When ever a Sender is selected the main thread will set the Send flag of the node
						break;					//Every Node i.e. (Host and router) have send flags which they constantly 
				case 2: a2.send=true;
						break;
				case 3:a3.send=true;
						break;
				case 4:b1.send=true;
						break;
				case 5:b2.send=true;
						break;
				case 6: b3.send=true;
						break;
				case 7:c1.send=true;
						break;
				case 8:c2.send=true;
						break;
				default:continue;
				}
				t2.suspend();      //The main thread is Suspended until the msg is received by the Destination
									//So that there will be no disturbance In the Format of Output
				
			}
			else System.exit(0);    //Exit option for entires other than 1 in outer menu
			
		}
		}
		
	}


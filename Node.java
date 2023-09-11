package anpp;
import java.util.*;
import java.lang.Math;
import java.util.Scanner;
public class Node extends Thread{
	static int i=48,j=48,k=48;             //They are initialized to 48 as the ascii value of 0 that is 48 so if we print (char)i it will print a '0'
	Thread t;
	String sm,gateway,ip,mac;
	String rmsg,dip;
	 Scanner s=new Scanner(System.in);				//To read Input
	 volatile boolean  send=false,receive=false;    //These variables are Declared Volatile To make sure that the change made by one Thread
	 												//Will be Reflected in the others
	 
	 /*rmsg-Received Message this is where the received message of every node will be saved and updated with new messages
		 *As it is class variable every node has its own rmsg variable and dip (Destination IP ) which is used to store the 
		 *DIP of this received messages
		 *This DIP is important because in case of forwarding to gateways the DIP of the msg will be different and we need forward it to
		 *That particular host hence we must store the DIP of a Received Message */
	Node(char subnetName){
		if(subnetName=='a') {
			i++;
			t=new Thread(this,"Node "+(char)i+"in subnet A");
			sm="255.255.255.0";
			gateway="124.123.1.0";
			ip="124.123.1."+(char)i;					//i,j,k are static variables and used to assign ip addresses and mac adresses
			mac="D4:6A:6A:BA:10:A"+(char)(i);               
			Protocol.val[Protocol.v][0]=ip;
			Protocol.val[Protocol.v][1]=mac;
			Protocol.Oid[Protocol.v++]=this;
			t.start();
		}
		else if(subnetName=='b') {							//Assigning values according to subnet b
			j++;
			t=new Thread(this,"Node "+(char)j+"in subnet B");
			sm="255.255.255.248";
			gateway="124.123.2.0";
			ip="124.123.2."+(char)j;
			mac="D4:2E:4C:AD:14:B"+(char)(j);
			Protocol.val[Protocol.v][0]=ip;
			Protocol.val[Protocol.v][1]=mac;
			Protocol.Oid[Protocol.v++]=this;
			t.start();
			
		}
		else if(subnetName=='c') {						//Assigning values according to Subnet c
			k++;	
			t=new Thread(this,"Node "+(char)k+"in subnet C");
			sm="255.255.255.248";
			gateway="124.123.2.0";
			ip="124.123.2.1"+(char)k;
			mac="D4:2E:4C:AD:14:C"+(char)(k);
			Protocol.val[Protocol.v][0]=ip;
			Protocol.val[Protocol.v][1]=mac;
			Protocol.Oid[Protocol.v++]=this;
			t.start();
			
		}
		
	}
	
	
	/*This run method have an infinite loop so that the threads will be alive through out the Execution of the program
	 *  If user wanted to send a message from this node the main Thread will set the Send flag of that particular node
	 *  Now as each node have different thread of execution and the Run method of every node i.e.( Host) continuously-
	 *  check for the flags send and receive if send==True then This thread will ask for destination Ip, message to be sent 
	 *  and then check whether this Ip present in same Subnet or not with the help of Protocol.Cid() method 
	 *  (check Protocol.java for more details) Now Protocol.Arp() is used to find the MAC of dip and then sendMsg to that destination.
	 *  
	 * */
	
	
	public void run(){
		//System.out.println(Thread.currentThread()+" ");
		while(true) {
		if(send==true) {
			System.out.println(Thread.currentThread()+" ");
			System.out.println("Eneter Destination Ip address");
			String msg,Dip;
			Dip=s.nextLine();
			System.out.println("Eneter The Mesage to be sent");
			msg=s.nextLine();
			if(Protocol.Cid(ip,Dip,sm)) {

				System.out.println("Dip is in same Sub net as source");
				int i=Protocol.Arp(Dip);
				this.sendMsg(Dip,msg,i);
				}
			else {
				
				System.out.println("Dip is not in this Sub-net So sending to gateway");
				int i=Protocol.Arp(gateway);
				this.sendMsg(Dip,msg,i);
				}
			
			
			
			send=false;
			
		}
		else if(receive==true) {
			System.out.println(Thread.currentThread()+" ");
			System.out.println("Received msg is : "+rmsg+"\n");
			receive=false;
			Router.t2.resume();
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

}

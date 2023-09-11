package anpp;


/*
 * contains a Static 2-D array of string which will contains all the IP's of nodes (Routers and Hosts) along with macs
 * for example val[0][0] contains IP address of a node where as val[0][1] will Contain Mac of that particular Node
 * V value is updated whenever an entry is made into the val 2-D string array
 * 
 * Oid is a Single Dimension array of type Object this one is used to store the Object ids of all the Nodes which can be used in case of 
 * Simulating the ARP
 * Here Type is taken as object because Object is Super class of all the classes in JAVA */
public class Protocol {
	static String val[][]=new String[100][2];
	static Object Oid[]=new Object[100];
	static int v=0;
/*
 *This will accept a String argument which will be an IP Address and then prints the MAC address associated with 
 *It also returns the Index of the MAC and Objectid which can be used to send messages (Refer to SendMsg() method in Router.java and Node.java) */
static int Arp(String dip) {
	System.out.println("Arp invoked for IP : "+dip);
		for(int i=0;i<v;i++) {
			if(dip.equals(val[i][0])) {	
				System.out.println("MAC address is : "+val[i][1]+"\n");
				return i;
			}
		}
		return -1;
}
/*
 * This method is used to Detect if the Source ip and Destination Ip peresent in the same Sub-net or not
 * For this to take place we must decode these strings into integers and perform Bitwise AND this can be done by ipDecode() method
 * Returns TRUE if Destination is in same Subnet as source
 * Other wise Returns false
 * */
static boolean Cid(String sourceIp,String destIp,String subnetMask) {

	int sip[]=ipDecode(sourceIp);
	int dip[]=ipDecode(destIp);
	int sm[]=ipDecode(subnetMask);

	 for(int i=0;i<4;i++)
		 {
		 if((sip[i]&sm[i])!=(dip[i]&sm[i])) 
			 return false;
		// System.out.println(sip[i]);
		 }
	return true;

}

/*This method Receives a String as argument and returs an array of 4 integers
 * for Example let s=124.123.2.1
 * Then the return value will be an array of integers with values {124,123,2,1} as they are integers we can perform BIT wise and operation
 * and can decide if destination present in same Subnet or Different one
 * */
static int[] ipDecode(String s) {
	int res[]=new int[4];
	int temp=0,j=0;
	for(int i=0;i<s.length();i++) {
		if(s.charAt(i)!='.') {
			temp=temp*10+s.charAt(i)-48;
		}
		else {
			res[j++]=temp;
			temp=0;
		}
	}
	res[j]=temp;
	return res;
}}

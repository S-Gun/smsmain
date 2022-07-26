//SMS MODULE LAST UPDATED ON 17/05/2022 BY SHAGUN JAIN-MTECH Y20 BATCH
package com.ehelpy.brihaspati4.sms;
//Glue Code = gc
// Authentication Manager = am
// Communication Manager = cm
// Indexing Manager = im
// Routing Manager = rm
// Web Server Module = ws
// Web Module = web
// DFS = dfs
// UFS = ufs
// Message = sms
// Mail = mail
// VoIP = voip
// Address Book = adbk
// Search = srch
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.*;
import java.util.LinkedList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
//import com.ehelpy.brihaspati4.authenticate.CertificateSignature;
import com.ehelpy.brihaspati4.GC.GlueCode;
//import com.ehelpy.brihaspati4.SearchB4.Buffer;
public class SmsMain extends Thread {
    private Static SmsMain sms;
    private String Node_Ip1 = "";
    private String Node_Ip2 = "";
    private String hash_Id = "";
    private String Login_time = "";
    final boolean[] seen = {false};
    private String attachment_path;
    //private Static Buffer Buffer;
    private String time;
    private int max_limit_of_buffer = 1024;
    private LinkedList receiver = new LinkedList();
    private LinkedList sms_query_manager_list = new LinkedList();
    private LinkedList sms_buffer_gc = new LinkedList();
    // Linkedlist defined where gc will write in the input buffer of sms module.
    private ArrayList process_var_sms = new ArrayList();
    // Array list will be the element of the above linked list containing the details of query and response
    private static LinkedList input_buffer = new LinkedList();
    private LinkedList group_list = new LinkedList();
    //private Arraylist group_id_and_name = new Arraylist();
    private String sm;
    private String sm2;
    private String que1;
    private String dm1;
    private String que_var1;
    private String que_var2;
    private String arg_var1;
    private String arg_var2;
    private String timeCheck;
    SmsMain() {
        //initialization of sms via gc
        System.out.println("Starting sms module");
        try {
            System.out.println("starting processingThread_sms");
            processingThread_sms(); // for continuous checking
        } catch (Exception e0) {
            System.out.println("error occured");
            e0.printStackTrace();
        }
    }

    public static synchronized SmsMain getInstance() {
        if (sms == null) {
            sms = new SmsMain();
        }
        return sms;
    }

    public void addMessage_sms_buffer_gc(List gcMessage) {
        // GLUE CODE will call this method to write in the input buffer of sms.
        if ((sms_buffer_gc.size()) < max_limit_of_buffer) {
            sms_buffer_gc.add(gcMessage);
            System.out.println("List received by sms’s input buffer.");
        } else {
            System.out.println("sms’s input buffer is Full.");
        }
    }

    public LinkedList display_sms_buffer_gc() {
        return (sms_buffer_gc);
    }

    public void test() {
        formMessage("que", "rm", "getNodeID", null, null);
        processingThread_sms();
    }

    public String getTime() {
        formMessage("que", "am", "getUpdatedTime", null, null);
        time = processingThread_sms();
        return time;
    }

    public boolean formMessage(String que_res, String dm, String query, String arguments, String response) {
        //format of communicating with gc by forming query or response
        GlueCode gc = GlueCode.getInstance();
        ArrayList abc = new ArrayList();
        //  abc.clear();
        abc.add(que_res);
        abc.add("sms");
        abc.add(dm);
        abc.add(query);
        abc.add(arguments);
        if (response != null) {
            abc.add(response);
        }
        System.out.println("Query/Response formed.");
        sms_query_manager_list.add(abc);
        System.out.println("Query/Response sent to GC.");
        if (gc != null) {
            gc.addMessage_gc_buffer_sms(abc);
            return true; //query successfuly sent to gc
        } else {
            System.out.println(" GC_SMS input buffer is not available.");
            return false; //sending query to gc failed
        }
    }

//    private class timerClass extends query_Timer {
//        //timer class for query response management
//        @Override
//        public void run() {
//           // System.out.println("Timer Class started at: on:" + new Time());
//            query_check();
//           // System.out.println("Timer Class finished at: on:" + new Time());
//        }
//
//        private void query_check() {
//            try {
//                timeCheck = (String) que_res_management.get(6);
//                //wait_time = getTime().plusMinutes(5);
//                //Boolean flag = timeCheck.isAfter(wait_time);
//                Boolean flag = true;
//                if (flag) {
//                    processingThread_sms();
//                } else {
//                    //addMessage_sms_buffer_gc();
//                }
//                //assuming it takes 5 minutess to wait for the response of a query
//                Thread.sleep(300000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public String search(String key){
        //function that will process the query received by search module,
        // (if not found in im then call dfs via gc)
        // or directly handover query to dfs
        try{
            formMessage("que", "srch", "key", "arg", null);
            boolean found = true;

            if(found)
            {
                return (key);
            }
            else
            {
                formMessage("que","dfs", "search", "key", null);
                System.out.println("searching in dfs");
            }
        }catch(Exception e9)
        {
            System.out.println("Exception occured and key not found!");
        }
    }

    public String processingThread_sms() {
        // sms runs this thread to process resposne or query
        Thread processingThread_sms = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if ((sms_buffer_gc.size()) > 0) {
                            System.out.println("Buffer size not zero, retrieving the data");
                            List<String> process_var_sms = new ArrayList<String>();
                            process_var_sms = (ArrayList) sms_buffer_gc.get(0); //typecasting first ele of linkedList to ArrayList
                            sms_buffer_gc.pollFirst(); // retrieve & removes first element
                            if (process_var_sms != null) {
                                que1 = (String) process_var_sms.get(0);
                                sm = (String) process_var_sms.get(1); //get source module field
                                dm1 = (String) process_var_sms.get(2); // get destination module field
                                que_var1 = (String) process_var_sms.get(3); // get query(function) field
                                arg_var1 = (String) process_var_sms.get(4);//// get argument field
                                //res_var1 = (String) process_var_sms.get(5); // get response field
                                if (que1.equals("que")) {
                                    // send response for the query
                                    if (que_var1.equals("search")) {
                                        System.out.println("search function is called");
                                        gc.addMessage_gc_buffer_sms(process_var_sms);
                                        formMessage("res", "srch", "query", "arguments", null);
                                        //sendResponse_gc_buffer_sms(response);
                                        List formMessageExt = new ArrayList(); //form message added with time
                                        formMessageExt.add(process_var_sms);
                                        formMessageExt.add(time);
                                        sms_query_manager_list.add(formMessageExt);// create link list to save the query with data to be sent
                                    }
                                    // else if (query == "last_seen"){
                                    //   System.out.println("last_seen function is called");
                                    //    //response = (String) getDeviceID();
                                    //   formMessage("res", "websrvr", "query", "arguments", "response");
                                    //    //sendResponse_gc_buffer_sms(response);
                                    // }
                                    else {
                                        System.out.println("this function is not present in sms");
                                        System.out.println("returning null as a response");
                                        //sendResponse_gc_buffer_sms(null);
                                    }
                                } else if (que1.equals("res") ) // get the response
                                {
                                    System.out.println("process_var_sms started with Response");
                                    formMessage("que", "am", "getUpdatedTime", null, null);
                                    //TimerClass(timeCheck);
                                    for (int i = 0; i < sms_query_manager_list.size(); i++)
                                    {
                                        //query response management
                                        List que_res_management = new ArrayList();
                                        que_res_management = (ArrayList) sms_query_manager_list.get(i);// get first element from com wait queue

                                        sm2 = (String) que_res_management.get(2);// get the sm field
                                        que_var2 = (String) que_res_management.get(3);// get query function field
                                        arg_var2 = (String) que_res_management.get(4); //get argument field

                                        if ((dm1.equals(sm2)) && (que_var1.equals(que_var2)) && (arg_var1.equals(arg_var2)) )// compare (query and argument) with stored query data and retrive data for process//
                                        {
                                            System.out.println("Response received matched with the query sent");
                                            String response = (String) process_var_sms.get(5); //get response function

                                            System.out.println("Response received by sms is: " + response);
                                            //return response;
                                            sms_query_manager_list.remove(i);//remove this element from query list
                                        } else {
                                            //response invalid
                                            System.out.println("Response received doesnot match the query sent");
                                        }
                                    }
                                } else{
                                    try {
                                        //System.out.println("Processing Thread going to sleep");
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch(Exception e2){
                                System.out.println("Exception Occurred");
                                e2.printStackTrace();
                            }
                        }
                    }
                }catch (Exception e8){
                    e8.printStackTrace();
                }
            }
        });
        processingThread_sms.start();
    }

    public boolean input_buffer_append(ArrayList message) {
        if((input_buffer.size())< (max_limit_of_buffer)) {
            input_buffer.add(message);
            //append "successful";
            return true;
        } else {
            //append "failed";
            return false;
        }
    }

    public void sendResponse_gc_buffer_sms(String response) {
        // sms will call this method to write response in the GC's input buffer
        GlueCode gc = GlueCode.getInstance();
        ArrayList pqr = new ArrayList();
        //pqr.clear();
        pqr = (ArrayList) sms_buffer_gc.get(0);
        sms_buffer_gc.pollFirst(); //delete the first element from the input buffer
        System.out.println("\nTemp Var: " + pqr + "\n");
        //String pqr_02 = (String)pqr.get(0);
        pqr.set(0, "res"); //que_res = Response
        pqr.set(2, pqr.get(1)); //source module becomes the destination module
        pqr.set(1, "sms"); //setting the source module
        //query & arguments are already present in the 3rd & 4th index of pqr
        pqr.add(response); // adding the response in the 5th index position
        System.out.println("\nTemp Var: " + pqr + "\n");
        //display_buffer_status();
        System.out.println("Response being sent to GC.");
        gc.addMessage_gc_buffer_sms(pqr);
        System.out.println("Response already received by GC.");
        //display_buffer_status();
    }

    private void broadcast_msg(){
        for(int ii=0 ; ii < receiver.size() ; ii++)
        {
            //rx = (String) receiver.get(ii);
            formMessage("que", "am", "Verifycerts", "rx", null);
            String user_cert = processingThread_sms();
            String valid;
            if(user_cert == valid ){
                send_message();
            }else{
                send_mail();
            }
        }

    }

    private void group_list(String grp_name, String grp_id){
        System.out.println("Group details received to add in the list");
        ArrayList grp = new ArrayList();
        grp.add(grp_name);
        grp.add(grp_id);
        System.out.println("one more group successfuly formed.");
        group_list.add(grp);
        System.out.println("group details added to group list.");
    }

    private LinkedList display_group_list() {
        return (group_list);
    }

    private void send_message() {
        final String tx;
        final String rx;
        final BufferedReader in;
        final BufferedReader out = null;
        final int chatID = 0;
        final String sc;
        final String[] report = {""};
        //out = new BufferedReader(rx.getOutputStream());
        //in = new BufferedReader(new InputStreamReader(tx.getInputStream()));
        try {
            //websrvr will fetch sender and receiver from browser
            //get input from the webserver via  gc
            formMessage("que", "ws", "getselfId()", "", null);
            // tx = response;
            formMessage("que", "ws", "getUserId()", "", null);
            // rx = response;
            System.out.println("i have taken both the users from web server");
            //for more than one recipient array will be used elss it will be a singleton array
            //string [ ] recipients = newString []);
            formMessage("que", "am", "certificateCheck", "rx", null);
            //call AM to check and verify the validation of rx and its certificate
            //sender thread
            Thread sender = new Thread(new Runnable() {
                byte[] sendData;
                public InetAddress send_IP;
                public InetAddress dest_IP;
                public InetAddress sendPacket;
                Scanner sc = new Scanner(System.in);
                String msg;
                @Override
                public void run() {
                    System.out.println("sender thread running");
                    while (true) {
                        msg = sc.nextLine();
                        System.out.println(msg);
                        if (!seen[0]) {
                            System.out.println("executing when seen is false");
                            sendData = msg.getBytes(); //text to bytes

                            //call rm for sender IP address fetch
                            formMessage("que", "rm", "getSystemIP()", null, null);
                           // Node_Ip1 = response;
                            System.out.println("The sender ip is " + Node_Ip1);
                            //call am for receiver IP address fetch
                            formMessage("res", "am", "getIPAddress()", "rx", null);
                           // Node_Ip2 = response;
                            System.out.println("The receiver ip is " + Node_Ip2);
                            //get hash id of the dest node from AM
                            sms.formMessage("que", "am", "getStringHash()", "Node_Ip2", null);
                           // dest_hash_Id = response;
                            //encrypting one to one messages
//                            private string encrypt_one_to_one(){
//                                sms.formMessage("que", "am", "getPubKeyFromCert()", "packet", null);
//                                //Creating a Cipher object
//                                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                                //Initializing a Cipher object
//                                cipher.init(Cipher.ENCRYPT_MODE, response);
//                                //Adding msg to the cipher
//                                cipher.update(sendData);
//                                //encrypting the data
//                                byte[] cipherText = cipher.doFinal();
//                                System.out.println(new String(cipherText, "UTF8"));
//                                System.out.println("one to one packet formed successfully");
//                                return(cipherText);
//                            }
                            //encrypting one to many messages
//                            private string encrypt_one_to_many(){
//                                //sms.formMessage("que", "ws", "getGroupName()", "", null); response with group id,node id, msg
//                                int key = (int) response;
//                                System.out.println("The ASCII value of " + response + " is: " + key);
//                                //Creating a Cipher object
//                                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                                //Initializing a Cipher object
//                                cipher.init(Cipher.ENCRYPT_MODE, key);
//                                //Adding msg to the cipher
//                                cipher.update(sendData);
//                                //encrypting the data
//                                byte[] cipherText = cipher.doFinal();
//                                System.out.println(new String(cipherText, "UTF8"));
//                                System.out.println("one to many packet formed successfully");
//                                return(cipherText);
//                            }
                            //encrpting broadcast messages
//                            private string encrypt_broadcast(){
//                                sms.formMessage("que", "am", "encrypt()", "sendData", null);
//                                System.out.println("broadcast packet formed successfully");
//                                return(response);
//                            }

                            //send the packet to CM
                            //formMessage("res", "cm", "msgSend", "Packet", null);
                            System.out.println("packet successfully sent");
                        } else {
                            // send delivery report to cm
                            report[0] = "Successfully Delivered";
                            formMessage("res", "cm", "send_Packet", "report", null);
                            return;
                        }try {
                            out.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                }
            });
            sender.start();
            //sleep when sending window not activated
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e4) {
                // TODO Auto-generated catch block
                e4.printStackTrace();
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    public void receive_message() {
        Thread receive = new Thread(new Runnable() {
            String msg;
            public InetAddress receivePacket;
            @Override
            public void run() {
                try {
                    System.out.println("receiver thread running");
                    InetAddress receiveData = null;
                    byte[] IPAddress = receivePacket.getAddress();
                    seen[0] = true;
                    send_message(); //call sender thread
                    //send delivery report to sender when flag is true
                //decrypting one to one messages
//                private string decrypt_one_to_one(){
//                    sms.formMessage("que", "am", "decrypt()", "receiveData", null);
//                    System.out.println("one-to-one packet decrypted successfully");
//                    return(response);
//                }
                //decrypting one to many messages
//                private string decrypt_one_to_many(){
//                    //sms.formMessage("que", "ws", "getGroupName()", "", null); response with group id,node id, msg
//                    int key = (int) response;
//                    System.out.println("The ASCII value of " + response + " is: " + key);
//                    //Creating a Cipher object
//                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                    //Initializing a Cipher object
//                    cipher.init(Cipher.ENCRYPT_MODE, key);
////                    //Adding msg to the cipher
////                    cipher.update(receiveData);
////                    //encrypting the data
////                    byte[] cipherText = cipher.doFinal();
////                    System.out.println(new String(cipherText, "UTF8"));
//                    //Initializing the same cipher for decryption
//                    cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
//                    //Decrypting the text
//                    byte[] decipheredText = cipher.doFinal(cipherText);
//                    System.out.println(new String(decipheredText));
//                    System.out.println("one to many packet decrypted successfully");
//                    return(cipherText);
//                }
                //decrypting broadcast messages
//                private string decrypt_broadcast(){
//                    sms.formMessage("que", "am", "getPubKeyFromCert()", "self", null);
//                    //Creating a Cipher object
//                    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                    //Initializing a Cipher object
//                    cipher.init(Cipher.ENCRYPT_MODE, response);
//                    //Adding msg to the cipher
//                    cipher.update(receiveData);
////                    //encrypting the data
////                    byte[] cipherText = cipher.doFinal();
////                    System.out.println(new String(cipherText, "UTF8"));
//                    cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
//                    //Decrypting the text
//                    byte[] decipheredText = cipher.doFinal(cipherText);
//                    System.out.println(new String(decipheredText));
//                    System.out.println("broadcast packet decrypted successfully");
//                    return(cipherText);
//                }
                //call am for IP address fetch
                    //formMessage("que", "am", "get_IP", "nodeIp", null);
                    //DatagramPacket Packet = new DatagramPacket(receiveData, chatID, IPAddress);
                    //msg = new String(receiveData, StandardCharsets.UTF_8);
                    // assign a unique chatID to every sender_Receiver pair
                    while (msg != null) {
                        System.out.println("rx : " + msg);
                         //msg = in.readLine();
                        //formMessage("res", "ws", null, "msg", "msg");
                        //display msg on the browser
                    }
                    //out.close();
                    //window close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
        });
        receive.start();
        //receive thread to run in background, it is a public thread that can be called by glue code whenever the message buffer is full.
    }

    private void send_mail(){
        System.out.println("e-mail thread activated ");
    }
}




/*
>> encryption from AM?
>> last login information from AM ?
>> managing the data by local storage done using react
>> have to use search, address book module
>> use of return instead of display commands
>> Format of msg packet acc to CM
>> api connsume and then integrate with react
 */
/*
>> browser to websrvr
>> websrvr to GC
>> GC to msg(java)
 */
/*
1. encryption and decryption of three different types of messages
2. maintaining a linked list for groupName, groupId and the respective key
3. dfs handover
4. message with attachment
 */
/*
inside processing thread,
implement as 2D arraylist,
separate function for timing class
query check after timer(not resend
 */
/*
broadcast messages:
the peers that are connected in the network will receive the information through message from the sender
whereas the peers that are not in the network will receive the information as mail from the sender

saving the time stamp with the query and then for loop to check if it is more than 5 minutes,
then query will be formed again and resent.
 */
/*
1. public, private, static
2. relative path for file storage
3. port change and check
 */

package com.ehelpy.brihaspati4.sms.apiSMS;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class ApiSMS {

    public static void main(String[] args) throws IOException {


        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/message", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
                //to send and fetch the msgs
                String input = exchange.getRequestURI().getQuery();
                String[] data = input.split("&", 10);
                String sender = "";
                String receiver = "";
                String type = "";
                String msg = "";
                for (String i : data) {
                    if (i.contains("sender")) {
                        sender = i.split("=", 10)[1];
                        System.out.println(sender + " is sending the information");
                    } else if (i.contains("receiver")) {
                        receiver = i.split("=", 10)[1];
                        System.out.println(receiver + " is receiving the information");
                    } else if (i.contains("type")) {
                        type = i.split("=", 10)[1];
                        System.out.println(type + " is the type of information");
                    } else if (i.contains("msg")){
                        msg = i.split("=", 10)[1];
                    }
                }
                System.out.println(data + " is the encrypted data");
                //System.out.println(type);
                String filepath = "C:\\Users\\Lenovo\\Desktop\\b4client\\java\\com\\ehelpy\\brihaspati4\\sms\\apiSMS\\conversations/";
                //filepath is the path where chat history is stored, it can be changed with advancements in B4 development
                String filename = "";
                File f = new File(filepath+sender+receiver+".txt");
                if (f.exists()) {
                    filename = sender+receiver;
                    System.out.println(filename + " : Exists");
                }else{
                    filename = receiver+sender;
                    System.out.println(filename + " : is the new file created");
                }
                if (type.contains("fetch")) {
                    String filedata = "";
                    try {
                        String my_file_name = filepath + filename + ".txt";
                        File myObj = new File(my_file_name);
                        Scanner myReader = new Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            filedata += myReader.nextLine() + "\n";
                        }
                        System.out.println("Conversation fetched");
                        myReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    exchange.getResponseHeaders().add("Content-Type", "text/json; charset=" + StandardCharsets.UTF_8.name());
                    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }
                    exchange.sendResponseHeaders(200, filedata.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(filedata.getBytes());
                    output.flush();
                } else if (type.contains("send")) {
                    //to send the msg
                    try {
                        String my_file_name = filepath + filename + ".txt";
                        Writer output;
                        output = new BufferedWriter(new FileWriter(my_file_name, true));
                        //clears file every time from buffer
                        output.append(sender + ":" + msg+"\n");
                        output.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    String responseText = "{'status':'Message delivered'}";
                    System.out.println(responseText);

                    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "x-prototype-version,x-requested-with");
                    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST");
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(responseText.getBytes());
                    output.flush();
                }else if(type.contains("users")){
                    String results="";

                    File[] files = new File(filepath).listFiles();
                    //If this pathname does not denote file in directory, then listFiles() returns null.

                    for (File file : files) {
                        if (file.isFile()){
                            String name = file.getName();
                            System.out.println(name + " name of file");
                            if(name.contains(sender)){
                                results+=name.replace(".txt", "").replace(sender,"")+"\n";
                            }
                            System.out.println(results +" updated name");
                        }
                    }
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    exchange.getResponseHeaders().add("Content-Type", "text/json; charset=" + StandardCharsets.UTF_8.name());
                    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                        exchange.sendResponseHeaders(204, -1);
                        return;
                    }
                    exchange.sendResponseHeaders(200, results.getBytes().length);
                    OutputStream output = exchange.getResponseBody();
                    output.write(results.getBytes());
                    output.flush();
                }
            }
            else {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
                exchange.getResponseHeaders().add("Content-Type", "text/json; charset=" + StandardCharsets.UTF_8.name());
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));


        server.setExecutor(null);
        // creates a default executor
        server.start();

    }
}
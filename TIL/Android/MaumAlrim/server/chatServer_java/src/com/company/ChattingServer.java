package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChattingServer {

    public static final int PORT = 5001;

    public static void main(String[] args) {
	// write your code here
        ServerSocket serverSocket = null;
        ConcurrentHashMap<String, PrintWriter> userLists = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> joinUsers = new ConcurrentHashMap<>();

        try {
            // 1. 서버 소켓 생성
            serverSocket = new ServerSocket();

            // 2. 서버소켓에 현재 로컬호스트의 주소와 연결할 포트번호를 바인딩시켜줌
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind( new InetSocketAddress(hostAddress, PORT) );
            consoleLog("1. 내 로컬주소와 5001 포트번호로 서버소켓을 생성한다.");
            consoleLog("2. 연결된 서버 주소 : 포트번호 - " + hostAddress + ":" + PORT);

            // 3. 요청 대기
            while(true) {
                consoleLog("1. 클라이언트 소켓 연결을 허용 하기 위해 계속 기다림");
                Socket socket = serverSocket.accept();
                consoleLog("2. 클라이언트 소켓과 서버소켓의 통신연결을 해줄 InputStream과 OutputStream이 있는 ChatServerProcessThread 클래스 실행");
                consoleLog("3. ChatServerProcessThread에 보낼 파라미터 값 socket - "+socket.toString()+" | userLists - "+userLists.toString());
                new ChatServerProcessThread(socket, userLists, joinUsers).start();
            }
        }
        catch (IOException e) {
            consoleLog("IOException 발생! 아래 오류 읽어라 : (참고) https://jwkim96.tistory.com/57");
            e.printStackTrace();
        }
        finally {
            consoleLog("finally");
            try {
                if( serverSocket != null && !serverSocket.isClosed() ) {
                    consoleLog("서버 소켓이 열려있으면 소켓 닫아라");
                    serverSocket.close();
                    consoleLog("");
                }
            } catch (IOException e) {
                consoleLog("IOException 발생! 아래 오류 읽어라 : (참고) https://jwkim96.tistory.com/57");
                e.printStackTrace();
            }
        }
    }
    private static void consoleLog(String log) {
        System.out.println("[server " + Thread.currentThread().getId() + "] " + log);
    }

}

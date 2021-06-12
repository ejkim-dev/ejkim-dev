package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


//클라이언트에서 유저 Id만 받고, 서버에서 유저id로 채팅 대상자를 구분한다.
//서버에는 채팅메세지와, 자신의 id와 보낼 사람 id를 클라이언트로 보내고,
//메세지 보낸 사람이 내담자일 경우(상담접수 및 상담 현황을 실시간 체크하기위해) 보낼 사람 id 전체에 내담자 정보를 전송한다.
//클라이언트에서는 소켓이 있는 상담사가 내담자의 메세지를 전달받고, 상담사가 내담자를 클릭하면
//서버에 상담사id, 메세지(채팅방생성), 유저id가 전달되고,
//서버는 해당 유저id에게 oo상담사와 연결됐다는 메세지를 보내고, 클라이언트에는 채팅방이 생성된다.


//String 형태로 문자열을 가져온다.
//구분자로 나눠서 어레이에 각 담아준다.


public class ChatServerProcessThread extends Thread {
    private String myId = null;
    private String yourId = null;
    private Socket socket = null;
    ConcurrentHashMap<String, PrintWriter> userLists = null;
    ConcurrentHashMap<String, String> joinUsers = null;

    public ChatServerProcessThread(Socket socket, ConcurrentHashMap<String, PrintWriter> userLists,  ConcurrentHashMap<String, String> joinUsers){
        this.socket = socket;
        this.userLists = userLists;
        this.joinUsers = joinUsers;
    }

    @Override
    public void run() {
        consoleLog("1. run : 클라이언트 "+this.socket.toString()+" 프로세스 실행");
        try {
            // soket으로 입력 스트림을 받아 bufferedReader 변수 선언
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            consoleLog("2. run : 클라이언트 소켓 InputStream (bufferedReader) : "+socket.getPort()+" | "+bufferedReader.toString());

            // outputstream 객체를 인수로 받아 스트림 연결하기위해 PrintWriter 사용
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            consoleLog("3. run :  클라이언트 소켓 OnputStream (printWriter) : "+socket.getPort()+" | "+printWriter.toString());

            while(true) {
                // 클라이언트가 보낸 메세지를 읽어서 request에 담음
                // bufferedReader로 받은 값을 readLine을 사용하여 String으로 고정함
                String request = bufferedReader.readLine();
                consoleLog("4. run_반복문 : "+this.socket.getPort()+" 요청 : "+request);// request = "join:" + name + "\r\n"

                if( request == null) {//request가 빈값으로 돌아오면
                    consoleLog("5. request가 null 일 때 : 클라이언트로부터 연결 끊김");
                    doQuit(printWriter);
                    break;
                }

                //클라이언트에서 받은 String값을 스플릿으로 나누어서 메세지의 유형(join, message, quit)을 구분하기 위한 변수이다.
                String[] tokens = request.split("::");

                //클라이언트에서 받아온 메세지 로그 출력
                for (int i = 0; i < tokens.length; i++) {
                    consoleLog("	4-"+(i+1)+". run_반복문 : "+tokens[i]);
                }

                consoleLog("5. run_반복문 : tokens[0] - "+tokens[0]+" 으로 가기");
                System.out.println("");

                if("join".equals(tokens[0])) {
                    System.out.println("");
                    consoleLog("6. run_반복문 : "+tokens[0]+" 들어옴");

                    consoleLog("7. run_반복문 : "+tokens[0]+" doJoin 으로 가기 - 파라미터("+tokens[1] + " | "
                            +tokens[2] + " | 메세지 : "+tokens[3]+" | "+printWriter.toString()+")");

                    doJoin(tokens[1], tokens[2], "join;;"+tokens[3], printWriter);
                }
                else if("message".equals(tokens[0])) {
                    System.out.println("");
                    consoleLog("7. run_반복문 : "+tokens[0]+" 들어옴");
                    consoleLog("8. run_반복문"+tokens[0]+" :broadcast로 보내기 - 파라미터("+tokens[1]+")");
                    doMessage(tokens[1], tokens[2]);
                }
                else if("quit".equals(tokens[0])) {
                    System.out.println("");
                    consoleLog("7. run_반복문 : "+tokens[0]+" 들어옴");

                    doQuit(printWriter);
                }
                else {
                    consoleLog("에러: 알수 없는 요청 (" + tokens[0] + ")");
                }
            }
        }
        catch(IOException e) {
            consoleLog(this.myId + "님이 채팅방을 나갔습니다.");
            e.printStackTrace();
        }
    }

    //퇴장했을 때(소켓 연결이 끊겼을 때) : 퇴장 메세지를 보내고, 유저리스트에서 삭제하는게 맞겠지?
    private void doQuit(PrintWriter writer) {
        System.out.println("");
        consoleLog("6. doQuit 들어옴");
        consoleLog("7. doQuit - removeWriter 실행");

        //메세지타입;;발신자;;수신자;;퇴장메세지 
        // -> 내담자 일 경우 전체 메세지로 보내야함 
        // -> 상담사 일 경우 유저에게만 보내야함
        String data = "quit;;"+this.myId +";;"+this.yourId;

       /* if(this.yourId.equals("null")){
            data += this.myId+";;null;;null;;";
        }*/

        consoleLog("8. doQuit - 퇴장 메세지 : "+data);

        quitMessage(data);
        removeWriter(writer);
    }

    //클라이언트 삭제
    private void removeWriter(PrintWriter writer) {
        System.out.println("");
        consoleLog("9. removeWriter 들어옴");

        //Set<String> keys = mymembers.keySet();
        Set<String> keys = userLists.keySet();
        consoleLog("10. removeWriter Set keys : "+keys.toString());

        for (String key : keys) {
            if (userLists.get(key).equals(writer)) {
                consoleLog("11. removeWriter : 현제 삭제할 유저 : "+key);
                userLists.remove(key);
                consoleLog("12. removeWriter : 삭제되었습니다.");
                if (joinUsers.containsKey(key)){
                    consoleLog("    12-1. join리스트에 삭제된 유저가 있다.");
                    joinUsers.remove(key);
                    consoleLog("    12-2. join리스트에서 해당 유저 삭제하기");
                }
            }
        }


        consoleLog("13. removeWriter Set keys : "+keys.toString());
    }


    private void doMessage(String data, String yourId) {
        System.out.println("");
        this.yourId = yourId;

        if (joinUsers.containsKey(this.yourId)){
            consoleLog("만약 누군가와 대화하고 있는 사람이 joinUsers에 있다면");
            joinUsers.remove(this.yourId);
            consoleLog("해당 키 삭제 : 삭제된 유저 - "+this.yourId);
            consoleLog("현재 join 리스트 - "+joinUsers.toString());
        }

        consoleLog("9. doMessage 들어옴");
        consoleLog("10. doMessage : 수신자 : "+this.yourId+" | 데이터 : "+data);
        broadcast(this.myId + "::" + data);
    }

    //처음 클라이언트에서 소켓 연결하면서 request 보낼 때 doJoin 메서드에 들어옴
    private void doJoin(String myId, String yourId, String data, PrintWriter writer) {
        System.out.println("");
        consoleLog("8. doJoin 들어옴");
        this.myId = myId;
        this.yourId = yourId;//당연히 "null"이겠지

//        if (data.equals("null")) {
//            data = this.myId + "님과 연결되었습니다.";
//        }
        consoleLog("9. boadcast로 보낼 데이터 : "+data);

        addWriter(writer);
        joinMessage(data);

    }

    private void addWriter(PrintWriter writer) {

        System.out.println("");
        consoleLog("10. addWriter 들어옴");

        userLists.put(this.myId, writer);
        consoleLog("11. addWriter : "+this.myId+" 추가하기.");
        System.out.println("--------들어옴----------");
        consoleLog("12. addWriter : "+userLists.toString());
        System.out.println("");
    }

    //특정 아이디에게 채팅을 보내고나면 방이 없어질 때까지 채팅진행중인 것을 알려줘야함
    private void broadcast(String data) {
        System.out.println("");

        consoleLog("▶ broadcast 발신자 : "+this.myId+" | 수신자 : "+this.yourId+" | 데이터 : "+data);
        consoleLog("▶ broadcast : 수신자가 유저 리스트에 존재하나? "+userLists.containsKey(this.yourId));
        consoleLog("▶ broadcast : 발신자가 유저 리스트에 존재하나? "+userLists.containsKey(this.myId));
        consoleLog("-----------------------------------------------------------------");

        if (userLists.containsKey(this.yourId)) {
            consoleLog("▶ 수신자가 userLists에 있을 때 : 수신자 - "+this.yourId+" | 메세지 - "+data);

            //만약 유저면 그냥 상담사에게 보내고, 상담사이면 상담사에게 해당 유저와 연락하고 있다는 것을 보냄
            if (this.myId.contains("@")){
                userLists.get(this.yourId).println(data);
                userLists.get(this.yourId).flush();
            }
            else {
                for (String key : userLists.keySet()) {
                    consoleLog("  ▶ key- "+key+" | keys - "+userLists.keySet()+" | 크기 : "+userLists.keySet().size());

                    if (key.contains("@")) {
                        consoleLog("  ▶ 유저key- "+key);
                        if (key.equals(this.yourId)){
                            userLists.get(key).println(data);
                            userLists.get(key).flush();
                        }

                    }
                    else {
                        consoleLog("  ▶ 수신인 key- "+key);
                        userLists.get(key).println(this.yourId+ "::상담중");
                        userLists.get(key).flush();
                    }
                    System.out.println("");
                }
            }

        }
        else {
            consoleLog("▶ 수신자가 userLists에 없을 때 : 수신자 - "+this.yourId+" | 메세지 - "+data);
        }

        System.out.println("========================================================\n");

    }

    private void joinMessage(String data) {
        System.out.println("");

        consoleLog("▶ joinMessage 발신자 : "+this.myId+" | 수신자 : "+this.yourId+" | 데이터 : "+data);
        consoleLog("▶ joinMessage : 수신자가 유저 리스트에 존재하나? "+userLists.containsKey(this.yourId));
        consoleLog("▶ joinMessage : 발신자가 유저 리스트에 존재하나? "+userLists.containsKey(this.myId));
        consoleLog("-----------------------------------------------------------------");

        consoleLog("▶ joinMessage : join 일때 들어옴");

        if (userLists.size()>0) {
            consoleLog("▶ joinMessage : 현재 유저ID : "+this.myId);

            if (this.myId.contains("@")) {
                consoleLog("▶ joinMessage : 현재 유저ID는 내담자다");
                Set<String> keys = userLists.keySet();
                consoleLog("▶ joinMessage : Set keys : "+keys.toString());

                //유저가 상담신청을 한 데이터를 보관
                joinUsers.put(this.myId, data);
                consoleLog("▶ joinMessage : joinUsers = "+joinUsers.toString());

                for (String key : keys) {
                    consoleLog("  ▶ key- "+key+" | keys - "+keys+" | 크기 : "+keys.size());

                    if (key.contains("@")) {
                        consoleLog("  ▶ 유저key- "+key);

                    }
                    else {
                        //상담사에게 보내기
                        consoleLog("  ▶ 수신인 key- "+key);
                        userLists.get(key).println(data);
                        userLists.get(key).flush();
                    }
                    System.out.println("");
                }
            }
            else {
                consoleLog("▶ joinMessage : 현재 유저ID는 상담사다");//유저가 있으면 유저리스트를 보냄
                Set<String> keys = joinUsers.keySet();
                consoleLog("▶ joinMessage : 현재 내담자 리스트 = "+keys);

                //상담사가 접속했을 때 먼저 접속해서 대기중인 유저ID를 보내야함
                for (String key: keys){
                    consoleLog( "▶ joinMessage : 지금 내담자 - "+key+" | 내용 - "+joinUsers.get(key));
                    userLists.get(this.myId).println(joinUsers.get(key));
                    userLists.get(this.myId).flush();
                    consoleLog(this.myId+"에게 전송 완료");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    consoleLog("다시 전송 할 사람 있나?");
                }

                System.out.println("");

            }

        }
        else {
            consoleLog("▶ joinMessage 유저리스트에 "+this.myId+"이 존재하는가 - "+userLists.containsKey(this.myId));

        }


        System.out.println("========================================================\n");

    }
    
    //유저가 나갈 경우 전체 상담사에게 메세지가 가야하고, 상담사가 나갈 경우엔 대화중이던 유저가 소켓에 있으면 보내고 없으면 안보냄
    private void quitMessage(String data){
        System.out.println("");

        consoleLog("▶ quitMessage 발신자 : "+this.myId+" | 수신자 : "+this.yourId+" | 데이터 : "+data);
        consoleLog("▶ quitMessage : 수신자가 유저 리스트에 존재하나? "+userLists.containsKey(this.yourId));
        consoleLog("▶ quitMessage : 발신자가 유저 리스트에 존재하나? "+userLists.containsKey(this.myId));
        consoleLog("-----------------------------------------------------------------");

        if (userLists.size()>0) {
            consoleLog("▶ quitMessage : 현재 유저ID : "+this.myId+"("+userLists.contains(this.myId)+") " +
                    "| 수신자ID - "+this.yourId+"("+userLists.contains(this.yourId)+")");
            
            if (this.myId.contains("@")) {

                Set<String> keys = userLists.keySet();
                consoleLog("▶ quitMessage : Set keys : "+keys.toString());

                for (String key : keys) {
                    consoleLog("  ▶ key- "+key+" | keys - "+keys+" | 크기 : "+keys.size());

                    if (key.contains("@")) {
                        consoleLog("  ▶ 유저key- "+key);
                    }
                    else {//상담사에게만 전송
                        consoleLog("  ▶ 수신인 key- "+key);
                        userLists.get(key).println(data);
                        userLists.get(key).flush();
                    }
                    System.out.println("");
                }
            }
            else {
                consoleLog("▶ quitMessage 상담사ID - "+this.myId+"("+userLists.contains(this.myId)+") " +
                        "| 수신자ID - "+this.yourId+"("+userLists.contains(this.myId)+")");//수신자id가 갑자기 true에서 false로 바뀌는 이유 찾기

                if (userLists.contains(this.yourId)){//유저 소켓이 있을 경우 메세지 전송
                    consoleLog("  ▶ 수신인 key- "+this.yourId);
                    userLists.get(this.yourId).println(data);
                    userLists.get(this.yourId).flush();
                }
                System.out.println("");
            }
        }

        else {
            //유저리스트에 아무도 없음
            consoleLog("▶ quitMessage 유저리스트에 "+this.myId+"이 존재하는가 - "+userLists.containsKey(this.myId));
            //join;;ma@ma.com;;마마마;;2020-06-22 08:23;;ㄹㄹㄹㄹㄹㄹㄹㄹㄹ
        }
        System.out.println("========================================================\n");
    }

    private void consoleLog(String log) {
        System.out.println(log);
    }
}

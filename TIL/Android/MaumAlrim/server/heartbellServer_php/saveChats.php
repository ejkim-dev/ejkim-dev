<?php

// 에러메세지 출력 :   ini_set('display_errors',1); 오류메세지 끄기 :   ini_set('display_errors',0);
    error_reporting(E_ALL);
    ini_set('display_errors',0);
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

// $_SERVER['HTTP_USER_AGENT'] : php 접속 환경 정보에 strpos(문자열 포함 확인 함수)로 Androi가 있는지 확인
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    $requestRoomJson = isset($_POST['requestRoomJson']) ? $_POST['requestRoomJson'] : '';
    $requestChatJson = isset($_POST['requestChatJson']) ? $_POST['requestChatJson'] : '';

    if ($android) {
      // echo "받은 데이터";
      // echo $requestRoomJson;
      //  [{"chat_category":"기타 상담","end_time":"","is_public":"","is_status":"",
      //"master_id":"","other_id":"ma@ma.com","other_name":"마마마","room_id":"",
      //"summary":"dfsdfsfsdfsdfsd","user_id":"01011112222","user_name":"aaa"}]
      echo "서버에서 디코딩";
      // var_dump($requestRoomJson);

      // parse to php array
      $room_data_array = json_decode($requestRoomJson, true);
      // $arrstr = print_r($room_data_array, true);

      // echo $arrstr;

      echo "채팅";

      //chat data
      $chat_data_array = json_decode($requestChatJson, true);
      $arrstr2 = print_r($chat_data_array, true);

      echo $arrstr2;

      /*Array
    (
        [0] => Array
            (
                [chat_category] => 기타 상담
                [end_time] =>
                [is_public] =>
                [is_status] =>
                [master_id] =>
                [other_id] => ma@ma.com
                [other_name] => 마마마
                [room_id] =>
                [summary] => dddddddddddd
                [user_id] => 01011112222
                [user_name] => aaa
            )

    )*/
    echo " | 배열 크기 = ".sizeof($room_data_array);
    echo " | chat_category 출력 = ";
    echo $room_data_array[0]['chat_category'];
    echo $room_data_array[0]['end_time'];
    echo $room_data_array[0]['is_public'];
    echo $room_data_array[0]['is_status'];
    echo $room_data_array[0]['master_id'];
    echo $room_data_array[0]['other_id'];
    echo $room_data_array[0]['other_name'];
    echo $room_data_array[0]['summary'];
    echo $room_data_array[0]['user_id'];
    echo $room_data_array[0]['user_name'];

    $i = 0;
    while ($i < sizeof($room_data_array)) {

      $chat_category = $room_data_array[$i]['chat_category'];
      $other_id = $room_data_array[$i]['other_id'];
      $other_name = $room_data_array[$i]['other_name'];
      $contents =$room_data_array[$i]['contents'];
      $summary = $room_data_array[$i]['summary'];
      $user_id = $room_data_array[$i]['user_id'];
      $user_name = $room_data_array[$i]['user_name'];
      $this_time =  time();

      echo " 방 크기 카운트 할 수 있니? ".sizeof($room_data_array);
      echo " 채팅 크기 카운트 할 수 있니? ".sizeof($chat_data_array);

//업로드한 유저 아이디 개수 카운트해서 채팅방 이름으로 저장
      // $sql = "SELECT * FROM chatRoom where user_id='$user_id'";
      // // $sql = "SELECT * FROM employee where is_cert='N'";
      // $result = $con->prepare($sql);
      // if ($result->execute()) {
      //   echo " 출력값 : ";
      //   echo "결과1 = ".$result->fetchColumn();
      //   echo "결과2 = ".$result->fetchColumn()+1;
        $room_id = $user_id.$this_time;
        $end_time = date('Y-m-d', time());


        try{
            // 4. SQL문을 실행하여 데이터를 MySQL 서버의 member 테이블에 저장합니다.
            $stmt = "INSERT INTO chatRoom(user_id, user_name, room_id, other_id, other_name, chat_category, contents, summary, end_time)";
            $stmt .= "VALUES('{$user_id}', '{$user_name}', '{$room_id}', '{$other_id}', '{$other_name}', '{$chat_category}', '{$contents}', '{$summary}', '{$end_time}')";
            $insertData = $con->query($stmt);

            echo " | 채팅방에 데이터 insert | ";

            //채팅 디비 추가
            $j = 0;
            while ($j < sizeof($chat_data_array)) {
              // code...
              echo $j." 번 채팅 / ";
              $user_text = $chat_data_array[$j]['userChattext'];
              $user_id = $chat_data_array[$j]['userEmail'];
              echo "텍스트 : ";
              echo $chat_data_array[$j]['userChattext'];
              echo " / 이메일 : ";
              echo $chat_data_array[$j]['userEmail'];

              $sql = "INSERT INTO chat(room_id, user_text, user_id)";
              $sql .= "VALUES('{$room_id}', '{$user_text}', '{$user_id}')";
              $insertChat = $con->query($sql);

              if ($insertChat) {
                // code...
                echo $j."번 채팅 업로드가 완료되었습니다.";
              }
              else {
                echo $j."번 채팅 업로드 에러";
              }

              $j++;
            }
            echo " / 채팅 개수? ".$j;


            // 5. SQL 실행 결과를 위한 메시지를 생성합니다.
            echo " Sql 잘 들어감? = ";
            echo $insertData;

            if($insertData)
            {
                echo "업로드가 완료되었습니다.";
            }
            else
            {
                echo "업로드 에러";
            }

        } catch(PDOException $e) {
            die("Database error: " . $e->getMessage());
        }
      // }
      // else {
      //   $errMSG = "개수 출력 에러";
      //   echo $errMSG;
      // }
      echo "채팅창 업로드 마지막줄";
      $i++;
    }


    }


  ?>

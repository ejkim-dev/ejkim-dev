<?php

// 에러메세지 출력 :   ini_set('display_errors',1); 오류메세지 끄기 :   ini_set('display_errors',0);
    error_reporting(E_ALL);
    ini_set('display_errors',0);
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

// $_SERVER['HTTP_USER_AGENT'] : php 접속 환경 정보에 strpos(문자열 포함 확인 함수)로 Androi가 있는지 확인
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
/*json 결과 : [{"chat_category":"기타 상담",
"end_time":"","is_public":"","is_status":"",
"master_id":"","other_id":"ma@ma.com",
"other_name":"마마마","room_id":"",
"summary":"dfsdfsfsdfsdfsd","user_id":"01011112222","user_name":"aaa"}]*/
    if($android)//($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android
    {

        // 1. 안드로이드 코드의 postParameters 변수($name, $country)에 적어준 이름을 가지고 값을 전달 받습니다.
        $user_id = $_POST['user_id']
        $user_name = $_POST['user_name'];
        $room_id = "";
        $other_id = $_POST['other_id'];
        $other_name = $_POST['other_name'];
        $chat_category = $_POST['chat_category'];
        $end_time = time();

        // 2. 입력안된 항목이 있을 경우 에러 메시지를 생성합니다.
        if(empty($user_nickname)||empty($user_email)||empty($user_pw)
        || empty($birth_year) ||empty($job)||empty($gender)){
            $errMSG = "입력을 확인해주세요.";
        }


        // 3. 에러 메시지가 정의안되어 있다면 이름과 나라 모두 입력된 경우입니다.
        if(!isset($errMSG)) // 모두 입력이 되었다면
        {
            try{
                // 4. SQL문을 실행하여 데이터를 MySQL 서버의 member 테이블에 저장합니다.
                $stmt = $con->prepare('INSERT INTO chatRoom(user_id, room_id, other_id, chat_category, end_time, is_public, is_status)
                VALUES(:user_id, :room_id, :other_id, :chat_category, :end_time, :is_public, :is_status)');
                $stmt->bindParam(':user_id', $user_id);
                $stmt->bindParam(':room_id', $room_id);
                $stmt->bindParam(':other_id', $other_id);
                $stmt->bindParam(':chat_category', $chat_category);
                $stmt->bindParam(':end_time', $end_time);
                $stmt->bindParam(':is_public', $is_public);
                $stmt->bindParam(':is_status', $is_status);

                // 5. SQL 실행 결과를 위한 메시지를 생성합니다.
                if($stmt->execute())
                {
                    $successMSG = "업로드가 완료되었습니다.";
                }
                else
                {
                    $errMSG = "업로드 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
        }

    }
?>

<?php
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( !$android )
    {
      // 안드로이드가 아닐때는?
    }
?>

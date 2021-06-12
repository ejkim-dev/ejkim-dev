<?php

// 에러메세지 출력 :   ini_set('display_errors',1); 오류메세지 끄기 :   ini_set('display_errors',0);
    error_reporting(E_ALL);
    ini_set('display_errors',0);
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

// $_SERVER['HTTP_USER_AGENT'] : php 접속 환경 정보에 strpos(문자열 포함 확인 함수)로 Androi가 있는지 확인
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if($android)//($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android
    {

        // 1. 안드로이드 코드의 postParameters 변수($name, $country)에 적어준 이름을 가지고 값을 전달 받습니다.
        $user_nickname = $_POST['user_nickname'];
        $user_phone = $_POST['user_phone'];
        $user_pw = $_POST['user_pw'];
        $reg_time = time();

        // 2. 입력안된 항목이 있을 경우 에러 메시지를 생성합니다.
        if(empty($user_nickname)||empty($user_phone)||empty($user_pw)){
            $errMSG = "입력을 확인해주세요.";
        }


      // sha1 함수를 이용해 암호화
      // sogha586 는 임의적으로 비밀번호에 값을 더하여 실제 입력한 값과 다르게 암호화함
        $user_pw = sha1('sogha586'.$user_pw);


        // 3. 에러 메시지가 정의안되어 있다면 이름과 나라 모두 입력된 경우입니다.
        if(!isset($errMSG)) // 모두 입력이 되었다면
        {
            try{
                // 4. SQL문을 실행하여 데이터를 MySQL 서버의 member 테이블에 저장합니다.
                $stmt = $con->prepare('INSERT INTO employee(user_nickname, user_phone, user_pw, reg_time) VALUES(:user_nickname, :user_phone, :user_pw, :reg_time)');
                $stmt->bindParam(':user_nickname', $user_nickname);
                $stmt->bindParam(':user_phone', $user_phone);
                $stmt->bindParam(':user_pw', $user_pw);
                $stmt->bindParam(':reg_time', $reg_time);

                // 5. SQL 실행 결과를 위한 메시지를 생성합니다.
                if($stmt->execute())
                {
                    $successMSG = "회원가입이 완료되었습니다.";
                }
                else
                {
                    $errMSG = "사용자 추가 에러";
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
?>

<?php
    }
?>

<?php
// 에러메세지 출력 :   ini_set('display_errors',1); 오류메세지 끄기 :   ini_set('display_errors',0);
    error_reporting(E_ALL);
    ini_set('display_errors',0);
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속


    // $_SERVER['HTTP_USER_AGENT'] : php 접속 환경 정보에 strpos(문자열 포함 확인 함수)로 Androi가 있는지 확인
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    //POST 값을 읽어온다.
    $user_id =isset($_POST['user_id']) ? $_POST['user_id'] : '';
    $user_title=isset($_POST['user_title']) ? $_POST['user_title'] : '';
    $user_condition=isset($_POST['user_condition']) ? $_POST['user_condition'] : '';
    $user_text=isset($_POST['user_text']) ? $_POST['user_text'] : '';
    $reg_time = date('Y-m-d', time());

    if ($user_id != "") {
      try{

        if ($user_condition == "") {
          // code...
          $user_condition = "모름";
        }

        if ($user_title == "") {
          // code...
          $user_title = "제목없음";
        }

        if ($user_text == "") {
          // code...
          $user_text = "내용없음";
        }

          // 4. SQL문을 실행하여 데이터를 MySQL 서버의 member 테이블에 저장합니다.
          $stmt = "INSERT INTO diary(user_id, user_title, user_condition, user_text, reg_time)";
          $stmt .= "VALUES('{$user_id}', '{$user_title}', '{$user_condition}', '{$user_text}', '{$reg_time}')";
          $insertData = $con->query($stmt);

          // 5. SQL 실행 결과를 위한 메시지를 생성합니다.
          if($insertData)
          {
              echo "저장되었습니다.";
          }
          else
          {
              echo "저장되지 않았습니다.";
          }

      }
      catch(PDOException $e) {
          echo "클라이언트에서 값이 안들어옴 ";
      }

    }



  if( !$android )
    {
      echo "<pre>";
      echo $my;
      echo '</pre>';
?>

<html>
   <body>

        <form action="<?php $_PHP_SELF ?>" method="POST">
            Id: <input type = "email" name = "user_id" />
            Title: <input type = "text" name = "user_title" />
            Text: <input type = "text" name = "user_text" />
            date:<input type = "text" name = "reg_time" />
            <input type = "submit" name = "submit" />
        </form>

   </body>
</html>
<?php
    }
?>

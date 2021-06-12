<?php
// 에러메세지 출력 :   ini_set('display_errors',1); 오류메세지 끄기 :   ini_set('display_errors',0);
    error_reporting(E_ALL);
    ini_set('display_errors',0);
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속


    // $_SERVER['HTTP_USER_AGENT'] : php 접속 환경 정보에 strpos(문자열 포함 확인 함수)로 Androi가 있는지 확인
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    //POST 값을 읽어온다.
    $master_id =isset($_POST['user_id']) ? $_POST['user_id'] : '';//다이어리 코드를 불러옴
    $user_title=isset($_POST['user_title']) ? $_POST['user_title'] : '';
    $user_condition=isset($_POST['user_condition']) ? $_POST['user_condition'] : '';
    $user_text=isset($_POST['user_text']) ? $_POST['user_text'] : '';

    if ($master_id != "") {
      try{

        $sql="update diary set is_status ='N' where master_id='{$master_id}'";
        $stmt = $con->prepare($sql);
        $stmt->execute();

        if ($stmt) {
          // code...
          echo "삭제하였습니다.";
        }
        else {
          echo "삭제 실패 하였습니다.";
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
            Id: <input type = "number" name = "user_id" />
            <input type = "submit" name = "submit" />
        </form>

   </body>
  </html>
  <?php
    }
  ?>

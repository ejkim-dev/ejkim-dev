<?php
// 안드로이드 앱에서 입력한 나라 이름에 대응하는 레코드만 조회하여  안드로이드앱으로 리턴하기 위해서 사용됩니다.
error_reporting(E_ALL);
ini_set('display_errors',1);
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속
include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include


//POST 값을 읽어온다.
$user_phone=isset($_POST['user_email']) ? $_POST['user_email'] : '';
$user_pw=isset($_POST['user_pw']) ? $_POST['user_pw'] : '';

function goSignInPage($alert){ //로그인 정보가 일치하지 않으면
// 다이얼로그창
  echo "<script>
      window.alert('$alert');
      window.history.back();
  </script>";
  return;

}

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if ($user_phone != "" && $user_pw != "" ){

// 전달받은 비밀번호는 암호화처리함
    $user_pw = sha1('sogha586'.$user_pw);

// POST 방식으로 전달받은 값을 사용하여 SELECT문을 실행합니다.
    $sql="select * from employee where user_phone='$user_phone' and user_pw='$user_pw'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    // echo "데이터 개수 : ".$stmt->rowCount();
    // echo " 닉네임 : ".$user_nickname;
    // echo " 이메일 : ".$user_email;

   if ($stmt->rowCount() == 0){
     echo "로그인 정보를 다시 확인해주세요";

     if (!$android) {
       goSignInPage("로그인 정보를 다시 확인해주세요");
       exit;
     }

   }else {
     //로그인 성공했을때
        		$data = array();

             while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

             	extract($row);

               array_push($data,
                   array('master_id'=>$row["master_id"],
                   'user_phone'=>$row["user_phone"],
                   'user_nickname'=>$row["user_nickname"],
                   'reg_time'=>$row["reg_time"],
                   'is_cert'=>$row["is_cert"],
                   'is_status'=>$row["is_status"],
                   'is_super'=>$row["is_super"]
               ));
               if (!$android) {
                 // code...
                 $_SESSION['memberID'] = $row['user_phone'];
                 $_SESSION['nickName'] = $row['user_nickname'];
                 $_SESSION['isSuper'] = $row['is_super'];
               }
             }

             // 안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코합니다.
                     if (!$android) {
                         // echo "<pre>";
                         // print_r($data);
                         setcookie('memberID',$user_phone,time()+86400);
                         echo "<script>location.replace('./index.php')</script>";//세션 없으면 로그인 페이지로
                         exit;
                         // echo "아이디 : ";
                         // echo $_SESSION['memberID'];
                         //  echo " | 닉네임 : ";
                         // echo $_SESSION['nickName'];

                         // Header("Location:/index.php");//로그인 정보 일치하면 넘어가기

                         // echo '</pre>';
                         // https://zetawiki.com/wiki/PHP_%EC%84%B8%EC%85%98_%EB%A1%9C%EA%B7%B8%EC%9D%B8_%EA%B5%AC%ED%98%84
                     }else
                     {
                         header('Content-Type: application/json; charset=utf8');
                         $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                         echo $json;
                     }
   }

}
else {
    echo "빈칸에 입력하세요 ";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){

?>

<!-- <html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
          아이디: <input type = "number" name = "user_email" />
          비밀번호: <input type = "password" name = "user_pw" />
         <input type = "submit" />
      </form>

   </body>
</html> -->
<?php
}


?>

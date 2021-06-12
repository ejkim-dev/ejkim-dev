<?php
// 전체회원 조회
error_reporting(E_ALL);
ini_set('display_errors',1);
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속


//POST 값을 읽어온다.
$user_id =isset($_POST['user_id']) ? $_POST['user_id'] : '';

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if ($user_id != "") {

  $sql="select * from diary where user_id = '$user_id' and is_status = 'Y'";
  $result = $con->prepare($sql);
  $result -> execute();

  if ($result->rowCount() == 0) {
    echo "해당 아이디의 데이터가 없습니다. ";
    echo "해당 아이디 = $user_id";
  }
  else {
    //해당 아이디 데이터가 있을 때
    $data = array();

     while($row=$result->fetch(PDO::FETCH_ASSOC)){

       extract($row);

       array_push($data,
           array('master_id'=>$row["master_id"],
           'user_id'=>$row["user_id"],
           'user_title'=>$row["user_title"],
           'user_condition'=>$row["user_condition"],
           'user_text'=>$row["user_text"],
           'reg_time'=>$row["reg_time"],
           'is_status'=>$row["is_status"],
       ));
     }

     // 안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코합니다.
         if (!$android) {
           $end_time = date('Y-m-d', time());
           echo $end_time;
             echo "<pre>";
             print_r($data);
             echo '</pre>';
         }else
         {
           header('Content-Type: application/json; charset=utf8');
           $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
           echo $json;
         }
  }
}
else {
  echo "클라이언트에서 값이 안들어옴 ";
}

if (!$android){
?>
<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
          아이디: <input type = "email" name = "user_id" />
         <input type = "submit" />
      </form>

   </body>
</html>

<?php
}

?>

<?php
// 전체회원 조회
error_reporting(E_ALL);
ini_set('display_errors',1);
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속


//POST 값을 읽어온다.
$room_id =isset($_POST['room_id']) ? $_POST['room_id'] : '';

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if ($room_id != "") {

  $sql="select * from chat where room_id = '$room_id'";
  $result = $con->prepare($sql);
  $result -> execute();

  if ($result->rowCount() == 0) {
    echo "해당 아이디의 데이터가 없습니다. ";
    echo "해당 아이디 = $room_id";
  }
  else {
    //해당 아이디 데이터가 있을 때
    $data = array();

     while($row=$result->fetch(PDO::FETCH_ASSOC)){

       extract($row);

       array_push($data,
           array('master_id'=>$row["master_id"],
           'room_id'=>$row["room_id"],
           'user_text'=>$row["user_text"],
           'user_id'=>$row["user_id"]
       ));
     }

     // 안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코합니다.
         if (!$android) {
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
          방 아이디: <input type = "number" name = "room_id" />
         <input type = "submit" />
      </form>

   </body>
</html>

<?php
}

?>

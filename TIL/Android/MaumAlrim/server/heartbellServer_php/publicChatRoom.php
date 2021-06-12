<?php
// 전체회원 조회
error_reporting(E_ALL);
ini_set('display_errors',1);
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속


// if ($is_public != "") {

  $sql="select * from chatRoom where is_public = 'Y'";
  $result = $con->prepare($sql);
  $result -> execute();

  if ($result->rowCount() == 0) {
    echo "해당 아이디의 데이터가 없습니다. ";
    echo "해당 아이디 = $is_public";
  }
  else {
    //해당 아이디 데이터가 있을 때
    $data = array();

     while($row=$result->fetch(PDO::FETCH_ASSOC)){

       extract($row);

       array_push($data,
           array('master_id'=>$row["master_id"],
           'user_id'=>$row["user_id"],
           'user_name'=>$row["user_name"],
           'room_id'=>$row["room_id"],
           'other_id'=>$row["other_id"],
           'other_name'=>$row["other_name"],
           'chat_category'=>$row["chat_category"],
           'contents'=>$row["contents"],
           'summary'=>$row["summary"],
           'end_time'=>$row["end_time"],
           'is_public'=>$row["is_public"],
           'is_status'=>$row["is_status"]
       ));
     }

     header('Content-Type: application/json; charset=utf8');
     $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
     echo $json;

  }
// }
// else {
//   echo "클라이언트에서 값이 안들어옴 ";
// }


?>

<?php
// 전체회원 조회
error_reporting(E_ALL);
ini_set('display_errors',1);
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

//POST 값을 읽어온다.
$room_id=isset($_POST['room_id']) ? $_POST['room_id'] : '';
$is_public=isset($_POST['is_public']) ? $_POST['is_public'] : '';

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if ($room_id != "" && $is_public != "") {
  // code...
  $sql="update chatRoom set is_public ='$is_public' where room_id='$room_id'";
  $stmt = $con->prepare($sql);
  $stmt->execute();

  if ($stmt) {
    // code...
    echo "변경이 완료되었습니다";
  }
  else {
    echo "변경 실패 하였습니다.";
  }



}
if (!$android){
?>
<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
          채팅방: <input type = "number" name = "room_id" />
          바꿀값: <input type = "text" name = "is_public" />
         <input type = "submit" />
      </form>

   </body>
</html>

<?php
}

?>

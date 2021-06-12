<?php
// 안드로이드 앱에서 입력한 나라 이름에 대응하는 레코드만 조회하여  안드로이드앱으로 리턴하기 위해서 사용됩니다.
error_reporting(E_ALL);
ini_set('display_errors',1);
include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속



//POST 값을 읽어온다.
$country=isset($_POST['country']) ? $_POST['country'] : '';
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($country != "" ){

// POST 방식으로 전달받은 값을 사용하여 SELECT문을 실행합니다.
    $sql="select * from person where country='$country'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

// 질의 결과 데이터가 없으면 에러를 출력하고 데이터를 얻으면 배열을 생성합니다.
    if ($stmt->rowCount() == 0){

        echo "'";
        echo $country;
        echo "'은 찾을 수 없습니다. ".$stmt->rowCount()."개";
        // echo $stmt->rowCount();
    }
	else{

//    		$data = array();
//
//         while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
//
//         	extract($row);
//
//             array_push($data,
//                 array('id'=>$row["id"],
//                 'name'=>$row["name"],
//                 'country'=>$row["country"]
//             ));
//         }
//
// // 안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코합니다.
//         if (!$android) {
//             echo "<pre>";
//             print_r($data);
//             echo '</pre>';
//         }else
//         {
//             header('Content-Type: application/json; charset=utf8');
//             $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
//             echo $json;
//         }
    }
}
else {
    echo "검색할 나라를 입력하세요 ";
}

?>



<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>

      <form action="<?php $_PHP_SELF ?>" method="POST">
         나라 이름: <input type = "text" name = "country" />
         <input type = "submit" />
      </form>

   </body>
</html>
<?php
}


?>

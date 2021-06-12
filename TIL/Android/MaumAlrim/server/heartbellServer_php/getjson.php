<?php
// 데이터베이스의 테이블에 있는 모든 데이터를 안드로이드 앱으로 가져오기 위해서 사용됩니다.
    error_reporting(E_ALL);
    ini_set('display_errors',1);
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속


    $stmt = $con->prepare('select * from person');
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();

        //PDO::FETCH_ASSOC : Fetch 모드를 설정 - 배열로 가져오기 위해
        // while로 1 row씩 가져오기
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);

            array_push($data,
                array('id'=>$id,
                'name'=>$name,
                'country'=>$country
            ));
        }

        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }

?>

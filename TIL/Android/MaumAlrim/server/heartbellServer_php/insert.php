<?php

    error_reporting(E_ALL);
    ini_set('display_errors',1);//1 : 에러메세지 출력, 0 : 에러메세지 끄기
    include $_SERVER['DOCUMENT_ROOT'].'/common/dbcon.php';//데이터베이스 접속

/*strpos 는 대상 문자열을 앞에서 부터 검색하여 찾고자 하는 문자열이 몇번째 위치에 있는지를 리턴
*주의 : 영문자의 대소문자를 구별하여 검색
*사용법 : strpos([대상 문자열], [조건 문자열], [검색 시작위치]);*/
// $_SERVER['HTTP_USER_AGENT'] : php전역변수-유저의 브라우저 접속환경 파악
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");// 접속 환경 정보에 strpos(문자열 포함 확인 함수)로 Android가 있는지 확인

    //안드로이드만 볼 수 있는 페이지
    if(($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android)//($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit']) || $android
    {

        // 1. 안드로이드 코드의 postParameters 변수($name, $country)에 적어준 이름을 가지고 값을 전달 받습니다.
        $name=$_POST['name'];
        $country=$_POST['country'];

        // 2. 입력안된 항목이 있을 경우 에러 메시지를 생성합니다.
        if(empty($name)){
            $errMSG = "이름을 입력하세요.";
        }
        else if(empty($country)){
            $errMSG = "나라를 입력하세요.";
        }

        // 3. 에러 메시지가 정의안되어 있다면 이름과 나라 모두 입력된 경우입니다.
        if(!isset($errMSG)) // 이름과 나라 모두 입력이 되었다면
        {
            try{
                // 4. SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다.
                $stmt = $con->prepare('INSERT INTO person(name, country) VALUES(:name, :country)');
                $stmt->bindParam(':name', $name);
                $stmt->bindParam(':country', $country);

                // 5. SQL 실행 결과를 위한 메시지를 생성합니다.
                if($stmt->execute())
                {
                    $successMSG = "새로운 사용자를 추가했습니다.";
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

<html>
   <body>

        <form action="<?php $_PHP_SELF ?>" method="POST">
            Name: <input type = "text" name = "name" />
            Country: <input type = "text" name = "country" />
            <input type = "submit" name = "submit" />
        </form>

   </body>
</html>
<?php
    }
?>

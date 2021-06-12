<?php
    echo "<pre>";
    var_dump($_FILES);
    echo "</pre>";

    // 임시저장된 정보 : 전송된 파일은 $_FILES를 통해 접근 가능
    $myTempFile = $_FILES['imgFile']['tmp_name'];
    echo " | myTempFile =  {$myTempFile}"; //  /tmp/php31dfqx
    echo "<br>";
    $fileName = $_FILES['imgFile']['name'];
    echo " | fileName =  {$fileName}"; //  /tmp/php31dfqx
    echo "<br>";
    // 파일 타입 및 확장자 구하기
    $fileTypeExtension = explode("/", $_FILES['imgFile']['type']);
    echo " | fileTypeExtension = {$fileTypeExtension}"; // Array
    echo "<br>";

    // 파일 타입
    $fileType = $fileTypeExtension[0];
    echo " | fileType = {$fileType}"; // image
    echo "<br>";

    // 파일 확장자
    $extension = $fileTypeExtension[1];
    echo " | extension = {$extension}"; // jpeg
    echo "<br>";

    //확장자 검사
    $isExtGood = false;

    switch($extension){
        case 'jpeg':
        case 'bmp':
        case 'gif':
        case 'png':
        $isExtGood = true;
        break;
        default :
            echo "허용하는 확장자는 jpg, bmp, gif, png 입니다. - switch";
            exit;
            break;
    }

    //이미지 파일이 맞는지 확인
    if($fileType == 'image'){
        // 허용할 확장자를 jpg, bmp, gif, png로 정함 그외는 업로드 불가
        if($isExtGood){
            // 임시 파일 옮길 저장 및 파일명
            $nowTime = time();
            echo " | nowTime = {$nowTime}"; // jpeg
            echo "<br>";

            $filePath = "../images/$nowTime"."_".$fileName; // 결과 : 1580274370_about.jpg
            echo " | filePath = {$filePath}";
            echo "<br>";
            // 임시 저장된 파일을 우리가 저장할 장소 및 파일명으로 옮김
            // move_uploaded_file()은 서버로 전송된 파일을 저장할 때 사용하는 함수
            $imageUpload = move_uploaded_file($myTempFile,$filePath);
            echo " | imageUpload = {$imageUpload}";
            echo "<br>";
            //업로드 성공 여부 확인
            if($imageUpload == true){
                echo '파일이 정상적으로 업로드 되었습니다. <br>';
                echo "<img src='{$filePath}' width='100'/>";//저장된 파일 넓이 100으로 불러오기
            }else{
                echo '파일 업로드에 실패했습니다. ';
            }
        }
        //확장자가 jpg, bmp, gif, png가 아닐때
        else{
            echo "허용하는 확장자는 jpg, bmp, gif, png 입니다. - else";
            exit;
        }
    }
    // type이 image가 아닐때
    else{
        echo "이미지 파일이 아닙니다. ";
        exit;
    }
 ?>
